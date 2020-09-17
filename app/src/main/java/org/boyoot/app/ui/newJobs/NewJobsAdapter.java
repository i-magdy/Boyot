package org.boyoot.app.ui.newJobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.database.Jobs;
import org.boyoot.app.utilities.WorkUtility;

import java.util.List;

public class NewJobsAdapter extends RecyclerView.Adapter<NewJobsAdapter.NewJobsViewHolder> {

    private List<Jobs> jobs;
    private OnItemClickListener listener;
    private Context context;

    public NewJobsAdapter(OnItemClickListener listener,Context context) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public NewJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewJobsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewJobsViewHolder holder, int position) {
        holder.phone.setText(jobs.get(position).getPhone());
        holder.id.setText(jobs.get(position).getContactId());
        holder.branch.setText(jobs.get(position).getCity());
        holder.duration.setText(WorkUtility.getDurationTextForNewJob(jobs.get(position).getAcsTotal()));
        holder.jobAcsTotal.setText(jobs.get(position).getAcsTotal());

        if (jobs.get(position).getInterval().equals("Morning")){
            holder.jobIntervalTv.setBackground(context.getDrawable(R.drawable.ic_day_light));
        }else {
            holder.jobIntervalTv.setBackground(context.getDrawable(R.drawable.ic_night));
        }
    }

    @Override
    public int getItemCount() {
        if (jobs != null) return jobs.size();
        else return 0;

    }


    class NewJobsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView phone;
        TextView id;
        TextView branch;
        TextView duration;
        TextView jobAcsTotal;
        ImageView jobIntervalTv;

        public NewJobsViewHolder(@NonNull View itemView) {
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
            int index = getAdapterPosition();
            listener.onItemClicked(jobs.get(index).getKey());
        }
    }



    interface  OnItemClickListener{
        void onItemClicked(String jobId);
    }
    public void setJobs(List<Jobs> jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();
    }
}
