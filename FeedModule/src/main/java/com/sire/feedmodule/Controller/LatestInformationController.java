package com.sire.feedmodule.Controller;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sire.corelibrary.DI.Injectable;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.OnScrollDelegate;
import com.sire.corelibrary.RecyclerView.base.ItemViewDataBindingDelegate;
import com.sire.feedmodule.BR;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.R;
import com.sire.feedmodule.ViewModel.FeedViewModel;
import com.sire.feedmodule.Views.TopToast;

import java.util.List;

import javax.inject.Inject;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */

public class LatestInformationController extends LifecycleFragment implements OnRefreshListener, OnLoadmoreListener, Injectable {


    @Inject
    ViewModelProvider.Factory factory;
    private SmartRefreshLayout swipeRefreshView;
    private InformationAdapter informationAdapter;
    private FeedViewModel feedViewModel;

    private void initModel() {
        feedViewModel = ViewModelProviders.of(this, factory).get(FeedViewModel.class);
        feedViewModel.getFeedInfors().observe(this, (DataResource<List<FeedInfor>> listDataResource) -> {
            switch (listDataResource.status) {
                case SUCCESS:
                    closeDataLoading();
                    if (informationAdapter != null) {
                        List<FeedInfor> newFeedInfors = feedViewModel.collectFeedDataList(informationAdapter.getDataSource(), listDataResource, new FeedViewModel.RefreshNew() {
                            @Override
                            public void onNew(int count) {
                                    TopToast.showToast(getActivity(), String.format("更新了%d条信息", count));
                            }

                            @Override
                            public void onNoMore() {
                                if(swipeRefreshView!=null){
                                    // TODO: 2017/11/6 加载更多优化查勘新库
                                    swipeRefreshView.setEnableAutoLoadmore(false);
                                }
                            }
                        });
                        informationAdapter.refreshDataSource(newFeedInfors);
                    }
                    break;
                case ERROR:
                    closeDataLoading();

                    break;
                case LOADING:

                    break;
                default:
                    break;
            }
        });
    }


    private void closeDataLoading() {
        if (swipeRefreshView.isRefreshing()) {
            swipeRefreshView.finishRefresh();
        }
        if (swipeRefreshView.isLoading()) {
            swipeRefreshView.finishLoadmore();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_latest_information, container, false);
        initView(view);
        initModel();
        return view;
    }

    private void initView(View view) {
        //Recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.rv_information);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = getResources().getDrawable(R.drawable.shape_line);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        if (getActivity() instanceof OnScrollDelegate) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    ((OnScrollDelegate) getActivity()).onScroll(recyclerView, dx, dy);
                }
            });
        }

        //Adapter
        informationAdapter = new InformationAdapter(null, recyclerView);
        recyclerView.setAdapter(informationAdapter);
        //pull Refresh

        swipeRefreshView = view.findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setOnLoadmoreListener(this);
        swipeRefreshView.setDisableContentWhenLoading(true);
        swipeRefreshView.setDisableContentWhenRefresh(true);
        swipeRefreshView.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefreshView.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
//        swipeRefreshView.autoRefresh();

    }


    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        feedViewModel.getNewFeedInfor(feedViewModel.getMaxTimeLineBy(informationAdapter.getDataSource()));
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()));
    }


    public class InformationAdapter extends AutoViewStateAdapter<FeedInfor> {

        public InformationAdapter(List<FeedInfor> dataSource, RecyclerView recyclerView) {
            super(dataSource, recyclerView);
            addItemViewDelegate(new ItemViewDataBindingDelegate<FeedInfor>() {
                @Override
                protected int getItemBRName() {
                    return BR.feedItem;
                }

                @Override
                public int getItemViewLayoutId() {
                    return R.layout.view_componnent_information_floa_item;
                }

                @Override
                public boolean isForViewType(FeedInfor item, int position) {
                    return true;
                }
            });
        }

        @Override
        protected boolean areContentsTheSame(FeedInfor oldItem, FeedInfor newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        protected boolean areItemsTheSame(FeedInfor oldItem, FeedInfor newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    }

}
