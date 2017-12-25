package com.sire.feedmodule.Controller.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.feedmodule.BR;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.R;
import com.sire.feedmodule.ViewModel.CacheClearViewModel;
import com.sire.feedmodule.ViewModel.FeedViewModel;
import com.sire.feedmodule.Views.TopToast;
import com.sire.mediators.BBSModuleInterface.BBSMediator;
import com.sire.mediators.UserModuleInterface.UserLoginState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.sire.corelibrary.Controller.SireController.FOR_CONTROLLER_BACK;
import static com.sire.feedmodule.Constant.Constant.FEED_INFOR;
import static com.sire.feedmodule.Constant.Constant.FEED_TYPE;
import static com.sire.feedmodule.Constant.Constant.USER_FEED;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */

public class FeedInformationController extends LifecycleFragment implements OnRefreshListener, OnLoadmoreListener, Injectable, AutoViewStateAdapter.OnItemClickListener {


    /**
     * 用户关注的人
     */
    private static List<String> followIds = new ArrayList<>();
    protected SmartRefreshLayout swipeRefreshView;
    protected InformationAdapter informationAdapter;
    protected FeedViewModel feedViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    CacheClearViewModel cacheClearViewModel;
    @Inject
    BBSMediator bbsMediator;
    /**
     * 首次初始化
     */
    private boolean init = true;

    private void initModel() {
        feedViewModel = ViewModelProviders.of(this, factory).get(FeedViewModel.class);
        fetchFeeds();
        checkFollowingStateAndRefresh();
    }

    /**
     * 获取feed流信息
     */
    private void fetchFeeds() {
        feedViewModel.getFeedInfors().observe(this, (DataResource<List<FeedInfor>> listDataResource) -> {
            switch (listDataResource.status) {
                case SUCCESS:
                    closeDataLoading();
                    refreshFeeds(listDataResource.data);
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

    private void refreshFeeds(List<FeedInfor> listDataResource) {
        if (informationAdapter != null) {
            List<FeedInfor> newFeedInfors = feedViewModel.collectFeedDataList(informationAdapter.getDataSource(), listDataResource, new FeedViewModel.RefreshNew() {
                @Override
                public void onNew(int count) {
                    TopToast.showToast(getActivity(), String.format("更新了%d条信息", count));
                }

                @Override
                public void onNoMore() {
                    if (swipeRefreshView != null) {
                        // TODO: 2017/11/6 加载更多优化查看新库
                        swipeRefreshView.setEnableAutoLoadmore(false);
                    }
                }
            });
            informationAdapter.refreshDataSource(newFeedInfors);
        }
    }

    /**
     * 检测本地的关注信息，刷新feed信息流
     */
    private void checkFollowingStateAndRefresh() {
        feedViewModel.getFollowings().observe(this, dataResource -> {
            switch (dataResource.status) {
                case SUCCESS:
                    //监听关注数据变动，刷新UI
                    feedViewModel.observeDBDataChange().observe(FeedInformationController.this, feedDataResource -> {
                        switch (feedDataResource.status) {
                            case SUCCESS:
                                followIds.clear();
                                for (int i = 0; i < feedDataResource.data.size(); i++) {
                                    followIds.add(feedDataResource.data.get(i).getFollowingId());
                                }

                                if (init) {
                                    //获取以前时间为时间线的历史数据
                                    feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()), getFeedType());
                                    init = false;
                                } else {
                                    informationAdapter.notifyDataSetChanged();
                                }

                                break;
                            case ERROR:
                                break;
                            case LOADING:
                                break;
                        }
                    });
                    break;
                case ERROR:
                    break;
                case LOADING:
                    break;
                default:
                    break;
            }

        });
    }

    /**
     * 用户登陆成功会回调此方法
     *
     * @param userLoginState
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserLoginState userLoginState) {
        Timber.d(userLoginState.isLogin() ? "用户登陆成功" : "用户退出登陆状态成功");
        //刷新信息流中feed的状态,主动同步关注数据至数据库
        feedViewModel.getFollowings().observe(this, dataResource -> {
        });
        if (getFeedType().equals(USER_FEED)) {
            //重新获取关注feed
            feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()), getFeedType());
        }
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
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.controller_feed_information, container, false);
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
        //初始为加载状态
        informationAdapter.setLoadingState();
        informationAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(informationAdapter);
        //pull Refresh
        swipeRefreshView = view.findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setOnLoadmoreListener(this);

        swipeRefreshView.setDisableContentWhenLoading(true);
        swipeRefreshView.setDisableContentWhenRefresh(true);
        swipeRefreshView.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefreshView.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));


    }

    public String getFeedType() {
        Bundle arguments = getArguments();
        String feedType = arguments.getString(FEED_TYPE, FEED_INFOR);
        return feedType;
    }

    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        feedViewModel.getNewFeedInfor(feedViewModel.getMaxTimeLineBy(informationAdapter.getDataSource()), getFeedType());
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()), getFeedType());
    }

    @Override
    public void onItemClick(View view, ViewHolder holder, int position) {
        segueToPostController(position);
    }

    private void segueToPostController(int position) {
        FeedInfor feedInfor = informationAdapter.getDataSource().get(position);
        String feedStr = JSONUtils.bean2JsonString(feedInfor);
        bbsMediator.segueToPostController(getActivity(), feedStr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FOR_CONTROLLER_BACK) {
            if (requestCode == 104 && getFeedType().equals(FEED_INFOR)) {
                //帖子删除返回
                String deleteFeedId = data.getStringExtra("feedId");
                List<FeedInfor> dataSource = informationAdapter.getDataSource();
                for (int i = 0; i < dataSource.size(); i++) {
                    if (dataSource.get(i).getFeedId().equals(deleteFeedId)) {
                        dataSource.remove(i);
                        informationAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public static class InformationAdapter extends AutoViewStateAdapter<FeedInfor> {

        public InformationAdapter(List<FeedInfor> dataSource, RecyclerView recyclerView) {
            super(dataSource, recyclerView);
            addItemViewDelegate(new FeedItemDelegate());
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

    public static class FeedItemDelegate extends ItemViewDataBindingDelegate<FeedInfor> {
        @Override
        protected int getItemBRName() {
            return BR.feedItem;
        }

        @Override
        public int getItemViewLayoutId() {
            return R.layout.view_componnent_information_flow_item;
        }

        @Override
        public void convert(ViewHolder holder, FeedInfor item, int position) {
            item.setFollow(followIds.contains(item.getAuthorId()));
            super.convert(holder, item, position);
        }


        @Override
        public boolean isForViewType(FeedInfor item, int position) {
            return true;
        }
    }
}
