package com.example.komplexbeadando;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BitmapListConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String bitmapListToString(List<Bitmap> bitmaps) {
        if (bitmaps == null || bitmaps.isEmpty()) return "[]";
        List<String> encodedImages = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            encodedImages.add(encodeBitmapToString(bitmap));
        }
        return gson.toJson(encodedImages);
    }

    @TypeConverter
    public static List<Bitmap> stringToBitmapList(String encodedString) {
        if (encodedString == null || encodedString.isEmpty()) return Collections.emptyList();
        try {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> encodedImages = gson.fromJson(encodedString, listType);

            List<Bitmap> bitmaps = new ArrayList<>();
            for (String encodedImage : encodedImages) {
                Bitmap bitmap = decodeStringToBitmap(encodedImage);
                if (bitmap != null) {
                    bitmaps.add(bitmap);
                }
            }
            return bitmaps;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static String encodeBitmapToString(Bitmap bitmap) {
        if (bitmap == null) return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private static Bitmap decodeStringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
