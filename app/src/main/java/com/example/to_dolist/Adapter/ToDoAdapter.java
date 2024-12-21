package com.example.to_dolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.AddNewTask;
import com.example.to_dolist.MainActivity;
import com.example.to_dolist.Model.ToDoModel;
import com.example.to_dolist.R;
import com.example.to_dolist.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mlist = new ArrayList<>();
    private final MainActivity activity;
    private final DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mlist.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                myDB.updateStatus(item.getId(), 1);
            } else {
                myDB.updateStatus(item.getId(), 0);
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        if (mlist != null && position < mlist.size()) {
            ToDoModel item = mlist.get(position);
            myDB.deleteTask(item.getId());
            mlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void editItem(int position) {
        if (mlist != null && position < mlist.size()) {
            ToDoModel item = mlist.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("task", item.getTask());

            AddNewTask task = new AddNewTask();
            task.setArguments(bundle);
            task.show(activity.getSupportFragmentManager(), task.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
