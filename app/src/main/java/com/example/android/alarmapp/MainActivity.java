package com.example.android.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.alarmapp.database.DBOperations;
import com.example.android.alarmapp.service.AlarmService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DateFragment.OnDatePickListener, TimeFragment.OnTimePickListener, RecyclerAdapter.SwitchListener{

    private LinearLayout layoutRoot;
    private View dialogLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ArrayList<Alarm> mAlarms;
    private DBOperations operations;
    private Calendar calendar;
    private Switch alarmSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutRoot = findViewById(R.id.dialog_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        operations = new DBOperations(this);
        mAlarms = operations.readAllAlarms();

        // clicking on item will view the delete dialog
        mAdapter = new RecyclerAdapter(mAlarms, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewDeleteDialog(position);
            }
        });
        mAdapter.setSwitchListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // clicking on the floating button will view the add dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = null;
                viewAddDialog();
            }
        });

    }

    public void viewAddDialog() {
        dialogLayout = getLayoutInflater().inflate(R.layout.dialog_layout, layoutRoot);

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add Alarm")
                .setCancelable(false)
                .setView(dialogLayout)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (calendar != null) {
                            addNewAlarm();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Time not set!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

        dialog.show();
    }

    public void addNewAlarm() {
        Alarm alarm = new Alarm(calendar.getTimeInMillis(), 1);
        long rowId = operations.addAlarm(alarm);

        if (rowId > 0) {
            Toast.makeText(this, "Alarm added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error while adding the alarm", Toast.LENGTH_SHORT).show();
        }

        mAlarms = operations.readAllAlarms();
        mAdapter.addAlarm(mAlarms);
    }

    public void viewDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Alarm");
        builder.setMessage("Do you want to delete this alarm?");
        builder.setCancelable(true);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAlarm(position);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    public void deleteAlarm(int position) {
        int deletionId = operations.deleteAlarm(mAlarms.get(position));

        if (deletionId > 0) {
            Toast.makeText(this, "Alarm was deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting Alarm", Toast.LENGTH_SHORT).show();
        }

        mAlarms = operations.readAllAlarms();
        mAdapter.addAlarm(mAlarms);
    }

    public void setAlarmTime(View view) {
        TimeFragment timeFragment = new TimeFragment();
        timeFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void setAlarmDate(View view) {
        DateFragment dateFragment = new DateFragment();
        dateFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        TextView timeTextView = dialogLayout.findViewById(R.id.time_text_view);
        timeTextView.setText(hour + " : " + minute);

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onDateSelected(int day, int month, int year) {
        TextView dateTextView = dialogLayout.findViewById(R.id.date_text_view);
        dateTextView.setText(day + "/" + month + "/" + year);

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
    }

    // Start the alarm if the switch is on and stop it if it's off
    @Override
    public void onSwitchChanged(int position) {
        Log.d("TAG", "Status: " + mAlarms.get(position).isEnabled());
        if (mAlarms.get(position).isEnabled() == 1) {
            setAlarm(position);
        } else if (mAlarms.get(position).isEnabled() == 0) {
            stopAlarm();
        }
    }

    private void setAlarm(int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,  0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, mAlarms.get(position).getTime(), pendingIntent);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

    public void stopAlarm() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//             to try the alarm service
//            setAlarm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
