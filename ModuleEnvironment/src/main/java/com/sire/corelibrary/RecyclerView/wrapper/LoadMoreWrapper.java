package com.sire.corelibrary.RecyclerView.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.R;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.DataNotify;
import com.sire.corelibrary.RecyclerView.ViewState;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.corelibrary.RecyclerView.utils.WrapperUtils;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/7
 * Author:sire
 * Description:
 * ==================================================
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DataNotify {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId = R.layout.view_componnent_load_more;
    private boolean needShowLoadMoreView;
    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }

    private boolean isShowLoadMore(int position) {
        int innerItemCount = mInnerAdapter.getItemCount();
        return hasLoadMore() && innerItemCount > 0 && (position >= innerItemCount);
    }

    private boolean isStateShow() {
        if (mInnerAdapter instanceof HeaderAndFooterWrapper) {
            RecyclerView.Adapter innerAdapter = ((HeaderAndFooterWrapper) mInnerAdapter).getInnerAdapter();
            return isAutoStateShow(innerAdapter);
        }
        return isAutoStateShow(mInnerAdapter);
    }

    private boolean isAutoStateShow(RecyclerView.Adapter innerAdapter) {
        if (mInnerAdapter instanceof AutoViewStateAdapter) {
            ViewState viewState = ((AutoViewStateAdapter) mInnerAdapter).getViewState();
            return viewState == ViewState.NORMAL;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent, mLoadMoreLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {

        return mInnerAdapter.getItemCount() + (hasLoadMore() && isStateShow() && needShowLoadMoreView ? 1 : 0);
    }

    @Override
    public void notifyViewState() {
        ((DataNotify) mInnerAdapter).notifyViewState();
        notifyDataSetChanged();
    }

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    /**
     * self define load more view
     *
     * @param loadMoreView
     * @return
     */
    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    /**
     * wheather show load more view
     * @param isShow
     */
    public void setShowLoadMoreView(boolean isShow) {
        this.needShowLoadMoreView = isShow;
    }

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }
}
