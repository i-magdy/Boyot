package org.boyoot.app.ui.locationNeeded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.List;

public class LocationNeededActivity extends AppCompatActivity implements LocationNeededAdapter.ListItemOnClickListener {

    private LocationNeededViewModel viewModel;
    private List<Contacts> contactsList;
    private static final String contactIdKey = "contactId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_needed);
        viewModel = new ViewModelProvider(this).get(LocationNeededViewModel.class);

        LocationNeededAdapter adapter = new LocationNeededAdapter(this,this);
        RecyclerView recyclerView = findViewById(R.id.location_needed_recycler);
        recyclerView.setAdapter(adapter);
        viewModel.getContacts().observe(this, new Observer<List<Contacts>>() {
            @Override
            public void onChanged(List<Contacts> contacts) {
                if (contacts != null) {
                    adapter.setDataList(contacts);
                    contactsList = contacts;
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Intent i = new Intent(this, ContactActivity.class);
        i.putExtra(contactIdKey,contactsList.get(clickedItemIndex).getId());
        startActivity(i);
    }
}
