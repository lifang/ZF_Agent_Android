package com.example.zf_android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.examlpe.zf_android.util.XListView;
import com.examlpe.zf_android.util.XListView.IXListViewListener;
import com.example.zf_android.Config;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.entity.GoodCommentEntity;
import com.example.zf_zandroid.adapter.GoodCommentAdapter;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/***
 * 
*    
* 类名称：GoodComment   
* 类描述：    商品评论
* 创建人： ljp 
* 创建时间：2015-3-11 上午10:18:58   
* @version    
*
 */
public class GoodComment extends BaseActivity implements  IXListViewListener{
 
	private XListView Xlistview;
	private int page=1;
	private int rows=Config.ROWS;
	private int goodId;
	private String title;
	private LinearLayout eva_nodata;
	private boolean onRefresh_number = true;
	private GoodCommentAdapter myAdapter;
	List<GoodCommentEntity>  myList = new ArrayList<GoodCommentEntity>();
	List<GoodCommentEntity>  moreList = new ArrayList<GoodCommentEntity>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
                case 0:
                    onLoad();

                    if (myList.size() == 0) {
                        Xlistview.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    }
                    onRefresh_number = true;
                    myAdapter.notifyDataSetChanged();
                    break;
            }
		}
	};
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_commet);
		title=getIntent().getStringExtra("commentsCount");
		goodId=getIntent().getIntExtra("goodId", 1);
		
		
		initView();
		getData(); 
	}

	private void initView() {

		new TitleMenuUtil(GoodComment.this, "评论列表").show();
		myAdapter=new GoodCommentAdapter(GoodComment.this, myList);
		eva_nodata=(LinearLayout) findViewById(R.id.eva_nodata);
		Xlistview=(XListView) findViewById(R.id.x_listview);
		Xlistview.setPullLoadEnable(true);
		Xlistview.setXListViewListener(this);
		Xlistview.setDivider(null);

		Xlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			//	Intent i = new Intent(GoodComment.this, OrderDetail.class);
			//	startActivity(i);
			}
		});
		Xlistview.setAdapter(myAdapter);
	}

    @Override
    public void onRefresh() {
        page = 1;
        myList.clear();
        getData();
    }


    @Override
    public void onLoadMore() {
        if (onRefresh_number) {
            page = page + 1;
            if (Tools.isConnect(getApplicationContext())) {
                onRefresh_number = false;
                getData();
            } else {
                onRefresh_number = true;
                EventBus.getDefault().post(new Events.NoConnectEvent());
            }
        } else {
            EventBus.getDefault().post(new Events.RefreshToMuch());
        }
    }

    private void onLoad() {
        Xlistview.stopRefresh();
        Xlistview.stopLoadMore();
        Xlistview.setRefreshTime(Tools.getHourAndMin());
    }

    private void getData() {
        JsonParams params = new JsonParams();
        params.put("goodId", goodId);
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.GoodsCommentListEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onClick(View v) {
        // 特殊 onclick 处理，如有特殊处理，
        // 则直接 return，不再调用 super 处理
        super.onClick(v);
    }

    // events
    public void onEventMainThread(Events.GoodsCommentListCompleteEvent event) {
        myList.addAll(event.getList());
        Xlistview.setPullLoadEnable(event.getList().size() >= rows);
        handler.sendEmptyMessage(0);
    }
}
