package com.sire.corelibrary.View;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sire.corelibrary.R;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.CommonUtils;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PopOperation extends DialogFragment implements View.OnClickListener {

    public static final String ITEMS = "items";
    private CallBack callBack;

    public void showDialog(FragmentActivity activity, CallBack callBack, String... itemName) {
        this.callBack = callBack;
        Bundle bundle = new Bundle();
        bundle.putStringArray(ITEMS, itemName);
        setArguments(bundle);
        show(activity.getSupportFragmentManager(), "popOperation");
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return generateView();
    }

    private View generateView() {
        LinearLayout container = new LinearLayout(getActivity());
        int padding = CommonUtils.dip2px(getActivity(), 10);
        container.setPadding(0, padding, 0, padding);
        container.setOrientation(LinearLayout.VERTICAL);
        String[] items = getArguments().getStringArray(ITEMS);
        for (int i = 0; i < items.length; i++) {
            TextView itemView = new TextView(getActivity());
            itemView.setText(items[i]);
            itemView.setTag(i);
            itemView.setTextColor(getResources().getColor(R.color.white_bg_1_text));
            itemView.setTextSize(14);
            itemView.setOnClickListener(this);
            itemView.setBackground(getResources().getDrawable(R.drawable.selector_item_press));
            int leftRight = CommonUtils.dip2px(getActivity(), 15);
            int topBottom = CommonUtils.dip2px(getActivity(), 15);
            itemView.setPadding(leftRight, topBottom, leftRight, topBottom);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.addView(itemView);
        }
        return container;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.progressHUDTheme);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int screenWidth = APPUtils.getScreenWidth(getContext());
            dialog.getWindow().setLayout((int) (screenWidth * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_pop_bg);
            dialog.setCanceledOnTouchOutside(true);
        }

    }

    @Override
    public void onClick(View v) {
        if (callBack != null) {
            callBack.onSelect((Integer) v.getTag(), ((TextView) v).getText().toString());
        }
    }


    public interface CallBack {
        void onSelect(int index, String item);
    }
}
