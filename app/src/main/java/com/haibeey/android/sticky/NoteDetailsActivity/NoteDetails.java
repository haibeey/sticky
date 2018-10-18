package com.haibeey.android.sticky.NoteDetailsActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.haibeey.android.sticky.BaseActivity;
import com.haibeey.android.sticky.R;
import com.haibeey.android.sticky.models.Note;

public class NoteDetails extends BaseActivity {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
    }
}
