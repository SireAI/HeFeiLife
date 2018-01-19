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
import android.widget.TextView;

import com.sire.corelibrary.Bug.CleanLeakUtils;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.usermodule.R;

import static com.sire.usermodule.Constant.Constant.COLLEGE;
import static com.sire.usermodule.Constant.Constant.INSTRUCTION;
import static com.sire.usermodule.Constant.Constant.OLD_COLLEGE;
import static com.sire.usermodule.Constant.Constant.OLD_SIGN;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/17
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PersonalSignController extends SireController implements TextWatcher {

    private EditText etSign;
    private TextView tvLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_personal_sign);
        etSign = findViewById(R.id.et_sign);
        tvLeft = findViewById(R.id.tv_left);
        etSign.addTextChangedListener(this);
        String oldSign = getIntent().getStringExtra(OLD_SIGN);
        if (!TextUtils.isEmpty(oldSign)) {
            etSign.setText(oldSign);
            etSign.setSelection(oldSign.length());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(s)&& s.length()>0){
            int alreadyWriteCount = s.length();
            int leftCount = 150 - alreadyWriteCount;
            tvLeft.setText(leftCount+"");
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onSave(View view) {
        hideSoftInput(etSign);
        if (TextUtils.isEmpty(etSign.getText().toString())) {
            ToastUtils.showToast(this, getString(R.string.instruction_not_null));
        } else {
            Intent intent = new Intent();
            intent.putExtra(INSTRUCTION, etSign.getText().toString());
            finishForResult(intent);
        }
    }
    /**
     * 关闭键盘
     **/
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanLeakUtils.fixInputMethodManagerLeak(this);
    }
}
