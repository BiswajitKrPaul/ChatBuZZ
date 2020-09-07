package com.example.chattestapp.Utils;

import android.content.Context;
import android.widget.Toast;

public class ChatUtils {

    public static void maketoast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
