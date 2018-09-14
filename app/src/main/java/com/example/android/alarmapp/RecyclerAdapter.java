package com.example.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.alarmapp.service.AlarmService;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Alarm> mAlarms;
    private OnItemClickListener mListener;

    public RecyclerAdapter(ArrayList<Alarm> alarms, OnItemClickListener listener) {
        mAlarms = alarms;
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView alarmTextView;
        Switch alarmSwitch;


        public ViewHolder(View taskView) {
            super(taskView);
            alarmTextView = taskView.findViewById(R.id.item_time);
            alarmSwitch = taskView.findViewById(R.id.item_switch);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        final ViewHolder viewHolder =  new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int hours = new Date(mAlarms.get(position).getTime()).getHours();
        int minutes = new Date(mAlarms.get(position).getTime()).getMinutes();
        holder.alarmTextView.setText("" + hours + " : " + minutes); // try formatting the time

        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAlarms.get(position).setIsEnabled(1);
                } else {
                    mAlarms.get(position).setIsEnabled(0);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void addAlarm(ArrayList<Alarm> alarms){
        mAlarms = alarms;
        notifyDataSetChanged();
    }
}
