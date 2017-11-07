package com.example.shristi.notes;

import android.os.Environment;
import android.preference.ListPreference;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shristi on 11/7/2017.
 */

public class SaveNotes {

    String directoryName = "MobileSystemE";
    String fileName;
    String notesContent;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getNotesContent() {
        return notesContent;
    }

    public void setNotesContent(String notesContent) {
        this.notesContent = notesContent;
    }

    public void createDirectory() {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MobileSystemE");
                Log.i("dirName",directory.getName());
                if (!directory.exists()) {
                    directory.mkdir();
                    Log.i("make directory", "mkdir");
                }
                Log.i("Directory", "Created");

            }
        } catch (Exception e) {
            Log.i("Directory", "Not Created");
            e.printStackTrace();
        }
    }
    public File createNotesFile() {
        File notesFile = null;
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                 notesFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .getAbsolutePath() + "/MobileSystemE/" + getFileName());
                if (!notesFile.exists()) {
                    notesFile.createNewFile();
                }

            }
            Log.i("File", "Created");
        }
        catch (Exception e) {
            Log.i("File", "Not Created");
            e.printStackTrace();
        }
        return notesFile;
    }

    public  void writeContentIntoFile() {

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                FileOutputStream fileOutputStream = new FileOutputStream(createNotesFile());
                OutputStreamWriter out = new OutputStreamWriter(fileOutputStream);

                Log.i("NotesCC",getNotesContent()+"");
                out.write(getNotesContent());
                out.flush();
                out.close();

            }
            Log.i("Written", "Into file");
        } catch (Exception e) {
            Log.i("Could Not", "Write");
            e.printStackTrace();

        }

    }

    public List getAllFileNames() {
        List<String> list = new ArrayList<String>();
        list.clear();
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MobileSystemE");
                for(File file: directory.listFiles()){
                    if(file.isFile()){
                        list.add(file.getName());
                        Log.i("FileName:::",file.getName());
                    }
                }
            }


        } catch (Exception e) {

        }
        return  list;
    }


    public List getAllContentOfNotes(){
        List<String> listOfContents = new ArrayList<String>();
        List<String> listOfFiles = new ArrayList<String>();

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                listOfFiles = getAllFileNames();
                for(int i=0; i<listOfFiles.size();i++){
                    Log.i("Here I am in","getAllContentOfNotes");
                    setFileName(listOfFiles.get(i));
                    FileInputStream fileInputStream = new FileInputStream(createNotesFile());
                    StringBuffer stringBuffer = new StringBuffer();
                    int ch;
                    while((ch = fileInputStream.read()) != -1){
                        stringBuffer.append((char)ch);
                    }
                    listOfContents.add(stringBuffer.toString());

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listOfContents;
    }
}
