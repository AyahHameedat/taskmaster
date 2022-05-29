package com.example.lab16;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab16.data.TaskData;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    List<TaskData> taskDataList;
    TaskClickListener taskListener;


    public TaskRecyclerViewAdapter(List<TaskData> taskDataList,
                                   TaskClickListener taskListener) {
        this.taskDataList = taskDataList;
        this.taskListener = taskListener;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listTaskItemView = layoutInflater.inflate(R.layout.task_item, parent, false);
        return null;
        View listTaskItemView = layoutInflater.inflate(R.layout.activity_task_recycler_view_adapter, parent, false);
        return new TaskViewHolder(listTaskItemView, taskListener);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.title.setText(taskDataList.get(position).getTitle());
        holder.body.setText(taskDataList.get(position).getBody());
        holder.state.setText(taskDataList.get(position).getState());

//        holder.body.setText(taskDataList.get(position).getBody());
//        holder.state.setText(taskDataList.get(position).getState());

    }

    @Override
    public int getItemCount() {
        return taskDataList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView body;
        TextView state;


        final static class TaskViewHolder extends RecyclerView.ViewHolder{

        Button title;
//        TextView body;
//        TextView state;

        TaskClickListener taskListener;

        public TaskViewHolder(@NonNull View itemView, TaskClickListener taskListener) {
            super(itemView);

            this.taskListener = taskListener;


            title = itemView.findViewById(R.id.recycler_view_Button);
            title = itemView.findViewById(R.id.task_title);
            body = itemView.findViewById(R.id.task_body);
            state = itemView.findViewById(R.id.task_state);

            itemView.setOnClickListener(view -> taskListener.onTaskClicked(getAdapterPosition()));
            title = itemView.findViewById(R.id.recycler_view);
//            body = itemView.findViewById(R.id.task_body);
//            state = itemView.findViewById(R.id.task_state);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskListener.onTaskClicked(getAdapterPosition());
                }
            });

//            title.setOnClickListener(view -> taskListener.onTaskClicked(getAdapterPosition()));


//            itemView.setOnClickListener(view -> taskListener.onTaskClicked(getAdapterPosition()));

        }
    }


    public interface TaskClickListener{
        void onTaskClicked(int position);
    }

}
