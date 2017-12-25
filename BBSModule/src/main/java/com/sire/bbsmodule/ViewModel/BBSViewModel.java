package com.sire.bbsmodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sire.bbsmodule.Controller.PostPublishController;
import com.sire.bbsmodule.DB.Entry.CommentPraiseInfor;
import com.sire.bbsmodule.Pojo.Comment;
import com.sire.bbsmodule.Pojo.CommentRequestInfor;
import com.sire.bbsmodule.Pojo.EditData;
import com.sire.bbsmodule.Pojo.PostInfor;
import com.sire.bbsmodule.Pojo.PublishInfor;
import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.bbsmodule.Repository.BBSRepository;
import com.sire.bbsmodule.Utils.StringUtils;
import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.CommonUtils;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.mediators.UserModuleInterface.UserMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/20
 * Author:Sire
 * Description:
 * ==================================================
 */

public class BBSViewModel extends ViewModel {
    private final LiveData<DataResource<List<Comment>>> comments;
    private final BBSRepository bbsRepository;
    private UserMediator userMediator;
    private int pageSize = 10;
    public static final String DRAFT = "draft";

    private MutableLiveData<CommentRequestInfor> commentRequestInforMutableLiveData = new MutableLiveData<>();
    private PostInfor postInfor;

    @Inject
    public BBSViewModel(BBSRepository bbsRepository, UserMediator userMediator) {
        comments = Transformations.switchMap(commentRequestInforMutableLiveData, commentRequestInfor -> {
            if (commentRequestInfor == null) {
                return AbsentLiveData.create();
            } else {
                return bbsRepository.fetchCommentsInfor(commentRequestInfor);
            }
        });
        this.bbsRepository = bbsRepository;
        this.userMediator = userMediator;
    }

    public LiveData<DataResource<List<Comment>>> getComments() {
        return comments;
    }


    public List<Comment> collectCommentDataList(List<Comment> oldCommentInfors, DataResource<List<Comment>> listDataResource) {

        List<Comment> newCommentInfors = new ArrayList<>();
        //如果最新的Item事件大于当前后的时间，就认为是刷新操作，这里排序很重要，错误的排序导致错误的时间
        if (oldCommentInfors != null && !refreshNew(oldCommentInfors, listDataResource.data)) {
            newCommentInfors.addAll(oldCommentInfors);
        }
        newCommentInfors.addAll(0, listDataResource.data);
        Collections.sort(newCommentInfors, (o1, o2) -> (int) (o2.getTimeLine().getTime() - o1.getTimeLine().getTime()));
        return newCommentInfors;
    }

    /**
     * 判断是加载更多还是刷新最新，最近的时间排在序列第一个，最久的时间排在对垒最后一个
     *
     * @param oldCommentInfors
     * @param newCommentInfors
     * @return
     */
    private boolean refreshNew(List<Comment> oldCommentInfors, List<Comment> newCommentInfors) {
        if (oldCommentInfors == null || oldCommentInfors.size() == 0) {
            return true;
        }
        if (newCommentInfors == null || newCommentInfors.size() == 0) {
            return false;
        }
        Date lastTimeLine = oldCommentInfors.get(oldCommentInfors.size() - 1).getTimeLine();
        Date nearestTimeLine = newCommentInfors.get(0).getTimeLine();
        return nearestTimeLine.getTime() > lastTimeLine.getTime();
    }

    public Date getTimeLineBy(List<Comment> comments) {
        Date date = new Date();
        //从最早的时间线开始获取评论
        if (comments != null && comments.size() > 0) {
            //接着当前的评论获取下个评论
            date = comments.get(comments.size() - 1).getTimeLine();
        }
        return date;
    }

    public void getCommentsInfor(Date dateLine, String feedId) {
        CommentRequestInfor commentRequestInfor = new CommentRequestInfor();
        commentRequestInfor.setFeedId(feedId);
        commentRequestInfor.setPageSize(pageSize);
        commentRequestInfor.setTimeLine(dateLine);
        commentRequestInforMutableLiveData.setValue(commentRequestInfor);
    }

    public LiveData<DataResource> cancelPraise(String feedId, String commentId) {
        CommentPraiseInfor praiseInfor = new CommentPraiseInfor();
        praiseInfor.setFeedId(feedId);
        praiseInfor.setCommentId(commentId);
        return bbsRepository.cancelPraise(praiseInfor);
    }

    public LiveData<DataResource<List<CommentPraiseInfor>>> getCommentPraiseInfor(String feedId) {
        return bbsRepository.getCommentPraiseInfor(feedId);
    }

    public LiveData<DataResource> praise(String feedId, String commentId) {
        CommentPraiseInfor praiseInfor = new CommentPraiseInfor();
        praiseInfor.setFeedId(feedId);
        praiseInfor.setCommentId(commentId);
        return bbsRepository.praise(praiseInfor);
    }

    public LiveData<DataResource<Comment>> publishComment(String feedId, String comment, String questionCommentId) {
        Comment commentEntity = new Comment();
        commentEntity.setFeedId(feedId);
        commentEntity.setContent(comment);
        commentEntity.setFromAuthorId(userMediator.getUserId());
        commentEntity.setFromAuthorLevel(userMediator.getUserLevel());
        commentEntity.setFromAuthorName(userMediator.getUserName());
        commentEntity.setFromAuthorImg(userMediator.getUserImage());
        commentEntity.setPublishAddress(userMediator.getUserCurrentAddress());
        commentEntity.setTimeLine(new Date());
        if (!TextUtils.isEmpty(questionCommentId)) {
            commentEntity.setQuestionId(questionCommentId);
        }
        return bbsRepository.publishComment(commentEntity);

    }

    public LiveData<DataResource<ReportReason>> report(ReportReason reportReason) {
        return bbsRepository.report(reportReason);
    }

    public String getUserId() {
        return userMediator.getUserId();
    }


    public LiveData<DataResource<JsonResponse>> userDeleteComment(Comment comment) {
        return bbsRepository.userDeleteComment(comment.getCommentId());
    }

    public PostInfor getPostInfor() {
        return postInfor;
    }

    public void setPostInfor(PostInfor postInfor) {
        this.postInfor = postInfor;
    }

    public boolean isUserComment(Comment comment) {
        return comment.getFromAuthorId().equals(userMediator.getUserId());
    }

    public boolean isUserFeed() {
        return getPostInfor().getAuthorId().equals(getUserId());
    }


    public LiveData<DataResource<JsonResponse>> publishPost(List<EditData> postData) {
        //生成html
        String htmlPostData = createHtml(postData);
        List<String> pictures = findPostPictures(postData);
        //标题
        String title = postData.get(0).inputStr;
        //分类
        String categary = "";
        //发布地点
        String publishAddresss = "高新区";

        PublishInfor publishInfor = new PublishInfor();
        publishInfor.setAuthorId(getUserId());
        publishInfor.setAuthorIcon(userMediator.getUserImage());
        publishInfor.setAuthorLevel(userMediator.getUserLevel());
        publishInfor.setAuthorName(userMediator.getUserName());
        publishInfor.setTitle(title);
        publishInfor.setCategary(categary);
        publishInfor.setFeedType(0);
        publishInfor.setPublishAddress(publishAddresss);
        publishInfor.setContent(htmlPostData);
        publishInfor.setPictureUrls(pictures);
        publishInfor.setTimeLine(new Date());
        return bbsRepository.publishPost(publishInfor);
    }

    private List<String> findPostPictures(List<EditData> postData) {
        List<String> pictures = new ArrayList<>();
        for (int i = 0; i < postData.size(); i++) {
            if(!TextUtils.isEmpty(postData.get(i).imagePath)){
                pictures.add(postData.get(i).imagePath);
            }
        }
        return pictures;
    }

    @NonNull
    private String createHtml(List<EditData> postData) {
        StringBuilder htmlPostData = new StringBuilder();
        for (int i = 0; i < postData.size(); i++) {
            EditData lineData = postData.get(i);
            if (i == 0) {
                htmlPostData.append("<h4>").append(lineData.inputStr).append("</h4>");
            } else {
                if (!TextUtils.isEmpty(lineData.inputStr)) {
                    htmlPostData.append(lineData.inputStr);
                } else if (!TextUtils.isEmpty(lineData.imagePath)) {
                    htmlPostData.append("<img src=\"").append(lineData.imagePath).append("\"/>");
//                    htmlPostData.append("<img src=\"").append("http://ww2.sinaimg.cn/bmiddle/813a1fc7jw1ee4xpejq4lj20g00o0gnu.jpg").append("\"/>");
                }
            }
        }
        return htmlPostData.toString();
    }

    public void clearDraft(Context context) {
        SPUtils.saveKeyValueString(context, DRAFT,"");

    }

    public void saveDraft(Context context, List<EditData> editData) {
        String html = createHtml(editData);
        SPUtils.saveKeyValueString(context.getApplicationContext(), DRAFT,html);
    }

    public List<EditData> getDraft(Context context) {
        String draft = SPUtils.getValueString(context, DRAFT);
        List<EditData> editData = null;
        if(!TextUtils.isEmpty(draft)){
            editData = StringUtils.cutStringBy(draft);
        }
        return editData;
    }
}
