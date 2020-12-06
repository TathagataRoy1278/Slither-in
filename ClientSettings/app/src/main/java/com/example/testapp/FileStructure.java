package com.example.testapp;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class FileStructure {
    HashMap<String,LinkedList<String>> fs;
    private String tree = "";

    private void getHashMap(File prevFile,File root) {
        //Log.d("Test",root.getName());
        try {
            if (!root.isFile()) {

                String[] contents = root.list();
                //Log.d("Test",Arrays.toString(contents));

                for (String content : contents) {
                    //Log.d("Test",content);

                    getHashMap(root,new File(root, content));
                }
            }
            LinkedList<String> list = new LinkedList<String>();
            if (fs.get(prevFile.getName()) != null)
                list = fs.get(prevFile.getName());
            if(root.isFile())
                list.add(root.getAbsolutePath());
            else
                list.add(root.getName());
            if(prevFile.getName().equals(""))
                fs.put("/", list);

            fs.put(prevFile.getName(), list);

        }
        catch(Exception e)
        {}
    }

    public String pickleStructure(Client client,File startFile) throws Exception {
        fs = new HashMap<String,LinkedList<String>>();
        getHashMap(new File("/"),startFile);
        File structureFile = new File(Environment.getExternalStorageDirectory(),"FileStructure.data");
        if(!structureFile.exists()) {
            structureFile.createNewFile();
        }
        FileOutputStream fout = new FileOutputStream(structureFile);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(fs);
        Log.d("Test","written");

        Log.d("Test",client.uploadFile(new File(Environment.getExternalStorageDirectory(), "FileStructure.data")));

        return "";


    }
}
