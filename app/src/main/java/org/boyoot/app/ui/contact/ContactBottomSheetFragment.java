package org.boyoot.app.ui.contact;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ContactBottomSheetBinding;
import org.boyoot.app.model.Contact;

import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ContactBottomSheetFragment extends Fragment implements OnClickListener ,
        View.OnLongClickListener {

    private ContactBottomSheetBinding binding;
    private static final String contactIdKey = "contactId";
    private String contactId;
    private ContactBottomSheetViewModel viewModel;
    private Intent call;
    private String phone;
    private String location;
    private static final  int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private boolean isBottomExpended = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_bottom_sheet,container,false);
        viewModel = new ViewModelProvider(this).get(ContactBottomSheetViewModel.class);
        if (Objects.requireNonNull(getActivity()).getIntent().hasExtra(contactIdKey)){
            contactId = Objects.requireNonNull(getActivity()).getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactId);
        }
        FragmentContainerView bottomSheet = Objects.requireNonNull(getActivity()).findViewById(R.id.contact_bottom_sheet);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
        binding.expendBottomSheet.setBackground(getActivity().getDrawable(R.drawable.arrowbottom));
        binding.expendBottomSheet.setOnClickListener(v -> {
            if (!isBottomExpended) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                isBottomExpended = true;
            } else {
                isBottomExpended = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == 3){
                    isBottomExpended = true;
                }else if (i == 4){
                    isBottomExpended = false;
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v >0.5){
                    binding.expendBottomSheet.setBackground(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.close));
                    binding.expendBottomSheet.animate().rotation(90*v).start();
                }else{
                    binding.expendBottomSheet.setBackground(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.arrowbottom));
                    binding.expendBottomSheet.animate().rotation(180*v).start();
                    //binding.expendBottomSheet.animate().rotation(180).start();
                }
            }
        });

        viewModel.getContact().observe(getViewLifecycleOwner(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if (contact != null){
                    phone = contact.getPhone();
                    binding.sheetOptions.offerCheckBox.setChecked(contact.getWork().isOffer());
                    binding.sheetOptions.authTv.setText(contact.getAuth());
                    if (!contact.getPriority().equals("1")){
                        fillLocationField(contact.getCity().getLocationCode());
                        location = contact.getCity().getLocationCode();
                    }
                }
            }
        });
        binding.makeCall.setOnClickListener(this);
        binding.sendWhatsApp.setOnClickListener(this);
        binding.addAppointment.setOnClickListener(this);
        binding.sheetOptions.copyLocationItem.setOnClickListener(this);
        binding.sheetOptions.addContactItem.setOnClickListener(this);
        binding.sheetOptions.offerItem.setOnLongClickListener(this);
        binding.sheetOptions.workDelayItem.setOnLongClickListener(this);
        binding.sheetOptions.workDoneItem.setOnLongClickListener(this);
        binding.sheetOptions.workCanceledItem.setOnLongClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.make_call:
                makePhoneCall();
                break;
            case R.id.send_whats_app:
                sendToWhatsApp();
                break;
            case R.id.copy_location_item:
                copyLocation();
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.offer_item:
                binding.sheetOptions.offerCheckBox.setChecked(true);
                return false;
            default:
                return false;
        }

    }

    void fillAppointmentField(String s){
        binding.sheetOptions.appointmentTv.setText(s);
    }

    void fillCostField(String s){
        binding.sheetOptions.costTv.setText(s);
    }

    private void fillLocationField(String s){
        binding.sheetOptions.locationTv.setTextSize(16);
        binding.sheetOptions.locationTv.setText(s);
    }
    private void makePhoneCall(){
        call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+966"+ phone));
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            return;
        }
        startActivity(call);
    }
    private void sendToWhatsApp(){
        Uri uri = Uri.parse("https://wa.me/"+"966"+phone);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(sendIntent,"Choose App"));
    }
    private void copyLocation(){
        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("location", location);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Location copied",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivity(call);
                } else {

                }
                return;
            }

        }
    }
}
