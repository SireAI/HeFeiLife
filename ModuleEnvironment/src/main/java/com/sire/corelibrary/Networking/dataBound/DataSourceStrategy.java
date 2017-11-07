package com.sire.corelibrary.Networking.dataBound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

import static com.sire.corelibrary.Networking.dataBound.DataResource.success;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/25
 * Author:Sire
 * Description:
 * ==================================================
 */

public class DataSourceStrategy<ResponseData,Data> {
    private final boolean cacheData;
    private final DataFromStrategy dataFromStrategy;
    private final MediatorLiveData<DataResource<Data>> result = new MediatorLiveData<>();
    private DataDecision<ResponseData, Data> dataDecision;

    public DataSourceStrategy(boolean cacheData, DataFromStrategy dataFromStrategy) {
        this.cacheData = cacheData;
        this.dataFromStrategy = dataFromStrategy;
    }

    public LiveData<DataResource<Data>> apply(DataDecision<ResponseData,Data> dataDecision) {

        //init state
        this.dataDecision = dataDecision;
        Timber.w("---------------- current data source strategy "+ dataFromStrategy.name()+" ----------------");
        result.setValue(DataResource.loading(null));
        switch (dataFromStrategy) {
            case NET:
                justFromNet();
                break;
            case CACHE:
                justFromCache();
                break;
            case CACHE_NET:
                fromCacheAndNet();
                break;
        }
        return result;
    }

    private void fromCacheAndNet() {
        LiveData<Data> dataFromDB = dataDecision.loadFromDb();
        result.addSource(dataFromDB, (Data data) -> {
            result.removeSource(dataFromDB);
            boolean dbHasData = true;
            if(data == null){
                dbHasData  = false;
            }else if (data instanceof List ){
                dbHasData = ((List) data).size()>0;
            }
            String cacheMessage = dbHasData  ?  data.getClass().getSimpleName() + " data from DB by notify:" + data.toString():"no cached data in DB" ;
            Timber.d(cacheMessage);
            boolean fetchFromNet = dataDecision.shouldFetchDataFromNet(data);
            String dataSourceMessage = fetchFromNet ? "fetch data from net by logic condition " : "just use cache data";
            Timber.d(dataSourceMessage);
            if (fetchFromNet) {
                fetchFromNetworkWithCache();
            } else {
                result.setValue(DataResource.success(data));
            }
        });
    }

    private void justFromCache() {
        LiveData<Data> dataFromDb = dataDecision.loadFromDb();
        result.addSource(dataFromDb, data -> {
            result.removeSource(dataFromDb);
            String cacheMessage = data == null ? "no cached data in DB" : data.getClass().getSimpleName() + " data from DB :" + data.toString();
            Timber.d(cacheMessage);
            result.setValue(success(data));
        });
    }

    private void justFromNet() {
        //fetch data from net ,it may take time
        LiveData<Response<ResponseData>> responseLiveData = dataDecision.makeNetCall();
        result.addSource(responseLiveData, response -> {
            result.removeSource(responseLiveData);
            String message = response == null || response.body() == null? "无响应,连接失败" : "响应成功:" + response.body().toString();
            Timber.d(message);
            if (response != null && response.isSuccessful()) {
                DataResource<Data> dataDataResource = dataDecision.logicResponseHandler(response.body());

                if(cacheData && dataDataResource.status == DataStatus.SUCCESS){
                    Flowable.just(dataDataResource.data).observeOn(Schedulers.io()).subscribe(new Consumer<Data>() {
                        @Override
                        public void accept(Data data) throws Exception {
                            dataDecision.saveData2DB(data);
                        }
                    });
                }
                result.setValue(dataDataResource);
            } else {
                errorNotify(response);
            }
        });
    }

    private void fetchFromNetworkWithCache() {
        //fetch data from net ,it may take time
        LiveData<Response<ResponseData>> responseLiveData = dataDecision.makeNetCall();
        result.addSource(responseLiveData, response -> {
            String message = response == null ? "无响应,连接失败" : "响应成功:" + response.body().toString();
            Timber.d(message);
            result.removeSource(responseLiveData);
            if (response != null && response.isSuccessful()) {
                Flowable.just(response.body())
                        .map(data -> dataDecision.logicResponseHandler(response.body()))
                        .filter(dataDataResource -> {
                            boolean success = dataDataResource.status == DataStatus.SUCCESS;
                            if (success) {
                                result.setValue(DataResource.success(dataDataResource.data));
                            }else {
                                result.setValue(DataResource.error(response.message(), null));
                            }
                            return success;
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .subscribe((DataResource<Data> dataFromDB) -> {
                           dataDecision.saveData2DB(dataFromDB.data);
                        });
            } else {
                errorNotify(response);
            }
        });
    }

    private void errorNotify(Response<ResponseData> response) {
        if (response != null) {
            result.setValue(DataResource.error(response.message(), null));
        } else {
            result.setValue(DataResource.error("网络连接失败", null));
        }
    }

    public enum DataFromStrategy {
        /**
         * 从网络中获取数据，可选是否缓存
         */
        NET,
        /**
         * 仅从缓存中读取数据
         */
        CACHE,
        /**
         * 优先从缓存中获取数据，如果没有，就从网络中获取数据，获取的数据将被缓存
         */
        CACHE_NET
    }

    /**
     * data strategy set
     *
     * @param <Data> final data
     * @param <ResponseData>  data from net
     */
    public interface DataDecision<ResponseData,Data> {
        default boolean shouldFetchDataFromNet(Data cachedData) {
            if(cachedData == null){
                return true;
            }else if(cachedData instanceof List ){
                return ((List) cachedData).size()==0;
            }
            return false;
        }

        default DataResource<Data> logicResponseHandler(ResponseData responseData) {


            if(responseData instanceof JsonResponse){
                JsonResponse<Data> jsonResponse = (JsonResponse)responseData;
                if(jsonResponse.isOK()){
                    return DataResource.success(jsonResponse.getData());
                }else {
                    String message =jsonResponse.getMessage();
                    return DataResource.error(message,null);
                }
            }

            return DataResource.success((Data) responseData);
        }

        default LiveData<Data> loadFromDb() {
            return null;
        }

        ;

        default LiveData<Response<ResponseData>> makeNetCall() {
            return null;
        }

        default void saveData2DB(Data data) {
        }
    }

    /**
     * builder for data strategy
     *
     */
    public static final class Builder {
        private boolean cacheData = false;
        private DataFromStrategy dataFromStrategy = DataFromStrategy.NET;


        public Builder cacheData(boolean cacheData) {
            this.cacheData = cacheData;
            return this;
        }


        public Builder appDataFromStrategy(DataFromStrategy dataFromStrategy) {
            this.dataFromStrategy = dataFromStrategy;
            return this;
        }

        public DataSourceStrategy build() {
            return new DataSourceStrategy(this.cacheData, this.dataFromStrategy);
        }
    }
}
