package com.example.ksplan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView date;
    RecyclerView recyclerViewTasks;
    //ArrayList<TaskHelper> mLists;
    ArrayList<TaskHelper> object;
    Button editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        date=findViewById(R.id.tv_date);
        recyclerViewTasks=findViewById(R.id.recyclerview_task);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        final Button dateButon=findViewById(R.id.btn_date);
        editor=findViewById(R.id.btn_edit);

        Calendar c= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance().format(c.getTime());
        date.setText(currentDate);

        dateButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TaskActivity.this,EditorActivity.class);
                if(object!=null){
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable)object);
                    intent.putExtra("BUNDLE",args);
                }
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTasks.setLayoutManager(layoutManager);
        recyclerViewTasks.setHasFixedSize(true);

        Intent i = new Intent(this, EditorActivity.class);
        startActivityForResult(i, 1);


//        Intent in = getIntent();
//        Bundle args = in.getBundleExtra("BUNDLE");
//        ArrayList<TaskHelper> object = (ArrayList<TaskHelper>) args.getSerializable("ARRAYLIST");


//        mLists=new ArrayList<>();
//
//        mLists.add(new TaskHelper("Water taken","Liter", Integer.parseInt("2"),Integer.parseInt("5")));
//        mLists.add(new TaskHelper("Workout","munite", Integer.parseInt("10"),Integer.parseInt("50")));
//        mLists.add(new TaskHelper("walk","hour", Integer.parseInt("1"),Integer.parseInt("2")));
//        mLists.add(new TaskHelper("calorie","kcal", Integer.parseInt("50"),Integer.parseInt("100")));
//        mLists.add(new TaskHelper("yoga","munite", Integer.parseInt("20"),Integer.parseInt("60")));
//        mLists.add(new TaskHelper("weigh lift","Kg", Integer.parseInt("20"),Integer.parseInt("100")));
//        mLists.add(new TaskHelper("Sleep","hour", Integer.parseInt("6"),Integer.parseInt("8")));
//        TaskRecyclerViewAdapter myAdapter=new TaskRecyclerViewAdapter(object);
//        recyclerViewTasks.setAdapter(myAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle args = data.getBundleExtra("BUNDLE");
                object = (ArrayList<TaskHelper>) args.getSerializable("ARRAYLIST");
                TaskRecyclerViewAdapter myAdapter=new TaskRecyclerViewAdapter(object,TaskActivity.this);
                recyclerViewTasks.setAdapter(myAdapter);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate= DateFormat.getDateInstance().format(c.getTime());
        date.setText(currentDate);
    }
}