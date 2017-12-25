package com.sire.corelibrary.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.sire.corelibrary.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/4
 * Author:sire
 * Description:manage segue and segue animation
 * ==================================================
 */
public class Segue {
    public static final int NET_ITEM_MARGIN = 4;
    public static final int REQUEST_CODE_UNDEFINED = 100;
    public static final String FOR_RESULT_REQUEST_CODE = "requestCode";
    private static final String SEGUE_TYPE = "segueType";
    private static final int NO_ANIM = 10000;
    private static Map<SegueType, SegueAnimation> mAnimResource = new HashMap<>();

    /**
     * add transition animation here
     */
    static {
        new SegueAnimation(SegueType.PUSH, R.anim.controller_push_enter, R.anim.controller_push_exit_under, R.anim.controller_push_enter_under, R.anim.controller_push_exit);
        new SegueAnimation(SegueType.MODAL, R.anim.controller_modal_enter_from_bottom, R.anim.controller_modal_exit_under, R.anim.controller_modal_enter_under, R.anim.controller_modal_exit_from_bottom);
        new SegueAnimation(SegueType.CROSS, R.anim.controller_cross_enter, R.anim.controller_cross_exit_under, R.anim.controller_cross_enter_under, R.anim.controller_cross_exit);
        new SegueAnimation(SegueType.SACLE_UP, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
        new SegueAnimation(SegueType.TRADITIONAL, NO_ANIM, NO_ANIM, NO_ANIM, NO_ANIM);
    }

    @Inject
    public Segue() {
    }

    @SuppressLint("RestrictedApi")
    public void segueForward(SegueType segueType, Intent intent, SireController controller, boolean forResult,PagePositionData pagePositionData) {

        switch (segueType) {
            case PUSH:
            case CROSS:
            case MODAL:
                intent.putExtra(SEGUE_TYPE, segueType.name());
                SegueAnimation segueAnimation = mAnimResource.get(segueType);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(controller, segueAnimation.forwordAnimIn, segueAnimation.forwordAnimOut);
                animateTransition(intent, controller, forResult, activityOptionsCompat);
                break;
            case TRADITIONAL:
                if (forResult) {
                    controller.startActivityForResult(intent, intent.getIntExtra(SEGUE_TYPE, 100));
                } else {
                    controller.startActivity(intent);
                }
                break;
            case CLIP_REVEAL:
                ActivityOptionsCompat activityOptionsCompatReveal;
                if (Build.VERSION.SDK_INT >= 23) {
                    activityOptionsCompatReveal = ActivityOptionsCompat.makeClipRevealAnimation(pagePositionData.getSource(), pagePositionData.getStartX(), pagePositionData.getStartY(), pagePositionData.getWidth(), pagePositionData.getHeight());
                }else {
                     activityOptionsCompatReveal = ActivityOptionsCompat.makeScaleUpAnimation(pagePositionData.getSource(), pagePositionData.getStartX(), pagePositionData.getStartY(), pagePositionData.getWidth(), pagePositionData.getHeight());
                }
                animateTransition(intent, controller, forResult, activityOptionsCompatReveal);
                break;
            case SACLE_UP:
                ActivityOptionsCompat  activityOptionsCompatScaleUp = ActivityOptionsCompat.makeScaleUpAnimation(pagePositionData.getSource(), pagePositionData.getStartX(), pagePositionData.getStartY(), pagePositionData.getWidth(), pagePositionData.getHeight());
                animateTransition(intent, controller, forResult, activityOptionsCompatScaleUp);

                break;
            default:
                break;

        }

    }

    @SuppressLint("RestrictedApi")
    private void animateTransition(Intent intent, SireController controller, boolean forResult, ActivityOptionsCompat activityOptionsCompat) {
        if (forResult) {
            controller.startActivityForResult(intent, intent.getIntExtra(FOR_RESULT_REQUEST_CODE, REQUEST_CODE_UNDEFINED), activityOptionsCompat.toBundle());
        } else {
            controller.startActivity(intent, activityOptionsCompat.toBundle());
        }
    }


    /**
     * back forward
     *
     * @param controller
     */
    public void segueBack(SireController controller) {
        SegueType segueType = getSegueType(controller);
        if(segueType!=null){
            switch (segueType){
                case TRADITIONAL:
                    break;
                case MODAL:
                case PUSH:
                case CROSS:
                    SegueAnimation segueAnimation = mAnimResource.get(segueType);
                    controller.overridePendingTransition(segueAnimation.backAnimIn, segueAnimation.backAnimOut);
                    break;
                case SACLE_UP:
                case CLIP_REVEAL:
                    break;
                    default:
                        break;
            }
        }

    }

    private SegueType getSegueType(SireController controller) {
        Intent intent = controller.getIntent();
        if (intent == null || !intent.hasExtra(SEGUE_TYPE)) {
            return null;
        }
        return SegueType.valueOf(intent.getStringExtra(SEGUE_TYPE));
    }


    /**
     * transition animation type
     */
    public enum SegueType {
        PUSH, MODAL, CROSS, TRADITIONAL, SACLE_UP,CLIP_REVEAL;
    }


    /**
     * animation entry
     */
    private static class SegueAnimation {
        SegueType segueType;
        int forwordAnimIn;
        int forwordAnimOut;
        int backAnimIn;
        int backAnimOut;

        public SegueAnimation(SegueType segueType, int forwordAnimIn, int forwordAnimOut, int backAnimIn, int backAnimOut) {
            this.segueType = segueType;
            this.forwordAnimIn = forwordAnimIn;
            this.forwordAnimOut = forwordAnimOut;
            this.backAnimIn = backAnimIn;
            this.backAnimOut = backAnimOut;
            mAnimResource.put(segueType, this);
        }
    }

    public static class PagePositionData implements Serializable{
        View source;
        int startX;
        int startY;
        int width;
        int height;

        public PagePositionData(View source, int startX, int startY, int width, int height) {
            this.source = source;
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
        }

        public View getSource() {
            return source;
        }

        public void setSource(View source) {
            this.source = source;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}


