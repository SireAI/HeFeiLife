package com.sire.messagepushmodule.Controller.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.SystemUtils;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.mediators.BBSModuleInterface.BBSMediator;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.core.CallBack;
import com.sire.messagepushmodule.Pojo.CommentMessage;
import com.sire.messagepushmodule.R;
import com.sire.messagepushmodule.databinding.ControllerMessageCommentDetailBinding;

import java.util.Map;

import javax.inject.Inject;

import static com.sire.messagepushmodule.Constant.Constant.COMMENT_MESSAGE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/02/06
 * Author:Sire
 * Description:
 * ==================================================
 */

public class MessageCommentDetailController extends SireController {
    @Inject
    BBSMediator bbsMediator;
    @Inject
    FeedMediator feedMediator;
    private CommentMessage commentMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllerMessageCommentDetailBinding controllerMessageCommentDetailBinding = DataBindingUtil.setContentView(this, R.layout.controller_message_comment_detail);
        commentMessage = getCommentMessage();
        controllerMessageCommentDetailBinding.setCommentItem(commentMessage);
        setActionBarEnabled(controllerMessageCommentDetailBinding.toolbar);
        controllerMessageCommentDetailBinding.toolbar.setTitle(commentMessage.getQuestionComment() == null ? "评论" : "回复");
    }


    public void onCommentItemClick(View view) {
        if(SystemUtils.isFastClick(500)){
            return;
        }
        ProgressHUD.showDialog(this);
        feedMediator.getPostInforById(commentMessage.getFeedId(), new CallBack<Map<String, String>>() {
            @Override
            public void apply(Map<String, String> data) {
                ProgressHUD.close();
                String dataStr = data.get("data");
                if(data.get("state").equals("success")){
                    bbsMediator.segueToPostController(view.getContext(),dataStr);
                }else {
                    ToastUtils.showToast(view.getContext(),dataStr);
                }
            }
        });
    }


    public CommentMessage getCommentMessage() {
        return (CommentMessage) getIntent().getSerializableExtra(COMMENT_MESSAGE);
    }
}
