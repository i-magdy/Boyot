package org.boyoot.app.ui.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;


import org.boyoot.app.R;



import java.util.Objects;


public class ConfigsFragment extends Fragment {

    private ConfigsViewModel viewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.main_search_view).setVisibility(View.GONE);
        viewModel =
                new ViewModelProvider(this).get(ConfigsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configs, container, false);
        MaterialCardView price = root.findViewById(R.id.price_cv);
        MaterialCardView branch = root.findViewById(R.id.branch_cv);
        price.setOnClickListener(v -> startActivity(new Intent(getContext(),PriceConfigActivity.class)));

        branch.setOnClickListener(v -> startActivity(new Intent(getContext(),BranchesConfigActivity.class)));



        return root;
    }


}