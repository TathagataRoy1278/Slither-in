package com.tatan.server;

import android.util.Log;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.DropBoxManager;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class Server extends AsyncTask<String,Void,String>
{
    HashMap<String, LinkedList<String>> fs;
    private final String ACCESS_TOKEN = "X_TUKgQ-0WAAAAAAAAAAC5_7LDj8UEFAOfK4NGbpCAlHf1aAo61aVYf6XTFb-h4Y";
    DbxClientV2 client;
    DbxUserFilesRequests metadata;
    File root;
    public Server()
    {
        try {
            root = new File(Environment.getExternalStorageDirectory(),"Server");
            if(!root.exists())
                root.mkdirs();
            Log.d("Test", "hello");
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            client = new DbxClientV2(config, ACCESS_TOKEN);
            metadata = client.files();
        }
        catch(Exception e)
        {
            Log.d("Test","From Server()\n"+e.toString());
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = strings[0];
        if(!path.startsWith("download")) {
            Log.d("Test", "in server");
            File gpxfile = new File(root, path);

            try {
                gpxfile.getParentFile().mkdirs();
                gpxfile.createNewFile();
            } catch (IOException e) {

                Log.d("Test", gpxfile.getAbsolutePath() + "\t" + e.toString());
            }

            //Log.d("Test",root.toString());
            //Log.d("Test",Environment.getExternalStorageDirectory().getAbsolutePath());
            try {
                OutputStream outputStream = new FileOutputStream(gpxfile);
                FileMetadata metadata = client.files()
                        .downloadBuilder("/" + path)//the file is created just to get the name of the path
                        .download(outputStream);
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(root, gpxfile.getName())));
                fs = (HashMap) in.readObject();
            } catch (Exception e) {
                Log.d("Test", "From Downloading:\t" + e.toString());
            }

        }
        else
        {
            try {
                Log.d("Test",downloadFile(path));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public void saveAs(File root,String path)throws Exception
    {
        File file = new File(root,path);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            FileMetadata metadata = client.files()
                    .downloadBuilder("/" + path)//the file is created just to get the name of the path
                    .download(outputStream);}
        catch(Exception e)
        {}
        Log.d("Test","downloaded");

    }
    private String read(File path) throws IOException {

        FileReader in = new FileReader(path);

        BufferedReader br = new BufferedReader(in);
        String text = "";
        String full = "";

        while((text=br.readLine())!=null)
        {

            full+=text+"\n";

        }

        return full;
    }
    public PrintWriter getOutFile(File file)throws IOException
    {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter outFile = new PrintWriter(bw);
        return outFile;
    }

    public boolean exists(String path) {
        try {
            Metadata tmp = metadata.getMetadata(path);
            if(tmp!=null)
                return true;
            else
                return false;
        }
        catch(Exception e)
        {
            return false;
        }
        }

        private String uploadFile(File localFile) throws DbxException {
        if(exists("/"+localFile.getName()))
            client.files().deleteV2("/"+localFile.getName());


        try (InputStream in = new FileInputStream(localFile)) {

            FileMetadata metadata = client.files().uploadBuilder("/"+localFile.getName())
                .withMode(WriteMode.ADD)
                .withClientModified(new Date(localFile.lastModified()))
                .uploadAndFinish(in);

            return localFile.getName()+" uploaded";
        }  catch (DbxException ex) {
            return ("Error uploading to Dropbox: " + ex.getMessage());
        } catch (IOException ex) {
            return ("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        }
    }

    public String downloadFile(String command) throws Exception {
        sendCommand(command);
        while(true) {
            Log.d("Test",command.substring(command.lastIndexOf('/') + 1));
            if(exists("/"+command.substring(command.lastIndexOf('/') + 1)))
            {

                Log.d("Test","downloading");
                saveAs(root, command.substring(command.lastIndexOf('/') + 1));
                return "downloaded";
            }
            continue;

        }

    }

    private String sendCommand(String s) throws IOException, DbxException {
        FileWriter fout = new FileWriter(new File(root,"command.txt"));
        BufferedWriter bout = new BufferedWriter(fout);
        PrintWriter out = new PrintWriter(bout);

        out.print(s);
        out.flush();
        out.close();
        return uploadFile(new File(root, "command.txt"));
    }
}
