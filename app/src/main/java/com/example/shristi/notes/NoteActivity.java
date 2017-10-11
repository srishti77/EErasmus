package com.example.shristi.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NoteActivity extends AppCompatActivity {

    //so that we can access it from the other activity

    static ArrayList notes = new ArrayList();
    static  ArrayAdapter arrayAdapter;
    static Set set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        final ListView listView = (ListView) findViewById(R.id.listView);
        final SharedPreferences sharedPreferences =  this.getSharedPreferences("com.example.shristi.notes", Context.MODE_PRIVATE);

        set = sharedPreferences.getStringSet("notes", null);

        notes.clear();
        if(set != null){


            notes.addAll(set);
        }

        else {

            notes.add("Example note");
            set = new HashSet();
            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", set).apply();
        }


         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView v, View view, int position, long id) {
                Intent editIntent = new Intent(NoteActivity.this, EditNotes.class);
                editIntent.putExtra("Note Id", position);
                startActivity(editIntent);
            }
        });

        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final PopupMenu popupMenu = new PopupMenu(NoteActivity.this, listView, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if(id == R.id.delete){
                            notes.remove(position);
                            SharedPreferences sharedPreferences =  NoteActivity.this.getSharedPreferences("com.example.shristi.notes", Context.MODE_PRIVATE);

                            if(set == null){
                                set = new HashSet();

                            }
                            else
                            {
                                set.clear();
                            }

                            set.addAll(notes);
                            sharedPreferences.edit().remove("notes").apply();
                            sharedPreferences.edit().putStringSet("notes", set).apply();
                            arrayAdapter.notifyDataSetChanged();
                        }

                        if(id == R.id.share){

                            item = popupMenu.getMenu().findItem(R.id.share);

                            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

                            Intent myShareIntent =  new Intent(Intent.ACTION_SEND);
                            myShareIntent.setType("text/plain");
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

        if(id == R.id.add){
                notes.add("");
            //because the user want an empty note
                SharedPreferences sharedPreferences =  this.getSharedPreferences("com.example.shristi.notes", Context.MODE_PRIVATE);

                if(set == null){
                    set = new HashSet();

                }
                else
                {
                    set.clear();
                }

                set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", set).apply();

            Intent editIntent = new Intent(NoteActivity.this, EditNotes.class);
            editIntent.putExtra("Note Id", notes.size()-1);
            startActivity(editIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
