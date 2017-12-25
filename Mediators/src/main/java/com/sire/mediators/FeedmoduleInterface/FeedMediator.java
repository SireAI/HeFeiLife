package com.sire.mediators.FeedmoduleInterface;

import com.sire.mediators.core.CallBack;

import java.util.List;
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
    Object getViewController();
    Object follow(String followingId);

    Object cancelFollow(String followingId);

    void tickPraise(boolean praised, String feedId, CallBack<Boolean> callBack);

    void getFeedFocusInfor(String authorId, String feedId, CallBack<Map<String, Boolean>> callBack);
    void getFeedPraiseInfor(String feedId, CallBack<String> callBack);

    void deleteFeed(String feedId,CallBack<Boolean> callBack);
}
