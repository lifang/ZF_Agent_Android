package com.example.zf_android.trade;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.entity.City;
import com.example.zf_android.trade.entity.Province;
import com.example.zf_android.trade.widget.LetterListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.zf_android.trade.Constants.CityIntent.CITY_ID;
import static com.example.zf_android.trade.Constants.CityIntent.CITY_NAME;

public class CitySelectActivity extends Activity {

    private String mCitySelected;
    private TextView mCityCurrent;

    private TextView overlay;
    private ListView mListView;
    private LetterListView mLetterListView;
    private CityListAdapter mAdapter;

    private Handler handler;
    private Thread overlayThread;

    private List<City> mCities = new ArrayList<City>();
    private List<String> mLetters = new ArrayList<String>();
    private List<Object> mItems = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);

        mCitySelected = getIntent().getStringExtra(CITY_NAME);

        new TitleMenuUtil(this, getString(R.string.title_city_select)).show();
        initViews();
    }

    private void initCities() {
        new Thread() {
            @Override
            public void run() {
                List<Province> provinces = CommonUtil.readProvincesAndCities(CitySelectActivity.this);
                for (Province province : provinces) {
                    List<City> cities = province.getCities();
                    mCities.addAll(cities);
                }

                Collections.sort(mCities, new Comparator<City>() {
                    @Override
                    public int compare(City lhs, City rhs) {
                        return lhs.getPinyin().compareTo(rhs.getPinyin());
                    }
                });

                char letter = '0';
                for (City city : mCities) {
                    if (!TextUtils.isEmpty(city.getPinyin())) {
                        char cur = city.getPinyin().charAt(0);
                        if (letter != cur) {
                            letter = cur;
                            String item = String.valueOf(letter).toUpperCase();
                            mLetters.add(item);
                            mItems.add(item);
                        }
                        mItems.add(city);
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }.start();

    }

    private void initViews() {
        initCities();
        initOverlay();

        mCityCurrent = (TextView) findViewById(R.id.city_current);
        if (!TextUtils.isEmpty(mCitySelected)) {
            mCityCurrent.setText(mCitySelected);
        }

        mListView = (ListView) findViewById(R.id.city_list_view);
        mLetterListView = (LetterListView) findViewById(R.id.letter_list_view);
        mAdapter = new CityListAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = ((TextView) view).getText().toString();
                if (mLetters.contains(cityName))
                    return;

                City city = (City) view.getTag(R.id.city_current);
                Intent intent = new Intent();
                intent.putExtra(CITY_ID, city.getId());
                intent.putExtra(CITY_NAME, city.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        overlayThread = new Thread() {
            @Override
            public void run() {
//                overlay.setVisibility(View.GONE);
            }
        };

        mLetterListView = (LetterListView) findViewById(R.id.letter_list_view);
        mLetterListView
                .setOnTouchingLetterChangedListener(new LetterListView.OnTouchingLetterChangedListener() {
                    @Override
                    public void onTouchingLetterChanged(String s) {
                        if (mLetters.contains(s)) {
                            int position = mItems.indexOf(s);
                            mListView.setSelection(position);
//                            overlay.setText(s);
//                            overlay.setVisibility(View.VISIBLE);
                            handler.removeCallbacks(overlayThread);
                            handler.postDelayed(overlayThread, 1500);
                        }
                    }
                });
    }

    private void initOverlay() {
        return;
//        LayoutInflater inflater = LayoutInflater.from(this);
//        overlay = (TextView) inflater.inflate(R.layout.letter_overlay, null);
//        overlay.setVisibility(View.INVISIBLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                PixelFormat.TRANSLUCENT);
//        WindowManager windowManager = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        windowManager.addView(overlay, lp);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class CityListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Resources resources = getResources();
            ViewHolder holder;
            if (convertView == null) {
                convertView = new TextView(CitySelectActivity.this);
                ((TextView) convertView).setHeight(dip2px(50));
                ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
                ((TextView) convertView).setTextColor(resources.getColor(R.color.text292929));
                ((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                convertView.setPadding(dip2px(24), 0, 0, 0);
                holder = new ViewHolder();
                holder.tv = (TextView) convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Object item = mItems.get(position);

            if (mLetters.contains(item.toString())) {
                holder.tv.setBackgroundColor(resources.getColor(R.color.F3F2F2));
            } else {
                holder.tv.setBackgroundColor(resources.getColor(R.color.white));
            }
            holder.tv.setText(item.toString().toUpperCase());
            holder.tv.setTag(R.id.city_current, item);
            return convertView;
        }

        private class ViewHolder {
            TextView tv;
        }
    }

}
