package org.boyoot.app.ui.preparedContacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import org.boyoot.app.R;
import org.boyoot.app.database.Contacts;
import org.boyoot.app.ui.contact.ContactActivity;

import java.util.List;

public class PreparedContactsActivity extends AppCompatActivity implements PreparedContactsAdapter.ListItemOnClickListener{


    private PreparedContactsViewModel viewModel;
    private List<Contacts> contactsList;
    private static final String contactIdKey = "contactId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepared_contacts);
        viewModel = new ViewModelProvider(this).get(PreparedContactsViewModel.class);
        PreparedContactsAdapter adapter = new PreparedContactsAdapter(this,this);
        RecyclerView recyclerView = findViewById(R.id.prepared_contact_recycler);
        recyclerView.setAdapter(adapter);
        viewModel.getContacts().observe(this, new Observer<List<Contacts>>() {
            @Override
            public void onChanged(List<Contacts> contacts) {
                if (contacts.size() > 0){
                    adapter.setDataList(contacts);
                    contactsList = contacts;
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        if (contactsList.size() > 0) {
            Intent i = new Intent(this, ContactActivity.class);
            i.putExtra(contactIdKey, contactsList.get(clickedItemIndex).getId());
            startActivity(i);
        }
    }
}
