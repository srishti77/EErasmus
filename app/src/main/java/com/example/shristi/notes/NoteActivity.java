package com.example.shristi.notes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteActivity extends AppCompatActivity {

    //so that we can access it from the other activity

    static ArrayList notes = new ArrayList();
    static ArrayList notesTitle = new ArrayList();
    static ArrayAdapter arrayAdapter;
    List<String> listOfContents = new ArrayList<String>();

    SaveNotes saveNotes = new SaveNotes();
    private int requestCode;
    private int grantResults[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //if you dont have required permissions ask for it (only required for API 23+)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);


            onRequestPermissionsResult(requestCode, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
        }
        if (saveNotes.getAllContentOfNotes() != null) {
            notes.clear();
            notes.addAll(saveNotes.getAllContentOfNotes());
        }

        if (saveNotes.getAllFileNames() != null) {
            notesTitle.clear();
            notesTitle.addAll(saveNotes.getAllFileNames());
        }

        final ListView listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesTitle);
        listView.setAdapter(arrayAdapter);


        saveNotes.getAllFileNames();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView v, View view, int position, long id) {
                Intent editIntent = new Intent(NoteActivity.this, EditNotes.class);
                editIntent.putExtra("Note Id", position);
                startActivity(editIntent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final PopupMenu popupMenu = new PopupMenu(NoteActivity.this, listView, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.delete) {
                            notesTitle.remove(position);

                            arrayAdapter.notifyDataSetChanged();
                        }

                        if (id == R.id.share) {

                            item = popupMenu.getMenu().findItem(R.id.share);

                            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

                            Intent myShareIntent = new Intent();
                            myShareIntent.setAction(Intent.ACTION_SEND);
                            myShareIntent.setType("text/plain");
                            myShareIntent.putExtra(Intent.EXTRA_TEXT, notes.get(position) + "");
                            shareActionProvider.setShareIntent(myShareIntent);


                        }


                        return true;
                    }
                });

                popupMenu.show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add) {
            notes.add("");
            notesTitle.add("");

            Intent editIntent = new Intent(NoteActivity.this, EditNotes.class);
            editIntent.putExtra("Note Id", notesTitle.size() - 1);
            startActivity(editIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("permission", "granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.uujm
                    Toast.makeText(NoteActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    //app cannot function without this permission for now so close it...
                    onDestroy();
                }
                return;
            }

            // other 'case' line to check fosr other
            // permissions this app might request
        }

    }
}