package com.example.zf_android.trade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.examlpe.zf_android.util.StringUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.entity.MerchantEntity;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.entity.ApplyChooseItem;
import com.example.zf_android.trade.entity.ApplyCustomerDetail;
import com.example.zf_android.trade.entity.ApplyDetail;
import com.example.zf_android.trade.entity.ApplyMaterial;
import com.example.zf_android.trade.entity.ApplyTerminalDetail;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Merchant;
import com.example.zf_android.trade.entity.Province;
import com.example.zf_android.trade.entity.TerminalOpenInfo;
import com.example.zf_android.trade.widget.MyTabWidget;
import com.posagent.MyApplication;
import com.posagent.activities.ChannelSelecter;
import com.posagent.activities.ImageViewer;
import com.posagent.activities.terminal.BankList;
import com.posagent.events.Events;
import com.posagent.utils.JsonParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.example.zf_android.trade.Constants.ApplyIntent.CHOOSE_ITEMS;
import static com.example.zf_android.trade.Constants.ApplyIntent.CHOOSE_TITLE;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_CHOOSE_BANK;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_CHOOSE_CHANNEL;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_CHOOSE_CITY;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_CHOOSE_MERCHANT;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_TAKE_PHOTO;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_UPLOAD_IMAGE;
import static com.example.zf_android.trade.Constants.ApplyIntent.SELECTED_ID;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_CITY;
import static com.example.zf_android.trade.Constants.CityIntent.SELECTED_PROVINCE;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_ID;
import static com.example.zf_android.trade.Constants.TradeIntent.AGENT_NAME;

/**
 * Created by Leo on 2015/3/5.
 */
public class ApplyDetailActivity extends FragmentActivity {

	private static final int TYPE_TEXT = 1;
	private static final int TYPE_IMAGE = 2;
	private static final int TYPE_BANK = 3;

	private static final int APPLY_PUBLIC = 1;
	private static final int APPLY_PRIVATE = 2;
	private int mApplyType;

	private static final int ITEM_EDIT = 1;
	private static final int ITEM_CHOOSE = 2;
	private static final int ITEM_UPLOAD = 3;
	private static final int ITEM_VIEW = 4;

	private int mTerminalId;
	private int mTerminalStatus;

    private Merchant mMerchant;
    private MerchantEntity mMerchantEntity;

    private String mAgentName;
    private int mAgentId;
    private String mCityName;
    private int mCityId;

    private boolean tabInitialized = false;


    private LayoutInflater mInflater;

	private TextView mPosBrand;
	private TextView mPosModel;
	private TextView mSerialNum;
	private TextView mPayChannel;
	private TextView tv_support_type_name;

	private String[] mMerchantKeys;
	private String[] mBankKeys;

	private LinearLayout mContainer;
	private LinearLayout mMerchantContainer;
	private LinearLayout mCustomerContainer;
	private LinearLayout mMaterialContainer;
	private Button mApplySubmit;

	private MyTabWidget mTab;

	private int mMerchantId;
	private String mMerchantGender;
	private String mMerchantBirthday;
	private Province mMerchantProvince;
	private City mMerchantCity;
	private int mChannelId;
	private int mBillingId;
    private String mBankName;
    private String mBankNo;

    private Object customTag;

	private String photoPath;
	private TextView uploadingTextView;

	private ArrayList<ApplyChooseItem> mChannelItems = new ArrayList<ApplyChooseItem>();

	private List<String> mImageUrls = new ArrayList<String>();
	private List<String> mImageNames = new ArrayList<String>();
	private Map<Integer, ApplyCustomerDetail> mapCustomerDetails = new HashMap<Integer, ApplyCustomerDetail>();
	private Map<Integer, ApplyMaterial> mapMaterials = new HashMap<Integer, ApplyMaterial>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_detail);
		new TitleMenuUtil(this, getString(R.string.title_apply_open)).show();
		mTerminalId = getIntent().getIntExtra(TERMINAL_ID, 0);
		mTerminalStatus = getIntent().getIntExtra(TERMINAL_STATUS, 0);

        try {
            EventBus.getDefault().register(this);
        } catch (RuntimeException ex) {
            Log.d("UNCatchException", ex.getMessage());
        }

		initViews();
		loadData(mApplyType);
	}

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
		mInflater = LayoutInflater.from(this);

		mTab = (MyTabWidget) findViewById(R.id.apply_detail_tab);


        tv_support_type_name = (TextView) findViewById(R.id.tv_support_type_name);
		mPosBrand = (TextView) findViewById(R.id.apply_detail_brand);
		mPosModel = (TextView) findViewById(R.id.apply_detail_model);
		mSerialNum = (TextView) findViewById(R.id.apply_detail_serial);
		mPayChannel = (TextView) findViewById(R.id.apply_detail_channel);

		mContainer = (LinearLayout) findViewById(R.id.apply_detail_container);
		mMerchantContainer = (LinearLayout) findViewById(R.id.apply_detail_merchant_container);
		mCustomerContainer = (LinearLayout) findViewById(R.id.apply_detail_customer_container);
		mMaterialContainer = (LinearLayout) findViewById(R.id.apply_detail_material_container);
		mApplySubmit = (Button) findViewById(R.id.apply_submit);

		mApplySubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSubmit();
			}
		});
	}

	private void loadData(int applyType) {
		mMerchantContainer.removeAllViews();
		mCustomerContainer.removeAllViews();
		mMaterialContainer.removeAllViews();
		initMerchantDetailKeys();

        JsonParams params = new JsonParams();
        params.put("terminalsId", mTerminalId);
        params.put("status", mApplyType);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.ApplyDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);

	}

    // events
    public void onEventMainThread(Events.ApplyDetailCompleteEvent event) {

        if (!event.success()) {
            CommonUtil.toastShort(ApplyDetailActivity.this, event.getMessage());
            return;
        }

        ApplyDetail data = event.getEntity();
        ApplyTerminalDetail terminalDetail = data.getTerminalDetail();
        TerminalOpenInfo openInfo = data.getOpeningInfos();
        final List<ApplyChooseItem> merchants = data.getMerchants();
        List<ApplyMaterial> materials = data.getMaterials();
        List<ApplyCustomerDetail> customerDetails = data.getCustomerDetails();

        if (null != terminalDetail) {
            mPosBrand.setText(terminalDetail.getBrandName());
            mPosModel.setText(terminalDetail.getModelNumber());
            mSerialNum.setText(terminalDetail.getSerialNumber());
            mPayChannel.setText(terminalDetail.getChannelName());

            initTab(terminalDetail);
        }

        // set the choosing merchant listener
        View merchantChoose = mMerchantContainer.findViewWithTag(mMerchantKeys[0]);
        merchantChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyDetailActivity.this, com.posagent.activities.terminal.MerchantList.class);
                intent.putExtra(AGENT_NAME, mAgentName);
                intent.putExtra("terminalId", mTerminalId);

                startActivityForResult(intent, REQUEST_CHOOSE_MERCHANT);
            }
        });


        // prepare custom details
        mapMaterials.clear();
        if (null != materials) {
            //update map
            for (ApplyMaterial item: materials) {
                mapMaterials.put(item.getId(), item);
            }

            prepareMaterials(materials);
        }

        // set the customer details
        mapCustomerDetails.clear();
        if (null != customerDetails) {
            //update map
            for (ApplyCustomerDetail item: customerDetails) {
                mapCustomerDetails.put(item.getTarget_id(), item);
            }
            setCustomerDetail(customerDetails);
        }




        if (null != openInfo) {
            updateOpenInfo(openInfo);
        }


        // setup material choose item
        for (final ApplyMaterial item: materials) {
            if (TYPE_BANK == item.getInfo_type()) {
                LinearLayout ll = (LinearLayout) mContainer.findViewWithTag(item.getId());
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customTag = item.getId();
                        Intent intent = new Intent(ApplyDetailActivity.this, BankList.class);
                        intent.putExtra(AGENT_NAME, mBankName);
                        intent.putExtra("terminalId", mTerminalId);
                        startActivityForResult(intent, REQUEST_CHOOSE_BANK);
                    }
                });
            }
        }

        showHideSomeFields();

    }


    public void onEventMainThread(Events.MerchantDetailCompleteEvent event) {
        MerchantEntity data = event.getEntity();
        mMerchantEntity = data;
        setMerchantDetailValues(data);
        mApplySubmit.setEnabled(true);
    }

    public void onEventMainThread(Events.AddOpeningApplyCompleteEvent event) {
        CommonUtil.toastShort(this, event.getMessage());
    }

	@Override
	protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
            case REQUEST_CHOOSE_MERCHANT: {

                mAgentId = mMerchantId = data.getIntExtra(AGENT_ID, 0);
                mAgentName = data.getStringExtra(AGENT_NAME);
                setItemValue(mMerchantKeys[0], mAgentName);

                getAgentInfo();

                break;
            }
            case REQUEST_CHOOSE_BANK: {

                mBankName = data.getStringExtra("bank_name");
                mBankNo = data.getStringExtra("bank_no");
                setItemValue(customTag, mBankName);
                setItemValue(mBankKeys[0], mBankName);
                setItemValue(mBankKeys[1], mBankNo);

                break;
            }
			case REQUEST_CHOOSE_CITY: {
                mMerchantProvince = (Province) data.getSerializableExtra(SELECTED_PROVINCE);
                mMerchantCity = (City) data.getSerializableExtra(SELECTED_CITY);
                mCityId = mMerchantCity.getId();
                setItemValue(mMerchantKeys[8], mMerchantCity.getName());
                break;
			}
			case REQUEST_CHOOSE_CHANNEL: {
				mChannelId = data.getIntExtra("channelId", 0);
				mBillingId = data.getIntExtra("billId", 0);
                String channelName = data.getStringExtra("channelName");
                String billName = data.getStringExtra("billName");

				setItemValue(getString(R.string.apply_detail_channel), channelName + " " + billName);
				break;
			}
			case REQUEST_UPLOAD_IMAGE:
			case REQUEST_TAKE_PHOTO: {

				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
//							CommonUtil.toastShort(ApplyDetailActivity.this, (String) msg.obj);
							if (null != uploadingTextView) {
                                final String url = (String) msg.obj;
                                LinearLayout item = (LinearLayout)uploadingTextView.getParent().getParent();

                                updateCustomerDetails(item.getTag(), url);
                                uploadingTextView.setVisibility(View.GONE);


                                ImageView iv_view = (ImageView)item.findViewById(R.id.apply_detail_view);
                                iv_view.setVisibility(View.VISIBLE);
                                iv_view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(ApplyDetailActivity.this, ImageViewer.class);
                                        i.putExtra("url", url);
                                        i.putExtra("justviewer", true);
                                        startActivity(i);
                                    }
                                });
							}
						} else {
							CommonUtil.toastShort(ApplyDetailActivity.this, getString(R.string.toast_upload_failed));
							if (null != uploadingTextView) {
								uploadingTextView.setText(getString(R.string.apply_upload_again));
								uploadingTextView.setClickable(true);
							}
						}

					}
				};
				if (null != uploadingTextView) {
					uploadingTextView.setText(getString(R.string.apply_uploading));
					uploadingTextView.setClickable(false);
				}
				new Thread() {
					@Override
					public void run() {
						String realPath = "";
						if (requestCode == REQUEST_TAKE_PHOTO) {
							realPath = photoPath;
						} else {
							Uri uri = data.getData();
							if (uri != null) {
								realPath = getRealPathFromURI(uri);
							}
						}
						if (TextUtils.isEmpty(realPath)) {
							handler.sendEmptyMessage(0);
							return;
						}
						CommonUtil.uploadFile(realPath, "img", new CommonUtil.OnUploadListener() {
							@Override
							public void onSuccess(String result) {
								try {
									JSONObject jo = new JSONObject(result);
									String url = jo.getString("result");
									Message msg = new Message();
									msg.what = 1;
									msg.obj = url;
									handler.sendMessage(msg);
								} catch (JSONException e) {
									handler.sendEmptyMessage(0);
								}
							}

							@Override
							public void onFailed(String message) {
								handler.sendEmptyMessage(0);
							}
						});
					}
				}.start();
				break;
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}


    private void hideItem(String key) {
        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
        item.setVisibility(View.GONE);
    }

    private void showItem(String key) {
        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
        item.setVisibility(View.VISIBLE);
    }

	/**
	 * set the item value by key
	 *
	 * @param key
	 * @param value
	 */
    private void setItemValue(String key, String value) {
        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
        TextView tvValue = (TextView) item.findViewById(R.id.apply_detail_value);
        tvValue.setText(value);
    }

    private void setItemValue(Object key, String value) {
        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
        TextView tvValue = (TextView) item.findViewById(R.id.apply_detail_value);
        tvValue.setText(value);
    }

    /**
	 * get the item value by key
	 *
	 * @param key
	 * @return
	 */
    private String getItemValue(Object key) {
        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(key);
        TextView tvValue = (TextView) item.findViewById(R.id.apply_detail_value);
        return tvValue.getText().toString();
    }

	/**
	 * firstly init the merchant category with item keys,
	 * and after user select the merchant the values will be set
	 */
	private void initMerchantDetailKeys() {
		// the first category
		mMerchantKeys = getResources().getStringArray(R.array.apply_detail_merchant_keys);

		mMerchantContainer.addView(getDetailItem(ITEM_CHOOSE, mMerchantKeys[0], null));
		mMerchantContainer.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[1], null));
		mMerchantContainer.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[2], null));

		View merchantGender = getDetailItem(ITEM_CHOOSE, mMerchantKeys[3], null);
		merchantGender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ApplyDetailActivity.this);
				final String[] items = getResources().getStringArray(R.array.apply_detail_gender);
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setItemValue(mMerchantKeys[3], items[which]);
					}
				});
				builder.show();
			}
		});
		mMerchantContainer.addView(merchantGender);

		View merchantBirthday = getDetailItem(ITEM_CHOOSE, mMerchantKeys[4], null);
		merchantBirthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonUtil.showDatePicker(ApplyDetailActivity.this, mMerchantBirthday, new CommonUtil.OnDateSetListener() {
					@Override
					public void onDateSet(String date) {
						setItemValue(mMerchantKeys[4], date);
					}
				});
			}
		});
		mMerchantContainer.addView(merchantBirthday);
		mMerchantContainer.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[5], null));
		mMerchantContainer.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[6], null));
		mMerchantContainer.addView(getDetailItem(ITEM_EDIT, mMerchantKeys[7], null));

		View merchantCity = getDetailItem(ITEM_CHOOSE, mMerchantKeys[8], null);
		merchantCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

                Intent intent = new Intent(ApplyDetailActivity.this, CityProvinceActivity.class);
                intent.putExtra(SELECTED_PROVINCE, mMerchantProvince);
                intent.putExtra(SELECTED_CITY, mMerchantCity);
                startActivityForResult(intent, REQUEST_CHOOSE_CITY);
			}
		});
		mMerchantContainer.addView(merchantCity);

		// the second category
		mBankKeys = getResources().getStringArray(R.array.apply_detail_bank_keys);

		mCustomerContainer.addView(getDetailItem(ITEM_EDIT, mBankKeys[0], null));
		mCustomerContainer.addView(getDetailItem(ITEM_EDIT, mBankKeys[1], null));
		mCustomerContainer.addView(getDetailItem(ITEM_EDIT, mBankKeys[2], null));
		mCustomerContainer.addView(getDetailItem(ITEM_EDIT, mBankKeys[3], null));
		mCustomerContainer.addView(getDetailItem(ITEM_EDIT, mBankKeys[4], null));

		View chooseChannel = getDetailItem(ITEM_CHOOSE, getString(R.string.apply_detail_channel), null);
		chooseChannel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

                Intent intent = new Intent(ApplyDetailActivity.this, ChannelSelecter.class);
                startActivityForResult(intent, REQUEST_CHOOSE_CHANNEL);

			}
		});
		mCustomerContainer.addView(chooseChannel);
	}

	/**
	 * set the item values after user select a merchant
	 *
	 * @param merchant
	 */
	private void setMerchantDetailValues(MerchantEntity merchant) {
		setItemValue(mMerchantKeys[1], merchant.getLegal_person_name());
		setItemValue(mMerchantKeys[2], merchant.getTitle());
		setItemValue(mMerchantKeys[5], merchant.getLegal_person_card_id());
		setItemValue(mMerchantKeys[6], merchant.getPhone());

        mCityId = merchant.getCity_id();
        mCityName = ((MyApplication)getApplication()).cityNameForId(mCityId);

		setItemValue(mMerchantKeys[8], mCityName);

        setItemValue(mBankKeys[0], merchant.getAccount_bank_name());
        setItemValue(mBankKeys[1], merchant.getAccount_bank_num());
        setItemValue(mBankKeys[2], merchant.getBank_open_account());
        setItemValue(mBankKeys[3], merchant.getTax_registered_no());
        setItemValue(mBankKeys[4], merchant.getBusiness_license_no());

    }

	/**
	 * start the {@link ApplyChooseActivity} to choose item
	 *
	 * @param requestCode handle the return item according to request code
	 * @param title       the started activity title
	 * @param selectedId  the id of the selected item
	 * @param items       the items to choose
	 */
	private void startChooseItemActivity(int requestCode, String title, int selectedId, ArrayList<ApplyChooseItem> items) {
        Intent intent = new Intent(ApplyDetailActivity.this, ApplyChooseActivity.class);
        intent.putExtra(CHOOSE_TITLE, title);
        intent.putExtra(SELECTED_ID, selectedId);
        intent.putExtra(CHOOSE_ITEMS, items);
        startActivityForResult(intent, requestCode);

	}

	/**
	 * set the customer's details after the first request returned
	 *
	 * @param customerDetails
	 */
    private void setCustomerDetail(List<ApplyCustomerDetail> customerDetails) {
        for (ApplyCustomerDetail customerDetail : customerDetails) {
            ApplyMaterial material = mapMaterials.get(customerDetail.getTarget_id());
            if(null == material) {
                continue;
            }
            Integer tag = material.getId();
            switch (customerDetail.getTypes()) {
                case TYPE_TEXT:
                    setItemValue(material.getId(), customerDetail.getValue());
                    break;
                case TYPE_IMAGE:
                    String imageName = customerDetail.getKey();
                    String imageUrl = customerDetail.getValue();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        mImageNames.add(customerDetail.getKey());
                        mImageUrls.add(customerDetail.getValue());

                        LinearLayout item = (LinearLayout) mContainer.findViewWithTag(tag);
                        mMaterialContainer.removeView(item);

                        mMaterialContainer.addView(getDetailItem(ITEM_VIEW, imageName, imageUrl, tag));
                    }
                    break;
                case TYPE_BANK:
                    setItemValue(material.getId(), customerDetail.getValue());
                    break;
            }
        }

    }

    private void prepareMaterials(List<ApplyMaterial> materials) {
        for (ApplyMaterial material : materials) {
            switch (material.getInfo_type()) {
                case TYPE_TEXT:
                    mMaterialContainer.addView(getDetailItem(ITEM_EDIT, material.getName(), null, material.getId()));
                    break;
                case TYPE_IMAGE:
                    String imageName = material.getName();
                    String imageUrl = "";
                    mMaterialContainer.addView(getDetailItem(ITEM_UPLOAD, imageName, imageUrl, material.getId()));
                    break;
                case TYPE_BANK:
                    mMaterialContainer.addView(getDetailItem(ITEM_CHOOSE, material.getName(), null, material.getId()));
                    break;
            }
        }

    }

	private void setMerchantItem(int itemType, String key, String value) {
		LinearLayout item = (LinearLayout) mMerchantContainer.findViewWithTag(key);
		setupItem(item, itemType, key, value);
	}

    private LinearLayout getDetailItem(int itemType, String key, String value) {
        return getDetailItem(itemType, key, value, key);
    }

    private LinearLayout getDetailItem(int itemType, String key, String value, Object tag) {
        LinearLayout item;
        switch (itemType) {
            case ITEM_EDIT:
                item = (LinearLayout) mInflater.inflate(R.layout.apply_detail_item_edit, null);
                break;
            case ITEM_CHOOSE:
                item = (LinearLayout) mInflater.inflate(R.layout.apply_detail_item_choose, null);
                break;
            case ITEM_UPLOAD:
                item = (LinearLayout) mInflater.inflate(R.layout.apply_detail_item_upload, null);
                break;
            case ITEM_VIEW:
                item = (LinearLayout) mInflater.inflate(R.layout.apply_detail_item_view, null);
                break;
            default:
                item = (LinearLayout) mInflater.inflate(R.layout.apply_detail_item_edit, null);
        }
        item.setTag(tag);
        setupItem(item, itemType, key, value);
        return item;
    }

	private void setupItem(LinearLayout item, int itemType, final String key, final String value) {
		switch (itemType) {
			case ITEM_EDIT: {
				TextView tvKey = (TextView) item.findViewById(R.id.apply_detail_key);
				EditText etValue = (EditText) item.findViewById(R.id.apply_detail_value);

				if (!TextUtils.isEmpty(key))
					tvKey.setText(key);
				if (!TextUtils.isEmpty(value))
					etValue.setText(value);
				break;
			}
			case ITEM_CHOOSE: {
				TextView tvKey = (TextView) item.findViewById(R.id.apply_detail_key);
				TextView tvValue = (TextView) item.findViewById(R.id.apply_detail_value);

				if (!TextUtils.isEmpty(key))
					tvKey.setText(key);
				if (!TextUtils.isEmpty(value))
					tvValue.setText(value);
				break;
			}
			case ITEM_UPLOAD: {
				TextView tvKey = (TextView) item.findViewById(R.id.apply_detail_key);
				final TextView tvValue = (TextView) item.findViewById(R.id.apply_detail_value);

				if (!TextUtils.isEmpty(key))
					tvKey.setText(key);
				tvValue.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						uploadingTextView = tvValue;
						AlertDialog.Builder builder = new AlertDialog.Builder(ApplyDetailActivity.this);
						final String[] items = getResources().getStringArray(R.array.apply_detail_upload);
						builder.setItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
									case 0: {
										Intent intent = new Intent();
										intent.setType("image/*");
										intent.setAction(Intent.ACTION_GET_CONTENT);
										startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
										break;
									}
									case 1: {
										String state = Environment.getExternalStorageState();
										if (state.equals(Environment.MEDIA_MOUNTED)) {
											Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
											File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
											if (!outDir.exists()) {
												outDir.mkdirs();
											}
											File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
											photoPath = outFile.getAbsolutePath();
											intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
											intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
											startActivityForResult(intent, REQUEST_TAKE_PHOTO);
										} else {
											CommonUtil.toastShort(ApplyDetailActivity.this, getString(R.string.toast_no_sdcard));
										}
										break;
									}
								}
							}
						});
						builder.show();


					}
				});
				break;
			}
			case ITEM_VIEW: {
				TextView tvKey = (TextView) item.findViewById(R.id.apply_detail_key);
				ImageButton ibView = (ImageButton) item.findViewById(R.id.apply_detail_view);

				if (!TextUtils.isEmpty(key))
					tvKey.setText(key);
				ibView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
                        Intent i = new Intent(ApplyDetailActivity.this, ImageViewer.class);
                        i.putExtra("url", value);
                        i.putExtra("justviewer", true);
                        startActivity(i);
					}
				});
			}
		}
	}

    private void getAgentInfo() {
        JsonParams params = new JsonParams();
        params.put("merchantId", mMerchantId);
        String strParams = params.toString();
        Events.CommonRequestEvent event = new Events.MerchantDetailEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }

    private void doSubmit() {
        JsonParams allParams = new JsonParams();
        List<Object> list = new ArrayList<Object>();

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("terminalId", mTerminalId);
        params.put("applyCustomerId", MyApplication.user().getId());
        params.put("publicPrivateStatus", mApplyType);

        params.put("merchantId", mMerchantId);
        params.put("merchantName", getItemValue(mMerchantKeys[2]));
        params.put("sex", StringUtil.intSex(getItemValue(mMerchantKeys[3])));
        params.put("birthday", getItemValue(mMerchantKeys[4]));
        params.put("cardId", getItemValue(mMerchantKeys[5]));
        params.put("phone", getItemValue(mMerchantKeys[6]));
        params.put("name", getItemValue(mMerchantKeys[1]));
        params.put("email", getItemValue(mMerchantKeys[7]));
        params.put("cityId", mCityId);
        params.put("channel", mChannelId);
        params.put("billingId", mBillingId);

        params.put("bankNum", getItemValue(mBankKeys[2]));
        params.put("bankName", getItemValue(mBankKeys[0]));
        params.put("bankCode", getItemValue(mBankKeys[1]));
        params.put("registeredNo", getItemValue(mBankKeys[3]));
        params.put("organizationNo", getItemValue(mBankKeys[4]));

        list.add(params);

        ApplyCustomerDetail detail;
        for (ApplyMaterial material: mapMaterials.values()) {
            String value = "";

            if (TYPE_IMAGE == material.getInfo_type()) {
                detail = mapCustomerDetails.get(material.getId());
                if (null != detail) {
                    value = detail.getValue();
                }
            } else {
                value = getItemValue(material.getId());
            }


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", material.getName());
            map.put("value", value);
            map.put("types", material.getInfo_type());
            map.put("openingRequirementId", material.getOpening_requirements_id());
            map.put("targetId", material.getId());

            list.add(map);
        }



        allParams.put("paramMap", list);



        String strParams = allParams.toString();
        Events.CommonRequestEvent event = new Events.AddOpeningApplyEvent();
        event.setParams(strParams);
        EventBus.getDefault().post(event);
    }


    private void updateOpenInfo(TerminalOpenInfo openInfo) {

        mMerchantId = openInfo.getMerchant_id();
        mCityId = openInfo.getCity_id();
        mChannelId = openInfo.getPay_channel_id();
        mBillingId = openInfo.getBilling_cyde_id();

        setItemValue(mMerchantKeys[1], openInfo.getName());
        setItemValue(mMerchantKeys[2], openInfo.getMerchant_name());
        setItemValue(mMerchantKeys[3], com.examlpe.zf_android.util.StringUtil.strSex(openInfo.getSex()));
        setItemValue(mMerchantKeys[4], openInfo.getBirthday());
        setItemValue(mMerchantKeys[5], openInfo.getCard_id());
        setItemValue(mMerchantKeys[6], openInfo.getPhone());
        setItemValue(mMerchantKeys[7], openInfo.getEmail());
        setItemValue(mMerchantKeys[8], ((MyApplication)getApplication()).cityNameForId(openInfo.getCity_id()));

        setItemValue(mBankKeys[0], openInfo.getAccount_bank_name());
        setItemValue(mBankKeys[1], openInfo.getAccount_bank_code());
        setItemValue(mBankKeys[2], openInfo.getAccount_bank_num());
        setItemValue(mBankKeys[3], openInfo.getTax_registered_no());
        setItemValue(mBankKeys[4], openInfo.getOrganization_code_no());
        setItemValue(mBankKeys[5], openInfo.getBillingname());


    }


    private void updateCustomerDetails(Object key, String value) {
        Integer realKey = (Integer) key;
        ApplyCustomerDetail item = mapCustomerDetails.get(realKey);
        ApplyMaterial material = mapMaterials.get(realKey);
        if (null == item) {
            item = new ApplyCustomerDetail();
            mapCustomerDetails.put(realKey, item);
        }
        item.setKey(material.getName());
        item.setTarget_id(material.getId());
        item.setValue(value);

    }

    private void initTab(ApplyTerminalDetail terminalDetail) {
        if (tabInitialized) {
            return;
        }
        tabInitialized = true;
        switch (terminalDetail.getSupportRequirementType()) {
            case 1:
                tv_support_type_name.setVisibility(View.VISIBLE);
                mApplyType = APPLY_PUBLIC;
                break;
            case 2:
                tv_support_type_name.setVisibility(View.VISIBLE);
                tv_support_type_name.setText("对私");
                mApplyType = APPLY_PRIVATE;
                break;
            default:
                mTab.setVisibility(View.VISIBLE);
                mTab.addTab("对公", 17);
                mTab.addTab("对私", 17);
                mTab.setOnTabSelectedListener(new MyTabWidget.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        mApplyType = position + 1;
                        loadData(mApplyType);
                    }
                });

                mApplyType = APPLY_PUBLIC;
                mTab.updateTabs(0);

        }

    }

    private void showHideSomeFields() {
        if (mApplyType == APPLY_PRIVATE) {
            hideItem(mBankKeys[3]);
            hideItem(mBankKeys[4]);
        } else if (mApplyType == APPLY_PUBLIC) {
            showItem(mBankKeys[3]);
            showItem(mBankKeys[4]);
        }
    }

}
