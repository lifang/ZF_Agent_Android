package com.posagent.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.zf_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holin on 4/9/15.
 */
public class ViewHelper {

    public static List<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }

    public static TableRow tableRow(Context context, List<String> texts,
                                            int textColor, int textSize, boolean isLast) {
        TableRow tr = new TableRow(context);
        TableRow.LayoutParams tlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        tr.setLayoutParams(tlp);

        TextView tv;

        int len = texts.size();
        int padding = 5;

        boolean lastColumn = false;

        for (int i = 0; i < len; i++) {
            lastColumn = len - 1 == i;
            String text = texts.get(i);
            tv = new TextView(context);
            tv.setPadding(padding, padding, padding, padding);
            tv.setText(text);
            tv.setTextColor(context.getResources().getColor(textColor));
            TableRow.LayoutParams lp = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            if (isLast) {
                if (lastColumn) {
                    lp.setMargins(1, 1, 1, 1);
                } else {
                    lp.setMargins(1, 1, 0, 1);
                }
            } else {
                if (lastColumn) {
                    lp.setMargins(1, 1, 1, 0);
                } else {
                    lp.setMargins(1, 1, 0, 0);
                }
            }

            tv.setLayoutParams(lp);
            tv.setBackgroundColor(context.getResources().getColor(R.color.white));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(textSize);
            tr.addView(tv);
        }

        return tr;
    }
}
