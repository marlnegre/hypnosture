package com.google.hangouts.hypnosture;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cath on 2/24/18.
 */

public class PhotoManager {

    String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    final String MEDIA_PATH = baseDir + "/Android/data/com.google.hangouts.hypnosture";
    private ArrayList<String> songsList= new ArrayList<String>();
    private String mp3Pattern = ".jpg";
    private String pngPattern = ".png";

    public ArrayList<String> getPlayList() {
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {

//adding JPEG and PNG format Images
        if (song.getName().endsWith(mp3Pattern) || song.getName().endsWith(pngPattern)) {
            songsList.add(song.getPath());
        }
    }

}
