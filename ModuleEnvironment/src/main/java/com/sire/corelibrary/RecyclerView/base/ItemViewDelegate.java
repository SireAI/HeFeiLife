package com.sire.corelibrary.RecyclerView.base;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/6
 * Author:sire
 * Description:
 * ==================================================
 */
public interface ItemViewDelegate<T> {
    //由viewType确定布局
    int getItemViewLayoutId(int viewType);
    //由数据和位置共同确定viewType
    boolean isForViewType(T item, int position);
    //进行viewholder的数据与视图的设置
    void convert(ViewHolder holder, T t, int position);
}
