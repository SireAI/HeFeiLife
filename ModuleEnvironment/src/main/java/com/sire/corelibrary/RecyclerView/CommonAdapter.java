package com.sire.corelibrary.RecyclerView;

import android.support.v7.widget.RecyclerView;

import com.sire.corelibrary.RecyclerView.base.ItemViewDelegate;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;

import java.util.List;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/6
 * Author:sire
 * Description:
 * ==================================================
 */
public abstract class CommonAdapter<T> extends AutoViewStateAdapter<T> {

    public CommonAdapter(RecyclerView recyclerView, final int layoutId, List<T> datas) {
        super(datas, recyclerView);
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId(int viewType) {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
