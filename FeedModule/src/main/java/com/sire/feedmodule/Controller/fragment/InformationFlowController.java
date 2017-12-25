package com.sire.feedmodule.Controller.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sire.feedmodule.R;
import com.sire.mediators.BBSModuleInterface.BBSMediator;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.sire.feedmodule.Constant.Constant.FEED_INFOR;
import static com.sire.feedmodule.Constant.Constant.FEED_TYPE;
import static com.sire.feedmodule.Constant.Constant.USER_FEED;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/27
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class InformationFlowController extends LifecycleFragment implements View.OnClickListener {
    @Inject
    BBSMediator bbsMediator;
    private FragmentPagerItemAdapter adapter;

    @Inject
    public InformationFlowController() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_information_flow, container, false);
        view.findViewById(R.id.fab).setOnClickListener(this);
        adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.hot_information, FeedInformationController.class, setArguments(FEED_INFOR))
                .add(R.string.attention_information, FeedInformationController.class, setArguments(USER_FEED))
                .create());
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        return view;
    }

    private Bundle setArguments(String feedType) {
        Bundle bundle = new Bundle();
        bundle.putString(FEED_TYPE, feedType);
        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(adapter!=null){
            for (int i = 0; i < adapter.getCount(); i++) {
                adapter.getPage(i).onActivityResult(requestCode,resultCode,data);
            }
        }
    }

    @Override
    public void onClick(View view) {
        bbsMediator.segueToPostPublishController(getActivity(),view);
    }
}
