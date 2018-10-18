package com.haibeey.android.sticky.NoteAdd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.haibeey.android.sticky.BaseActivity;
import com.haibeey.android.sticky.DataBaseManager.DataBase;
import com.haibeey.android.sticky.NoteService;
import com.haibeey.android.sticky.R;
import com.haibeey.android.sticky.models.Note;
import com.haibeey.android.sticky.models.NoteImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;

public class NoteAddActivity extends BaseActivity {
    private ImageForNote imageForNote;
    private Mova mova;
    private ColorPallet colorPallet;
    private ImageView imageViewErase;
    private ImageView imageViewPencil;
    private ImageView imageViewBiggerPencil;
    //start of field to insert to the database
    private EditText Title;
    private EditText Description;
    private static boolean Recur;
    private static int Year;
    private static int Week;
    private static int Day;
    private static int Hour;
    private static int Interval;
    private String Email;
    private String Label;
    private static int Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageForNote=findViewById(R.id.image_view);
        mova=findViewById(R.id.d_mova);
        colorPallet=findViewById(R.id.d_collor_pallet);
        colorPallet.setMova(mova);
        imageForNote.setMova(mova);
        imageViewErase=findViewById(R.id.erase);
        imageViewErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForNote.Erase();
            }
        });
        imageViewBiggerPencil=findViewById(R.id.bigger_pencil);
        imageViewBiggerPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForNote.setStrokeWidth(25);
            }
        });
        imageViewPencil=findViewById(R.id.pencil);
        imageViewPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageForNote.setStrokeWidth(10);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        MenuItem item = menu.findItem(R.id.action_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        setSpinner(spinner,R.array.interval);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            HandleAddButton();
        }
        else if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void HandleAddButton() {
        final EditText editTextTitle=findViewById(R.id.title);
        final EditText editTextLabel=findViewById(R.id.label);
        final EditText editTextDescription=findViewById(R.id.description);

        if((TextUtils.isEmpty(editTextDescription.getText())||
                TextUtils.isEmpty(editTextLabel.getText()) ||
                TextUtils.isEmpty(editTextTitle.getText()))){
            Snackbar.make(imageForNote,"String Title or label or description field is empty",Snackbar.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                DataBase dataBase=new DataBase(NoteAddActivity.this.getApplicationContext());
                Note note=new Note();
                note.setDay(Day);
                note.setDescription(editTextDescription.getText().toString());
                note.setTitle(editTextTitle.getText().toString());
                note.setLabel(editTextLabel.getText().toString());
                note.setHour(Hour);
                note.setInterval(Interval);
                note.setMonth(Month);
                note.setYear(Year);
                note.setRecurEveryday(Recur);

                long id =dataBase.InsertNote(note);
                NoteImage noteImage=new NoteImage();
                noteImage.setNoteId(id);
                byte[] bytes=GetIMageNoteBitmap();
                if(bytes!=null){
                    noteImage.setImageByte(GetIMageNoteBitmap());
                    long idImage=dataBase.InsertImage(noteImage);
                    return id>-1 && idImage>-1;
                }else{
                    return id>-1;
                }

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    Toast.makeText(NoteAddActivity.this,"Data Added",Toast.LENGTH_SHORT).show();
                    startService(new Intent(NoteAddActivity.this,NoteService.class));
                    finish();
                }else{
                    Snackbar.make(imageForNote,"Something went wrong",Snackbar.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    public void  AddImage(View view){
        Intent I = new Intent();
        I.setType("image/*");
        I.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(I,"select image"), 1);
    }

    public void AddRemider(View view){
        new DatePicker().show(getFragmentManager(),"data");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            Toast.makeText(this,"No Data Selected",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case 1:
                Uri ImageUri = data.getData();
                String[] location = {MediaStore.Images.Media.DATA};
                assert ImageUri != null;
                Cursor cursor = getContentResolver().query(ImageUri, location, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                String FilePath = cursor.getString(cursor.getColumnIndex(location[0]));
                Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
                imageForNote.setImageBitmap(bitmap);
                setVisibilityOfImage(true);
                cursor.close();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            Hour=hourOfDay;
            new RecurDialogue().show(getFragmentManager(),"recurDialog");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
    }

    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener  {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Year=year;Month=month;Day=dayOfMonth;
            new TimePicker().show(getFragmentManager(),"time");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }
    public static class RecurDialogue extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.recur_dialogue,container,false);
            getDialog().setTitle("Should Recur Notification?");
            Button buttonYes=view.findViewById(R.id.recur_yes);
            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recur=true;
                    getDialog().dismiss();
                }
            });
            Button buttonNo=view.findViewById(R.id.recur_no);
            buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recur=false;
                    getDialog().dismiss();
                }
            });
            return view;
        }
    }
    private void setVisibilityOfImage(boolean value){
        imageForNote.setVisibility(value?View.VISIBLE:View.GONE);
        mova.setVisibility(value?View.VISIBLE:View.GONE);
        colorPallet.setVisibility(value?View.VISIBLE:View.GONE);
        findViewById(R.id.style_image).setVisibility(value?View.VISIBLE:View.GONE);
    }

    private void setSpinner(Spinner spinner, int stringResource){

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,stringResource,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Interval=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private byte[] GetIMageNoteBitmap(){
        imageForNote.buildDrawingCache();
        Bitmap bitmap=imageForNote.getDrawingCache();
        if(bitmap==null){
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
