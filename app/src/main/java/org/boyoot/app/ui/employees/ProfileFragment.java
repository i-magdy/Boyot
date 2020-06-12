package org.boyoot.app.ui.employees;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentProfileBinding;
import org.boyoot.app.model.UserProfileModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private static final String PROFILE_EMAIL_KEY = "profile email";
    private static final String USERS_PATH="users";
    private String email;
    private String PROFILE_ID;
    private String role;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setVm(viewModel);
        binding.profilePageMainLayout.setVisibility(View.INVISIBLE);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        email = requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY);
        viewModel.getProfile(email);
        viewModel.getProfile().observe(getViewLifecycleOwner(), new Observer<UserProfileModel>() {
            @Override
            public void onChanged(UserProfileModel profileModel) {
                PROFILE_ID =profileModel.getId();
            }
        });
        viewModel.getRole().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.profilePageMainLayout.setVisibility(View.VISIBLE);
                role = s;
                switch (s){
                    case "Admin":
                    case "Manager":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_admin));
                        showActiveState(true);
                        break;
                    case "Moderator":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_moderator));
                        showActiveState(true);
                        break;
                    case "Supervisor":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_supervisor_account));
                        showActiveState(true);
                        break;
                    case "Worker":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_worker));
                        showActiveState(true);
                        break;
                    default:
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_new_tag));
                        showActiveState(false);
                        break;
                }
            }
        });
        binding.activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeactivateDialog(role);
            }
        });

        binding.editProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditProfileDialog.class);
                i.putExtra(PROFILE_EMAIL_KEY,email);
                startActivity(i);
            }
        });
        return binding.getRoot();
    }

    private void showActiveState(boolean isActive){
        if (isActive){
            binding.activeStateCv.setCardBackgroundColor(requireActivity().getColor(R.color.colorTagDateApproved));
            binding.activeStateTv.setText(requireActivity().getString(R.string.activated_state));
            binding.activateButton.setBackgroundTintList(requireActivity().getColorStateList(R.color.colorSecondary));
            binding.activateButton.setRippleColor(requireActivity().getColorStateList(R.color.colorSecondaryDark));
            binding.activateButton.setText(requireActivity().getString(R.string.deactivate_button));

        }else{
            binding.activeStateCv.setCardBackgroundColor(requireActivity().getColor(R.color.colorSecondary));
            binding.activeStateTv.setText(requireActivity().getString(R.string.deactivated_state));
            binding.activateButton.setBackgroundTintList(requireActivity().getColorStateList(R.color.colorTagDateApproved));
            binding.activateButton.setRippleColor(requireActivity().getColorStateList(R.color.colorApprovedDark));
            binding.activateButton.setText(requireActivity().getString(R.string.active_button));
        }
    }


    private void showDeactivateDialog(String role){
        if (!role.equals("User")){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage(getString(R.string.message_deactivate_account))
                    .setTitle(getString(R.string.title_deactivate_account));

            builder.setPositiveButton(getString(R.string.confirm_deactivate_account), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref = db.collection(USERS_PATH).document(PROFILE_ID);
                    ref.update("role","User")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    viewModel.getProfile(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
                                    dialog.dismiss();
                                }
                            });

                }
            });
            builder.setNegativeButton(getString(R.string.canecl_deactivate_account), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        viewModel.getProfile(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
    }
}
