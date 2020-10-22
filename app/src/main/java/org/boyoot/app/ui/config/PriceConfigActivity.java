package org.boyoot.app.ui.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityPriceConfigBinding;
import org.boyoot.app.model.Price;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PriceConfigActivity extends AppCompatActivity implements MaterialCardView.OnClickListener  {

    ActivityPriceConfigBinding binding;
    private PriceConfigViewModel viewModel;
    private Price mPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_price_config);
        viewModel = new ViewModelProvider(this).get(PriceConfigViewModel.class);
        viewModel.fetchCurrentPrice();
        binding.priceContent.setViewModel(viewModel);
        binding.priceContent.setLifecycleOwner(this);
        binding.priceContent.windowCv.setOnClickListener(this);
        binding.priceContent.splitCv.setOnClickListener(this);
        binding.priceContent.standCv.setOnClickListener(this);
        binding.priceContent.coverCv.setOnClickListener(this);
        binding.priceContent.concealedCv.setOnClickListener(this);
        binding.priceContent.offersCv.setOnClickListener(this);
        viewModel.getCurrentPrice().observe(this, new Observer<Price>() {
            @Override
            public void onChanged(Price price) {
                mPrice = price;
                binding.syncPriceProgressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.window_cv:
                showEditDialog(getString(R.string.window),0);
                break;
            case R.id.split_cv:
                showEditDialog(getString(R.string.split),1);
                break;
            case R.id.stand_cv:
                showEditDialog(getString(R.string.stand),2);
                break;
            case R.id.cover_Cv:
                showEditDialog(getString(R.string.cover),3);
                break;
            case R.id.concealed_cv:
                showEditDialog(getString(R.string.concealed),4);
                break;
            case R.id.offers_cv:
                showEditDialog(getString(R.string.offers),5);
                break;
            default:
                break;
        }

    }

    void showEditDialog(String unit,int type){
        binding.syncPriceProgressBar.setVisibility(View.VISIBLE);
        Dialog settingsDialog = new Dialog(this);
        Objects.requireNonNull(settingsDialog.getWindow()).requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.price_edit_dialog,null));
        settingsDialog.show();
        settingsDialog.getWindow().setBackgroundDrawable(null);
        TextView tv = settingsDialog.findViewById(R.id.unit_type_tv);
        tv.setText(unit);
        EditText editText = settingsDialog.findViewById(R.id.price_edit_text);

        settingsDialog.findViewById(R.id.confirm_price_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  newPrice =editText.getEditableText().toString();
                if (newPrice.isEmpty()){
                    editText.setError(getString(R.string.empty_message));
                }
                if (!newPrice.isEmpty()) {
                    switch (type) {
                        case 0:
                            mPrice.setWindow(Integer.parseInt(newPrice));
                            break;
                        case 1:
                            mPrice.setSplit(Integer.parseInt(newPrice));
                            break;
                        case 2:
                            mPrice.setStand(Integer.parseInt(newPrice));
                            break;
                        case 3:
                            mPrice.setCover(Integer.parseInt(newPrice));
                            break;
                        case 4:
                            mPrice.setConcealed(Integer.parseInt(newPrice));
                            break;
                        case 5:
                            mPrice.setOffers(Integer.parseInt(newPrice));
                            break;
                        default:
                            break;
                    }

                    viewModel.updateNewPrice(mPrice);
                    viewModel.fetchCurrentPrice();
                    settingsDialog.dismiss();
                }
            }
        });

        settingsDialog.findViewById(R.id.cancel_price_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.syncPriceProgressBar.setVisibility(View.INVISIBLE);
                settingsDialog.dismiss();
            }
        });
        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                binding.syncPriceProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
