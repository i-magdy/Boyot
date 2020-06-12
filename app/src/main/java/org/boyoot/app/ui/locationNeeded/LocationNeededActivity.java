package org.boyoot.app.ui.locationNeeded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.List;

public class LocationNeededActivity extends AppCompatActivity implements LocationNeededAdapter.ListItemOnClickListener {

    private LocationNeededViewModel viewModel;

    private static final String contactIdKey = "contactId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_needed);
        viewModel = new ViewModelProvider(this).get(LocationNeededViewModel.class);
        Toolbar toolbar = findViewById(R.id.location_needed_toolbar);
        setSupportActionBar(toolbar);
        LocationNeededAdapter adapter = new LocationNeededAdapter(this,this);
        RecyclerView recyclerView = findViewById(R.id.location_needed_recycler);
        recyclerView.setAdapter(adapter);
        viewModel.getContacts().observe(this, adapter::setDataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClicked(String key) {
        Intent i = new Intent(this, ContactActivity.class);
        i.putExtra(contactIdKey,key);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.delete_contacts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_contact) {

            viewModel.deleteAllContacts();

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
