package com.sire.corelibrary.View;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sire.corelibrary.R;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ToastSuccess extends DialogFragment {
    private static ToastSuccess toastSuccess;
    public interface CallBack{
        void onFinish();
    }

    public static void showDialog(FragmentActivity activity,CallBack callBack) {
        showDialog(activity,"",callBack);
    }
    public static void showDialog(FragmentActivity activity,String message) {
        toastSuccess = new ToastSuccess();
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        toastSuccess.setArguments(bundle);
        toastSuccess.show(activity.getSupportFragmentManager(), "toastSuccess");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                close();
            }
        },1000);
    }
    public static void showDialog(FragmentActivity activity,String message,CallBack callBack) {
        toastSuccess = new ToastSuccess();
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        toastSuccess.setArguments(bundle);
        toastSuccess.show(activity.getSupportFragmentManager(), "toastSuccess");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                close();
                if(callBack!=null){
                    callBack.onFinish();
                }
            }
        },1000);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_componnent_success, container);
       String message =  getArguments().getString("message");
       if(!TextUtils.isEmpty(message)){
           TextView tvMessage =  view.findViewById(R.id.tv_message);
           tvMessage.setText(message);
       }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.toastTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    public static void close(){
        if(toastSuccess !=null){
            toastSuccess.dismiss();
            toastSuccess = null;
        }
    }
}
