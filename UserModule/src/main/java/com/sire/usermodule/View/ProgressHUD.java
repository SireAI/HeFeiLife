package com.sire.usermodule.View;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.usermodule.R;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ProgressHUD extends DialogFragment{

    private static ProgressHUD progressHUD;

    public static void showDialog(FragmentActivity activity) {
        close();
        progressHUD = new ProgressHUD();

        progressHUD.show(activity.getSupportFragmentManager(), "ProgressHUD");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading_fullscreen, container);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.progressHUDTheme);

        return super.onCreateDialog(savedInstanceState);
    }

    public static void close(){
        if(progressHUD !=null){
            progressHUD.dismiss();
            progressHUD = null;
        }
    }
}
