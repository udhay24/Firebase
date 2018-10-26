package com.example.udhay.firebase;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

public class Util {

    public static String getNameFromUri(Context context , Uri uri){
        Cursor cursor = context.getContentResolver().query(uri , null , null , null , null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return checkName(name);
    }

    public static String checkName(String name){
        String finalString = name.replaceAll("[^.,a-zA-Z0-9]+","");
        return finalString;

    }
}
