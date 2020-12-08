package com.example.ksplan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewViewHorder> {
    private ArrayList<TaskHelper> mTasks;
    private Context context;

//    public TaskRecyclerViewAdapter(ArrayList<TaskHelper> mTasks) {
//        this.mTasks = mTasks;
//    }
//
//    public TaskRecyclerViewAdapter(Context context) {
//        this.context = context;
//    }

    public TaskRecyclerViewAdapter(ArrayList<TaskHelper> mTasks, Context context) {
        this.mTasks = mTasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskRecyclerViewViewHorder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_single,parent,false);
        TaskRecyclerViewViewHorder viewHolder=new TaskRecyclerViewViewHorder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewViewHorder holder, int position) {

        TaskHelper currentTask=mTasks.get(position);
        holder.taskName.setText(currentTask.getTaskName());
        holder.tasDescription.setText(currentTask.getIncrement() +" von "+currentTask.getMin()+" "+currentTask.getUnit()+" (bis zu "+currentTask.getMax()+" "+currentTask.getUnit()+")");
        holder.progressBarTask.setProgress(0);
        holder.ProgressbarProgress.setText(String.valueOf(0)+"%");
        holder.buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebWindowActivity.class);
                intent.putExtra("URL", currentTask.getInfoLink());
                context.startActivity(intent);
            }
        });

        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask.setResultNumber(currentTask.getResultNumber()+currentTask.getIncrement());
                holder.progressBarTask.setProgress((int)currentTask.getResultNumber());
                holder.ProgressbarProgress.setText(String.valueOf(currentTask.getResultNumber())+"%");
            }
        });

        holder.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask.setResultNumber(currentTask.getResultNumber()-currentTask.getIncrement());
                holder.progressBarTask.setProgress((int)currentTask.getResultNumber());
                holder.ProgressbarProgress.setText(String.valueOf(currentTask.getResultNumber())+"%");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskRecyclerViewViewHorder extends RecyclerView.ViewHolder {

        TextView taskName;
        TextView tasDescription;
        ProgressBar progressBarTask;
        TextView ProgressbarProgress;
        Button buttonPlus;
        Button buttonMinus;
        Button buttonInfo;

        public TaskRecyclerViewViewHorder(@NonNull View itemView) {
            super(itemView);
            taskName=itemView.findViewById(R.id.task_name);
            tasDescription=itemView.findViewById(R.id.task_description);
            progressBarTask=itemView.findViewById(R.id.pb_task);
            ProgressbarProgress=itemView.findViewById(R.id.tv_progress_task);
            buttonPlus=itemView.findViewById(R.id.btn_plus);
            buttonMinus=itemView.findViewById(R.id.btn_minus);
            buttonInfo=itemView.findViewById(R.id.btn_info);

        }
    }
}
