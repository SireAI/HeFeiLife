package com.sire.feedmodule.Controller;

import android.arch.lifecycle.LifecycleFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sire.corelibrary.Controller.SireFragmentController;
import com.sire.corelibrary.RecyclerView.CommonAdapter;
import com.sire.corelibrary.RecyclerView.OnScrollDelegate;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;
import com.sire.feedmodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */

public class AttentionInformationController extends LifecycleFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.controller_attention_information, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_information);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        if(getActivity() instanceof OnScrollDelegate){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    ((OnScrollDelegate) getActivity()).onScroll(recyclerView,dx,dy);
                }
            });
        }

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add("" + i);
        }
        recyclerView.setAdapter(new CommonAdapter<String>(recyclerView, R.layout.view_componnent_information_floa_item, strings) {
            @Override
            protected boolean areContentsTheSame(String oldItem, String newItem) {
                return false;
            }

            @Override
            protected boolean areItemsTheSame(String oldItem, String newItem) {
                return false;
            }

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                System.out.println("=====" + s);
            }
        });
    }
}
