package org.boyoot.app.ui.employees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.UserProfileModel;

import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {


    private ListItemClickListener onItemClickListener;
    private List<UserProfileModel> users;
    private Context context;
    public EmployeesAdapter(Context context,ListItemClickListener listener){
        this.context =context;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employees_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {

        holder.userName.setText(users.get(position).getUserName());
        holder.userEmail.setText(users.get(position).getEmail());
        if (users.get(position).getRole().equals("Admin") || users.get(position).getRole().equals("Manager") ) {
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_admin));
        }else if(users.get(position).getRole().equals("Moderator")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_moderator));
        }else if (users.get(position).getRole().equals("Supervisor")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_supervisor_account));
        }else if (users.get(position).getRole().equals("Worker")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_worker));
        }else if (users.get(position).getRole().equals("User")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_new_tag));
        }

    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return Math.max(users.size(), 0);
        }else {
            return 0;
        }
    }


    class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView userIv;
        TextView userName;
        TextView userEmail;


        public EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            userIv = itemView.findViewById(R.id.employee_iv);
            userEmail = itemView.findViewById(R.id.employee_email_tv);
            userName = itemView.findViewById(R.id.employee_name_tv);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            onItemClickListener.onListItemClickListener( users.get(clickedIndex).getEmail());
        }
    }


    public interface ListItemClickListener{
        void onListItemClickListener(String email);
    }

    public void setUsers(List<UserProfileModel> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
