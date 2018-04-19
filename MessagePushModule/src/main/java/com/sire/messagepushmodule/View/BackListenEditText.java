package com.sire.messagepushmodule.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.sire.corelibrary.View.EmojiView.EmojiEditText;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/30
 * Author:Sire
 * Description:
 * ==================================================
 */

public class BackListenEditText extends EmojiEditText {

    private DeleteInputConnection deleteInputConnection;

    public interface BackListener{
        boolean onBackSoftInputDismiss();
    }
    private BackListener backListener;
    public BackListenEditText(Context context) {
        super(context);
    }

    public BackListenEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1) {
            boolean consume = false;
            if(backListener!=null){
                consume = backListener.onBackSoftInputDismiss();
            }
            super.onKeyPreIme(keyCode, event);
            return consume;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        deleteInputConnection = new DeleteInputConnection(super.onCreateInputConnection(outAttrs),
                true);

        return deleteInputConnection;
    }
    private void setDeleteInputConnection(){
        if(deleteInputConnection!=null){
            deleteInputConnection.setTarget(null);
            deleteInputConnection = null;
        }
    }

    public void onDestroy(){
        setBackListener(null);
        setDeleteInputConnection();
    }


    private static class DeleteInputConnection extends InputConnectionWrapper {

        public DeleteInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }
}
