package org.boyoot.app.ui.config;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.model.Car;

import java.util.ArrayList;
import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {

    private List<Car> list;
    private OnItemClickListener listener;
    private Context context;
    private String BRANCH;
    private static final String BRANCHES_PATH="branches";


    public CarsAdapter(Context context,OnItemClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.workers.setText(String.valueOf(list.get(position).getWorker()));
        holder.deleteListener(position);
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0){
            return 0;
        }else {
            return list.size();
        }
    }


    class CarsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView workers;
        ImageView deleteItem;

        public CarsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.car_title_tv);
            workers = itemView.findViewById(R.id.worker_tv);
            deleteItem = itemView.findViewById(R.id.delete_car_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            listener.onItemClickListener(index);

        }

        void deleteListener(int i){
            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Car car = list.get(i);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.message_delete_car))
                            .setTitle(context.getString(R.string.title_delete_car));

                    builder.setPositiveButton(context.getString(R.string.confirm_delete_car), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference ref = db.collection(BRANCHES_PATH).document(BRANCH);
                            ref.update("cars",FieldValue.arrayRemove(car))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            listener.updateCarList();
                                            dialog.dismiss();

                                        }
                                    });

                        }
                    });
                    builder.setNegativeButton(context.getString(R.string.cancel_delete_car), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    interface OnItemClickListener{
        void onItemClickListener(int index);
        void updateCarList();
    }

    public void setList(List<Car> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setBranch(String BRANCH) {
        this.BRANCH = BRANCH;
    }
}
