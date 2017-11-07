package com.example.shristi.notes;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class EditNotes extends AppCompatActivity {

    int noteId;
    SaveNotes saveNotes = new SaveNotes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        EditText editText = (EditText) findViewById(R.id.editText);
        EditText titleEditText = (EditText) findViewById(R.id.titleEditText);

        Intent i = getIntent();
        noteId = i.getIntExtra("Note Id", -1);

        if (noteId != -1) {

            editText.setText(NoteActivity.notes.get(noteId).toString());
            titleEditText.setText(NoteActivity.notesTitle.get(noteId).toString());

        }


        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("beforeTextChanged","called");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == ""){
                    NoteActivity.notesTitle.set(noteId,"Blank Title");
                }
                else{

                    NoteActivity.notesTitle.set(noteId,s);
                }

                NoteActivity.arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        titleEditText.addTextChangedListener(tw);





        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(s.equals("")){
                Log.i("Why is it", "empty");
            }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s == ""){
                    NoteActivity.notes.set(noteId,"Blank NOtes");
                }
                else{
                    NoteActivity.notes.set(noteId,s);
                }

                NoteActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.save){

            String fileName = NoteActivity.notesTitle.get(noteId).toString();
            if(fileName.equals("")){
                fileName= "Undefined"+noteId;
            }
            if(fileName.contains(".txt")){
                saveNotes.setFileName(fileName);
            }

            else{
                saveNotes.setFileName(fileName+".txt");
            }

            saveNotes.createDirectory();

            File fileName1 = saveNotes.createNotesFile();

            saveNotes.setNotesContent(NoteActivity.notes.get(noteId).toString());

            saveNotes.writeContentIntoFile();
            Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
