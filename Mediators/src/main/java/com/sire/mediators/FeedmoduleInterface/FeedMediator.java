package com.sire.mediators.FeedmoduleInterface;

import com.sire.mediators.core.CallBack;

import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface FeedMediator {
    Object getInformationFlowController();
    Object getUserDynamicController();
    Object follow(String followingId);

    Object cancelFollow(String followingId);

    void tickPraise(boolean praised, String feedId,String postAuthorId,String postTitle, CallBack<Boolean> callBack);

    void getFeedFocusInfor(String authorId, String feedId, CallBack<Map<String, Boolean>> callBack);
    void getFeedPraiseInfor(String feedId, CallBack<String> callBack);

    void deleteFeed(String feedId,CallBack<Boolean> callBack);

    void getPostInforById(String feedId,CallBack<Map<String,String>> callBack);
}
