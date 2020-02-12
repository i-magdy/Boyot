package org.boyoot.app.ui.config;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.boyoot.app.R;
import org.boyoot.app.model.CityCodeConfigModel;


public class ConfigsFragment extends Fragment {

    private ConfigsViewModel toolsViewModel;
    private  DatabaseReference mCityDatabase;

    private String city = null;
    private String code = null;
    private boolean isCityButtonClicked = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                new ViewModelProvider(this).get(ConfigsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configs, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        final LinearLayout configButtonsLayout = root.findViewById(R.id.buttons_config_layout);
        final LinearLayout cityViewLayout = root.findViewById(R.id.city_view_layout);
        final MaterialButton cityConfigButton = root.findViewById(R.id.city_config_button);
        final MaterialButton branchConfigButton = root.findViewById(R.id.branches_config_button);
        final EditText cityEditText = root.findViewById(R.id.city_edit_text);
        final EditText codeEditText = root.findViewById(R.id.code_edit_text);
        final FloatingActionButton fab = root.findViewById(R.id.config_add_fab);
        toolsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        mCityDatabase = FirebaseDatabase.getInstance().getReference().child("app_config").child("city");
        fab.hide();

       cityConfigButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               configButtonsLayout.setVisibility(View.GONE);
               cityViewLayout.setVisibility(View.VISIBLE);
               isCityButtonClicked = true;
               fab.show();
           }
       });

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               city = cityEditText.getEditableText().toString();
               code = codeEditText.getEditableText().toString();
               if (TextUtils.isEmpty(city)){
                   cityEditText.setError(getString(R.string.empty_message));
               }
               if (TextUtils.isEmpty(code)){
                   codeEditText.setError(getString(R.string.empty_message));
               }
               if (isCityButtonClicked){
                   if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(code)) {
                       mCityDatabase.push().setValue(new CityCodeConfigModel(city, code));
                       Snackbar snackbar  =Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                               .setAction("Action", null);
                       View view = snackbar.getView();
                       view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccentDark));
                       snackbar.show();
                       cityEditText.getText().clear();
                       codeEditText.getText().clear();
                   }
               }

           }
       });

        return root;
    }
}