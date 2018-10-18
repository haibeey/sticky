package com.haibeey.android.sticky;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibeey.android.sticky.DataBaseManager.DataBase;
import com.haibeey.android.sticky.main.MainActivity;
import com.haibeey.android.sticky.models.Note;

import java.util.ArrayList;

public class NoteService extends Service {
    private WindowManager windowManager;
    private ArrayList<Note> notes=new ArrayList<>();
    private ArrayList<View> views=new ArrayList<>();
    private final IBinder binder=new LocalBinder();
    boolean binded=false;


    public class LocalBinder extends Binder{
        public NoteService getService(){
            return NoteService.this;
        }
    }
    public NoteService() {
    }

    public void trigger(boolean hide){
        Log.e("istrigged",hide+" "+views+" "+views.size());
        if(hide){
            if(views!=null){
                for(View view:views){
                    view.setVisibility(View.GONE);
                }
            }
        }else{
            if(views!=null){
                for(View view:views){
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        binded=true;
        return binder;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate() {
        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                DataBase dataBase=new DataBase(getApplicationContext());
                notes=dataBase.queryAllNotes();
                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                int position=0;
                for(Note note:notes){
                    position+=1;
                    final View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.note_overlay,null,false);
                    TextView textView=view.findViewById(R.id.description_of_note);
                    textView.setText(note.getDescription());
                    ImageView remove=view.findViewById(R.id.remove_);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(views.contains(view)){
                                views.remove(view);
                                windowManager.removeView(view);
                            }
                        }
                    });
                    view.setOnTouchListener(new View.OnTouchListener() {
                        private int lastAction;
                        private int initialX;
                        private int initialY;
                        private float initialTouchX;
                        private float initialTouchY;
                        private WindowManager.LayoutParams params;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            params= (WindowManager.LayoutParams) view.getLayoutParams();
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    //remember the initial position.
                                    initialX = params.x;
                                    initialY = params.y;

                                    //get the touch location
                                    initialTouchX = event.getRawX();
                                    initialTouchY = event.getRawY();

                                    lastAction = event.getAction();
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    //As we implemented on touch listener with ACTION_MOVE,
                                    //we have to check if the previous action was ACTION_DOWN
                                    //to identify if the user clicked the view or not.
                                    if (lastAction == MotionEvent.ACTION_DOWN) {
                                        //Open the chat conversation click.
                                        Intent intent = new Intent(NoteService.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    lastAction = event.getAction();
                                    return true;
                                case MotionEvent.ACTION_MOVE:
                                    //Calculate the X and Y coordinates of the view.
                                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                    params.y = initialY + (int) (event.getRawY() - initialTouchY);

                                    //Update the layout with new X & Y coordinate
                                    windowManager.updateViewLayout(view, params);
                                    view.setLayoutParams(params);
                                    lastAction = event.getAction();
                                    return true;
                            }
                            return false;
                        }
                    });
                    if(binded){
                        view.setVisibility(View.GONE);
                    }
                    views.add(view);
                    windowManager.addView(view,GetParams(position));
                }

                super.onPostExecute(aBoolean);
            }
        }.execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(View view:views){
            windowManager.removeView(view);
        }

    }

    public  static void Trigger(){

    }
    private WindowManager.LayoutParams GetParams(int position){
        new WindowManager.LayoutParams(1,1,1,1,1);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                 200,
                                 200,
                                 WindowManager.LayoutParams.TYPE_PHONE,
                                 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSPARENT);

        params.gravity=Gravity.TOP|Gravity.START;
        params.x=(position%4)*200;

        params.y=((position/4)+1)*200+ (int)(Math.random()*10);;

        return  params;

    }
}
