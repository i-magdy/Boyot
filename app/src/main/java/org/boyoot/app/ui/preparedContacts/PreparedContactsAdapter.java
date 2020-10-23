package org.boyoot.app.ui.preparedContacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;


import java.util.ArrayList;
import java.util.List;

import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class PreparedContactsAdapter  extends RecyclerView.Adapter<PreparedContactsAdapter.PreparedContactsViewHolder>{



    private List<Contacts> dataList;
    private Context context;
    final private ListItemOnClickListener onClickListener;

    PreparedContactsAdapter(Context context , ListItemOnClickListener listener) {
        this.context = context;
        this.onClickListener = listener;
        dataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PreparedContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreparedContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreparedContactsViewHolder holder, int position) {




        holder.textView.setText(getValidPhoneNumber(dataList.get(position).getPhone()));
        holder.locationView.setText(dataList.get(position).getCity());
        holder.contactIdTv.setText(dataList.get(position).getContactId());
        holder.tagView.setText(dataList.get(position).getTimeStamp());
        holder.locationIv.setBackground(context.getDrawable(R.drawable.ic_outline_location));
        if (dataList.get(position).getInterval().equals("Morning")){
            holder.optionIv.setBackground(context.getDrawable(R.drawable.ic_day_light));
        }else {
            holder.optionIv.setBackground(context.getDrawable(R.drawable.ic_night));
        }



    }

    @Override
    public int getItemCount() {
        if (dataList != null){
            return dataList.size();
        }else {
            return 0;
        }
    }




    class PreparedContactsViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView textView;
        TextView tagView;
        TextView locationView;
        TextView contactIdTv;
        ImageView optionIv;
        ImageView locationIv;
        ImageView intervalIv;
        private PreparedContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            tagView = itemView.findViewById(R.id.contact_tag_view);
            locationView = itemView.findViewById(R.id.location_tv);
            contactIdTv = itemView.findViewById(R.id.contact_id_tv);
            intervalIv = itemView.findViewById(R.id.contact_interval_iv);
            intervalIv.setVisibility(View.GONE);
            optionIv = itemView.findViewById(R.id.contact_option_iv);
            locationIv = itemView.findViewById(R.id.contact_location_iv);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            onClickListener.onListItemClicked(dataList.get(clickedIndex).getId());
        }


    }

    void setDataList(List<Contacts> list){
        this.dataList = list;
        notifyDataSetChanged();
    }


    interface ListItemOnClickListener{
        void onListItemClicked(String contactKey);

    }

}
