package com.sire.corelibrary.DI.Environment;

import com.sire.mediators.core.CallBack;

import org.reactivestreams.Publisher;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ModuleInitActions {
    List<Flowable<ModuleInitInfor>> tasks = new LinkedList<>();
    private Disposable disposable;

    public ModuleInitActions with(ModuleInit moduleInit) {
        tasks.add(moduleInit.init());
        return this;
    }

    public void excute(CallBack<Boolean> callBack) {
        Flowable<ModuleInitInfor> startTask = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (i == 0) {
                startTask = tasks.get(0);
            } else {
                final int j = i;
                startTask = startTask.flatMap(new Function<ModuleInitInfor, Publisher<ModuleInitInfor>>() {
                    @Override
                    public Publisher<ModuleInitInfor> apply(ModuleInitInfor moduleInitInfor) throws Exception {
                        Timber.d("--------" + moduleInitInfor.toString());
                        return tasks.get(j);
                    }
                });
            }

        }
        if (startTask == null) {
            Timber.d("--------无初始化任务");
            callBack.apply(false);
            return;
        }
        disposable = startTask.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ModuleInitInfor moduleInitInfor) -> {
                    Timber.d("--------" + moduleInitInfor.toString());
                    if(!disposable.isDisposed()){
                        disposable.dispose();
                    }
                    callBack.apply(true);
                    onDestory();
                });

    }
    private void onDestory(){
        tasks.clear();
        disposable = null;
    }
}
