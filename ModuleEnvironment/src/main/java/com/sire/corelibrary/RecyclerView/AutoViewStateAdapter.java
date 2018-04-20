package com.sire.corelibrary.RecyclerView;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.RecyclerView.base.ItemViewDataBindingDelegate;
import com.sire.corelibrary.RecyclerView.base.ItemViewDelegate;
import com.sire.corelibrary.RecyclerView.base.ItemViewDelegateManager;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/07
 * Author:Sire
 * Description:
 * ==================================================
 */

public abstract class AutoViewStateAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements DataNotify{
    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    @Nullable
    private List<T> mDataSource;
    private AutoViewState autoViewState;
    private int dataVersion = 0;

    public AutoViewStateAdapter(List<T> dataSource, RecyclerView recyclerView) {
        this.mDataSource = dataSource;
        this.autoViewState = new AutoViewState();
        setErrorClick(recyclerView);
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public void setEmptyView(View view){
        if(autoViewState!=null){
            autoViewState.setEmptyView(view);
        }
    }

    /**
     * net error setting
     * @param recyclerView
     */
    private void setErrorClick(final RecyclerView recyclerView) {
        this.autoViewState.setOnErrorClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    if(adapter !=null && adapter instanceof DataNotify){
                        setLoadingState();
                        adapter.notifyDataSetChanged();
                    }
                    mOnItemClickListener.onNetErrorBtnClick();
                }
            }
        });
    }

    public AutoViewState getAutoViewState() {
        return autoViewState;
    }


    /**
     * UI刷新方法
     * @param dataSource
     */
    @MainThread
    public void refreshDataSource(List<T> dataSource) {
        dataVersion ++;
        if (mDataSource == null || mDataSource.size()==0) {
            if (dataSource == null) {
                return;
            }
            mDataSource = dataSource;
            this.autoViewState.setLoadingState(false);

            notifyDataSetChanged();

        } else if (dataSource == null) {
            int oldSize = mDataSource.size();
            mDataSource = null;
            this.autoViewState.setLoadingState(false);
            notifyItemRangeRemoved(0, oldSize);

        } else {
            final int startVersion = dataVersion;
            final List<T> oldItems = mDataSource;
            //出现运行时间特别长的情况
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult  doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems != null ? oldItems.size() : 0;
                        }

                        @Override
                        public int getNewListSize() {
                            return dataSource != null ? dataSource.size() : 0;
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = dataSource.get(newItemPosition);
                            return AutoViewStateAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = dataSource.get(newItemPosition);
                            return AutoViewStateAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    },false);
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    AutoViewStateAdapter.this.autoViewState.setLoadingState(false);
                    mDataSource = dataSource;
                    diffResult.dispatchUpdatesTo(AutoViewStateAdapter.this);
                }
            }.execute();
        }
    }

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    protected abstract boolean areItemsTheSame(T oldItem, T newItem) ;



    public void setLoadingState(){
        this.autoViewState.setLoadingState(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        ViewHolder holder = null;
        if (itemViewDelegate == null) {
            //代理为空的情况为几种特殊视图情况，当然也可以通过这种方式进行加载自定义视图来代替默认视图
            View stateView = autoViewState.createStateView(parent, viewType);
            if(stateView!=null){
                holder = ViewHolder.createViewHolder(stateView);
            }
        } else {
            int layoutId = itemViewDelegate.getItemViewLayoutId(viewType);
            if (itemViewDelegate instanceof ItemViewDataBindingDelegate) {
                holder = ViewHolder.createDataBindingViewHolder(parent, layoutId);
            } else {
                holder = ViewHolder.createViewHolder(parent, layoutId);
            }
            setListener(parent, holder, viewType);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(autoViewState.isNormalView()){
            convert(holder, mDataSource.get(position));
        }
    }
    protected void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }
    /**
     * 设置点击事件和长安事件
     *
     * @param parent
     * @param viewHolder
     * @param viewType
     */
    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnItemClickListener.onItemClick(v, viewHolder, position);
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
            }
            return false;
        });
    }
    protected boolean isEnabled(int viewType) {
        return true;
    }

    public ViewState getViewState(){
      return   autoViewState.getViewState();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()){
            throw new RuntimeException("there is no view delegate!");
        }
        int itemViewType = autoViewState.getItemViewType();
        if (itemViewType == ViewState.NORMAL.viewStateType) {
            itemViewType = mItemViewDelegateManager.getItemViewType(mDataSource.get(position), position);
        }
        return itemViewType;
    }


    @Override
    public int getItemCount() {
        return autoViewState.getItemCount(mDataSource);
    }


    /**
     * 添加view代理
     *
     * @param itemViewDelegate
     * @return
     */
    public AutoViewStateAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public AutoViewStateAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }


    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void notifyViewState() {
        this.autoViewState.setLoadingState(false);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int position);

        default boolean onItemLongClick(View view, ViewHolder holder, int position) {
            return false;
        }

        default void onNetErrorBtnClick() {

        }
    }

    @Nullable
    public List<T> getDataSource() {
        return mDataSource;
    }
}
