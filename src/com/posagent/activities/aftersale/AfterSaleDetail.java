package com.posagent.activities.aftersale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.AfterSaleCancel;
import com.example.zf_android.entity.AfterSaleMaintain;
import com.example.zf_android.entity.AfterSaleUpdate;
import com.example.zf_android.entity.CommentList;
import com.example.zf_android.entity.MarkEntity;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_ID;
import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_TYPE;
import static com.example.zf_android.trade.Constants.AfterSaleType.CANCEL;
import static com.example.zf_android.trade.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_android.trade.Constants.AfterSaleType.UPDATE;


/**
 * Created by Leo on 2015/2/28.
 */
public class AfterSaleDetail extends BaseActivity {

	private int mRecordType;
	private int mRecordId;
	private int mRecordStatus;

	private LayoutInflater mInflater;

    private AfterSaleMaintain entityMaintain;
    private AfterSaleUpdate entityUpdate;
    private AfterSaleCancel entityCancel;

    private LinearLayout container;
    private LinearLayout comments_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);
		mRecordId = getIntent().getIntExtra(RECORD_ID, 0);

        setContentView(R.layout.activity_after_sale_detail);

        String[] titles = getResources().getStringArray(R.array.title_after_sale_detail);
		new TitleMenuUtil(this, titles[mRecordType]).show();

		initViews();

		getData();
	}


	private void initViews() {
		mInflater = LayoutInflater.from(this);

        container = (LinearLayout) findViewById(R.id.after_sale_detail_category_container);
        comments_container = (LinearLayout) findViewById(R.id.after_sale_comment_container);

	}

	private void getData() {
        JsonParams params = new JsonParams();
        params.put("id", mRecordId);
        String strParams = params.toString();

        Events.CommonRequestEvent event = new Events.AfterSaleDetailMaintainEvent();
        switch (mRecordType) {
            case UPDATE:
                event = new Events.AfterSaleDetailUpdateEvent();
                break;
            case CANCEL:
                event = new Events.AfterSaleDetailCancelEvent();
                break;
        }

        event.setParams(strParams);
        EventBus.getDefault().post(event);
	}

    //events
    public void onEventMainThread(Events.AfterSaleDetailMaintainCompleteEvent event) {
        if (event.success()) {
            entityMaintain = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    public void onEventMainThread(Events.AfterSaleDetailUpdateCompleteEvent event) {
        if (event.success()) {
            entityUpdate = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    public void onEventMainThread(Events.AfterSaleDetailCancelCompleteEvent event) {
        if (event.success()) {
            entityCancel = event.getEntity();
            updateView();
        } else {
            toast(event.getMessage());
        }
    }

    private void updateView() {

        container.removeAllViews();
        comments_container.removeAllViews();

        switch (mRecordType) {
            case MAINTAIN:
                updateViewMaitain();
                break;
            case UPDATE:
                updateViewUpdate();

                break;
            case CANCEL:
                updateViewCancel();

                break;
        }

    }

    private void updateViewMaitain() {
        setText("after_sale_detail_status", statusName(entityMaintain.getStatus()));
        setText("after_sale_detail_time", entityMaintain.getApply_time());

        if (entityMaintain.getStatus() == 1) {
            Button btn = (Button)findViewById(R.id.after_sale_detail_button_1);
            btn.setText("取消申请");
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCancel();
                }
            });
        } else {
            hide("after_sale_detail_button_1");
        }

        // update info
        View view = mInflater.inflate(R.layout.aftersale_maitain_info, null);
        TextView tv = (TextView)view.findViewById(R.id.tv_terminal_numbers);
        tv.setText(entityMaintain.getTerminals_list());

        tv = (TextView)view.findViewById(R.id.tv_address);
        tv.setText(entityMaintain.getAddress());

        tv = (TextView)view.findViewById(R.id.tv_reson);
        tv.setText(entityMaintain.getReason());

        container.addView(view);

        // update comments
        CommentList comments = entityMaintain.getComments();

        if (null != comments) {
            updateComment(comments);
        }


    }

    private void updateViewUpdate() {

        hide("ll_comments");

        setText("after_sale_detail_status", statusName(entityUpdate.getStatus()));
        setText("after_sale_detail_time", entityUpdate.getApply_time());

        if (entityUpdate.getStatus() == 1) {
            Button btn = (Button)findViewById(R.id.after_sale_detail_button_1);
            btn.setText("取消申请");
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCancel();
                }
            });
        } else {
            hide("after_sale_detail_button_1");
        }

        // update info
        View view = mInflater.inflate(R.layout.aftersale_update_info, null);
        TextView tv = (TextView)view.findViewById(R.id.tv_terminal_number);
        tv.setText(entityUpdate.getTerminal_num());

        tv = (TextView)view.findViewById(R.id.tv_brand);
        tv.setText(entityUpdate.getBrand_name());

        tv = (TextView)view.findViewById(R.id.tv_model);
        tv.setText(entityUpdate.getBrand_number());

        tv = (TextView)view.findViewById(R.id.tv_pay_platform);
        tv.setText(entityUpdate.getZhifu_pingtai());

        tv = (TextView)view.findViewById(R.id.tv_merchant_name);
        tv.setText(entityUpdate.getMerchant_name());

        tv = (TextView)view.findViewById(R.id.tv_merchant_phone);
        tv.setText(entityUpdate.getMerchant_phone());

        container.addView(view);

        // update comments
        CommentList comments = entityUpdate.getComments();

        if (null != comments) {
            updateComment(comments);
        }
    }

    private void updateViewCancel() {

        hide("ll_comments");

        setText("after_sale_detail_status", statusName(entityCancel.getStatus()));
        setText("after_sale_detail_time", entityCancel.getApply_time());

        Button btn = (Button)findViewById(R.id.after_sale_detail_button_1);
        if (entityCancel.getStatus() == 1) {
            btn.setText("取消申请");
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCancel();
                }
            });
        } else {
            btn.setText("重新提交注销");
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doResubmit();
                }
            });
        }

        // update info
        View view = mInflater.inflate(R.layout.aftersale_update_info, null);
        TextView tv = (TextView)view.findViewById(R.id.tv_terminal_number);
        tv.setText(entityCancel.getTerminal_num());

        tv = (TextView)view.findViewById(R.id.tv_brand);
        tv.setText(entityCancel.getBrand_name());

        tv = (TextView)view.findViewById(R.id.tv_model);
        tv.setText(entityCancel.getBrand_number());

        tv = (TextView)view.findViewById(R.id.tv_pay_platform);
        tv.setText(entityCancel.getZhifu_pingtai());

        tv = (TextView)view.findViewById(R.id.tv_merchant_name);
        tv.setText(entityCancel.getMerchant_name());

        tv = (TextView)view.findViewById(R.id.tv_merchant_phone);
        tv.setText(entityCancel.getMerchant_phone());

        container.addView(view);

        // update comments
        CommentList comments = entityCancel.getComments();

        if (null != comments) {
            updateComment(comments);
        }

    }

    private String statusName(int status) {
        return Constants.AfterSale.STATUS[status];
    }


    private void doCancel() {
        JsonParams params = new JsonParams();
        Events.CommonRequestEvent event = new Events.AfterSaleMaintainCancelEvent();

        switch (mRecordType) {
            case MAINTAIN:
                params.put("id", entityMaintain.getId());

                event = new Events.AfterSaleCancelCancelEvent();
                break;
            case CANCEL:
                params.put("id", entityCancel.getId());

                event = new Events.AfterSaleCancelCancelEvent();

                break;
            case UPDATE:
                params.put("id", entityUpdate.getId());

                event = new Events.AfterSaleUpdateCancelEvent();

                break;
        }
        String strParams = params.toString();

        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    private void doResubmit() {
        JsonParams params = new JsonParams();
        params.put("id", entityCancel.getId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.AfterSaleCancelResubmitEvent();

        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    private void updateComment(CommentList commentList) {
        List<MarkEntity> comments = commentList.getList();

        View view;
        for (MarkEntity comment: comments) {
            view = mInflater.inflate(R.layout.remark_item, null);
            TextView tv = (TextView)view.findViewById(R.id.tv_name);
            tv.setText(comment.getMarks_person());
            tv = (TextView)view.findViewById(R.id.tv_content);
            tv.setText(comment.getMarks_content());
            tv = (TextView)view.findViewById(R.id.tv_time);
            tv.setText(comment.getMarks_time());
            comments_container.addView(view);
        }
    }



    // events
    public void onEventMainThread(Events.AfterSaleCancelCompleteEvent event) {
        getData();
        toast(event.getMessage());
    }

    public void onEventMainThread(Events.AfterSaleResubmitCompleteEvent event) {
        getData();
        toast(event.getMessage());
    }



}
