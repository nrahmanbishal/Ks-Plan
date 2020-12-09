package com.example.ksplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//Adaptar for recycler view Table

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    ArrayList<TaskHelper> tableList;
    Context mContext;

    public TableAdapter(ArrayList<TaskHelper> tableList, Context mContext) {
        this.tableList = tableList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_layout,parent,false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {

        if(tableList!=null && tableList.size()>0)
        {
            TaskHelper currentTaskRow=tableList.get(position);
            holder.tv_name.setText(currentTaskRow.getTaskName());
            holder.tv_unit.setText(currentTaskRow.getUnit());
            holder.tv_max.setText(String.valueOf(currentTaskRow.getMax()));
        }
        else
        {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_unit,tv_max;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.tv_table_task_name);
            tv_unit=itemView.findViewById(R.id.tv_table_task_unit);
            tv_max=itemView.findViewById(R.id.tv_table_task_max);
        }
    }
}
