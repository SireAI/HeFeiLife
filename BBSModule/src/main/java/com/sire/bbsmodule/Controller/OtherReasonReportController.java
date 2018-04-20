package com.sire.bbsmodule.Controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.ViewModel.BBSViewModel;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.ProgressHUD;

import javax.inject.Inject;

import static com.sire.bbsmodule.Constant.Constant.REPORT_REASON;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/08
 * Author:Sire
 * Description:
 * ==================================================
 */

public class OtherReasonReportController extends SireController {

    @Inject
    ViewModelProvider.Factory factory;
    private EditText etReason;
    private BBSViewModel bbsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_other_reason_report);
        setSupportActionBar(findViewById(R.id.toolbar));
        etReason = findViewById(R.id.et_reason);
        bbsViewModel = ViewModelProviders.of(this, factory).get(BBSViewModel.class);

    }

    public void onSubmit(View view) {
        String reason = etReason.getText().toString();
        ReportReason reportReason = (ReportReason) getIntent().getSerializableExtra(REPORT_REASON);
        reportReason.setReportReason(reason);
        bbsViewModel.report(reportReason).observe(this, new Observer<DataResource<ReportReason>>() {
            @Override
            public void onChanged(@Nullable DataResource<ReportReason> reportReasonDataResource) {
                switch (reportReasonDataResource.status) {
                    case LOADING:
                        ProgressHUD.showDialog(OtherReasonReportController.this);
                        break;
                    case SUCCESS:
                        ProgressHUD.close();
                        ToastUtils.showToast(OtherReasonReportController.this, "举报成功");
                        finish();
                        break;
                    case ERROR:
                        ProgressHUD.close();
                        ToastUtils.showToast(OtherReasonReportController.this, "举报失败");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
