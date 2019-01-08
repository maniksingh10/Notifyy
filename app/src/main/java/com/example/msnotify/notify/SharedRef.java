package com.example.msnotify.notify;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRef {

    SharedPreferences sharedPreferences;
    public SharedRef (Context context){
        sharedPreferences=context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }

    public void SaveRef(String branch,String year){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("branch",branch);
        editor.putString("year",year);
    }
}
