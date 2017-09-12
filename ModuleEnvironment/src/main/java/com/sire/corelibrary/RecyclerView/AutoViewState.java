package com.sire.corelibrary.RecyclerView;

import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.R;
import com.sire.corelibrary.Utils.UIUtils;

import java.util.List;



/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/07
 * Author:Sire
 * Description:
 * ==================================================
 */
public class AutoViewState  {
    private ViewState viewState = ViewState.LOADING;
    private boolean isShowLoadingState = false;
    private View.OnClickListener errorClickListener;

    <T> int getItemCount(List<T> mDataSource) {
        int itemCount ;
        if(isShowLoadingState){
            if(mDataSource == null || mDataSource.size()==0){
                itemCount = 1;
                this.viewState = ViewState.LOADING;
                return itemCount;
            }
        }
        if (mDataSource == null) {
            itemCount = 1;
            this.viewState = ViewState.ERROR;
        } else if (mDataSource.size() == 0) {
            itemCount = 1;
            this.viewState = ViewState.EMPTY;
        } else {
            itemCount = mDataSource.size();
            this.viewState = ViewState.NORMAL;
        }
        return itemCount;
    }

    int getItemViewType() {
        switch (viewState) {
            case LOADING:
                return ViewState.LOADING.viewStateType;
            case NORMAL:
                return ViewState.NORMAL.viewStateType;
            case EMPTY:
                return ViewState.EMPTY.viewStateType;
            case ERROR:
                return ViewState.ERROR.viewStateType;
            default:
                break;
        }
        throw new RuntimeException("no view state match!");
    }

    public View createStateView(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 101://ERROR
                view = UIUtils.inflate(R.layout.view_componnent_error, parent);
                setErrorClick(view);
                break;
            case 102://LOADING
                view = UIUtils.inflate(R.layout.view_componnent_loading, parent);
                break;
            case 103://EMPTY
                view = UIUtils.inflate(R.layout.view_componnent_empty, parent);
                break;
        }
        return view;
    }

    private void setErrorClick(View view) {
        View retrayBtn = view.findViewById(R.id.net_error_btn);
        retrayBtn.setOnClickListener(errorClickListener);
    }

    public boolean isNormalView() {
        return viewState == ViewState.NORMAL;
    }

    public ViewState getViewState() {
        return viewState;
    }

     void setLoadingState(boolean isShowLoadingState) {
        this.isShowLoadingState = isShowLoadingState;
    }
    public void setOnErrorClick(View.OnClickListener onClickListener){
        this.errorClickListener = onClickListener;
    }
}
