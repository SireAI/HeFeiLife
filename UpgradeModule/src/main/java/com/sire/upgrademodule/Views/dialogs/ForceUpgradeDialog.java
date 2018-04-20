package com.sire.upgrademodule.Views.dialogs;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sire.upgrademodule.Pojo.UpgradeInfor;
import com.sire.upgrademodule.R;
import com.sire.upgrademodule.databinding.DialogForceUpgradeBinding;

import java.io.Serializable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ForceUpgradeDialog extends DialogFragment {

    public static final String CALLBACK = "callback";

    public static final String UPGRADE_INFOR = "upgradeInfor";

    public static void showDialog(FragmentActivity activity, UpgradeInfor upgradeInfor, Callback callback) {
        ForceUpgradeDialog forceUpgradeDialog = new ForceUpgradeDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CALLBACK, callback);
        bundle.putSerializable(UPGRADE_INFOR, upgradeInfor);
        forceUpgradeDialog.setArguments(bundle);
        forceUpgradeDialog.show(activity.getSupportFragmentManager(), "ForceUpgradeDialog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_force_upgrade, container);
        DialogForceUpgradeBinding bind = DataBindingUtil.bind(view);
        Bundle arguments = getArguments();
        UpgradeInfor upgradeInfor = (UpgradeInfor) arguments.getSerializable(UPGRADE_INFOR);
        Callback callback = (Callback) arguments.getSerializable(CALLBACK);
        bind.setUpgradeInfor(upgradeInfor);
        bind.setCallback(callback);
        bind.setDialog(this);
        setCancelable(!upgradeInfor.isForceUpgrade());
        if (upgradeInfor.isForceUpgrade()) {
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            });
        }

        return view;
    }

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.dialogTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    public interface Callback extends Serializable {
        void onUpgrade(DialogFragment dialog, boolean force);
    }
}
