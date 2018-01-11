package com.sire.usermodule.Controller.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.DI.Injectable;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.View.AutoStateView;
import com.sire.mediators.core.CallBack;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.R;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerPersonalInforBinding;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/08
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PersonalInforController extends LifecycleFragment implements Injectable {

    @Inject
    UserViewModel userViewModel;
    private AutoStateView autoStateView;
    private ControllerPersonalInforBinding controllerPersonalInforBinding;

    @Inject
    public PersonalInforController() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        controllerPersonalInforBinding = DataBindingUtil.inflate(inflater, R.layout.controller_personal_infor, container, false);
        User user = new User();
        controllerPersonalInforBinding.setUser(user);
        autoStateView = new AutoStateView();
        userViewModel.getUserInfor(getArguments().getString("authorId"), true).observe(this, new Observer<DataResource<User>>() {
            @Override
            public void onChanged(@Nullable DataResource<User> userDataResource) {
                switch (userDataResource.status) {
                    case LOADING:
                        autoStateView.loadView(AutoStateView.State.LOADING, (ViewGroup) ((ViewGroup) controllerPersonalInforBinding.getRoot()).getChildAt(0));
                        break;
                    case SUCCESS:
                        controllerPersonalInforBinding.setUser(userDataResource.data);
                        if(getActivity() instanceof CallBack){
                            Map<String, String> map = new HashMap<>();
                            map.put("followingCount",userDataResource.data.getFollowingCount());
                            map.put("followerCount",userDataResource.data.getFollowoerCount());
                            ((CallBack) getActivity()).apply(map);
                        }
                        autoStateView.loadView(AutoStateView.State.NORMAL, (ViewGroup) ((ViewGroup) controllerPersonalInforBinding.getRoot()).getChildAt(0));
                        break;
                    case ERROR:
                        autoStateView.loadView(AutoStateView.State.ERROR, (ViewGroup) ((ViewGroup) controllerPersonalInforBinding.getRoot()).getChildAt(0));
                        break;
                    default:
                        break;
                }
            }
        });
        return controllerPersonalInforBinding.getRoot();
    }

    public void setUser(User user) {
        if (user != null) {
            controllerPersonalInforBinding.setUser(user);
            autoStateView.loadView(AutoStateView.State.NORMAL, (ViewGroup) ((ViewGroup) controllerPersonalInforBinding.getRoot()).getChildAt(0));
        } else {
            autoStateView.loadView(AutoStateView.State.ERROR, (ViewGroup) ((ViewGroup) controllerPersonalInforBinding.getRoot()).getChildAt(0));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoStateView = null;
    }
}
