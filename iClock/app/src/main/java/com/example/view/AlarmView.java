package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.iclock.R;

import java.util.Date;

/**
 * Created by Administrator on 2016/6/25.
 */
public class AlarmView  extends LinearLayout{
    private ListView alarmListView;
    private Button alarmButton;
    private ArrayAdapter<AlarmDate> adapter;
    public AlarmView(Context context) {
        super(context);
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void addAlarm() {
        //TODO
        adapter.add(new AlarmDate(System.currentTimeMillis()));
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        alarmListView = (ListView)findViewById(R.id.AlarmListView);
        alarmButton = (Button)findViewById(R.id.AddAlarmBtn);
        alarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        alarmListView.setAdapter(adapter);
        adapter.add(new AlarmDate(System.currentTimeMillis()));
    }

    private static class  AlarmDate
    {
        private Date date;
        private long time = 0;
        private String timeLable = "";
        public AlarmDate(long time){
            this.time = time;
            date = new Date(time);
            timeLable = date.getHours() + ":" + date.getMinutes();
        }

        public long getTime(){
            return this.time;
        }

        public String getTimeLable(){
            return this.timeLable;
        }

        @Override
        public String toString() {
            return getTimeLable();
        }
    }
}
