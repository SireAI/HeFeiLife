package com.sire.corelibrary.Controller;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sire.corelibrary.Permission.PermissionHandler;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;



/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/03
 * Author:Sire
 * Description:
 * ==================================================
 */

public abstract class SireController extends AppCompatActivity implements LifecycleRegistryOwner,HasSupportFragmentInjector {
    public static final int SEGUE_TIME_DEFAULT_DELAY_MIN = 250;
    public static final int SEGUE_TIME_DEFAULT_DELAY_MAX = 1000;
    public static final int FOR_CONTROLLER_BACK = -100;

    @Inject
    Segue mSegue;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private PermissionHandler permissionHandler;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setActionBarEnabled(@NonNull Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * @param permissions permission in mainfest to request
     */
    protected void requestNeedPermissions(String[] permissions) {
        permissionHandler = new PermissionHandler(permissions);
        permissionHandler.requestPermissions(this,false);
    }


    protected void showPermissionInstuctions(View view) {
        if (permissionHandler != null) {
            permissionHandler.showPermissionInstructions(view);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionHandler != null) {
            permissionHandler.onRequestPermissionsResult(this,requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isActionBarVisible() {
        return getSupportActionBar() != null && getSupportActionBar().isShowing();
    }

    /**
     * when you want to get the back result
     */
    protected void finishForResult(Intent data) {
        if (data == null) {
            throw new RuntimeException("data can't be null !");
        }
        setResult(FOR_CONTROLLER_BACK, data);
        onBackPressed();
    }

    /**
     * instead of startActivity,we can only use this method to go to anothoer controller
     *
     * @param segueType the way of animation you want to show the controller
     * @param intent    same as the old intent
     */
    public void segue(Segue.SegueType segueType, Intent intent) {
        if (intent == null) {
            return;
        }
        mSegue.segueForward(segueType, intent, this);
    }

    /**
     * segue delay time
     *
     * @param segueType
     * @param intent
     * @param timeDelay
     */
    public void segue(final Segue.SegueType segueType, final Intent intent, int timeDelay) {
        timeDelay = Math.max(timeDelay, SEGUE_TIME_DEFAULT_DELAY_MIN);
        timeDelay = Math.min(timeDelay, SEGUE_TIME_DEFAULT_DELAY_MAX);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                segue(segueType, intent);
            }
        }, timeDelay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSegue.segueBack(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSegue = null;
    }

    //lifecycle
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    //fragmetn inject support
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
