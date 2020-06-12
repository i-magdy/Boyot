package org.boyoot.app.ui.preparedContacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.List;

public class PreparedContactsActivity extends AppCompatActivity implements PreparedContactsAdapter.ListItemOnClickListener{


    private PreparedContactsViewModel viewModel;
    private static final String contactIdKey = "contactId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepared_contacts);
        viewModel = new ViewModelProvider(this).get(PreparedContactsViewModel.class);
        Toolbar toolbar = findViewById(R.id.prepared_contacts_toolbar);
        setSupportActionBar(toolbar);
        PreparedContactsAdapter adapter = new PreparedContactsAdapter(this,this);
        RecyclerView recyclerView = findViewById(R.id.prepared_contact_recycler);
        recyclerView.setAdapter(adapter);
        viewModel.getContacts().observe(this, adapter::setDataList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClicked(String contactKey) {
            Intent i = new Intent(this, ContactActivity.class);
            i.putExtra(contactIdKey,contactKey);
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
