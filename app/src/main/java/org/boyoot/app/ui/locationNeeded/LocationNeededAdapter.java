package org.boyoot.app.ui.locationNeeded;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.database.GoogleSheet;
import org.boyoot.app.ui.googleSheet.GoogleSheetListAdapter;

import java.util.ArrayList;
import java.util.List;

import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class LocationNeededAdapter extends RecyclerView.Adapter<LocationNeededAdapter.LocationNeededViewHolder>{



    private List<Contacts> dataList;
    private Context context;
    final private LocationNeededAdapter.ListItemOnClickListener onClickListener;

    LocationNeededAdapter(Context context , LocationNeededAdapter.ListItemOnClickListener listener) {
        this.context = context;
        this.onClickListener = listener;
        dataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationNeededAdapter.LocationNeededViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationNeededAdapter.LocationNeededViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.google_sheet_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationNeededAdapter.LocationNeededViewHolder holder, int position) {




        holder.textView.setText(getValidPhoneNumber(dataList.get(position).getPhone()));
        holder.locationView.setText(dataList.get(position).getCity());
        holder.contactIdTv.setText(dataList.get(position).getContactId());
        holder.tagView.setText(dataList.get(position).getTimeStamp());
        holder.dateTv.setText(dataList.get(position).getInterval());
        holder.locationIv.setBackground(context.getDrawable(R.drawable.pin));



    }

    @Override
    public int getItemCount() {
        if (dataList != null){
            return dataList.size();
        }else {
            return 0;
        }
    }




    class LocationNeededViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView textView;
        TextView tagView;
        TextView locationView;
        TextView contactIdTv;
        TextView dateTv;
        ImageView cloudIv;
        ImageView locationIv;
        private LocationNeededViewHolder(@NonNull View itemView) {
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

    void setDataList(List<Contacts> list){
        this.dataList = list;
        notifyDataSetChanged();
    }


    interface ListItemOnClickListener{
        void onListItemClicked(int clickedItemIndex);

    }


}
