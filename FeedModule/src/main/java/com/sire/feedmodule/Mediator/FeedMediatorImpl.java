package com.sire.feedmodule.Mediator;

import android.os.Bundle;

import com.sire.feedmodule.Controller.fragment.InformationFlowController;
import com.sire.feedmodule.Controller.fragment.UserDynamicController;
import com.sire.feedmodule.ViewModel.FeedViewModel;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.core.CallBack;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */
public class FeedMediatorImpl implements FeedMediator {
    private final Lazy<FeedViewModel> feedViewModel;
    private  final Lazy<InformationFlowController> informationFlowController;
    @Inject
     Provider<UserDynamicController> userDynamicControllerProvider;

    @Inject
    public FeedMediatorImpl(Lazy<InformationFlowController> informationFlowController, Lazy<FeedViewModel> feedViewModel) {
        this.informationFlowController = informationFlowController;
        this.feedViewModel = feedViewModel;
    }

    @Override
    public Object getInformationFlowController() {
        return informationFlowController.get();
    }

    @Override
    public Object getUserDynamicController() {
        return userDynamicControllerProvider.get();
    }

    @Override
    public Object follow(String followingId) {
        return feedViewModel.get().follow(followingId);
    }

    @Override
    public Object cancelFollow(String followingId) {
        return feedViewModel.get().cancelFollow(followingId);
    }

    @Override
    public void tickPraise(boolean praised, String feedId,String postAuthorId,String postTitle, CallBack<Boolean> callBack) {
         feedViewModel.get().tickPraise(praised,feedId,postAuthorId,postTitle,callBack);
    }

    @Override
    public void getFeedFocusInfor(String authorId, String feedId, CallBack<Map<String, Boolean>> callBack) {
        feedViewModel.get().getFeedFocusInfor(authorId,feedId,callBack);
    }

    @Override
    public void getFeedPraiseInfor(String feedId, CallBack<String> callBack) {
        feedViewModel.get().getFeedPraiseInfor(feedId,callBack);
    }

    @Override
    public void deleteFeed(String feedId,CallBack<Boolean> callBack) {
        
         feedViewModel.get().deleteFeed(feedId,callBack);
    }

    @Override
    public void getPostInforById(String feedId, CallBack<Map<String, String>> callBack) {
        feedViewModel.get().getFeedById(feedId,callBack);
    }

}
