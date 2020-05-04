package org.boyoot.app.ui.employees;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.Tasks;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {


    private ItemClickListener onItemClickListener;
    private List<Tasks> tasks;
    private Activity activity;
    public TasksAdapter(ItemClickListener listener, Activity activity){
        this.onItemClickListener = listener;
        this.activity = activity;
    }
    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TasksViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {

        holder.titleTv.setText(tasks.get(position).getTitle());

        if (tasks.get(position).isDone()){
            holder.check.setBackground(activity.getDrawable(R.drawable.ic_task_checked));
        }else {
            holder.check.setBackground(activity.getDrawable(R.drawable.ic_undone_task));
        }
    }

    @Override
    public int getItemCount() {
        if (tasks != null) return tasks.size();
        return 0;
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView check;
        TextView titleTv;
        TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check_iv);
            titleTv = itemView.findViewById(R.id.task_title_tv);
            itemView.setOnClickListener(this);
            check.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            if (v.getId() == R.id.check_iv){
                onItemClickListener.onCheckClicked(clickedIndex);
                Log.i("task_checked","true");
            }else {
                onItemClickListener.onItemClicked(clickedIndex);
                Log.i("task_checked","false");
            }
        }
    }

    interface ItemClickListener {
        void onCheckClicked(int position);
        void onItemClicked(int position);
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }
}
