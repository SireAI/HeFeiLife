package com.sire.bbsmodule.Mediator;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sire.bbsmodule.Controller.PostController;
import com.sire.bbsmodule.Controller.PostPublishController;
import com.sire.corelibrary.View.EmojiView.EmojiManager;
import com.sire.corelibrary.View.EmojiView.category.IosEmojiProvider;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.DI.Environment.ModuleInit;
import com.sire.corelibrary.DI.Environment.ModuleInitInfor;
import com.sire.mediators.BBSModuleInterface.BBSMediator;

import javax.inject.Inject;

import io.reactivex.Flowable;

import static com.sire.bbsmodule.Constant.Constant.FEED_INFOR;
import static com.sire.bbsmodule.Controller.PostController.POST_CODE;
import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/15
 * Author:Sire
 * Description:
 * ==================================================
 */
public class BBSMediatorImpl implements BBSMediator ,ModuleInit{
    @Inject
    public BBSMediatorImpl() {
    }

    @Override
    public void segueToPostController(Object context,String post) {
        if(context instanceof SireController){
            Intent intent = new Intent((Context) context, PostController.class);
            intent.putExtra(FEED_INFOR,post);
            intent.putExtra(FOR_RESULT_REQUEST_CODE,POST_CODE);
            ((SireController) context).segueForResult(Segue.SegueType.PUSH,intent);
        }
    }

    @Override
    public void segueToPostPublishController(Object context, Object view) {
        Intent intent = new Intent((Context) context, PostPublishController.class);
        View positionView = (View) view;
        float startX = positionView.getX() + positionView.getWidth() / 2;
        float startY = positionView.getY() + positionView.getHeight() / 2;
        Segue.PagePositionData pagePositionData = new Segue.PagePositionData(positionView, (int) startX, (int) startY, positionView.getWidth(), positionView.getHeight());
        ((SireController) context).segueForResult(Segue.SegueType.MODAL,intent);
    }


    @Override
    public Flowable<ModuleInitInfor> init() {
       return Flowable.just("bbs").flatMap(o -> {
           EmojiManager.install(new IosEmojiProvider());
           return Flowable.just(new ModuleInitInfor("BBSModule","装载表情,初始化图片选择器加载库"));
       });

    }
}
