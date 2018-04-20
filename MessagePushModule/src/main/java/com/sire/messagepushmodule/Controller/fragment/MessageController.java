package com.sire.messagepushmodule.Controller.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.OnScrollDelegate;
import com.sire.corelibrary.RecyclerView.base.ItemViewDataBindingDelegate;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.mediators.UserModuleInterface.UserLoginState;
import com.sire.messagepushmodule.BR;
import com.sire.messagepushmodule.Controller.IMMessageController;
import com.sire.messagepushmodule.DB.Entry.IMMessage;
import com.sire.messagepushmodule.DB.Entry.Message;
import com.sire.messagepushmodule.Pojo.CommentMessage;
import com.sire.messagepushmodule.Pojo.FeedPraiseMessage;
import com.sire.messagepushmodule.R;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;
import java.util.List;

import javax.inject.Inject;

import static com.sire.messagepushmodule.Constant.Constant.COMMENT_MESSAGE;
import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_COMMENT;
import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_POST;
import static com.sire.messagepushmodule.Constant.Constant.IM_TO_IM;
import static com.sire.messagepushmodule.Constant.Constant.PRAISE_TO_POST;
import static com.sire.messagepushmodule.Constant.Constant.TALK_USER_ID;
import static com.sire.messagepushmodule.Constant.Constant.TALK_USER_IMG;
import static com.sire.messagepushmodule.Constant.Constant.TALK_USER_NAME;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/27
 * Author:Sire
 * Description:
 * ==================================================
 */
public class MessageController extends Fragment implements OnRefreshListener, OnLoadmoreListener {


    @Inject
    ViewModelProvider.Factory factory;
    private RecyclerView recyclerView;
    private SmartRefreshLayout swipeRefreshView;
    private MessageAdapter messageAdapter;
    private MessagePushViewModel messagePushViewModel;

    @Inject
    public MessageController() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_message, container, false);
        messagePushViewModel = ViewModelProviders.of(this, factory).get(MessagePushViewModel.class);
        initView(view);
        initModel();
        EventBus.getDefault().register(this);
        return view;
    }

    private void initModel() {
        messageCallBack();
        messagePushViewModel.initMesageRequest();
    }

    private void messageCallBack() {
        messagePushViewModel.getMessageData().observe(this, messageDataSource -> {
            switch (messageDataSource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    closeLoading();
                    messagePushViewModel.transformData(messageDataSource.data, data -> getActivity().runOnUiThread(() -> {
                        if (needRefresh(data)) {
                            MessageController.this.refreshUI(data);
                        }
                    }));
                    break;
                case ERROR:
                    closeLoading();
                    break;
                default:
                    break;
            }
        });
    }

    private boolean needRefresh(List<Message> data) {
        boolean need = messageAdapter.getDataSource() == null || messageAdapter.getDataSource().size() == 0 || messageAdapter.getDataSource().size() < data.size() || messageAdapter.getDataSource().get(0).getMessageCreateTime().getTime() != data.get(0).getMessageCreateTime().getTime();
        if (!need) {
            swipeRefreshView.setLoadmoreFinished(true);
        }
        return need;
    }

    private void refreshUI(List<Message> clientMessages) {
        if (messageAdapter != null) {
            messageAdapter.refreshDataSource(clientMessages);
        }
    }

    public void closeLoading() {
        if (swipeRefreshView != null) {
            if (swipeRefreshView.isRefreshing()) {
                swipeRefreshView.finishRefresh();
            }
            if (swipeRefreshView.isLoading()) {
                swipeRefreshView.finishLoadmore();
            }
        }
    }

    private void initView(View view) {
        //Recyclerview
        recyclerView = view.findViewById(R.id.rv_message);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = getResources().getDrawable(R.drawable.shape_line_1);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        if (getActivity() instanceof OnScrollDelegate) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    ((OnScrollDelegate) getActivity()).onScroll(recyclerView, dx, dy);
                }
            });
        }

        //Adapter
        messageAdapter = new MessageAdapter(null, recyclerView, messagePushViewModel);
        //初始为加载状态
        messageAdapter.setLoadingState();
        recyclerView.setAdapter(messageAdapter);
        //pull Refresh
        swipeRefreshView = view.findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setOnLoadmoreListener(this);
        swipeRefreshView.setDisableContentWhenLoading(true);
        swipeRefreshView.setDisableContentWhenRefresh(true);
        swipeRefreshView.setRefreshHeader(new ClassicsHeader(getActivity()));
        swipeRefreshView.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        swipeRefreshView.finishRefresh();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        messagePushViewModel.requestMoreMessageData();
    }


    /**
     * 登陆成功重新获取缓存消息
     *
     * @param userLoginState
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserLoginState userLoginState) {
        //清楚原先的数据监听，创建新的监听
        messagePushViewModel.getMessageData().removeObservers(this);
        initModel();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        recyclerView = null;
        swipeRefreshView = null;
        messageAdapter = null;
    }

    public static class MessageAdapter extends AutoViewStateAdapter<Message> {

        public MessageAdapter(List<Message> dataSource, RecyclerView recyclerView, MessagePushViewModel messagePushViewModel) {
            super(dataSource, recyclerView);
            addItemViewDelegate(COMMENT_TO_POST, new CommentToPostMessageDelegate(messagePushViewModel));
            addItemViewDelegate(COMMENT_TO_COMMENT, new CommentToCommentMessageDelegate(messagePushViewModel));
            addItemViewDelegate(PRAISE_TO_POST, new PraiseToPostMessageDelegate(messagePushViewModel));
            addItemViewDelegate(IM_TO_IM, new IMToIMMessageDelegate(messagePushViewModel));
        }

        @Override
        protected boolean areContentsTheSame(Message oldItem, Message newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        protected boolean areItemsTheSame(Message oldItem, Message newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    }

    /**
     * 评论的回复消息消息
     */
    public static class CommentToCommentMessageDelegate extends ItemViewDataBindingDelegate<Message> {

        private final SoftReference<MessagePushViewModel> messagePushViewModelSoftReference;

        public CommentToCommentMessageDelegate(MessagePushViewModel messagePushViewModel) {
            messagePushViewModelSoftReference = new SoftReference(messagePushViewModel);
        }

        @Override
        protected int getItemBRName() {
            return BR.commentToCommentItem;
        }

        @Override
        public int getItemViewLayoutId(int viewType) {
            return R.layout.view_commponent_comment_to_comment;
        }

        @Override
        public void convert(ViewHolder holder, Message item, int position) {
            ViewDataBinding dataBinding = holder.getDataBinding();
            dataBinding.setVariable(getItemBRName(), item.getData());
            dataBinding.setVariable(BR.commentToCommentDelegate, this);
            dataBinding.setVariable(BR.message, item);
            dataBinding.executePendingBindings();
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return item.getMessageType() == COMMENT_TO_COMMENT;
        }

        public void onClick(View view, Message message) {
            segueToMessageCommentDetailController(view.getContext(), (CommentMessage) message.getData());
            MessagePushViewModel messagePushViewModel = messagePushViewModelSoftReference.get();
            if (messagePushViewModel != null) {
                messagePushViewModel.changeMessageState(message);
            }
        }

        private void segueToMessageCommentDetailController(Context context, CommentMessage data) {
            Intent intent = new Intent(context, MessageCommentDetailController.class);
            intent.putExtra(COMMENT_MESSAGE, data);
            ((SireController) context).segue(Segue.SegueType.PUSH, intent);
        }

    }

    public static class CommentToPostMessageDelegate extends ItemViewDataBindingDelegate<Message> {
        private SoftReference<MessagePushViewModel> messagePushViewModelSoftReference;

        public CommentToPostMessageDelegate(MessagePushViewModel messagePushViewModel) {
            messagePushViewModelSoftReference = new SoftReference(messagePushViewModel);
        }


        @Override
        protected int getItemBRName() {
            return BR.commentToPostItem;
        }

        @Override
        public int getItemViewLayoutId(int viewType) {
            return R.layout.view_commponent_comment_to_post;
        }

        @Override
        public void convert(ViewHolder holder, Message item, int position) {
            ViewDataBinding dataBinding = holder.getDataBinding();
            dataBinding.setVariable(getItemBRName(), item.getData());
            dataBinding.setVariable(BR.commentToPostDelegate, this);
            dataBinding.setVariable(BR.message, item);
            dataBinding.executePendingBindings();
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return item.getMessageType() == COMMENT_TO_POST;
        }

        public void onClick(View view, Message message) {
            segueToMessageCommentDetailController(view.getContext(), (CommentMessage) message.getData());

            MessagePushViewModel messagePushViewModel = messagePushViewModelSoftReference.get();
            if (messagePushViewModel != null) {
                messagePushViewModel.changeMessageState(message);
            }
        }

        private void segueToMessageCommentDetailController(Context context, CommentMessage data) {
            Intent intent = new Intent(context, MessageCommentDetailController.class);
            intent.putExtra(COMMENT_MESSAGE, data);
            ((SireController) context).segue(Segue.SegueType.PUSH, intent);
        }
    }

    public static class PraiseToPostMessageDelegate extends ItemViewDataBindingDelegate<Message> {

        public PraiseToPostMessageDelegate(MessagePushViewModel messagePushViewModel) {
        }

        @Override
        protected int getItemBRName() {
            return BR.praiseFeedMessageItem;
        }

        @Override
        public int getItemViewLayoutId(int viewType) {
            return R.layout.view_commponent_praise;
        }

        @Override
        public void convert(ViewHolder holder, Message item, int position) {
            holder.getDataBinding().setVariable(getItemBRName(), item.getData());
            holder.getDataBinding().setVariable(BR.praiseToPostDelegate, this);
            holder.getDataBinding().executePendingBindings();
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return item.getMessageType() == PRAISE_TO_POST;
        }

        public void onClick(View view, FeedPraiseMessage feedPraiseMessage) {

        }
    }


    public static class IMToIMMessageDelegate extends ItemViewDataBindingDelegate<Message> {

        private final MessagePushViewModel messagePushViewModel;

        public IMToIMMessageDelegate(MessagePushViewModel messagePushViewModel) {
            this.messagePushViewModel = messagePushViewModel;
        }

        @Override
        protected int getItemBRName() {
            return BR.iMMessageItem;
        }

        @Override
        public int getItemViewLayoutId(int viewType) {
            return R.layout.view_commponent_im;
        }

        @Override
        public void convert(ViewHolder holder, Message item, int position) {
//            holder.setIsRecyclable(false);
            //此处的messageid相当于消息的用户id
            LiveData<DataResource<Integer>> dataResourceLiveData = messagePushViewModel.observerIMMessageCount(item.getMessageId());
            dataResourceLiveData.observe((SireController) holder.getConvertView().getContext(), new Observer<DataResource<Integer>>() {
                @Override
                public void onChanged(@Nullable DataResource<Integer> integerDataResource) {
                    switch (integerDataResource.status) {
                        case SUCCESS:
                            holder.getDataBinding().setVariable(BR.imMessageUnreadCount,integerDataResource.data);
                            messagePushViewModel.updateMessageState(integerDataResource.data,item);
                            break;
                        default:
                            break;
                    }
                }
            });
            holder.getDataBinding().setVariable(getItemBRName(), item.getData());
            holder.getDataBinding().setVariable(BR.iMDelegate, this);
            holder.getDataBinding().setVariable(BR.currentUserId, messagePushViewModel.getUserId());
            holder.getDataBinding().setVariable(BR.message, item);
            holder.getDataBinding().executePendingBindings();
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return item.getMessageType() == IM_TO_IM;
        }

        public void onClick(View view, Message message) {
            segueToIMMessageContorller(view.getContext(), message);
        }

        private void segueToIMMessageContorller(Context context, Message message) {
            Intent intent = new Intent(context, IMMessageController.class);
            intent.putExtra(TALK_USER_ID, message.getMessageId());
            IMMessage imMessage = (IMMessage) message.getData();
            String talkAuthorName = imMessage.getTalkAuthorName(messagePushViewModel.getUserId());
            intent.putExtra(TALK_USER_NAME, talkAuthorName);
            String talkUserImg = imMessage.getFromAuthorImageUrl(messagePushViewModel.getUserId());
            intent.putExtra(TALK_USER_IMG, talkUserImg);
            ((SireController) context).segue(Segue.SegueType.PUSH, intent);
        }
    }
}
