package com.example.appusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AppInfo> appInfos;

    UsageListAdapter(Context context, ArrayList<AppInfo> appInfos) {
        mContext = context;
        this.appInfos = appInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo appInfo = appInfos.get(position);
        holder.bindTo(appInfo);
    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView appNameTextView;
        private TextView dateTextView;
        private TextView usageTextView;
        private ImageView appImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.app_name);
            dateTextView = itemView.findViewById(R.id.date);
            usageTextView = itemView.findViewById(R.id.time_used);
            appImageView = itemView.findViewById(R.id.image_holder);
        }

        void bindTo(AppInfo appInfo) {
            appNameTextView.setText(appInfo.getName());
            dateTextView.setText(appInfo.getDate());
            usageTextView.setText(appInfo.getUsage());
            Glide.with(mContext).load(appInfo.getImageId()).into(appImageView);
        }
    }
}
