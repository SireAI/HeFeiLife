package com.sire.hefeilife.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;

import com.sire.corelibrary.Bug.CleanLeakUtils;
import com.sire.corelibrary.Bug.IMMLeaks;
import com.sire.corelibrary.Controller.MainContainerComponnent;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Delegate.HomeTabDelegate;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadWebService;
import com.sire.corelibrary.RecyclerView.OnScrollDelegate;
import com.sire.hefeilife.Controller.fragment.DiscoveryController;
import com.sire.hefeilife.Controller.fragment.MessageController;
import com.sire.hefeilife.Controller.fragment.MineController;
import com.sire.hefeilife.R;
import com.sire.hefeilife.Views.NavigateTabItem;
import com.sire.hefeilife.Views.NoScrollViewPager;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.mediators.UpgradeModuleInterface.UpgradeMediator;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.AppExit;
import com.sire.upgrademodule.ViewMoudle.AppUpgradeViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.sire.corelibrary.Networking.downlaod.DownloadCacheUtil.getBasUrl;

public class MainController extends SireController implements MainContainerComponnent, OnScrollDelegate, OnTabItemSelectedListener {

    @Inject
    UpgradeMediator upgradeMediator;
    @Inject
    FeedMediator feedMediator;
    @Inject
    MessagePushMediator messagePushMediator;
    @Inject
    UserMediator userMediator;

    @Inject
    AppUpgradeViewModel appUpgradeViewModel;
    private PageNavigationView tab;
    private NavigationController navigationController;
    private List<Fragment> pages;
    private MessgeCountReceiver messageReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_main);
        tab = findViewById(R.id.tab);
        NoScrollViewPager viewPagerContainer = findViewById(R.id.vp_page_container);
        navigationController = tab.custom()
                .addItem(NavigateTabItem.newItem(R.mipmap.home_unchecked, R.mipmap.home_checked, R.mipmap.home_pressed, "微讯", this))
                .addItem(NavigateTabItem.newItem(R.mipmap.search_unchecked, R.mipmap.search_checked, R.mipmap.search_pressed, "发现", this))
                .addItem(NavigateTabItem.newItem(R.mipmap.message_unchecked, R.mipmap.message_checked, R.mipmap.message_pressed, "消息", this))
                .addItem(NavigateTabItem.newItem(R.mipmap.mine_unchecked, R.mipmap.mine_checked, R.mipmap.mine_pressed, "我的", this))
                .build();
        navigationController.addTabItemSelectedListener(this);
        viewPagerContainer.setNoScroll(true);
        viewPagerContainer.setOffscreenPageLimit(4);
        viewPagerContainer.setAdapter(new PagerAdapter(getSupportFragmentManager(), pages = initPages()));
        navigationController.setupWithViewPager(viewPagerContainer);
        tab.postDelayed(() -> upgradeMediator.checkVersion(MainController.this),500);
        registerMessageBroadcast();

    }

    private void registerMessageBroadcast() {
        IntentFilter intentFilter = new IntentFilter("com.sire.messagecount");
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver = new MessgeCountReceiver(navigationController),intentFilter);
    }


    private List<Fragment> initPages() {
        List<Fragment> pages = new ArrayList<>();
        pages.add((Fragment) feedMediator.getInformationFlowController());
        pages.add(new DiscoveryController());
        pages.add((Fragment) messagePushMediator.getMessageController());
        pages.add(new MineController());
        return pages;
    }

    /**
     * show or hide the actionbar
     *
     * @param show
     */
    public void toggleActionBar(boolean show) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (show) {
            if (actionBar.isShowing()) {
                return;
            }
            actionBar.show();
        } else {
            if (actionBar.isShowing()) {
                actionBar.hide();
            }
        }
    }


    /**
     * 回调传递
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pages != null) {
            for (int i = 0; i < pages.size(); i++) {
                pages.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public PageNavigationView getPageNavigationView() {
        return tab;
    }

    @Override
    public void onScroll(RecyclerView recyclerView, int dx, int dy) {
        if (navigationController != null&&navigationController.getSelected()==0) {
            if (dy > 8) {//列表向上滑动
                navigationController.hideBottomLayout();
            } else if (dy < -8) {//列表向下滑动
                navigationController.showBottomLayout();
            }
        }
    }

    @Override
    public void onSelected(int index, int old) {
        //消息与用户认证关联，查勘前检测是否登陆
        if(index == 2){
            userMediator.isLoginState(this,true);
        }
    }

    @Override
    public void onRepeat(int index) {
        if (index == 0) {
            if (pages != null && pages.size() > 0) {
                ((HomeTabDelegate) pages.get(0)).onTabClickRepeat(index);
            }
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            if (fragments != null && fragments.size() > 0) {
                return fragments.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (fragments != null) {
                return fragments.size();
            }
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(messageReceiver!=null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        }

        AppExit appExit = new AppExit();
        appExit.setApp(getApplication());
        EventBus.getDefault().post(appExit);

    }

    /**
     * 消息广播接收
     */
    private static class MessgeCountReceiver extends BroadcastReceiver{
        private NavigationController navigationController;

        public MessgeCountReceiver(NavigationController navigationController) {
            this.navigationController = navigationController;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(navigationController!=null){
                int messageCount = intent.getIntExtra("messageCount", 0);
                navigationController.setHasMessage(2,messageCount>0);
                navigationController.setMessageNumber(2,0);
            }
        }
    }
}
