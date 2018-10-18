package com.haibeey.android.sticky.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibeey.android.sticky.DataBaseManager.DataBase;
import com.haibeey.android.sticky.R;
import com.haibeey.android.sticky.models.Note;
import com.haibeey.android.sticky.models.NoteImage;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class NoteAdapter  extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private ArrayList<Note> notes=new ArrayList<>();
    private DataBase dataBase;
    private Activity activity;

    public  NoteAdapter(ArrayList<Note> notes,Activity activity){
        this.notes=notes;
        this.activity=activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView NoteImage;
        private ImageView NoteDialogueImage;
        private TextView textViewTitle;
        private TextView textViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            NoteImage=itemView.findViewById(R.id.note_image);
            NoteDialogueImage=itemView.findViewById(R.id.note_menu);
            textViewTitle=itemView.findViewById(R.id.note_title);
            textViewDescription=itemView.findViewById(R.id.note_description);
        }

        @SuppressLint("StaticFieldLeak")
        public void bind(final int position){
            textViewDescription.setText(notes.get(position).getDescription());
            textViewTitle.setText(notes.get(position).getTitle());
            NoteDialogueImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new NoteDialogue().show(activity.getFragmentManager(),String.valueOf(notes.get(position).getId()));
                }
            });
            new AsyncTask<Void, Void, NoteImage>() {
                @Override
                protected NoteImage doInBackground(Void... voids) {
                    if(dataBase==null){
                        dataBase=new DataBase(textViewDescription.getContext());
                    }

                    return dataBase.GetImage(notes.get(position).getId());
                }

                @Override
                protected void onPostExecute(NoteImage noteImage) {
                    super.onPostExecute(noteImage);
                    if(noteImage.getImageByte()!=null){
                        Bitmap bitmap= Bitmap.createBitmap(textViewDescription.getWidth(),200, Bitmap.Config.ARGB_8888);
                        ByteBuffer byteBuffer=ByteBuffer.wrap(noteImage.getImageByte());
                        bitmap.copyPixelsFromBuffer(byteBuffer);
                        Log.e("settings_bitmap",bitmap+" "+noteImage.getImageByte().length);
                        NoteImage.setImageBitmap(bitmap);
                    }

                }
            }.execute();
        }
    }

    public static class NoteDialogue extends DialogFragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
