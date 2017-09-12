package com.sire.corelibrary.Controller;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;

import com.sire.corelibrary.R;

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


    public void segueForward(SegueType segueType, Intent intent, SireController controller) {
        intent.putExtra(SEGUE_TYPE, segueType.name());
        SegueAnimation segueAnimation = mAnimResource.get(segueType);
        if (segueType != SegueType.TRADITIONAL) {
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(controller, segueAnimation.forwordAnimIn, segueAnimation.forwordAnimOut);
            controller.startActivity(intent, activityOptionsCompat.toBundle());
        } else {
            controller.startActivity(intent);
        }
    }

    ;

    /**
     * back forward
     *
     * @param controller
     */
    public void segueBack(SireController controller) {
        SegueType segueType = getSegueType(controller);
        if (segueType != null && segueType != SegueType.TRADITIONAL) {
            SegueAnimation segueAnimation = mAnimResource.get(segueType);
            controller.overridePendingTransition(segueAnimation.backAnimIn, segueAnimation.backAnimOut);
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
        PUSH, MODAL, CROSS, TRADITIONAL, SACLE_UP;
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
}


