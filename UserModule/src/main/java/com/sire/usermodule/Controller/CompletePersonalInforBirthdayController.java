package com.sire.usermodule.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.CommonUtils;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.usermodule.R;
import com.sire.usermodule.View.DatePickerSet;
import com.sire.usermodule.ViewModel.UserViewModel;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.SEX;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/20
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CompletePersonalInforBirthdayController extends SireController{
    private UserViewModel userViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    private TextView tvBirthday;
    private CoordinatorLayout clBirthday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_complete_personalinfor_birthday);
        tvBirthday = findViewById(R.id.tv_birthday);
        clBirthday = findViewById(R.id.cl_birthday);
        DatePicker dp = findViewById(R.id.dp);
        setSupportActionBar(findViewById(R.id.toolbar));

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        initPicker(dp);
        setSex();
        try {
            DatePickerSet.setDatePicker(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSex() {
        Intent intent = getIntent();
        String sex = intent.getStringExtra(SEX);
       ImageView ivSex =  findViewById(R.id.iv_sex);
       if("男".equals(sex)){
           ivSex.setImageResource(R.mipmap.male);
       }else {
           ivSex.setImageResource(R.mipmap.female);
       }
    }

    public void onSkip(View view){
        segueToCompletePersonalInforHobbyController();
    }
    public void onNext(View view){
        userViewModel.updateBirthday(((String)tvBirthday.getTag()),userViewModel.getUserId()).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(CompletePersonalInforBirthdayController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    segueToCompletePersonalInforHobbyController();
                    break;
                case ERROR:
                    ProgressHUD.close();
                    SnackbarUtils.basicSnackBar(clBirthday, dataResource.message, CompletePersonalInforBirthdayController.this);
                    break;
                default:
                    break;
            }
        });
    }

    private void segueToCompletePersonalInforHobbyController() {
        Intent intent = new Intent(this, CompletePersonalInforHobbyController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
        segueForResult(Segue.SegueType.PUSH,intent);
    }

    private void initPicker(DatePicker dp) {
        //设置中文日历
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int syear=calendar.get(Calendar.YEAR);
        int smonth=calendar.get(Calendar.MONTH);
        int sday=calendar.get(Calendar.DAY_OF_MONTH);
        setDate(calendar,syear,smonth,sday);
        //初始化datePicker
        dp.init(syear, smonth, sday, (view, year, monthOfYear, dayOfMonth) -> {
            setDate(calendar, year, monthOfYear, dayOfMonth);
        });

    }

    private void setDate(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear+=1;
        String month="";
        String day="";
        if(monthOfYear<10){
            month="0";
        }
        month+=monthOfYear;
        if(dayOfMonth<10){
            day+="0";
        }
        day+=dayOfMonth;
        tvBirthday.setText(year+"年"+month+"月"+day+"日");
        int tempMonth = monthOfYear-1;
        calendar.set(year,tempMonth,dayOfMonth);
        saveDate(calendar);
    }

    private void saveDate(Calendar calendar) {
        String strDate = CommonUtils.formatDate(calendar.getTime(), "yyyy-MM-dd");
        tvBirthday.setTag(strDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            setResult(LOGIN_REQUEST_CODE);
            finish();
        }
    }
}
