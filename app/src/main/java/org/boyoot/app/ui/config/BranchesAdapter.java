package org.boyoot.app.ui.config;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.Branch;

import java.util.ArrayList;
import java.util.List;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.BranchesViewHolder> {

    private List<Branch> branchesList;
    private OnListItemClicked listener;

    BranchesAdapter(OnListItemClicked listener){
        branchesList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public BranchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BranchesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BranchesViewHolder holder, int position) {
        holder.code.setText(branchesList.get(position).getBranchId());
        holder.title.setText(branchesList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (branchesList.size() == 0){
            return 0;
        }else {
            return branchesList.size();
        }
    }


    class BranchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView code;
        public BranchesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.branch_title_tv);
            code = itemView.findViewById(R.id.branch_code_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            listener.onItemClickedListener(branchesList.get(clickedIndex).getBranchId());
        }
    }
    interface OnListItemClicked{
        void onItemClickedListener(String branchId);
    }

    void setBranchesList (List<Branch> branchesList){
        this.branchesList = branchesList;
        notifyDataSetChanged();
    }
}
