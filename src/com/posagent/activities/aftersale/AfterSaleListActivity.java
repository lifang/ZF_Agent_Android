package com.posagent.activities.aftersale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.examlpe.zf_android.util.Tools;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.activity.SearchFormCommon;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.entity.AfterSaleRecord;
import com.example.zf_android.trade.widget.XListView;
import com.posagent.MyApplication;
import com.posagent.activities.BaseActivity;
import com.posagent.events.Events;
import com.posagent.utils.Constants;
import com.posagent.utils.JsonParams;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_ID;
import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_STATUS;
import static com.example.zf_android.trade.Constants.AfterSaleIntent.RECORD_TYPE;
import static com.example.zf_android.trade.Constants.AfterSaleIntent.REQUEST_DETAIL;
import static com.example.zf_android.trade.Constants.AfterSaleIntent.REQUEST_MARK;
import static com.example.zf_android.trade.Constants.AfterSaleType.CANCEL;
import static com.example.zf_android.trade.Constants.AfterSaleType.CHANGE;
import static com.example.zf_android.trade.Constants.AfterSaleType.LEASE;
import static com.example.zf_android.trade.Constants.AfterSaleType.MAINTAIN;
import static com.example.zf_android.trade.Constants.AfterSaleType.RETURN;
import static com.example.zf_android.trade.Constants.AfterSaleType.UPDATE;

/**
 * Created by Leo on 2015/2/26.
 */
public class AfterSaleListActivity extends BaseActivity implements XListView.IXListViewListener,
        AdapterView.OnItemSelectedListener
{

	private int mRecordType;

	private XListView mListView;
	private RecordListAdapter mAdapter;
	private List<AfterSaleRecord> mEntities;
    private Spinner spinnerstate;

	private int page = 1;
	private int total = 0;
	private final int rows = 10;

    private String keys;

    private int q = 0;

	private LayoutInflater mInflater;

    private LinearLayout eva_nodata;

	// cancel apply button listener
	private View.OnClickListener mCancelApplyListener;
	// submit mark button listener
	private View.OnClickListener mSubmitMarkListener;
	// pay maintain button listener;
	private View.OnClickListener mPayMaintainListener;
	// submit cancel button listener
	private View.OnClickListener mSubmitCancelListener;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    if (mEntities.size() == 0) {
                        mListView.setVisibility(View.GONE);
                        eva_nodata.setVisibility(View.VISIBLE);
                    } else {
                        mListView.setVisibility(View.VISIBLE);
                        eva_nodata.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private String[] status;
    private String[] spinnerStatus;
    private Map<String, Integer> mapStatus;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);

		setContentView(R.layout.activity_after_sale_list);
		String[] titles = getResources().getStringArray(R.array.title_after_sale_list);
		new TitleMenuUtil(this, titles[mRecordType]).show();

        status = Constants.AfterSale.STATUS;

        mapStatus = new LinkedHashMap<String, Integer>();
        mapStatus.put("请选择状态", 0);

        for (int i = 0; i < status.length; i++) {
            String state = status[i];
            if (!state.equals("") && !state.equals("未知")) {
                mapStatus.put(state, i);
            }
        }
        spinnerStatus = mapStatus.keySet().toArray(new String[mapStatus.keySet().size()]);

        spinnerstate = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, spinnerStatus);
        spinnerstate.setAdapter(adapter_state);
        spinnerstate.setOnItemSelectedListener(this);


		mInflater = LayoutInflater.from(this);
		mEntities = new ArrayList<AfterSaleRecord>();
        eva_nodata = (LinearLayout) findViewById(R.id.eva_nodata);
		mListView = (XListView) findViewById(R.id.after_sale_list);
		mAdapter = new RecordListAdapter();

        //icons
        show("iv_search_icon_terminal");
        findViewById(R.id.iv_search_icon_terminal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SearchFormCommon.class);
                i.putExtra("save_key", "AfterSaleHistory");
                i.putExtra("hint_text", "输入售后记录编号");
                i.putExtra("keys", keys);
                startActivityForResult(i, Constants.REQUEST_CODE);
            }
        });

		// init the XListView
		mListView.initHeaderAndFooter();
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);

		mListView.setAdapter(mAdapter);
		initButtonListeners();
//		loadData();
	}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String key = spinnerStatus[position];
        q = mapStatus.get(key);
        keys = null;
        onRefresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        mEntities.clear();
        mAdapter.notifyDataSetChanged();
        loadData();
    }

    private void loadData() {
        JsonParams params = new JsonParams();
        params.put("customerId", MyApplication.user().getId());
        if (q > 0) {
            params.put("q", q);
        }
        if (null != keys) {
            params.put("search", keys);
        }
        params.put("page", page);
        params.put("rows", rows);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.AfterSaleMaintainListEvent();
        switch (mRecordType) {
            case CANCEL:
                event = new Events.AfterSaleCancelListEvent();

                break;
            case UPDATE:
                event = new Events.AfterSaleUpdateListEvent();

                break;
        }
        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }

    private void doCancel(final AfterSaleRecord record) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定取消吗？");
        builder.setTitle("请确认");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JsonParams params = new JsonParams();
                params.put("id", record.getId());
                String strParams = params.toString();
                Events.CommonRequestEvent event = new Events.AfterSaleMaintainCancelEvent();
                switch (mRecordType) {
                    case CANCEL:
                        event = new Events.AfterSaleCancelCancelEvent();

                        break;
                    case UPDATE:
                        event = new Events.AfterSaleUpdateCancelEvent();

                        break;
                }
                event.setParams(strParams);
                EventBus.getDefault().post(event);

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();



    }

    private void doResubmit(AfterSaleRecord record) {
        JsonParams params = new JsonParams();
        params.put("id", record.getId());
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.AfterSaleCancelResubmitEvent();

        event.setParams(strParams);
        EventBus.getDefault().post(event);

    }



    // events
    public void onEventMainThread(Events.AfterSaleListCompleteEvent event) {
        mEntities.addAll(event.getList());
        mListView.setPullLoadEnable(event.getList().size() >= rows);

        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(Tools.getHourAndMin());

        handler.sendEmptyMessage(0);
    }

    public void onEventMainThread(Events.AfterSaleCancelCompleteEvent event) {
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(Events.AfterSaleResubmitCompleteEvent event) {
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(Events.GoodsDoSearchCompleteEvent event) {
        keys = event.getKeys();
        onRefresh();
    }

	private void initButtonListeners() {
		mCancelApplyListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AfterSaleRecord record = (AfterSaleRecord) v.getTag();
                record.setStatus(5);
                doCancel(record);
			}
		};

		mSubmitMarkListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AfterSaleRecord record = (AfterSaleRecord) v.getTag();
				Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleMarkActivity.class);
				intent.putExtra(RECORD_TYPE, mRecordType);
				intent.putExtra(RECORD_ID, record.getId());
				startActivityForResult(intent, REQUEST_MARK);
			}
		};

		mPayMaintainListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(AfterSaleListActivity.this, AfterSalePayActivity.class));
			}
		};

		mSubmitCancelListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AfterSaleRecord record = (AfterSaleRecord) v.getTag();
                record.setStatus(1);
                doResubmit(record);
			}
		};
	}

	@Override
	public void onLoadMore() {
		if (mEntities.size() >= total) {
			mListView.stopLoadMore();
			CommonUtil.toastShort(this, "no more data");
		} else {
			loadData();
		}
	}

	private void loadFinished() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(Tools.getHourAndMin());
	}

	class RecordListAdapter extends BaseAdapter {
		RecordListAdapter() {
		}

		@Override
		public int getCount() {
			return mEntities.size();
		}

		@Override
		public AfterSaleRecord getItem(int position) {
			return mEntities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.after_sale_record_item, null);
				holder = new ViewHolder();
				holder.tvNumberTitle = (TextView) convertView.findViewById(R.id.after_sale_number_title);
				holder.tvNumber = (TextView) convertView.findViewById(R.id.after_sale_number);
				holder.tvTime = (TextView) convertView.findViewById(R.id.after_sale_time);
				holder.tvTerminal = (TextView) convertView.findViewById(R.id.after_sale_terminal);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.after_sale_status);
				holder.llButtonContainer = (LinearLayout) convertView.findViewById(R.id.after_sale_button_container);
				holder.btnLeft = (Button) convertView.findViewById(R.id.after_sale_button_left);
				holder.btnRight = (Button) convertView.findViewById(R.id.after_sale_button_right);
				holder.btnCenter = (Button) convertView.findViewById(R.id.after_sale_button_center);
				holder.btnCenterBlank = (Button) convertView.findViewById(R.id.after_sale_button_center_blank);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AfterSaleRecord data = getItem(position);
			String[] numberTitles = getResources().getStringArray(R.array.after_sale_number);
			holder.tvNumberTitle.setText(numberTitles[mRecordType]);
			holder.tvNumber.setText(data.getApplyNum());
			holder.tvTime.setText(data.getCreateTime());
			holder.tvTerminal.setText(data.getTerminalNum());
			holder.tvStatus.setText(Constants.AfterSale.STATUS[data.getStatus()]);

			switch (mRecordType) {
				case MAINTAIN:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(getString(R.string.button_cancel_apply));
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);

					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case CANCEL:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else if (data.getStatus() == 5) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);

						holder.btnCenter.setText(R.string.button_submit_cancel);
						holder.btnCenter.setTag(data);
						holder.btnCenter.setOnClickListener(mSubmitCancelListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case UPDATE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(getString(R.string.button_cancel_apply));
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case RETURN:
				case CHANGE:
				case LEASE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);

						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
						holder.btnCenterBlank.setTag(data);
						holder.btnCenterBlank.setOnClickListener(mCancelApplyListener);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleDetail.class);
					intent.putExtra(RECORD_TYPE, mRecordType);
					intent.putExtra(RECORD_ID, data.getId());
					startActivityForResult(intent, REQUEST_DETAIL);
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvNumberTitle;
		public TextView tvNumber;
		public TextView tvTime;
		public TextView tvTerminal;
		public TextView tvStatus;
		public LinearLayout llButtonContainer;
		public Button btnLeft;
		public Button btnRight;
		public Button btnCenter;
		public Button btnCenterBlank;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_DETAIL:
					int id = data.getIntExtra(RECORD_ID, 0);
					int status = data.getIntExtra(RECORD_STATUS, 0);
					if (id > 0 && status > 0) {
						for (AfterSaleRecord record : mEntities) {
							if (record.getId() == id) {
								record.setStatus(status);
								mAdapter.notifyDataSetChanged();
							}
						}
					}
					break;
				case REQUEST_MARK:
					CommonUtil.toastShort(this, getString(R.string.toast_add_mark_success));
					break;
			}

		}
	}
}
