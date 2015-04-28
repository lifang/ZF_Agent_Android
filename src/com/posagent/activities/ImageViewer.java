package com.posagent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.examlpe.zf_android.util.ImageCacheUtil;
import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.common.CommonUtil;
import com.posagent.utils.PhotoManager;

import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_TAKE_PHOTO;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_UPLOAD_IMAGE;

/**
 * ImageViewer
 */
public class ImageViewer extends BaseActivity {

    private ImageView iv_viewer;

    private String url;
    private String kind;

    private boolean justviewer = false;

    private PhotoManager photoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        new TitleMenuUtil(ImageViewer.this, "图片查看").show();

        photoManager = new PhotoManager(this);

        kind = getIntent().getStringExtra("kind");
        justviewer = getIntent().getBooleanExtra("justviewer", false);


        url = getIntent().getStringExtra("url");
//        url = "http://d.hiphotos.baidu.com/image/w%3D2048/sign=48fd3c26f01fbe091c5ec4145f580d33/64380cd7912397dd92729b545b82b2b7d0a28752.jpg";
        initView();
    }

    private void initView() {
        iv_viewer = (ImageView)findViewById(R.id.iv_viewer);
        ImageCacheUtil.IMAGE_CACHE.get(url, iv_viewer);


        findViewById(R.id.tv_reupload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoManager.prompt();
            }
        });

        if (justviewer) {
            hide("tv_reupload");
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_UPLOAD_IMAGE:
            case REQUEST_TAKE_PHOTO: {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            String url = (String) msg.obj;
                            updatePhotoUrl(url);
                        } else {
                            CommonUtil.toastShort(ImageViewer.this, getString(R.string.toast_upload_failed));
                        }

                    }
                };
                photoManager.onActivityResult(requestCode, resultCode, data, handler);
                break;
            }
        }
    }

    private void updatePhotoUrl(String _url) {
        ImageCacheUtil.IMAGE_CACHE.get(_url, iv_viewer);
        Intent i = getIntent();
        i.putExtra("url", _url);
        i.putExtra("kind", kind);
        setResult(RESULT_OK, i);
        finish();
    }
}
