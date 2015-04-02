package com.posagent.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.example.zf_android.Config;
import com.example.zf_android.R;
import com.example.zf_android.entity.PicEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holin on 4/1/15.
 */
public class HMSlideFragment extends Fragment {

    private LinearLayout indicatorContainer;
    private Context context;
    private String jsonData;

    private ArrayList<String> mal = new ArrayList<String>();
    private ArrayList<PicEntity> myList = new ArrayList<PicEntity>();
    private ViewPager view_pager;
    private MyAdapter adapter ;
    private ImageView[] indicator_imgs  ;//存放引到图片数组
    private View item ;
    private LayoutInflater inflater;
    private ImageView image;
    private ArrayList<String> ma = new ArrayList<String>();
    List<View> list = new ArrayList<View>();
    private int index_ima;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    for (int i = 0; i <myList.size(); i++) {
                        item = inflater.inflate(R.layout.item, null);
                        list.add(item);
                        ma.add(myList.get(i).getPicture_url());
                    }
                    indicator_imgs	= new ImageView[ma.size()];
                    initIndicator();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = _inflater.getContext();
        inflater = LayoutInflater.from(context);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout mainContainer = new RelativeLayout(context);
        mainContainer.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        indicatorContainer = new LinearLayout(context);
        indicatorContainer.setGravity(Gravity.CENTER);
        indicatorContainer.setBackgroundColor(0x33000000);
        indicatorContainer.setLayoutParams(lp);

        // viewpager
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view_pager = new ViewPager(context);
        view_pager.setLayoutParams(lp);


        adapter = new MyAdapter(list);
        view_pager.setAdapter(adapter);
        //绑定动作监听器：如翻页的动画
        view_pager.setOnPageChangeListener(new MyListener());


        mainContainer.addView(view_pager);


        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 40);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, view_pager.getId());
        mainContainer.addView(indicatorContainer, layoutParams);


        getdata();
        return mainContainer;
    }

    private void initIndicator(){
        ImageView imgView;
        View v = indicatorContainer;
        for (int i = 0; i < this.myList.size(); i++) {
            imgView = new ImageView(context);

            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(10, 10);
            params_linear.setMargins(7, 20, 7, 20);
            imgView.setLayoutParams(params_linear);

            if (i == index_ima) {
                setCurrentDot(imgView);
            } else {
                setDefaultDot(imgView);
            }

            indicator_imgs[i] = imgView;


            ((ViewGroup)v).addView(imgView);
        }
    }

    private LayerDrawable dotWithColor(int color) {
        ShapeDrawable smallerCircle= new ShapeDrawable( new OvalShape());
        smallerCircle.setIntrinsicHeight( 10 );
        smallerCircle.setIntrinsicWidth( 10);
        smallerCircle.getPaint().setColor(color);
        Drawable[] d = {smallerCircle};

        return new LayerDrawable(d);
    }

    /**
     * 适配器，负责装配 、销毁  数据  和  组件 。
     */
    private class MyAdapter extends PagerAdapter {

        private List<View> mList;
        private int index ;



        public MyAdapter(List<View> list) {
            mList = list;

        }

        public int getIndex() {
            return index;
        }



        public void setIndex(int index) {
            this.index = index;
        }



        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return mList.size();
        }


        /**
         * Remove a page for the given position.
         * 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
         * instantiateItem(View container, int position)
         * This method was deprecated in API level . Use instantiateItem(ViewGroup, int)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }


        /**
         * Create the page for the given position.
         */
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {


            View view = mList.get(position);
            image = ((ImageView) view.findViewById(R.id.image));

            ImageCacheUtil.IMAGE_CACHE.get(  ma.get(position),
                    image);


            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            setIndex(position);
            return mList.get(position);
        }


    }


    /**
     * 动作监听器，可异步加载图片
     *
     */
    private class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                //new MyAdapter(null).notifyDataSetChanged();
            }
        }


        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            index_ima=position;
            ImageView _imageView;
            for (int i = 0; i < indicator_imgs.length; i++) {
                _imageView = indicator_imgs[i];
                if (position == i) {
                    setCurrentDot(_imageView);
                } else {
                    setDefaultDot(_imageView);
                }
            }
        }


    }

    private void setDefaultDot(ImageView _imageView) {
        _imageView.setBackgroundDrawable(dotWithColor(Color.argb(55, 0xff, 0xff, 0xff)));
    }

    private void setCurrentDot(ImageView _imageView) {
        _imageView.setBackgroundDrawable(dotWithColor(Color.argb(0xff, 0xff, 0xff, 0xff)));
    }

    private void getdata() {
        jsonData = "{'code':1,'message':'success','result':[{'id':5,'picture_url':'http://file.youboy.com/a/142/67/57/6/660666.jpg','website_url':'http://baidu.com'},{'id':4,'picture_url':'http://img1.100ye.com/img1/4/1181/892/10772392/msgpic/61260332.jpg','website_url':'http://baidu.com'},{'id':3,'picture_url':'http://image5.huangye88.com/2013/01/08/db4ed2c6a01ec5ef.jpg','website_url':'http://baidu.com'},{'id':2,'picture_url':'http://file.youboy.com/a/149/94/17/5/669625.jpg','website_url':'http://baidu.com'}]}";

        Gson gson = new Gson();

        JSONObject jsonobject = null;
        String code = null;
        try {
            jsonobject = new JSONObject(jsonData);
            code = jsonobject.getString("code");
            int a =jsonobject.getInt("code");
            if(a== Config.CODE){
                String res =jsonobject.getString("result");
                myList= gson.fromJson(res, new TypeToken<List<PicEntity>>() {
                }.getType());

                handler.sendEmptyMessage(0);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ;
            e.printStackTrace();

        }

    }
}
