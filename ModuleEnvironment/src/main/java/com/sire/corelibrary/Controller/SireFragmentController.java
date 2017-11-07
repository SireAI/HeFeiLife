package com.sire.corelibrary.Controller;

import android.app.Activity;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/01
 * Author:Sire
 * Description:
 * ==================================================
 */

public class SireFragmentController extends Fragment implements HasSupportFragmentInjector{
@Inject
DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        System.out.println("======dispatchingFragmentInjector"+dispatchingFragmentInjector);
        return dispatchingFragmentInjector;
    }
}
