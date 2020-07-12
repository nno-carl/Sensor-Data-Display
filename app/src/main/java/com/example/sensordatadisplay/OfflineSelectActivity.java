package com.example.sensordatadisplay;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OfflineSelectActivity extends ListActivity {
    ArrayList FileNames = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FileNames = Read_Write.getFileName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FileNames);
        setListAdapter(adapter);

        getActionBar().setTitle("Offline");
        getActionBar().setDisplayHomeAsUpEnabled(true);


    }
    //select file
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(OfflineSelectActivity.this, OfflineActivity.class);
        intent.putExtra(OfflineActivity.DATE, String.valueOf(FileNames.get(position)));
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
