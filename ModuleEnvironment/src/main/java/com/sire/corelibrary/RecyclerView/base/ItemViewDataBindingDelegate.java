package com.sire.corelibrary.RecyclerView.base;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/06
 * Author:Sire
 * Description:
 * ==================================================
 */

public abstract class ItemViewDataBindingDelegate<T> implements ItemViewDelegate<T> {
    @Override
    public void convert(ViewHolder holder, T item, int position) {
        holder.getDataBinding().setVariable(getItemBRName(),item);
        holder.getDataBinding().executePendingBindings();
    }
    protected abstract int getItemBRName();
}
