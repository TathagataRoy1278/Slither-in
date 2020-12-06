package com.tatan.server;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends ListActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    static HashMap<String,LinkedList<String>> structure = new HashMap<String,LinkedList<String>>();
    private String path;
    File root = new File(Environment.getExternalStorageDirectory(),"Server");

    static Server out;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String rootPath = getIntent().getStringExtra("root");

        out = new Server();
        try {
            if(!exists(rootPath+"/FileStructure.data")){
                out.execute(rootPath+"/FileStructure.data");

                Thread.sleep(10000);

                structure = out.fs;}
            else
                structure = (HashMap)objectRead(rootPath+"/FileStructure.data");

        } catch (Exception e) {
            Log.d("Test",e.toString());
        }
        //Log.d("Test",tree);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else{}

        //Log.d("Test",tree);
        setTitle("File explorer");

        Log.d("Test","done");
        // Read all files sorted into the values-array
        List<String> values = new ArrayList<String>();
        values.add("");

        Log.d("Test","came");
        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);

    }



    private Object objectRead(String file) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(new FileInputStream(new File(root,file))).readObject();
    }

    private boolean exists(String str) {
        File f = new File(root,str);
        return f.exists();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);
        Log.d("Test","into");

        if (structure.get(filename)!=null) {
            Intent intent = new Intent(this, browse.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {
            Toast.makeText(this,"downloanding : "+ filename, Toast.LENGTH_LONG).show();
            Log.d("Test","is this a file?");
            try {
                out.downloadFile(filename);
            } catch (Exception e) {
                Log.d("Test", e.toString());
            }
        }
    }


    private String read(File path) throws IOException {
        FileReader in = new FileReader(path);
        BufferedReader br = new BufferedReader(in);
        String text = "";
        String full = "";
        while((text=br.readLine())!=null)
        {
            full+=text;
        }
        return full;
    }





    }



