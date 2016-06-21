package com.example.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.iclock.R;

import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2016/6/21.
 */
public class TimeView extends LinearLayout {
    private TextView textView;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = (TextView)findViewById(R.id.tvTime);
        textView.setText("Hello");
        timeHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == View.VISIBLE)
            timeHandler.sendEmptyMessage(0);
        else
            timeHandler.removeMessages(0);
    }

    private void refreshTime()
    {
        Calendar calendar = Calendar.getInstance();
        textView.setText(String.format("%d:%d:%d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND)));
    }

    private android.os.Handler timeHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshTime();
            if(getVisibility() == View.VISIBLE)
            sendEmptyMessage(0);
        }
    };
}
