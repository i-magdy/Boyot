package org.boyoot.app.ui.employees;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.boyoot.app.R;
import org.boyoot.app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private static final String PROFILE_EMAIL_KEY = "profile email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setVm(viewModel);
        binding.profilePageMainLayout.setVisibility(View.INVISIBLE);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel.getProfile(requireActivity().getIntent().getStringExtra(PROFILE_EMAIL_KEY));
        viewModel.getRole().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.profilePageMainLayout.setVisibility(View.VISIBLE);
                switch (s){
                    case "admin":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_admin));
                        showActiveState(true);
                        break;
                    case "moderator":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_moderator));
                        showActiveState(true);
                        break;
                    case "supervisor":
                        binding.accountTypeIv.setBackground(requireActivity().getDrawable(R.drawable.ic_supervisor_account));
                        showActiveState(true);
                        break;
                    case "worker":
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
                startActivity(new Intent(requireActivity(), AddTaskDialog.class));
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
}
