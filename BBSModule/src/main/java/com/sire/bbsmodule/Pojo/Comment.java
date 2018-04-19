package com.sire.bbsmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
  /**
   * 评论id
   */
  private String commentId;
  /**
   * feed id
   */
  private String feedId;

  /**
   * 帖子作者id
   */
  private String postAuthorId;
  private String postTitle;

  /**
   * 评论内容
   */
  private String content;
  /**
   * 发表评论的用户id
   */
  private String fromAuthorId;

  /**
   * 发表评论用户的名字
   */
  private String fromAuthorName;
  /**
   * 评论者头像
   */
  private String fromAuthorImg;

  /**
   * 赞数
   */
  private int praiseCount;




  /**
   * 被回答的评论Id
   */
  private String questionId;

  /**
   * 发表日期
   */
  @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss SSS")
  private Date timeLine;
  /**
   * 发表人等级
   */
  private String fromAuthorLevel;

  /**
   * 发布物理地址
   */
  private String publishAddress;

  private boolean isClientPraise;

  public boolean isClientPraise() {
    return isClientPraise;
  }

  public void setClientPraise(boolean clientPraise) {
    isClientPraise = clientPraise;
  }

  public String getFromAuthorLevel() {
    return fromAuthorLevel;
  }

  public void setFromAuthorLevel(String fromAuthorLevel) {
    this.fromAuthorLevel = fromAuthorLevel;
  }

  public String getPublishAddress() {
    return publishAddress;
  }

  public void setPublishAddress(String publishAddress) {
    this.publishAddress = publishAddress;
  }

  /**
   * 被回答的评论实体
   */
  private Comment questionComment;

  public Comment getQuestionComment() {
    return questionComment;
  }

  public void setQuestionComment(Comment questionComment) {
    this.questionComment = questionComment;
  }

  public String getPostAuthorId() {
    return postAuthorId;
  }

  public void setPostAuthorId(String postAuthorId) {
    this.postAuthorId = postAuthorId;
  }

  public int getPraiseCount() {
    return praiseCount;
  }

  public void setPraiseCount(int praiseCount) {
    this.praiseCount = praiseCount;
  }

  public String getCommentId() {
    return commentId;
  }

  public void setCommentId(String commentId) {
    this.commentId = commentId;
  }

  public String getFeedId() {
    return feedId;
  }

  public void setFeedId(String feedId) {
    this.feedId = feedId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFromAuthorId() {
    return fromAuthorId;
  }

  public void setFromAuthorId(String fromAuthorId) {
    this.fromAuthorId = fromAuthorId;
  }


  public String getFromAuthorName() {
    return fromAuthorName;
  }

  public void setFromAuthorName(String fromAuthorName) {
    this.fromAuthorName = fromAuthorName;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public Date getTimeLine() {
    return timeLine;
  }

  public void setTimeLine(Date timeLine) {
    this.timeLine = timeLine;
  }

  public String getFromAuthorImg() {
    return fromAuthorImg;
  }

  public void setFromAuthorImg(String fromAuthorImg) {
    this.fromAuthorImg = fromAuthorImg;
  }



  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  @Override
  public String toString() {
    return "Comment{" +
            "commentId='" + commentId + '\'' +
            ", feedId='" + feedId + '\'' +
            ", postAuthorId='" + postAuthorId + '\'' +
            ", postTitle='" + postTitle + '\'' +
            ", content='" + content + '\'' +
            ", fromAuthorId='" + fromAuthorId + '\'' +
            ", fromAuthorName='" + fromAuthorName + '\'' +
            ", fromAuthorImg='" + fromAuthorImg + '\'' +
            ", praiseCount=" + praiseCount +
            ", questionId='" + questionId + '\'' +
            ", timeLine=" + timeLine +
            ", fromAuthorLevel='" + fromAuthorLevel + '\'' +
            ", publishAddress='" + publishAddress + '\'' +
            ", isClientPraise=" + isClientPraise +
            ", questionComment=" + questionComment +
            '}';
  }

  @Override
  public boolean equals(Object o) {

    Comment comment = (Comment) o;

    return commentId.equals(comment.commentId) && feedId.equals(comment.feedId);
  }

}
