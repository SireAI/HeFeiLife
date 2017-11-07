package com.sire.feedmodule.Mediator;

import com.sire.feedmodule.Controller.InformationFlowController;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;

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
@Singleton
public class FeedMediatorImpl implements FeedMediator {
    private  InformationFlowController controller;

    @Inject
    public FeedMediatorImpl(InformationFlowController controller) {
        this.controller = controller;
    }

    @Override
    public Object getViewController() {
        return controller;
    }
}
