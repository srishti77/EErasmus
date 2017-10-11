package com.example.shristi.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditNotes extends AppCompatActivity implements TextWatcher {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        EditText editText = (EditText) findViewById(R.id.editText);
        //to get the content from the list view

        Intent i = getIntent();
         noteId = i.getIntExtra("Note Id", -1);

        if(noteId !=-1){
            //get the content from the notes NoteActivity.notes.get(noteId);
            editText.setText(NoteActivity.notes.get(noteId).toString());

        }

        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Update the arraylist item
        //s is the charsequence that is the text after editing the notes
        if(s == ""){
            NoteActivity.notes.set(noteId,"Blank NOtes");
        }
        else{
            NoteActivity.notes.set(noteId,s);
        }

        NoteActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences =  this.getSharedPreferences("com.example.shristi.notes", Context.MODE_PRIVATE);

        if(NoteActivity.set == null){
            NoteActivity.set = new HashSet();

        }
        else
        {
            NoteActivity.set.clear();
        }

        NoteActivity.set.addAll(NoteActivity.notes);
        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes", NoteActivity.set).apply();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
