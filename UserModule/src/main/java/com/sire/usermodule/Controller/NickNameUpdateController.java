package com.sire.usermodule.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.sire.corelibrary.Bug.CleanLeakUtils;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.usermodule.R;

import static com.sire.usermodule.Constant.Constant.NEW_NICK_NAME;
import static com.sire.usermodule.Constant.Constant.OLD_NICK_NAME;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/16
 * Author:Sire
 * Description:
 * ==================================================
 */

public class NickNameUpdateController extends SireController implements TextWatcher, View.OnClickListener {

    private EditText editText;
    private ImageView ivDelete;

    /**
     * 关闭键盘
     **/
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_nick_name_update);
        setActionBarEnabled(findViewById(R.id.toolbar));
        editText = findViewById(R.id.et_text);
        editText.addTextChangedListener(this);
        ivDelete = findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(this);
        String oldNickName = getIntent().getStringExtra(OLD_NICK_NAME);
        if (!TextUtils.isEmpty(oldNickName)) {
            editText.setText(oldNickName);
            editText.setSelection(oldNickName.length());
        }
    }

    public void onSave(View view) {
        hideSoftInput(editText);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            ToastUtils.showToast(this, getString(R.string.nick_name_can_not_null));
        } else {
            Intent intent = new Intent();
            intent.putExtra(NEW_NICK_NAME, editText.getText().toString());
            finishForResult(intent);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ivDelete.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        editText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanLeakUtils.fixInputMethodManagerLeak(this);
    }
}
