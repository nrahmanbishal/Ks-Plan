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
    //declaring variables
    TextView date;
    RecyclerView recyclerViewTasks;
    ArrayList<TaskHelper> object;
    Button editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Finding layouts, hiding actionbar
        setContentView(R.layout.activity_task);
        date=findViewById(R.id.tv_date);
        recyclerViewTasks=findViewById(R.id.recyclerview_task);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        final Button dateButon=findViewById(R.id.btn_date);
        editor=findViewById(R.id.btn_edit);

        //Always starting the app with current date and time.
        Calendar c= Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance().format(c.getTime());
        date.setText(currentDate);

        //launching Datepicker
        dateButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        //Intent for starting Editor activity empty or having data
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(TaskActivity.this,EditorActivity.class);
                if(object!=null){
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable)object);
                    intent.putExtra("BUNDLE",args);
                    intent.putExtra("name",c.getTimeInMillis());
                }
                startActivity(intent);
            }
        });

        //Making ready the recyclerview
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTasks.setLayoutManager(layoutManager);
        recyclerViewTasks.setHasFixedSize(true);

        Intent i = new Intent(this, EditorActivity.class);
        startActivityForResult(i, 1);

    }

    //Populating the task activity recycler view
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

    //Getting the selected date from datepicker
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