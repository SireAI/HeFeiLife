package com.sire.mediators.BBSModuleInterface;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface BBSMediator {
    void segueToPostController(Object context,String post);

    void segueToPostPublishController(Object context, Object view);

}
