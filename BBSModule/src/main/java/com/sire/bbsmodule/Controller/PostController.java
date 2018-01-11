package com.sire.bbsmodule.Controller;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.sire.bbsmodule.BR;
import com.sire.bbsmodule.Constant.Constant;
import com.sire.bbsmodule.Pojo.Comment;
import com.sire.bbsmodule.Pojo.PostInfor;
import com.sire.bbsmodule.Pojo.PraiseInfor;
import com.sire.bbsmodule.Pojo.PraiseUser;
import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.Utils.ScreenUtils;
import com.sire.bbsmodule.Utils.StringUtils;
import com.sire.bbsmodule.ViewModel.BBSViewModel;
import com.sire.bbsmodule.Views.EmojiView.EmojiPopup;
import com.sire.bbsmodule.Views.PopOperation;
import com.sire.bbsmodule.Views.RichEditor.BackListenEditText;
import com.sire.bbsmodule.databinding.ViewCommponentPostBinding;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.base.ItemViewDataBindingDelegate;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.corelibrary.RecyclerView.wrapper.HeaderAndFooterWrapper;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.Utils.UIUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.corelibrary.View.ToastSuccess;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.UserModuleInterface.UserLoginState;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

import static com.sire.bbsmodule.Constant.Constant.AUTHOR_ID;
import static com.sire.bbsmodule.Constant.Constant.AUTHOR_NAME;
import static com.sire.bbsmodule.Constant.Constant.FOLLOW;
import static com.sire.bbsmodule.Constant.Constant.HEADIMAGE;
import static com.sire.bbsmodule.Constant.Constant.REPORT_REASON;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/09
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PostController extends SireController implements OnLoadmoreListener, TextWatcher, View.OnClickListener, BackListenEditText.BackListener, View.OnFocusChangeListener {

    public static final String INPUT_TEXT = "comment";
    public static final int POST_CODE = 104;

    private static List<String> commentIds = new ArrayList<>();
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    FeedMediator feedMediator;
    @Inject
    UserMediator userMediator;
    @Inject
    AppExecutors appExecutors;
    private SmartRefreshLayout swipeRefreshView;
    private BBSViewModel bbsViewModel;
    private boolean init = true;
    private ImageButton ibPraise;
    private TextView tvSend;
    private BackListenEditText etComment;
    private TextView tvFeedPraiseCount;
    private boolean praised;
    private boolean isPanShow;
    private PostViewHolder postViewHolder;
    private EmojiPopup emojiPopup;
    private boolean scrollToFirstComment;

    private RecyclerView rvPost;
    private HeaderAndFooterWrapper wrapperAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        bbsViewModel = ViewModelProviders.of(this, factory).get(BBSViewModel.class);
        initView();
        initModel();
    }

    private void initModel() {
        //获取关注信息和点赞信息
        getFeedFocus();
        //评论信息回调
        commentsCallback();
        //获取feed的赞信息
        getFeedPraiseUsers();
        //评论点赞状态
        bbsViewModel.getCommentPraiseInfor(bbsViewModel.getPostInfor().getFeedId()).observe(this, listDataResource -> {
            switch (listDataResource.status) {
                case SUCCESS:
                    commentIds.clear();
                    for (int i = 0; i < listDataResource.data.size(); i++) {
                        commentIds.add(listDataResource.data.get(i).getCommentId());
                    }
                    if (init) {
                        bbsViewModel.getCommentsInfor(bbsViewModel.getTimeLineBy(((AutoViewStateAdapter) wrapperAdapter.getInnerAdapter()).getDataSource()), bbsViewModel.getPostInfor().getFeedId());
                        init = false;
                    }
                    break;
                case ERROR:
                    break;
                case LOADING:

                    break;
                default:
                    break;
            }
        });

    }

    private void getFeedPraiseUsers() {
        feedMediator.getFeedPraiseInfor(bbsViewModel.getPostInfor().getFeedId(), data -> {
            PraiseUser praiseUser = JSONUtils.jsonString2Bean(data, PraiseUser.class);
            int praiseCount = praiseUser.getPraiseCount();
            if (praiseCount != bbsViewModel.getPostInfor().getPraiseCount()) {
                tvFeedPraiseCount.setText(praiseCount + "");
            }
            if (praiseUser.getFeedPraises() != null && postViewHolder != null) {
                postViewHolder.refreshPraiseUsers(praiseUser.getFeedPraises());
            }
        });
    }


    private void commentsCallback() {
        bbsViewModel.getComments().observe(this, listDataResource -> {
            switch (listDataResource.status) {
                case SUCCESS:
                    closeDataLoading();
                    if (wrapperAdapter != null) {
                        AutoViewStateAdapter innerAdapter = (AutoViewStateAdapter) wrapperAdapter.getInnerAdapter();
                        List<Comment> newCommentInfors = bbsViewModel.collectCommentDataList(innerAdapter.getDataSource(), listDataResource, () -> swipeRefreshView.setLoadmoreFinished(true));
                        innerAdapter.getDataSource().clear();
                        innerAdapter.getDataSource().addAll(newCommentInfors);
                        refreshUI();
                        if (scrollToFirstComment) {
                            rvPost.smoothScrollToPosition(1);
                            scrollToFirstComment = false;
                        }
                    }
                    break;
                case ERROR:
                    closeDataLoading();
                    break;
                case LOADING:

                    break;
                default:
                    break;
            }
        });
    }

    private void refreshUI() {
        if(rvPost.getAdapter() == null){
            rvPost.setAdapter(wrapperAdapter);
        }else {
            wrapperAdapter.notifyDataSetChanged();
        }
    }

    private void closeDataLoading() {
        if (swipeRefreshView.isLoading()) {
            swipeRefreshView.finishLoadmore();
        }
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    private void initView() {
        setContentView(R.layout.controller_post);
        //RecycleView Comment
        rvPost = findViewById(R.id.rv_post);
        rvPost.setItemAnimator(new DefaultItemAnimator());
        rvPost.requestFocus();
        PostAdapter postAdapter = new PostAdapter(new ArrayList<>(), rvPost, bbsViewModel);
        postAdapter.setEmptyView(UIUtils.inflate(R.layout.view_component_comment_empty_view, this));
        wrapperAdapter = new HeaderAndFooterWrapper(postAdapter);
        postViewHolder = new PostViewHolder();
        bbsViewModel.setPostInfor(getPostInfor());
        View postView = postViewHolder.initView(this);
        postViewHolder.setData(bbsViewModel.getPostInfor(), feedMediator, userMediator);
        wrapperAdapter.addHeaderView(postView);
        //pull refresh
        swipeRefreshView = findViewById(R.id.srl);
        swipeRefreshView.setEnableRefresh(false);
        swipeRefreshView.setOnLoadmoreListener(this);
        swipeRefreshView.setDisableContentWhenLoading(true);
        swipeRefreshView.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        setSupportActionBar(findViewById(R.id.toolbar));
        //comment write
        tvFeedPraiseCount = findViewById(R.id.tv_feed_praise_count);
        tvFeedPraiseCount.setText(bbsViewModel.getPostInfor().getPraiseCount() + "");
        ibPraise = findViewById(R.id.ib_praise);
        ibPraise.setOnClickListener(this);
        tvSend = findViewById(R.id.tv_send);
        etComment = findViewById(R.id.et_comment);
        etComment.setBackListener(this);
        etComment.setOnClickListener(this);
        etComment.setOnFocusChangeListener(this);
        String text = SPUtils.getValueString(this, INPUT_TEXT + bbsViewModel.getPostInfor().getFeedId());
        setSendState(text);
        if (!TextUtils.isEmpty(text)) {
            etComment.setText(text);
        }
        tvSend.setOnClickListener(this);

        etComment.addTextChangedListener(this);
        setUpEmojiPopup();

    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.contentLay))
                .build(etComment);
    }


    /**
     * 获取feed信息
     *
     * @return
     */
    private PostInfor getPostInfor() {
        String postStr = getIntent().getStringExtra(Constant.FEED_INFOR);
        PostInfor postInfor = JSONUtils.jsonString2Bean(postStr, PostInfor.class);
        return postInfor;
    }


    /**
     * @param show pan是否显示
     */
    private void setPanInputImage(boolean show) {
        tvFeedPraiseCount.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        ibPraise.setImageResource(show ? R.drawable.svg_emoji : getTickImageId());
    }

    private int getTickImageId() {
        return praised ? R.drawable.svg_good_select : R.drawable.svg_good;
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        bbsViewModel.getCommentsInfor(bbsViewModel.getTimeLineBy(((AutoViewStateAdapter) wrapperAdapter.getInnerAdapter()).getDataSource()), bbsViewModel.getPostInfor().getFeedId());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setSendState(s);
    }

    /**
     * 设置发送文字按钮状态
     *
     * @param s
     */
    private void setSendState(CharSequence s) {
        tvSend.setEnabled(!checkEmpty(s));
        if (checkEmpty(s)) {
            tvSend.setEnabled(false);
            tvSend.setTextColor(getResources().getColor(R.color.white_bg_3_text
            ));
        } else {
            tvSend.setEnabled(true);
            tvSend.setTextColor(getResources().getColor(R.color.colorPrimaryDark
            ));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean checkEmpty(CharSequence s) {
        return s == null || TextUtils.isEmpty(s.toString().trim());
    }

    @Override
    protected void onStop() {
        super.onStop();
        //记录或清空评论输入历史
        saveOrClearComment();
    }

    private void saveOrClearComment() {
        if (bbsViewModel.getPostInfor() != null) {
            String text = etComment.getText().toString();
            if(TextUtils.isEmpty(text)){
                SPUtils.removeKeyValue(this,INPUT_TEXT + bbsViewModel.getPostInfor().getFeedId());
            }else {
                SPUtils.saveKeyValueString(this, INPUT_TEXT + bbsViewModel.getPostInfor().getFeedId(), TextUtils.isEmpty(text) ? "" : text);
            }
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ib_praise) {
            if (isPanShow) {
                emojiPopup.toggle();
                ibPraise.setImageResource(emojiPopup.isShowing() ? R.drawable.svg_keyboard : R.drawable.svg_emoji);
            } else {
                tickPraise();
            }
        } else if (i == R.id.et_comment) {
            if (!isPanShow) {
                setPanState(true);
            }
        } else if (i == R.id.tv_send) {

            //传递
            final String comment = etComment.getText().toString().trim();
            String questionCommentId = (String) etComment.getTag();
            publishComment(comment, questionCommentId);
            resetInput();
        }
    }

    /**
     * 发表评论
     *
     * @param comment
     * @param questionCommentId
     */
    private void publishComment(String comment, String questionCommentId) {
        bbsViewModel.publishComment(bbsViewModel.getPostInfor().getFeedId(), comment, questionCommentId).observe(this, new Observer<DataResource<Comment>>() {
            @Override
            public void onChanged(@Nullable DataResource<Comment> commentDataResource) {
                switch (commentDataResource.status) {
                    case SUCCESS:
                        ProgressHUD.close();
                        bbsViewModel.getCommentsInfor(new Date(), bbsViewModel.getPostInfor().getFeedId());
                        //回复评论情况
                        if (!TextUtils.isEmpty(questionCommentId)) {
                            etComment.setHint(getResources().getString(R.string.let_me_say));
                            etComment.setTag(null);
                            scrollToFirstComment = true;
                        }
                        break;
                    case ERROR:
                        ProgressHUD.close();
                        ToastUtils.showToast(PostController.this, getString(R.string.comment_failed));
                        break;
                    case LOADING:
                        ProgressHUD.showDialog(PostController.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(bbsViewModel.isUserFeed() ? R.menu.bbs_menu_delete : R.menu.bbs_menu_report, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.report) {
            if (bbsViewModel.isUserFeed()) {
                deleteFeed();
            } else {
                ReportReason reportReason = new ReportReason();
                reportReason.setType(1);
                reportReason.setId(bbsViewModel.getPostInfor().getFeedId());
                reportReason.setContent(bbsViewModel.getPostInfor().getContent());
                reportComment(this, reportReason);
            }
        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteFeed() {
        ProgressHUD.showDialog(this);
        feedMediator.deleteFeed(bbsViewModel.getPostInfor().getFeedId(), new CallBack<Boolean>() {
            @Override
            public void apply(Boolean deleted) {
                ProgressHUD.close();
                if (deleted) {
                    ToastSuccess.showDialog(PostController.this, new ToastSuccess.CallBack() {
                        @Override
                        public void onFinish() {
                            Intent intent = new Intent();
                            intent.putExtra("feedId", bbsViewModel.getPostInfor().getFeedId());
                            PostController.this.finishForResult(intent);
                        }
                    });
                } else {
                    ToastUtils.showToast(PostController.this, "帖子删除失败");
                }
            }
        });
    }

    private void resetInput() {
        isPanShow = false;
        etComment.setText("");
        if (emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        }
        ScreenUtils.hideSoftInput(etComment);
        setPanInputImage(false);
    }


    /**
     * 对信息点赞或者取消赞
     */
    private void tickPraise() {
        if (!userMediator.isLoginState(this, true)) {
            return;
        }
        praised = !praised;
        ibPraise.setImageResource(praised ? R.drawable.svg_good_select : getTickImageId());
        int praiseCount = 0;
        try {
            praiseCount = Integer.valueOf(tvFeedPraiseCount.getText().toString());
        } catch (Exception e) {
        }
        tvFeedPraiseCount.setText(praised ? String.valueOf(++praiseCount) : String.valueOf(--praiseCount));
        feedMediator.tickPraise(praised, bbsViewModel.getPostInfor().getFeedId(), data -> {
            //成功
            if (data) {
                getFeedPraiseUsers();
            }
        });
    }


    /**
     * 用户登陆后刷新操作
     *
     * @param userLoginState
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserLoginState userLoginState) {
        Timber.d(userLoginState.isLogin() ? "用户登陆成功" : "用户退出登陆状态成功");
        getFeedFocus();
    }

    private void getFeedFocus() {
        feedMediator.getFeedFocusInfor(bbsViewModel.getPostInfor().getAuthorId(), bbsViewModel.getPostInfor().getFeedId(), data -> {
            //刷新关注按钮
            if (postViewHolder != null) {
                bbsViewModel.getPostInfor().setFollow(data.get("following"));
                postViewHolder.viewCommponentPostBinding.setPostInfor(bbsViewModel.getPostInfor());
            }
            //刷新点赞按钮
            praised = data.get("feedPraiseInfor");
            ibPraise.setImageResource(praised ? R.drawable.svg_good_select : R.drawable.svg_good);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wrapperAdapter.onDestroy();
        EventBus.getDefault().unregister(this);
        postViewHolder = null;
    }

    @Override
    public boolean onBackSoftInputDismiss() {
        setPanState(false);
        return false;
    }

    private void setPanState(boolean show) {
        isPanShow = show;
        setPanInputImage(show);
    }



    @Override
    public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == etComment && hasFocus) {
            setPanState(hasFocus);
        }
    }

    void reportComment(Context context, ReportReason reportReason) {
        PopOperation popOperation = new PopOperation();
        popOperation.showDialog((FragmentActivity) context, (index, item) -> {
            popOperation.dismiss();
            switch (index) {
                case 0:
                case 1:
                case 2:
                case 3:
                    report(context, reportReason, item);
                    break;
                case 4:
                    segueToOtherReasonReportController(context, reportReason);
                    break;
                default:
                    break;
            }
        }, "恶意攻击谩骂", "营销广告", "淫秽色情", "政治反动", "其他原因");
    }

    private void report(Context context, ReportReason reportReason, String item) {

        reportReason.setReportReason(item);
        bbsViewModel.report(reportReason).observe((LifecycleOwner) context, new Observer<DataResource<ReportReason>>() {
            @Override
            public void onChanged(@Nullable DataResource<ReportReason> reportReasonDataResource) {
                switch (reportReasonDataResource.status) {
                    case LOADING:
                        ProgressHUD.showDialog((FragmentActivity) context);
                        break;
                    case SUCCESS:
                        ProgressHUD.close();
                        ToastUtils.showToast(context, "举报成功");
                        break;
                    case ERROR:
                        ProgressHUD.close();
                        ToastUtils.showToast(context, "举报失败");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void segueToOtherReasonReportController(Context context, ReportReason reportReason) {

        Intent intent = new Intent(context, OtherReasonReportController.class);
        intent.putExtra(REPORT_REASON, reportReason);
        ((SireController) context).segue(Segue.SegueType.PUSH, intent);
    }

    /**
     * 评论适配器
     */
    static class PostAdapter extends AutoViewStateAdapter<Comment> {

        public PostAdapter(List<Comment> dataSource, RecyclerView recyclerView, BBSViewModel bbsViewModel) {
            super(dataSource, recyclerView);
            addItemViewDelegate(new CommentBindingDelegate(bbsViewModel));
        }

        @Override
        protected boolean areContentsTheSame(Comment oldItem, Comment newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        protected boolean areItemsTheSame(Comment oldItem, Comment newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    }

    /**
     * 赞用户适配器
     */
    static class UserPraiseAdapter extends RecyclerView.Adapter<PraiseUserViewHolder> {
        private List<PraiseInfor> dataSource;

        public UserPraiseAdapter(List<PraiseInfor> dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public PraiseUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PraiseUserViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_commponnent_praise_user, parent, false));
        }

        @Override
        public void onBindViewHolder(PraiseUserViewHolder holder, int position) {
            holder.getDataBinding().setVariable(BR.praiseUser, dataSource.get(position));
        }



        @Override
        public int getItemCount() {
            return dataSource == null ? 0 : dataSource.size();
        }
    }

    static class PraiseUserViewHolder extends ViewHolder {

        public PraiseUserViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding);
        }
    }


    /**
     * holder bingding
     */
    public static class CommentBindingDelegate extends ItemViewDataBindingDelegate<Comment> {
        private final BBSViewModel bbsViewModel;

        public CommentBindingDelegate(BBSViewModel bbsViewModel) {
            this.bbsViewModel = bbsViewModel;
        }

        @Override
        protected int getItemBRName() {
            return BR.commentItem;
        }

        @Override
        public int getItemViewLayoutId() {
            return R.layout.view_commponent_comment;
        }

        @Override
        public boolean isForViewType(Comment item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, Comment item, int position) {
            holder.getDataBinding().setVariable(BR.commentBindingDelegate, this);
            item.setClientPraise(commentIds.contains(item.getCommentId()));
            super.convert(holder, item, position);
        }

        public void onClick(View view, Comment comment) {
            int id = view.getId();
            if (id == R.id.ib_praise_comment) {
                if (comment.isClientPraise()) {
                    comment.setPraiseCount(comment.getPraiseCount() - 1);
                    comment.setClientPraise(false);
                    bbsViewModel.cancelPraise(comment.getFeedId(), comment.getCommentId()).observe((LifecycleOwner) view.getContext(), dataResource -> Timber.d("取消点赞:" + dataResource.status));
                } else {
                    comment.setPraiseCount(comment.getPraiseCount() + 1);
                    comment.setClientPraise(true);
                    bbsViewModel.praise(comment.getFeedId(), comment.getCommentId()).observe((LifecycleOwner) view.getContext(), new Observer<DataResource>() {
                        @Override
                        public void onChanged(@Nullable DataResource dataResource) {
                            Timber.d("点赞:" + dataResource.status);
                        }
                    });
                }
                ViewDataBinding binding = DataBindingUtil.getBinding((View) view.getParent());
                binding.setVariable(BR.commentItem, comment);
            } else if (id == R.id.cl) {
                PopOperation popOperation = new PopOperation();
                popOperation.showDialog((FragmentActivity) view.getContext(), (index, item) -> {
                    popOperation.dismiss();
                    switch (index) {
                        case 0:
                            replyToComment(view, comment);
                            break;
                        case 1:
                            copyCommentToClipBoard(view, comment);
                            break;
                        case 2:
                            if (bbsViewModel.isUserComment(comment)) {
                                userDeleteComment(view, comment);
                            } else {
                                ReportReason reportReason = new ReportReason();
                                reportReason.setType(0);
                                reportReason.setId(comment.getCommentId());
                                reportReason.setContent(comment.getContent());
                                ((PostController) view.getContext()).reportComment(view.getContext(), reportReason);
                            }
                            break;
                        default:
                            break;
                    }
                }, view.getContext().getString(R.string.reply), view.getContext().getString(R.string.copy), bbsViewModel.isUserComment(comment) ? view.getContext().getString(R.string.delete_comment) : view.getContext().getString(R.string.report));
            }
        }


        private void userDeleteComment(View view, Comment comment) {
            bbsViewModel.userDeleteComment(comment).observe((LifecycleOwner) view.getContext(), new Observer<DataResource<JsonResponse>>() {
                @Override
                public void onChanged(@Nullable DataResource<JsonResponse> jsonResponseDataResource) {
                    switch (jsonResponseDataResource.status) {
                        case LOADING:
                            ProgressHUD.showDialog((FragmentActivity) view.getContext());
                            break;
                        case SUCCESS:
                            ProgressHUD.close();
                            ToastUtils.showToast(view.getContext(), view.getContext().getString(R.string.delete_comment_success));
                            bbsViewModel.getCommentsInfor(new Date(), bbsViewModel.getPostInfor().getFeedId());
                            break;
                        case ERROR:
                            ProgressHUD.close();
                            ToastUtils.showToast(view.getContext(), view.getContext().getString(R.string.delete_failed));
                            break;
                        default:
                            break;
                    }
                }
            });
        }


        private void copyCommentToClipBoard(View view, Comment comment) {
            if (!TextUtils.isEmpty(comment.getContent())) {
                APPUtils.setTextToClipboard(view.getContext(), comment.getContent());
                ToastUtils.showToast(view.getContext(), view.getContext().getResources().getString(R.string.copy_to_clipboard));
            }
        }

        private void replyToComment(View view, Comment comment) {
            PostController controller = (PostController) view.getContext();
            controller.etComment.setHint("回复@" + comment.getFromAuthorName());
            controller.etComment.setTag(comment.getCommentId());
            ScreenUtils.showKeyBoard(controller.etComment);
        }

    }

    /**
     * 贴子内容
     */
    public static class PostViewHolder {
        private ViewCommponentPostBinding viewCommponentPostBinding;
        private UserPraiseAdapter userPraiseAdapter;
        private List<PraiseInfor> praiseUsers;

        public View initView(Context context) {
            View postView = getPostView(context);
            viewCommponentPostBinding = DataBindingUtil.bind(postView);
            //RecycleView Praise User
            viewCommponentPostBinding.rvPraiseUsers.setFocusableInTouchMode(false);
            viewCommponentPostBinding.rvPraiseUsers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            viewCommponentPostBinding.rvPraiseUsers.setAdapter(userPraiseAdapter = new UserPraiseAdapter(praiseUsers = new ArrayList<>()));
            return postView;
        }


        public void setData(PostInfor postInfor, FeedMediator feedMediator, UserMediator userMediator) {

            viewCommponentPostBinding.setPostInfor(postInfor);
            viewCommponentPostBinding.setPostHolder(this);
            viewCommponentPostBinding.setFeedMediator(feedMediator);
            viewCommponentPostBinding.setUserMediator(userMediator);
            viewCommponentPostBinding.tvContent.showEditData(StringUtils.cutStringBy(postInfor.getContent()));
        }
        public void onPerson(View view,PostInfor postInfor) {
            segueToPersonalHomePageController(view.getContext(),postInfor);
        }

        private void segueToPersonalHomePageController(Context context, PostInfor postInfor) {
            Intent intent = new Intent(context,PersonalHomePageController.class);
            intent.putExtra(HEADIMAGE,postInfor.getAuthorIcon());
            intent.putExtra(AUTHOR_ID,postInfor.getAuthorId());
            intent.putExtra(AUTHOR_NAME,postInfor.getAuthorName());
            intent.putExtra(FOLLOW,postInfor.isFollow());
            ((SireController)context).segue(Segue.SegueType.PUSH,intent);
        }


        /**
             * 关注
             *
             * @param feedMediator
             * @param followingId
             * @param postInfor
             */
        public void onFollow(FeedMediator feedMediator, UserMediator userMediator, String followingId, PostInfor postInfor) {
            if (feedMediator != null && userMediator != null) {

                if (!userMediator.isLoginState(viewCommponentPostBinding.getRoot().getContext(), true)) {
                    return;
                }

                if (postInfor.isFollow()) {
                    ((LiveData<DataResource>) feedMediator.cancelFollow(followingId)).observe((LifecycleOwner) viewCommponentPostBinding.getRoot().getContext(), dataResource -> {
                        switch (dataResource.status) {
                            case SUCCESS:
                                setLoading(false);
                                postInfor.setFollow(false);
                                viewCommponentPostBinding.setPostInfor(postInfor);
                                break;
                            case ERROR:
                                setLoading(false);
                                break;
                            case LOADING:
                                setLoading(true);
                                break;
                            default:
                                break;
                        }
                    });
                } else {
                    ((LiveData<DataResource>) feedMediator.follow(followingId)).observe((LifecycleOwner) viewCommponentPostBinding.getRoot().getContext(), new Observer<DataResource>() {
                        @Override
                        public void onChanged(@Nullable DataResource dataResource) {
                            switch (dataResource.status) {
                                case SUCCESS:
                                    setLoading(false);
                                    postInfor.setFollow(true);
                                    viewCommponentPostBinding.setPostInfor(postInfor);
                                case ERROR:
                                    setLoading(false);
                                    break;
                                case LOADING:
                                    setLoading(true);
                                     break;
                                default:
                                    break;
                            }
                        }
                    });
                }
            }
        }

        private void setLoading(boolean loading) {
            viewCommponentPostBinding.tvFollow.setVisibility(loading ? View.GONE : View.VISIBLE);
            viewCommponentPostBinding.ivLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        }

        private View getPostView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.view_commponent_post, null);
        }

        public void refreshPraiseUsers(List<PraiseInfor> feedPraises) {
            praiseUsers.clear();
            praiseUsers.addAll(feedPraises);
            userPraiseAdapter.notifyDataSetChanged();
        }
    }
}
