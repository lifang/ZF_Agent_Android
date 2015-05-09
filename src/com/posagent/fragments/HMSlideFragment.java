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
import com.example.zf_android.R;
import com.example.zf_android.entity.PicEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by holin on 4/1/15.
 */
public class HMSlideFragment extends Fragment {

    private LinearLayout indicatorContainer;
    private Context context;
    private String jsonData;

    private List<String> mal = new ArrayList<String>();
    private List<PicEntity> myList = new ArrayList<PicEntity>();
    private ViewPager viewPager;
    private MyAdapter adapter ;
    private ImageView[] indicator_imgs  ;//存放引到图片数组
    private View item ;
    private LayoutInflater inflater;
    private ImageView image;
    private List<String> ma = new ArrayList<String>();
    private List<View> list = new ArrayList<View>();
    private int index_ima;


    Timer timer;
    int page = 1;



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

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout mainContainer = new RelativeLayout(context);
        mainContainer.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 100);
        indicatorContainer = new LinearLayout(context);
        indicatorContainer.setGravity(Gravity.CENTER);
        indicatorContainer.setBackgroundColor(0x33000000);
        indicatorContainer.setLayoutParams(lp);

        // viewpager
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(lp);


        adapter = new MyAdapter(list);
        viewPager.setAdapter(adapter);
        //绑定动作监听器：如翻页的动画
        viewPager.setOnPageChangeListener(new MyListener());


        mainContainer.addView(viewPager);


        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 40);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, viewPager.getId());
        mainContainer.addView(indicatorContainer, layoutParams);

        return mainContainer;
    }

    public void feedData(List<PicEntity> list) {
        myList= list;
        handler.sendEmptyMessage(0);

        pageSwitcher(2);
    }

    private void initIndicator(){
        ImageView imgView;
        View v = indicatorContainer;
        for (int i = 0; i < this.myList.size(); i++) {
            imgView = new ImageView(context);

            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(15, 15);
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
        smallerCircle.setIntrinsicHeight( 20 );
        smallerCircle.setIntrinsicWidth( 20);
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
            ImageCacheUtil.IMAGE_CACHE.get(ma.get(position), image);

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
                if (null == _imageView) {
                    continue;
                }
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



    //switcher
    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            if (null == getActivity()) {
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    page++;
                    if (page > myList.size()) { // In my case the number of pages are 5
                        page = 0;
                    }
                    viewPager.setCurrentItem(page);
                }
            });

        }
    }
}
