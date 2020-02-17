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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.boyoot.app.R;
import org.boyoot.app.model.GoogleSheetModel;

import java.util.ArrayList;
import java.util.List;


public class GoogleSheetListAdapter extends RecyclerView.Adapter<GoogleSheetListAdapter.GoogleSheetViewHolder> {


    private List<GoogleSheetModel> dataList;
    private Context context;
    final private ListItemOnClickListener onClickListener;
    private List<Integer> posList = new ArrayList<>();

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





            holder.textView.setText(dataList.get(position).getNumber()+" "+dataList.get(position).getState());
            if (TextUtils.equals(dataList.get(position).getState(),"8")) {
                posList.add(position);
                holder.tagView.setBackgroundResource(R.drawable.work_done_tag);
                holder.tagView.setText(context.getString(R.string.state_work_done));
            }else if (TextUtils.equals(dataList.get(position).getState(),"5")){
                holder.tagView.setBackgroundResource(R.drawable.date_approved_tag);
                holder.tagView.setText(context.getString(R.string.state_date_approved));
            }else if (TextUtils.equals(dataList.get(position).getState(),"3") || TextUtils.equals(dataList.get(position).getState(),"9")){
                holder.tagView.setBackgroundResource(R.drawable.work_delayed_tag);
                holder.tagView.setText(context.getString(R.string.state_work_delayed));
            }else if (TextUtils.equals(dataList.get(position).getState(),"")){
                holder.tagView.setBackgroundResource(R.drawable.new_contact_tag);
                holder.tagView.setText(context.getString(R.string.state_new_contact));
            }else if(TextUtils.equals(dataList.get(position).getState(),"4")){
                holder.tagView.setBackgroundResource(R.drawable.date_picked_tag);
                holder.tagView.setText(context.getString(R.string.state_date_picked));

            } else{
                holder.tagView.setBackgroundColor(Color.WHITE);
            }

            /*if (posList.size() > 0) {
                for (int i = 0; i < posList.size(); ++i) {
                    if (posList.get(i) == position) {
                        holder.tagView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTagNewContact));
                    }else {
                        holder.tagView.setBackgroundColor(Color.WHITE);
                    }
                }

            }*/
    }

    @Override
    public int getItemCount() {

        if (dataList != null){
            return dataList.size();
        }else {
            return 0;
        }
    }


    class GoogleSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        TextView tagView;
        private GoogleSheetViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            tagView = itemView.findViewById(R.id.contact_tag_view);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int clickedIndex = getAdapterPosition();
            onClickListener.onListItemClicked(clickedIndex);
        }
    }

    void setDataList(List<GoogleSheetModel> list){
        this.dataList = list;
        notifyDataSetChanged();
    }


    interface ListItemOnClickListener{
        void onListItemClicked(int clickedItemIndex);
    }

}


