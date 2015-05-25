package com.posagent.activities.goods;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.GoodsPictureEntity;
import com.example.zf_android.trade.common.CommonUtil;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.List;

import de.greenrobot.event.EventBus;

public class PictureDetail extends BaseActivity implements OnClickListener {

    private String goodsId;

    private LinearLayout ll_picture_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);

        goodsId = getIntent().getStringExtra("goodsId");

        new TitleMenuUtil(PictureDetail.this, "图文详情").show();

        ll_picture_list = (LinearLayout)findViewById(R.id.ll_picture_list);

        getData();
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("goodId", goodsId);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.GoodsPictureListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    // events

    public void onEventMainThread(Events.GoodsPictureListCompleteEvent event) {
        if (!event.success() || event.getList().size() == 0) {
            toast("没有图文信息");
            finish();
        }

        List<GoodsPictureEntity> list = event.getList();

        Point size = CommonUtil.screenSize(this);
        int width = size.x;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, width);
        lp.topMargin = 10;
        for (GoodsPictureEntity entity: list) {
            View view = getLayoutInflater().inflate(R.layout.item_for_guide, null);
            ImageView iv = (ImageView)view.findViewById(R.id.image);
            ImageCacheUtil.IMAGE_CACHE.get(entity.getUrlPath(), iv);
            view.setLayoutParams(lp);

            ll_picture_list.addView(view);
        }
    }
}

