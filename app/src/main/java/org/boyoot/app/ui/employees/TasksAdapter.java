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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.model.Tasks;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {


    private ItemClickListener onItemClickListener;
    private List<Tasks> tasks;
    private Activity activity;
    private boolean isAdmin;
    private String profileId;
    private static final String USERS_PATH="users";
    private static final String TASKS_PATH = "tasks";

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
        holder.deleteItemListener(position);
        if (tasks.get(position).isDone()){
            holder.check.setBackground(activity.getDrawable(R.drawable.ic_task_checked));
        }else {
            holder.check.setBackground(activity.getDrawable(R.drawable.ic_undone_task));
        }

        if (isAdmin) {
            holder.deleteItem.setVisibility(View.VISIBLE);
            if (tasks.get(position).isSeen()) {
                holder.seenIv.setVisibility(View.VISIBLE);
            } else {
                holder.seenIv.setVisibility(View.INVISIBLE);
            }
        }else {
            holder.deleteItem.setVisibility(View.INVISIBLE);
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
        ImageView seenIv;
        ImageView deleteItem;
        TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check_iv);
            titleTv = itemView.findViewById(R.id.task_title_tv);
            seenIv = itemView.findViewById(R.id.seen_iv);
            deleteItem = itemView.findViewById(R.id.delete_task_item);
            itemView.setOnClickListener(this);
            check.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            if (v.getId() == R.id.check_iv){
                onItemClickListener.onCheckClicked(tasks.get(clickedIndex));
            }else {
                onItemClickListener.onItemClicked(tasks.get(clickedIndex));
            }
        }

        void deleteItemListener(int position){
            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(USERS_PATH).document(profileId).collection(TASKS_PATH).document(tasks.get(position).getId())
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onItemClickListener.syncTasks();
                        }
                    });

                }
            });
        }
    }

    interface ItemClickListener {
        void onCheckClicked(Tasks task);
        void onItemClicked(Tasks task);
        void syncTasks();
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        notifyDataSetChanged();
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
