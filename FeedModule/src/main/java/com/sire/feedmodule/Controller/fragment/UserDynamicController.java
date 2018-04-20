package com.sire.feedmodule.Controller.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.sire.corelibrary.DI.Injectable;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.R;
import com.sire.feedmodule.ViewModel.FeedViewModel;
import com.sire.feedmodule.Views.TopToast;
import com.sire.mediators.BBSModuleInterface.BBSMediator;

import java.util.List;

import javax.inject.Inject;

import static com.sire.corelibrary.Controller.SireController.FOR_CONTROLLER_BACK;
import static com.sire.feedmodule.Constant.Constant.USER_DYNAMICS;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/05
 * Author:Sire
 * Description:
 * ==================================================
 */
public class UserDynamicController extends Fragment implements Injectable, OnLoadmoreListener, AutoViewStateAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private SmartRefreshLayout swipeRefreshView;
    private FeedInformationController.InformationAdapter informationAdapter;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    BBSMediator bbsMediator;
    private FeedViewModel feedViewModel;

    @Inject
    public UserDynamicController() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.controller_user_dynamic, container, false);
        initView(view);
        initModel();
        return view;
    }

    private void initModel() {
        feedViewModel = ViewModelProviders.of(this, factory).get(FeedViewModel.class);
        feedsCallBack();
        getFeed(getArguments().getString("authorId"));
    }

    private void getFeed(String userDynamicId) {
            //获取以前时间为时间线的历史数据
            feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()), USER_DYNAMICS,userDynamicId);
    }

    /**
     * 获取feed流信息
     */
    private void feedsCallBack() {
        feedViewModel.getFeedInfors().observe(this, (DataResource<List<FeedInfor>> listDataResource) -> {
            switch (listDataResource.status) {
                case SUCCESS:
                case ERROR:
                    closeDataLoading();
                    refreshFeedUI(listDataResource.data);
                    break;
                case LOADING:
                    break;
                default:
                    break;
            }
        });
    }

    private void refreshFeedUI(List<FeedInfor> listDataResource) {
        if (informationAdapter != null) {
            feedViewModel.collectFeedDataList(informationAdapter.getDataSource(), listDataResource, new FeedViewModel.RefreshNew() {
                @Override
                public void onNew(int count) {
                    TopToast.showToast(getActivity(), String.format("更新了%d条信息", count));
                }

                @Override
                public void onNoMore() {
                    if (swipeRefreshView != null) {
                        swipeRefreshView.setLoadmoreFinished(true);
                    }
                }

                @Override
                public void onCallBack(List<FeedInfor> feedInfors) {
                    informationAdapter.refreshDataSource(feedInfors);
                }
            });
        }
    }

    private void closeDataLoading() {
        if (swipeRefreshView.isLoading()) {
            swipeRefreshView.finishLoadmore();
        }
    }

    private void initView(View view) {
        //Recyclerview
        recyclerView = view.findViewById(R.id.rv_information);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = getResources().getDrawable(R.drawable.shape_line_6);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Adapter
        informationAdapter = new FeedInformationController.InformationAdapter(null, recyclerView);
        //初始为加载状态
        informationAdapter.setLoadingState();
        informationAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(informationAdapter);
        //pull Refresh
        swipeRefreshView = view.findViewById(R.id.srl);
        swipeRefreshView.setEnableRefresh(false);
        swipeRefreshView.setOnLoadmoreListener(this);

        swipeRefreshView.setDisableContentWhenLoading(true);
        swipeRefreshView.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefreshView.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
    }



    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        feedViewModel.getHistoryFeedInfor(feedViewModel.getMinTimeLineBy(informationAdapter.getDataSource()), USER_DYNAMICS);
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
            if (requestCode == 104) {
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
        recyclerView.setAdapter(null);
        recyclerView = null;
        swipeRefreshView = null;
        informationAdapter = null;
    }
}
