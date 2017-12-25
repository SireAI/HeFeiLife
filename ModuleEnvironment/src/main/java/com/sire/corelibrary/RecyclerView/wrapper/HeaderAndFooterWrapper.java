package com.sire.corelibrary.RecyclerView.wrapper;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.RecyclerView.DataNotify;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.corelibrary.RecyclerView.utils.WrapperUtils;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/6
 * Author:sire
 * Description:
 * ==================================================
 */
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DataNotify
{
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter)
    {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mHeaderViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 获取itemviewtype的类型，分为三种情况
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position))
        {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount()
    {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (isHeaderViewPos(position))
        {
            return;
        }
        if (isFooterViewPos(position))
        {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount()
    {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    /**
     * 设置宽度
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    /**
     * 头底部瀑布流设置满屏宽度
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position))
        {
            WrapperUtils.setFullSpan(holder);
        }
    }

    /**
     * 判断位置是否处于头部区域
     * @param position
     * @return
     */
    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    /**
     * 判断位置是否处于底部区域
     * @param position
     * @return
     */
    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view)
    {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view)
    {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    public int getFootersCount()
    {
        return mFootViews.size();
    }


    @Override
    public void notifyViewState() {
        ((DataNotify)mInnerAdapter).notifyViewState();
    }
    public void onDestroy(){
        if(mHeaderViews!=null){
            mHeaderViews.clear();
        }
        if(mFootViews!=null){
            mFootViews.clear();
        }
        if(mInnerAdapter!=null){
            mInnerAdapter = null;
        }
    }
}
