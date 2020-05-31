package org.boyoot.app.ui.googleSheet;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import androidx.recyclerview.widget.RecyclerView;




import org.boyoot.app.R;
import org.boyoot.app.database.GoogleSheet;

import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;
import java.util.ArrayList;
import java.util.List;


public class GoogleSheetListAdapter extends RecyclerView.Adapter<GoogleSheetListAdapter.GoogleSheetViewHolder> {


    private List<GoogleSheet> dataList;
    private Context context;
    final private ListItemOnClickListener onClickListener;


    GoogleSheetListAdapter(Context context ,ListItemOnClickListener listener) {
        this.context = context;
        this.onClickListener = listener;
        dataList = new ArrayList<>();

    }

    @NonNull
    @Override
    public GoogleSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoogleSheetViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.google_sheet_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleSheetViewHolder holder, int position) {

        holder.textView.setText(getValidPhoneNumber(dataList.get(position).getPhone()));
        holder.locationView.setText(dataList.get(position).getCity());
        holder.contactIdTv.setText(dataList.get(position).getContactId());
        holder.tagView.setText(dataList.get(position).getTimeStamp());

        if (TextUtils.equals(dataList.get(position).getState(),"2") || TextUtils.equals(dataList.get(position).getState(),"3")){
           holder.locationIv.setBackground(context.getDrawable(R.drawable.placeholder));
        }else{
           holder.locationIv.setBackground(context.getDrawable(R.drawable.pin));
        }
        if (TextUtils.equals(dataList.get(position).getDate(),"الفترة الأولى")){
            holder.dateTv.setText("صباحاً");
        }else{
            holder.dateTv.setText("مساءً");
        }
        if (TextUtils.equals(dataList.get(position).getState(),"1") || TextUtils.equals(dataList.get(position).getCloudId(),"3") ){
            holder.cloudIv.setBackground(context.getDrawable(R.drawable.ic_cloud_done));
        }else if (dataList.get(position).getState().equals("0")){
            holder.cloudIv.setBackground(context.getDrawable(R.drawable.ic_new_tag));
        }else{
            holder.cloudIv.setBackground(null);
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




    class GoogleSheetViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView textView;
        TextView tagView;
        TextView locationView;
        TextView contactIdTv;
        TextView dateTv;
        ImageView cloudIv;
        ImageView locationIv;
        private GoogleSheetViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            tagView = itemView.findViewById(R.id.contact_tag_view);
            locationView = itemView.findViewById(R.id.location_tv);
            contactIdTv = itemView.findViewById(R.id.contact_id_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            cloudIv = itemView.findViewById(R.id.cloud_iv);
            locationIv = itemView.findViewById(R.id.imageView2);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            onClickListener.onListItemClicked(clickedIndex);

        }


    }

    void setDataList(List<GoogleSheet> list){
        this.dataList = list;
        notifyDataSetChanged();
    }


    interface ListItemOnClickListener{
        void onListItemClicked(int clickedItemIndex);

    }



}


