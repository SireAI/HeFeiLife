package com.sire.corelibrary.RecyclerView;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/07
 * Author:Sire
 * Description:
 * ==================================================
 */

public enum ViewState {
    NORMAL(100), ERROR(101), LOADING(102), EMPTY(103);
    public int viewStateType;

    ViewState(int viewStateType) {
        this.viewStateType = viewStateType;
    }
}
