package org.boyoot.app.ui.contact;

import android.Manifest;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ContactBottomSheetBinding;
import org.boyoot.app.model.Contact;
import org.boyoot.app.model.JobAdded;
import org.boyoot.app.ui.jobs.JobsListActivity;
import org.jetbrains.annotations.NotNull;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ContactBottomSheetFragment extends Fragment implements OnClickListener ,
        View.OnLongClickListener {

    private ContactBottomSheetBinding binding;

    private String contactId;
    private ContactBottomSheetViewModel viewModel;
    private Intent call;
    private Contact mContact;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private static final  int MY_PERMISSIONS_REQUEST_MAKE_PHONE_CALL = 1;
    private static final  int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final String contactIdKey = "contactId";
    private static final String CONTACTS_PATH = "contacts";
    //private static final String CONTACT_APPOINTMENT_KEY="contact appointment key";

    private boolean isBottomExpended = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_bottom_sheet,container,false);
        viewModel = new ViewModelProvider(this).get(ContactBottomSheetViewModel.class);
        if (requireActivity().getIntent().hasExtra(contactIdKey)){
            contactId = requireActivity().getIntent().getStringExtra(contactIdKey);
            viewModel.fetchContact(contactId);
        }
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        FragmentContainerView bottomSheet = requireActivity().findViewById(R.id.contact_bottom_sheet);
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
                    binding.expendBottomSheet.setBackground(requireActivity().getDrawable(R.drawable.close));
                    binding.expendBottomSheet.animate().rotation(90*v).start();
                }else{
                    binding.expendBottomSheet.setBackground(requireActivity().getDrawable(R.drawable.arrowbottom));
                    binding.expendBottomSheet.animate().rotation(180*v).start();
                    //binding.expendBottomSheet.animate().rotation(180).start();
                }
            }
        });

        viewModel.getContact().observe(getViewLifecycleOwner(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if (contact != null){
                    mContact = contact;
                    binding.sheetOptions.authTv.setText(contact.getAuth());
                    if (!contact.getPriority().equals("1")){
                        fillLocationField(contact.getCity().getLocationCode());
                    }

                    checkAppointment(contact);
                    /**/
                    /*switch (contact.getPriority()){
                        case "6":
                            binding.sheetOptions.workDelayRadio.setChecked(true);
                            binding.sheetOptions.workDoneCheckBox.setChecked(false);
                            binding.sheetOptions.workCanceledRadio.setChecked(false);
                            break;
                        case "7":
                            binding.sheetOptions.workDoneCheckBox.setChecked(true);
                            binding.sheetOptions.workDelayRadio.setChecked(false);
                            binding.sheetOptions.workCanceledRadio.setChecked(false);
                            break;
                        case "9":
                            binding.sheetOptions.workCanceledRadio.setChecked(true);
                            binding.sheetOptions.workDoneCheckBox.setChecked(false);
                            binding.sheetOptions.workDelayRadio.setChecked(false);
                            break;
                        default:
                            binding.sheetOptions.workCanceledRadio.setChecked(false);
                            binding.sheetOptions.workDoneCheckBox.setChecked(false);
                            binding.sheetOptions.workDelayRadio.setChecked(false);
                            break;

                    }*/
                   /*if (contact.getWork().isOffer()){
                       binding.sheetOptions.offerCheckBox.setChecked(true);
                   }else {
                       binding.sheetOptions.offerCheckBox.setChecked(false);
                   }*/
                }
            }
        });
        binding.makeCall.setOnClickListener(this);
        binding.sendWhatsApp.setOnClickListener(this);
        binding.addAppointment.setOnClickListener(this);
        binding.sheetOptions.copyLocationItem.setOnClickListener(this);
        binding.sheetOptions.addContactItem.setOnClickListener(this);
        //binding.sheetOptions.offerItem.setOnLongClickListener(this);
      //  binding.sheetOptions.workDelayItem.setOnLongClickListener(this);
      //  binding.sheetOptions.workDoneItem.setOnLongClickListener(this);
      //  binding.sheetOptions.workCanceledItem.setOnLongClickListener(this);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_appointment:
                if (!mContact.getPriority().equals("1")) {
                    Intent i = new Intent(getContext(), JobsListActivity.class);
                    i.putExtra(contactIdKey, mContact.getContactId());
                    startActivity(i);
                }else{
                    Toast.makeText(getContext(),requireActivity().getString(R.string.location_require),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.make_call:
                makePhoneCall();
                break;
            case R.id.send_whats_app:
                sendToWhatsApp();
                break;
            case R.id.copy_location_item:
                copyLocation();
                break;
            case R.id.add_contact_item:
               Log.i("CONTACT",contactExists(requireContext(),mContact.getPhone())+"");
               addToContacts();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
/*
        switch (v.getId()){
            case R.id.offer_item:
                if (binding.sheetOptions.offerCheckBox.isChecked()){
                    changeOfferState(false);
                }else {
                    changeOfferState(true);
                }
                return false;
            case R.id.work_canceled_item:
                if (binding.sheetOptions.workCanceledRadio.isChecked()){
                    changeCancelState(false);
                }else{
                    changeCancelState(true);
                }

                return false;
            case R.id.work_delay_item:
                if (binding.sheetOptions.workDelayRadio.isChecked()){
                    changeDelayedState(false);
                }else{
                    changeDelayedState(true);
                }
                return false;
            case R.id.work_done_item:
                if (binding.sheetOptions.workDoneCheckBox.isChecked()){

                    changeDoneState(false);
                }else{
                    changeDoneState(true);
                }
                return false;
            default:
                return false;
        }
*/
return false;
    }

    private void checkAppointment(Contact contact){
        if (contact.getJobAdded() == null){
            Log.i("APPOINTMENT_TEST","checked");
            updateFakeJobAdded();
            binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
            contact.setJobAdded(new JobAdded(null,false,null));
        }else {
            Log.i("APPOINTMENT_TEST","NOT null");
            if (contact.getJobAdded().getDate() != null){
                if (contact.getRegistrationDate().equals(contact.getJobAdded().getDate())){
                    binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment_approved));
                }else {
                    binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
                    if (contact.getCity().getLocationCode() != null){
                        updateJobAddedForNewJob();
                    }
                }
            }else{
                binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
            }
        }
        if (contact.getJobAdded().isAdded()){
            binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment_approved));
        }else {
            binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
        }
    }
    private void updateFakeJobAdded(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update ("jobAdded.date",null,
                        "jobAdded.added",false).addOnSuccessListener(aVoid -> {
                            binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
                        });
    }
    private void updateJobAddedForNewJob(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update ("priority","3",
                        "jobAdded.date",null,
                        "jobAdded.appointment",null,
                        "jobAdded.added",false).addOnSuccessListener(aVoid -> {
            binding.addAppointment.setBackground(requireActivity().getDrawable(R.drawable.appointment));
        });
    }
    /*private void changeCancelState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="9";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
                    binding.sheetOptions.workCanceledRadio.setChecked(b);
                    author(contactId,currentUser.getEmail());
        });
    }*/

  /*  private void changeDelayedState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="6";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.workDelayRadio.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }
    private void changeDoneState(boolean b){
        String p = "3";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (b){
            p="7";
        }
        db.collection(CONTACTS_PATH).document(contactId)
                .update("priority",p).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.workDoneCheckBox.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }*/

   /* private void changeOfferState(boolean b){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update("work.offer",b).addOnSuccessListener(aVoid -> {
            binding.sheetOptions.offerCheckBox.setChecked(b);
            author(contactId,currentUser.getEmail());
        });
    }
    private void fillAppointmentField(String s){
        binding.sheetOptions.appointmentTv.setText(s);
    }

    void fillCostField(String s){
        binding.sheetOptions.costTv.setText(s);
    }*/

    private void fillLocationField(String s){
        binding.sheetOptions.locationTv.setTextSize(16);
        binding.sheetOptions.locationTv.setText(s);
    }
    private void makePhoneCall(){
        call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+966"+ mContact.getPhone()));
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_MAKE_PHONE_CALL);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_MAKE_PHONE_CALL);
            }
            return;
        }
        startActivity(call);
    }
    private void sendToWhatsApp(){
        Uri uri = Uri.parse("https://wa.me/"+"966"+mContact.getPhone());
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(sendIntent,"Choose App"));
    }
    private void copyLocation(){
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("location", mContact.getCity().getLocationCode());
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Location copied",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_MAKE_PHONE_CALL) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                startActivity(call);
            }
            if (grantResults.length > 0
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the

                addToContacts();
            }
            if (grantResults.length > 0
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the

                addToContacts();
            }
            return;
        }
    }

    private void addToContacts(){
    }

    private boolean contactExists(Context context, String number) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            return false;
        }
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }
    private void author(String contactId,String user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONTACTS_PATH).document(contactId)
                .update("auth",user).addOnSuccessListener(aVoid -> {
        });
    }
}
