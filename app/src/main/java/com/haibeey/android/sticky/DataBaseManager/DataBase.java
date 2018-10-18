package com.haibeey.android.sticky.DataBaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.haibeey.android.sticky.models.Note;
import com.haibeey.android.sticky.models.NoteImage;

import java.util.ArrayList;


public class DataBase extends SQLiteOpenHelper{
    private static final String TAG = DataBase.class.getName();
    private static final String DB_NAME = "colorvalue.db";
    public static String NotesTableName="NotesTable";
    public static String ImageTableName="ImageTable";
    private static final int DB_VERSION = 1;

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DataBase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateNotesTable="CREATE TABLE "+ NotesTableName+"("
                + Contract.ContractNotes.id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.ContractNotes.title+" TEXT NOT NULL, "
                + Contract.ContractNotes.description+" TEXT NOT NULL, "
                + Contract.ContractNotes.recurEveryday+" INTEGER, "
                + Contract.ContractNotes.year+" INTEGER, "
                + Contract.ContractNotes.day+" INTEGER, "
                + Contract.ContractNotes.hour+" INTEGER, "
                + Contract.ContractNotes.month+" INTEGER, "
                + Contract.ContractNotes.interval+" INTEGER, "
                + Contract.ContractNotes.week+" INTEGER, "
                + Contract.ContractNotes.email+" TEXT, "
                + Contract.ContractNotes.label+" TEXT,"
                + Contract.ContractNotes.show+" TEXT);";

        String queryCreateImageTable="CREATE TABLE "+ ImageTableName+"("
                + Contract.ContractImage.id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.ContractImage.NoteId+" INTEGER NOT NULL, "
                + Contract.ContractImage.ImageByte+" BLOB);";

        sqLiteDatabase.execSQL(queryCreateNotesTable);
        sqLiteDatabase.execSQL(queryCreateImageTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query1="DROP TABLE IF EXISTS "+  NotesTableName+";";
        String query2="DROP  TABLE IF EXISTS "+  ImageTableName+";";
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        onCreate(sqLiteDatabase);
    }


    public static String getColumnString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getColumnInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }
    public ArrayList<Note> queryAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from "+NotesTableName, null);

        ArrayList<Note> arrayList = new ArrayList<>();

        cur.moveToLast();
        while (!cur.isFirst() && cur.getCount()>0){
            arrayList.add(new Note(cur));
            cur.moveToPrevious();
        }
        if(cur.getCount()>0)
            arrayList.add(new Note(cur));
        return  arrayList;
    }

    public Note GetNote(long id){
    	SQLiteDatabase db=this.getReadableDatabase();
    	Cursor cur=db.rawQuery("Select * from "+NotesTableName+" where id ="+String.valueOf(id),null);
    	return new Note(cur);
    }

    public NoteImage GetImage(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("Select * from "+ImageTableName+" where "+Contract.ContractImage.NoteId+" ="+String.valueOf(id),null);
        return new NoteImage(cur);
    }

    public long InsertNote(Note dNote){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Contract.ContractNotes.day,dNote.getDay());
        values.put(Contract.ContractNotes.description,dNote.getDescription());
        values.put(Contract.ContractNotes.email,dNote.getEmail());
        values.put(Contract.ContractNotes.hour,dNote.getHour());
        values.put(Contract.ContractNotes.recurEveryday,dNote.isRecurEveryday());
        values.put(Contract.ContractNotes.interval,dNote.getInterval());
        values.put(Contract.ContractNotes.month,dNote.getMonth());
        values.put(Contract.ContractNotes.title,dNote.getTitle());
        values.put(Contract.ContractNotes.year,dNote.getYear());
        values.put(Contract.ContractNotes.week,dNote.getWeek());
        values.put(Contract.ContractNotes.label,dNote.getLabel());
        values.put(Contract.ContractNotes.show,dNote.isShow()?"1":"0");

        Log.wtf(TAG,"Data inserted of id "+dNote.getTitle());
        return db.insert(NotesTableName,null,values);

    }

    public void UpdateNote(Note dNote){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Contract.ContractNotes.day,dNote.getDay());
        values.put(Contract.ContractNotes.description,dNote.getDescription());
        values.put(Contract.ContractNotes.email,dNote.getEmail());
        values.put(Contract.ContractNotes.hour,dNote.getHour());
        values.put(Contract.ContractNotes.recurEveryday,dNote.isRecurEveryday());
        values.put(Contract.ContractNotes.interval,dNote.getInterval());
        values.put(Contract.ContractNotes.month,dNote.getMonth());
        values.put(Contract.ContractNotes.title,dNote.getTitle());
        values.put(Contract.ContractNotes.id,dNote.getId());
        values.put(Contract.ContractNotes.year,dNote.getYear());
        values.put(Contract.ContractNotes.week,dNote.getWeek());
        values.put(Contract.ContractNotes.label,dNote.getLabel());
        values.put(Contract.ContractNotes.show,dNote.isShow()?"1":"0");
        db.update(NotesTableName,values,Contract.ContractNotes.id+" =?", new String[] {String.valueOf(dNote.getId())});
        
        Log.wtf(TAG,"Data Updated of id "+dNote.getId());
    }

    public Long InsertImage(NoteImage noteImage){

    	SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Contract.ContractImage.NoteId,noteImage.getNoteId());
         values.put(Contract.ContractImage.ImageByte,noteImage.getImageByte());
        Log.wtf(TAG,"Data  insereted of id "+noteImage.getNoteId());
        return db.insert(ImageTableName,null,values);


    }

    public void UpdateImage(NoteImage noteImage){
    	SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Contract.ContractImage.id,noteImage.getId());
        values.put(Contract.ContractImage.NoteId,noteImage.getNoteId());
         values.put(Contract.ContractImage.ImageByte,noteImage.getImageByte());
        
        int d=db.update(NotesTableName, values, Contract.ContractImage.id+" =?", new String[] {String.valueOf(noteImage.getId())});
        Log.wtf(TAG,"Data Updated of id "+noteImage.getId());
    }
}
