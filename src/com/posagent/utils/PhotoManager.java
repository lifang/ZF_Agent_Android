package com.posagent.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.example.zf_android.R;
import com.example.zf_android.trade.common.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_TAKE_PHOTO;
import static com.example.zf_android.trade.Constants.ApplyIntent.REQUEST_UPLOAD_IMAGE;

/**
 * Created by holin on 4/23/15.
 */
public class PhotoManager {
    private Activity context;
    private Fragment fragment;


    private String photoPath;

    public PhotoManager(Activity _context) {
        context = _context;
    }
    public PhotoManager(Fragment _fragment) {
        fragment = _fragment;
        context = fragment.getActivity();

    }

    public void prompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String[] items = context.getResources().getStringArray(R.array.apply_detail_upload);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (isFragment()) {
                            fragment.startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
                        } else {
                            context.startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
                        }

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

                            if (isFragment()) {
                                fragment.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                            } else {
                                context.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                            }

                        } else {
                            CommonUtil.toastShort(context, context.getString(R.string.toast_no_sdcard));
                        }
                        break;
                    }
                }
            }
        });
        builder.show();
    }


    public void onActivityResult(final int requestCode, int resultCode,
                                 final Intent data, final Handler handler) {
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
    }


    private String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    private boolean isFragment() {
        return null != fragment;
    }

}
