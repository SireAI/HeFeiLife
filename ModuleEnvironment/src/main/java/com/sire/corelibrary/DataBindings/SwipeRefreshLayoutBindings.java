package com.sire.corelibrary.DataBindings;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.adapters.ListenerUtil;
import android.support.v4.widget.SwipeRefreshLayout;

import com.sire.corelibrary.R;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/10
 * Author:Sire
 * Description:
 * ==================================================
 */
@InverseBindingMethods({
        @InverseBindingMethod(
                type = android.support.v4.widget.SwipeRefreshLayout.class,
                attribute = "refreshing",
                event = "refreshingAttrChanged",
                method = "isRefreshing")})
public class SwipeRefreshLayoutBindings {
    @BindingAdapter(value = {"onRefreshListener", "refreshingAttrChanged"}, requireAll = false)
    public static void setOnRefreshListener(final SwipeRefreshLayout view, final SwipeRefreshLayout.OnRefreshListener listener, final InverseBindingListener refreshingAttrChanged) {
        SwipeRefreshLayout.OnRefreshListener newValue = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listener != null) {
                    listener.onRefresh();
                }
                if (refreshingAttrChanged != null) {
                    refreshingAttrChanged.onChange();

                }
            }
        };
        SwipeRefreshLayout.OnRefreshListener oldValue = ListenerUtil.trackListener(view, newValue, R.id.onRefreshListener);
        if (oldValue != null) {
            view.setOnRefreshListener(null);
        }
        view.setOnRefreshListener(newValue);
    }

}
