package org.boyoot.app.ui.jobs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.job.Job;

import java.util.ArrayList;
import java.util.List;

public class JobsListAdapter extends RecyclerView.Adapter<JobsListAdapter.AppointmentListViewHolder> {


    private List<Job> jobs;
    private ListItemOnClickListener listener;

    public JobsListAdapter(ListItemOnClickListener listener){
        this.listener = listener;
        jobs = new ArrayList<>();
    }

    @NonNull
    @Override
    public AppointmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListViewHolder holder, int position) {
        holder.phone.setText(jobs.get(position).getPhone());
        holder.id.setText(jobs.get(position).getId());
        holder.branch.setText(jobs.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        if (jobs != null){

            return jobs.size();
        }else {

            return 0;
        }
    }


    class AppointmentListViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView phone;
        TextView id;
        TextView branch;
        TextView duration;
        TextView appointment;


        public AppointmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.job_phone_tv);
            id = itemView.findViewById(R.id.job_id_tv);
            branch = itemView.findViewById(R.id.job_branch_tv);
            duration = itemView.findViewById(R.id.job_duration_tv);
            appointment = itemView.findViewById(R.id.job_appointment_tv);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onItemClickListener(position);
        }
    }

    public interface ListItemOnClickListener{
        void onItemClickListener(int position);
    }
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();
    }
}
