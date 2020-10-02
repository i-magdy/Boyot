package org.boyoot.app.ui.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);




        binding.paymentMethodCashCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.paymentMethodAtmCb.setChecked(false);
                    binding.paymentMethodTransferCb.setChecked(false);
                    binding.paymentMethodCashTv.setTypeface(null,Typeface.BOLD);
                    changeConfirmButtonState(true);
                }else {
                    binding.paymentMethodCashTv.setTypeface(Typeface.DEFAULT);
                    changeConfirmButtonState(false);
                }
            }
        });

        binding.paymentMethodAtmCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.paymentMethodCashCb.setChecked(false);
                    binding.paymentMethodTransferCb.setChecked(false);
                    binding.paymentMethodAtmTv.setTypeface(null,Typeface.BOLD);
                    changeConfirmButtonState(true);
                }else {
                    binding.paymentMethodAtmTv.setTypeface(Typeface.DEFAULT);
                    changeConfirmButtonState(false);
                }
            }
        });

        binding.paymentMethodTransferCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.paymentMethodAtmCb.setChecked(false);
                    binding.paymentMethodCashCb.setChecked(false);
                    binding.paymentMethodTransferTv.setTypeface(null,Typeface.BOLD);
                    changeConfirmButtonState(true);
                }else {
                    binding.paymentMethodTransferTv.setTypeface(Typeface.DEFAULT);
                    changeConfirmButtonState(false);
                }
            }
        });
    }

    private void changeConfirmButtonState(boolean b){
        if (b){
            binding.paymentMethodMaterialButton.setClickable(true);
            binding.paymentMethodMaterialButton.setRippleColorResource(R.color.colorTagDatePickedDark);
           binding.paymentMethodMaterialButton.setBackgroundColor(getColor(R.color.colorTagDatePicked));
        }else {
            binding.paymentMethodMaterialButton.setClickable(true);
            binding.paymentMethodMaterialButton.setRippleColorResource(R.color.colorRippleLight);
            binding.paymentMethodMaterialButton.setBackgroundColor(getColor(R.color.colorRippleLight));
        }

    }
}