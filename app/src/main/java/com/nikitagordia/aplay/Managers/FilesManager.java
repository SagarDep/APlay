package com.nikitagordia.aplay.Managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nikitagordia.aplay.Fragments.PlayerListFragment;
import com.nikitagordia.aplay.Models.AudioTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20.12.17.
 */

public class FilesManager extends AsyncTask<Void,Void,List<AudioTrack>>{

    private PlayerListFragment mContext;

    public FilesManager(PlayerListFragment context) {
        mContext = context;
    }

    @Override
    protected List<AudioTrack> doInBackground(Void... voids) {
        return getAudioFiles(mContext.getContext());
    }

    @Override
    protected void onPostExecute(List<AudioTrack> audioTracks) {
        mContext.onSongList(audioTracks);
    }

    public static List<AudioTrack> getAudioFiles(Context context) {
        ArrayList<AudioTrack> res = new ArrayList<>();
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor =  context.getContentResolver().query(allSongsUri, null, null, null, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    res.add(new AudioTrack(
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return res;
    }
}
