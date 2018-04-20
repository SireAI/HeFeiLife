package com.sire.bbsmodule.Controller;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.ViewModel.BBSViewModel;
import com.sire.corelibrary.Utils.PhotoPickUtils;
import com.sire.corelibrary.View.PopOperation;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.DI.Environment.GlideConfigure;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.sire.bbsmodule.Constant.Constant.AUTHOR_ID;
import static com.sire.bbsmodule.Constant.Constant.AUTHOR_NAME;
import static com.sire.bbsmodule.Constant.Constant.FOLLOW;
import static com.sire.bbsmodule.Constant.Constant.HEADIMAGE;
import static com.sire.bbsmodule.Constant.Constant.REPORT_REASON;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/04
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PersonalHomePageController extends SireController implements CallBack<Map<String,String>>{
    public static final int UN_FOLLOW = 0;
    public static final int FOLLOWED = 1;
    public static final int FOLLOWING = 2;

    @Inject
    FeedMediator feedMediator;
    @Inject
    UserMediator userMediator;
    @Inject
    BBSViewModel bbsViewModel;
    private Toolbar toolBar;
    private RelativeLayout rlPersonalInfor;
    private CircularProgressDrawable progressDrawable;
    private ImageView ivState;
    private CollapsingToolbarLayoutState state;
    private TextView tvStateText;
    private boolean isSelf;
    private ImageView backdrop;
    private ImageView ivUserPicture;
    private Adapter adapter;
    private String updatedUserName = "";
    private TextView tvUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initModel();
    }

    private void initModel() {

    }

    private boolean isFollow() {
        return getIntent().getBooleanExtra(FOLLOW, false);
    }

    private String getPersonId() {
        return getIntent().getStringExtra(AUTHOR_ID);
    }

    private String getPersonName() {
        String userName = getIntent().getStringExtra(AUTHOR_NAME);
        if(!TextUtils.isEmpty(updatedUserName)){
            userName = updatedUserName;
        }
        return userName;
    }

    private void initView() {
        setContentView(R.layout.controller_personal_home_page);
        ViewPager vp = findViewById(R.id.viewpager);
        toolBar = findViewById(R.id.toolbar);
        ivState = findViewById(R.id.iv_state);
        tvStateText = findViewById(R.id.tv_state_text);
        rlPersonalInfor = findViewById(R.id.rl_personal_infor);
        backdrop = findViewById(R.id.backdrop);
        ivUserPicture = findViewById(R.id.iv_user_picture);


        tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(getPersonName());
        toolBar.setTitle(getPersonName());
        setFollowUIState(isFollow()?FOLLOWED:UN_FOLLOW);
        checkFollow(getPersonId());
        progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setColorSchemeColors(getResources().getColor(R.color.main_bg_1_text));
        setActionBarEnabled(toolBar);
        AppBarLayout appBar = findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                alphaPersonalInfor(verticalOffset);
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        toolBar.setTitle(getPersonName());
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        toolBar.setTitle("");

                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            toolBar.setTitle("");
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        loadBackdrop();
        setupViewPager(vp);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vp);
    }

    /**
     * 检验进入主页的是作者自己还是观众，也可能是未登陆状态
     *
     * @param authorId
     * @return
     */
    private void checkFollow(String authorId) {
        String userId = userMediator.getUserId();
        boolean isSelf = authorId.equals(userId);
        findViewById(R.id.rl_stranger).setVisibility(isSelf ? View.GONE : View.VISIBLE);
        findViewById(R.id.rl_self).setVisibility(isSelf ? View.VISIBLE : View.GONE);
        this.isSelf = isSelf;
    }



    private void alphaPersonalInfor(int verticalOffset) {
        if (rlPersonalInfor != null) {
            float ratio = 1 - Float.valueOf(Math.abs(verticalOffset)) / 660;
            rlPersonalInfor.setAlpha(ratio);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFollow(getPersonId());
    }

    public void onUserPicture(View view) {

    }

    public void onFollowAuthor(View view) {
        if (isSelf) {
            userMediator.segueToPersonalProfileController(this);
        } else {
            if (feedMediator != null && userMediator != null) {

                if (!userMediator.isLoginState(this, true)) {
                    return;
                }
                String state = tvStateText.getText().toString();
                if ("已关注".equals(state)) {
                    ((LiveData<DataResource>) feedMediator.cancelFollow(getPersonId())).observe(this, dataResource -> {
                        switch (dataResource.status) {
                            case SUCCESS:
                                setFollowUIState(UN_FOLLOW);
                                break;
                            case ERROR:
                                setFollowUIState(FOLLOWED);
                                break;
                            case LOADING:
                                setFollowUIState(FOLLOWING);
                                break;
                            default:
                                break;
                        }
                    });
                } else {
                    ((LiveData<DataResource>) feedMediator.follow(getPersonId())).observe(this, dataResource -> {
                        switch (dataResource.status) {
                            case SUCCESS:
                                setFollowUIState(FOLLOWED);
                                break;
                            case ERROR:
                                setFollowUIState(UN_FOLLOW);
                                break;
                            case LOADING:
                                setFollowUIState(FOLLOWING);
                                break;
                            default:
                                break;
                        }
                    });
                }
            }
        }
    }

    private void loadBackdrop() {

        Glide.with(this).load(getIntent().getStringExtra(HEADIMAGE)).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC)).into(backdrop);
        Glide.with(this).load(getIntent().getStringExtra(HEADIMAGE)).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.svg_person)).into(ivUserPicture);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString(AUTHOR_ID,getPersonId());
        Fragment userDynamicController = (Fragment) feedMediator.getUserDynamicController();
        Fragment personalInforController = (Fragment) userMediator.getPersonalInforController();
        userDynamicController.setArguments(bundle);
        personalInforController.setArguments(bundle);
        adapter.addFragment(userDynamicController, "动态");
        adapter.addFragment(personalInforController, "关于TA");
        viewPager.setAdapter(adapter);
    }

    private void setFollowUIState(int state) {
        if (progressDrawable != null) {
            progressDrawable.stop();
        }
        if (state == UN_FOLLOW) {
            ivState.setImageResource(R.drawable.svg_add);
            tvStateText.setText("关注");
        } else if (state == FOLLOWED) {
            ivState.setImageResource(R.drawable.svg_select);
            tvStateText.setText("已关注");
        } else if (state == FOLLOWING) {
            ivState.setImageDrawable(progressDrawable);
            progressDrawable.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isSelf){
            getMenuInflater().inflate(R.menu.bbs_menu_report_person, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.report) {

            ReportReason reportReason = new ReportReason();
            reportReason.setType(2);
            reportReason.setId(getPersonId());
            reportReason.setContent("举报作者个人");
            reportComment(this, reportReason);
        }
        return super.onOptionsItemSelected(item);
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

    private void segueToOtherReasonReportController(Context context, ReportReason reportReason) {

        Intent intent = new Intent(context, OtherReasonReportController.class);
        intent.putExtra(REPORT_REASON, reportReason);
        ((SireController) context).segue(Segue.SegueType.PUSH, intent);
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

    /**
     * 用户数据回调
     * @param data
     */
    @Override
    public void apply(Map<String, String> data) {
        TextView tvFollowingCount = findViewById(R.id.tv_following_count);
        tvFollowingCount.setText(data.get("followingCount"));
        TextView tvFollowerCount = findViewById(R.id.tv_follower_count);
        tvFollowerCount.setText(data.get("followerCount"));
        String userHomeImage = data.get("userHomeImage");
        String headImage = data.get("headImage");
        updatedUserName = data.get("userName");
        tvUserName.setText(getPersonName());
        toolBar.setTitle(getPersonName());
        if(!TextUtils.isEmpty(userHomeImage)){
            Glide.with(this).load(userHomeImage).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC)).into(backdrop);
        }
        if(!TextUtils.isEmpty(headImage)){
            Glide.with(this).load(headImage).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.svg_person)).into(ivUserPicture);
        }
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public List<Fragment> getFragments() {
            return mFragments;
        }

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(adapter.getFragments()!=null&&adapter.getFragments().size()>0){
            for (int i = 0; i < adapter.getFragments().size(); i++) {
                adapter.getFragments().get(i).onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}
