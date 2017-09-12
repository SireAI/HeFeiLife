package com.sire.corelibrary.Lifecycle.ModuleLife;

import com.sire.mediators.core.Module;

import io.reactivex.Observable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface ModuleInitService<T> {
    Observable<T> moduleInitAction();
    Module serviceModule();
};