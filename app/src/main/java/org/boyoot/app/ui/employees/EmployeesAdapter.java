package org.boyoot.app.ui.employees;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.boyoot.app.R;
import org.boyoot.app.model.UserProfileModel;

import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {


    private ListItemClickListener onItemClickListener;
    private List<UserProfileModel> users;
    private Context context;
    EmployeesAdapter(Context context,ListItemClickListener listener){
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
        if (users.get(position).getRole().equals("admin")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_verified_user_black_24dp));
        }else if (users.get(position).getRole().equals("super")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_supervisor_account));
        }else if (users.get(position).getRole().equals("worker")){
            holder.userIv.setBackground(context.getDrawable(R.drawable.ic_worker));
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

        }

        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            onItemClickListener.onListItemClickListener(clickedIndex);


        }
    }


    interface ListItemClickListener{
        void onListItemClickListener(int itemIndex);
    }

    public void setUsers(List<UserProfileModel> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
