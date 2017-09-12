/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sire.corelibrary.Networking.dataBound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.sire.corelibrary.Executors.AppExecutors;

import retrofit2.Response;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/20
 * Author:sire
 * Description:
 * ==================================================
 */
public abstract class DataSourceBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<DataResource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    protected DataSourceBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        //ModuleInit state
        result.setValue(DataResource.loading(null));
        //fetch data from db,it may takes time
        LiveData<ResultType> dbSource = loadFromDb();
        // register observer ,decide wheather fetch data from net
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                //if the db changes ,data will be notified
                result.addSource(dbSource, newData -> result.setValue(DataResource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        //fetch data from net ,it may take time
        LiveData<Response<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData ->
                result.setValue(DataResource.loading(newData))
        );
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response != null && response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    appExecutors.mainThread().execute(() ->
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb(),
                                    newData -> result.setValue(DataResource.success(newData)))
                    );
                });
            } else {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> {
                            String errorMessage = "网络连接失败";
                            if (response != null) {
                                errorMessage = response.message();
                            }
                            result.setValue(DataResource.error(errorMessage, newData));
                        }

                );
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<DataResource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(Response<RequestType> response) {
        return response.body();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<Response<RequestType>> createCall();
}
