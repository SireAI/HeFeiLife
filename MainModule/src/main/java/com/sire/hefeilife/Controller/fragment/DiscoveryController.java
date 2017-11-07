package com.sire.hefeilife.Controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/27
 * Author:Sire
 * Description:
 * ==================================================
 */

public class DiscoveryController extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        TextView textView = new TextView(getContext());
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setText("Test\n\n" +"发现");
        textView.setBackgroundColor(0xFFececec);
        return textView;
    }
}
