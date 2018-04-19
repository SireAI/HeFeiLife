package com.sire.bbsmodule.Controller;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sire.bbsmodule.R;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.RecyclerView.AutoViewStateAdapter;
import com.sire.corelibrary.RecyclerView.base.ItemViewDelegate;
import com.sire.corelibrary.RecyclerView.base.ViewHolder;

import java.util.List;

import static com.sire.bbsmodule.Constant.Constant.NEARBY;
import static com.sire.bbsmodule.Constant.Constant.PLACE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/03
 * Author:Sire
 * Description:
 * ==================================================
 */

public class NearyByController extends SireController implements AutoViewStateAdapter.OnItemClickListener {

    private RecyclerView rvNearBy;
    private List<String> nearBys;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_near_by);
        initView();
    }


    private void initView() {
        //Recyclerview
        rvNearBy = findViewById(R.id.rv_mear_by);
        rvNearBy.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = getResources().getDrawable(R.drawable.shape_1_line);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(drawable);
        rvNearBy.addItemDecoration(dividerItemDecoration);

        //Adapter
        NearyByAdapter nearyByAdapter = new NearyByAdapter(getNeayBy(), rvNearBy);
        rvNearBy.setAdapter(nearyByAdapter);
        nearyByAdapter.setOnItemClickListener(this);
        setActionBarEnabled(findViewById(R.id.toolbar));
    }

    private List<String> getNeayBy(){
        nearBys = getIntent().getStringArrayListExtra(NEARBY);
        return nearBys;
    }

    @Override
    public void onItemClick(View view, ViewHolder holder, int position) {
        String place = nearBys.get(position);
        Intent intent = new Intent();
        intent.putExtra(PLACE,place);
        finishForResult(intent);
    }

    private static class NearyByAdapter extends AutoViewStateAdapter<String>{

        public NearyByAdapter(List<String> dataSource, RecyclerView recyclerView) {
            super(dataSource, recyclerView);
            addItemViewDelegate(new ItemViewDelegate<String>() {
                @Override
                public int getItemViewLayoutId(int viewType) {
                    return R.layout.view_commponent_nearby_item;
                }

                @Override
                public boolean isForViewType(String item, int position) {
                    return true;
                }

                @Override
                public void convert(ViewHolder holder, String place, int position) {
                    holder.setText(R.id.tv_place,place);
                }
            });
        }

        @Override
        protected boolean areContentsTheSame(String oldItem, String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        protected boolean areItemsTheSame(String oldItem, String newItem) {
            return oldItem.equals(newItem);
        }
    }
}
