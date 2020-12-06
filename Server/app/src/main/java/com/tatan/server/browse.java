package com.tatan.server;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class browse extends ListActivity {

    HashMap<String,LinkedList<String>> structure = new HashMap<String,LinkedList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String filename = intent.getStringExtra("path");
        structure = MainActivity.structure;
        LinkedList current = structure.get(filename);
        Collections.sort(current);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, current);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);


        if (structure.get(filename)!=null) {
            Intent intent = new Intent(this, browse.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {
            Toast.makeText(this,"downloanding : "+ filename, Toast.LENGTH_LONG).show();
            Log.d("Test","is this a file?");
            try {
                MainActivity.out.execute("download "+filename);
            } catch (Exception e) {
                Log.d("Test", e.toString());
            }
        }
    }
}
