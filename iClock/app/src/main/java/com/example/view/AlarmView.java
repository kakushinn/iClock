package com.example.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.administrator.iclock.R;
import com.example.broadcast.AlarmReceiver;

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
    public static  final String ALARM_LIST_KEY = "alarmlist";
    private AlarmManager alarmManager;
    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarSet.getTimeInMillis(), 1000 * 60, PendingIntent.getBroadcast(getContext(),0,new Intent(getContext(),AlarmReceiver.class),0));
                    saveAlarmList();
                    flag = false;
                }else{
                    flag = true;
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    private void init(){
        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        alarmListView = (ListView)findViewById(R.id.AlarmListView);
        alarmButton = (Button)findViewById(R.id.AddAlarmBtn);
        adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        alarmListView.setAdapter(adapter);
        getAlarmList();
        alarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return true;
            }

            ;
        });
    }

    private void deleteAlarm(int position){
        adapter.remove(adapter.getItem(position));
        saveAlarmList();
    }

    private void saveAlarmList(){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(),Context.MODE_PRIVATE).edit();
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < adapter.getCount();i++){
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        String alarmListStr = sb.toString().substring(0,sb.length()-1);
        editor.putString(ALARM_LIST_KEY, alarmListStr);
        editor.commit();
    }


    private void getAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(ALARM_LIST_KEY,null);
        if(content != null){
            String[] timeStrings = content.split(",");
            for(String str : timeStrings){
                adapter.add(new AlarmDate(Long.parseLong(str)));
            }
        }
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
