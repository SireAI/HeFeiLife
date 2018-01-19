package com.sire.feedmodule.Mediator;

import android.os.Bundle;

import com.sire.feedmodule.Controller.fragment.InformationFlowController;
import com.sire.feedmodule.Controller.fragment.UserDynamicController;
import com.sire.feedmodule.ViewModel.FeedViewModel;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.core.CallBack;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */
public class FeedMediatorImpl implements FeedMediator {
    private final FeedViewModel feedViewModel;
    private final UserDynamicController userDynamicController;
    private  final InformationFlowController informationFlowController;

    @Inject
    public FeedMediatorImpl(InformationFlowController informationFlowController, UserDynamicController userDynamicController, FeedViewModel feedViewModel) {
        this.informationFlowController = informationFlowController;
        this.userDynamicController = userDynamicController;
        this.feedViewModel = feedViewModel;
    }

    @Override
    public Object getInformationFlowController() {
        return informationFlowController;
    }

    @Override
    public Object getUserDynamicController() {
        return userDynamicController;
    }

    @Override
    public Object follow(String followingId) {
        return feedViewModel.follow(followingId);
    }

    @Override
    public Object cancelFollow(String followingId) {
        return feedViewModel.cancelFollow(followingId);
    }

    @Override
    public void tickPraise(boolean praised, String feedId, CallBack<Boolean> callBack) {
         feedViewModel.tickPraise(praised,feedId,callBack);
    }

    @Override
    public void getFeedFocusInfor(String authorId, String feedId, CallBack<Map<String, Boolean>> callBack) {
        feedViewModel.getFeedFocusInfor(authorId,feedId,callBack);
    }

    @Override
    public void getFeedPraiseInfor(String feedId, CallBack<String> callBack) {
        feedViewModel.getFeedPraiseInfor(feedId,callBack);
    }

    @Override
    public void deleteFeed(String feedId,CallBack<Boolean> callBack) {
         feedViewModel.deleteFeed(feedId,callBack);
    }

}
