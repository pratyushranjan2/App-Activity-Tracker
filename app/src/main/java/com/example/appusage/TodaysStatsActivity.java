package com.example.appusage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TodaysStatsActivity extends AppCompatActivity {

    private ArrayList<AppInfo> appInfos = new ArrayList<>();
    private String startTime;
    private String endTime;
    private RecyclerView recyclerView;
    private UsageListAdapter adapter;

    public ArrayList<AppInfo> getAppInfos() {
        return appInfos;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public UsageListAdapter getRecyclerViewAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_stats);

        loadData();


        recyclerView = findViewById(R.id.recycerView);
        adapter = new UsageListAdapter(this,appInfos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DATE);
        String currentDate="";

        switch (day) {
            case Calendar.SUNDAY:
                currentDate+="Sun ";
                break;
            case Calendar.MONDAY:
                currentDate+="Mon ";
                break;
            case Calendar.TUESDAY:
                currentDate+="Tue ";
                break;
            case Calendar.WEDNESDAY:
                currentDate+="Wed ";
                break;
            case Calendar.THURSDAY:
                currentDate+="Thu ";
                break;
            case Calendar.FRIDAY:
                currentDate+="Fri ";
                break;
            case Calendar.SATURDAY:
                currentDate+="Sat ";
                break;
            default:
        }


        switch (month) {
            case Calendar.JANUARY:
                currentDate += "Jan ";
                break;
            case Calendar.FEBRUARY:
                currentDate += "Feb ";
                break;
            case Calendar.MARCH:
                currentDate += "Mar ";
                break;
            case Calendar.APRIL:
                currentDate += "Apr ";
                break;
            case Calendar.MAY:
                currentDate += "May ";
                break;
            case Calendar.JUNE:
                currentDate += "Jun ";
                break;
            case Calendar.JULY:
                currentDate += "Jul ";
                break;
            case Calendar.AUGUST:
                currentDate += "Aug ";
                break;
            case Calendar.SEPTEMBER:
                currentDate += "Sep ";
                break;
            case Calendar.OCTOBER:
                currentDate += "Oct ";
                break;
            case Calendar.NOVEMBER:
                currentDate += "Nov ";
                break;
            case Calendar.DECEMBER:
                currentDate += "Dec ";
                break;
            default:
        }

        if (date/10 == 0) {
            currentDate += "0" + date + " ";
        }
        else {
            currentDate += date + " ";
        }
        startTime = currentDate + "00:00:01 GMT+05:30 " + year;
        endTime = currentDate + "23:59:59 GMT+05:30 " + year;
        Log.i("Start time",startTime);
        Log.i("End time",endTime);
        currentDate += year;
        Log.i("Current Date",currentDate);


        appInfos.add(new AppInfo(getString(R.string.INSTAGRAM),getString(R.string.instagram),R.mipmap.ic_launcher,currentDate));
        appInfos.add(new AppInfo(getString(R.string.YOUTUBE),getString(R.string.youtube),R.mipmap.ic_launcher,currentDate));
        appInfos.add(new AppInfo(getString(R.string.CALL_OF_DUTY_MOBILE),getString(R.string.callofdutymobile),R.mipmap.ic_launcher,currentDate));
        appInfos.add(new AppInfo(getString(R.string.WHATSAPP),getString(R.string.whatsapp),R.mipmap.ic_launcher,currentDate));
        appInfos.add(new AppInfo(getString(R.string.CHROME),getString(R.string.chrome),R.mipmap.ic_launcher,currentDate));
        appInfos.add(new AppInfo(getString(R.string.CAMPUS_CARE),getString(R.string.campuscare),R.mipmap.ic_launcher,currentDate));

        new AppUsageCalculation(this).execute();
    }

    private static class AppUsageCalculation extends AsyncTask<Void,Void,Void> {
        private ArrayList<AppInfo> appInfos;
        private WeakReference<TodaysStatsActivity> activity;

        AppUsageCalculation(TodaysStatsActivity activity) {
            this.activity = new WeakReference<>(activity);
            appInfos = activity.getAppInfos();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            String startDate = activity.get().getStartTime();
            String endDate = activity.get().getEndTime();
            long startTime = LocalDateTime.parse(startDate,formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
            long endTime = LocalDateTime.parse(endDate,formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();

            UsageStatsManager manager = (UsageStatsManager) activity.get().getSystemService(Context.USAGE_STATS_SERVICE);
            Map<String, UsageStats> stats = manager.queryAndAggregateUsageStats(startTime,endTime);
            Log.i("Stats size",""+stats.size());

            for (Map.Entry<String,UsageStats> entry: stats.entrySet()) {
                Log.i("##Stats :  ",entry.getKey()+" = "+entry.getValue().getTotalTimeInForeground());
            }

            for (AppInfo info: appInfos) {
                if (stats.containsKey(info.getPackageName())) {
                    long timeInMillis = stats.get(info.getPackageName()).getTotalTimeInForeground();
                    long seconds = timeInMillis/1000;
                    long minutes = seconds/60;
                    long hours = minutes/60;
                    minutes = minutes - (hours*60);
                    seconds = seconds - (hours*60*60 + minutes*60);

                    String usage = formatToString(hours)+" hrs: " + formatToString(minutes)+" min: " + formatToString(seconds)+" sec";
                    info.setUsage(usage);
                }
                else {
                    Log.i("Info","Package for "+info.getName()+" not found :: "+info.getPackageName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity.get().getRecyclerViewAdapter().notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        private String formatToString(long val) {
            String out;
            if (val/10 == 0) {
                out = "0"+val;
            }
            else {
                out = ""+val;
            }
            return out;
        }
    }
}