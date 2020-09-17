package org.boyoot.app.ui.jobs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.job.Job;
import org.boyoot.app.utilities.WorkUtility;

import java.util.ArrayList;
import java.util.List;

public class JobsListAdapter extends RecyclerView.Adapter<JobsListAdapter.AppointmentListViewHolder> {


    private List<Job> jobs;
    private ListItemOnClickListener listener;
    private Context context;

    public JobsListAdapter(ListItemOnClickListener listener, Context context){
        this.listener = listener;
        this.context = context;
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
        holder.jobAcsTotal.setText(WorkUtility.getTextTotalNumberOfWork(jobs.get(position).getCurrentWork()));
        holder.duration.setText(WorkUtility.getDurationTextOfJob(jobs.get(position).getCurrentWork(),2));
        if (jobs.get(position).getCurrentWork().getInterval().equals("Morning")){
            holder.jobIntervalTv.setBackground(context.getDrawable(R.drawable.ic_day_light));
        }else {
            holder.jobIntervalTv.setBackground(context.getDrawable(R.drawable.ic_night));
        }

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
        TextView jobAcsTotal;
        ImageView jobIntervalTv;

        public AppointmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.job_phone_tv);
            id = itemView.findViewById(R.id.job_id_tv);
            branch = itemView.findViewById(R.id.job_branch_tv);
            duration = itemView.findViewById(R.id.job_duration_tv);
            jobAcsTotal = itemView.findViewById(R.id.job_acs_total_tv);
            jobIntervalTv = itemView.findViewById(R.id.job_interval_iv);
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
