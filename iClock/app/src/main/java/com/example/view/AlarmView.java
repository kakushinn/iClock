package com.example.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.administrator.iclock.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/25.
 */
public class AlarmView  extends LinearLayout{
    private ListView alarmListView;
    private Button alarmButton;
    private ArrayAdapter<AlarmDate> adapter;
    private boolean flag = true;
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
        final Calendar calendar = Calendar.getInstance();

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(flag == true){
                    Calendar calendarSet = Calendar.getInstance();
                    calendarSet.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendarSet.set(Calendar.MINUTE,minute);
//                    Calendar currentTime = Calendar.getInstance();
                    if(calendarSet.getTimeInMillis() <= calendar.getTimeInMillis()){
                        calendarSet.setTimeInMillis(calendarSet.getTimeInMillis()+60*60*24*1000);
                    }
                    adapter.add(new AlarmDate(calendarSet.getTimeInMillis()));
                    flag = false;
                }else{
                    flag = true;
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        alarmListView = (ListView)findViewById(R.id.AlarmListView);
        alarmButton = (Button)findViewById(R.id.AddAlarmBtn);
        adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        alarmListView.setAdapter(adapter);
        alarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });

    }

    private static class  AlarmDate
    {
        private Calendar date;
        private long time = 0;
        private String timeLable = "";
        public AlarmDate(long time){
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLable = String.format("%d月%d日 %d:%d", date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
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
