package com.sire.corelibrary.Networking.NetConnection;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Event.NetChangeEvent;
import com.sire.corelibrary.R;
import com.sire.corelibrary.Utils.NetUtil;
import com.sire.corelibrary.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;




/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class NetViewHandler {
    private static final String ANDROID_NET_WIFI_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    private View netLink;

    public NetViewHandler(SireController controller) {
        EventBus.getDefault().register(this);
        initNetDisconnectedView(controller);
    }

    private void initNetDisconnectedView(final SireController controller) {
        netLink = LayoutInflater.from(controller).inflate(R.layout.view_componnent_net_check, null);
        netLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ANDROID_NET_WIFI_PICK_WIFI_NETWORK);
                controller.startActivity(intent);
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(netLink.getContext(),45));
        controller.addContentView(netLink, layoutParams);
        setViewVisibleState();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetChangeEvent event) {
        setViewVisibleState();
    }

    private void setViewVisibleState() {
        if(netLink!=null){
            netLink.setVisibility(NetUtil.isNetConnect(netLink.getContext()) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }
}
