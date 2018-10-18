package com.haibeey.android.sticky.models;

import android.database.Cursor;
import android.util.Log;
import android.widget.ImageView;

public class NoteImage {

    private long id;
    private long NoteId;
    private byte[] ImageByte;

    public NoteImage(Cursor cursor){
        Log.e("cursorcount",cursor.getCount()+" "+cursor);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            id=cursor.getInt(0);
            NoteId=cursor.getInt(1);
            ImageByte=cursor.getBlob(2);
        }

    }
    public NoteImage(){

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNoteId(long noteId) {
        NoteId = noteId;
    }

    public long getId() {
        return id;
    }

    public long getNoteId() {
        return NoteId;
    }

    public byte[] getImageByte(){
        return ImageByte;
    }

    public void setImageByte(byte[] dByte){
        ImageByte=dByte;
    }

}
