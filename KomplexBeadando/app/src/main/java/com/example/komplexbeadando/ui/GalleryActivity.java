package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.AppData;
import com.example.komplexbeadando.R;

public class GalleryActivity extends AppCompatActivity {

    public static AppData data;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialieActivity();
    }

    @SuppressLint("WrongViewCast")
    private void initialieActivity() {
        table = findViewById(R.id.table);
        generateGallery();
    }

    public void generateGallery(){

        generateTableRows(table, data.getPhotos().size(), getApplicationContext());
    }

    public void generateTableRows(TableLayout tableLayout, int number, Context context) {
        runOnUiThread(() -> {

            tableLayout.removeAllViews();
            tableLayout.setPadding(35,0,35,0);

            int imagesPerRow = 3;
            int totalRows = (int) Math.ceil(number / (double) imagesPerRow);
            int imageCount = 0;

            for (int i = 0; i < totalRows; i++) {
                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < imagesPerRow && imageCount < number; j++) {

                    ImageView imageView = new ImageView(context);
                    imageView.setLayoutParams(new TableRow.LayoutParams(340, 340));

                    //imageView.setImageResource(R.drawable.ic_launcher_background);
                    //Bitmap pic = data.getPhotos().get(imageCount);
                    //Log.d(TAG, "Photo list element: "+pic.toString());
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac1, null);
                    Drawable d = new BitmapDrawable(getResources(), data.getPhotos().get(imageCount));
                    Bitmap pic = ((BitmapDrawable)d).getBitmap();
                    imageView.setImageBitmap(pic);

                    imageView.setPadding(3, 3, 3, 3);

                    tableRow.addView(imageView);
                    imageCount++;
                }

                tableLayout.addView(tableRow);
            }

        });

    }

    public void back(View view) {
        finish();
    }
}