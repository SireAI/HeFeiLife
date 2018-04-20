package com.sire.corelibrary.View;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.R;
import com.sire.corelibrary.Utils.UIUtils;

import static com.sire.corelibrary.View.AutoStateView.State.ERROR;
import static com.sire.corelibrary.View.AutoStateView.State.LOADING;
import static com.sire.corelibrary.View.AutoStateView.State.UNDEFINED;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/10
 * Author:Sire
 * Description:
 * ==================================================
 */

public class AutoStateView {

    private State state = UNDEFINED;

    public void loadView(State state, SireController controller) {
        FrameLayout decorView = (FrameLayout) controller.getWindow().getDecorView();
        View view = createStateView(controller, state);
        if(view!=null){
            decorView.addView(view);
        }
        decorView.removeView(decorView.findViewWithTag(this.state));
        this.state = state;
    }

    public void loadView(State state, ViewGroup rootView) {
        View view = createStateView(rootView.getContext(), state);
        if(view!=null){
            rootView.addView(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        rootView.removeView(rootView.findViewWithTag(this.state));
        this.state = state;
    }

    private View createStateView(Context context, State state) {
        View view = null;
        switch (state) {
            case ERROR://ERROR
                view = UIUtils.inflate(R.layout.view_componnent_error, context);
                view.setTag(ERROR);
                break;
            case LOADING://LOADING
                view = UIUtils.inflate(R.layout.view_componnent_loading, context);
                view.setTag(LOADING);
                break;
        }
        return view;
    }

   public enum State {
        LOADING, ERROR,NORMAL,UNDEFINED
    }
}
