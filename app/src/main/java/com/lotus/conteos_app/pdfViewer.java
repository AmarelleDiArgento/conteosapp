package com.lotus.conteos_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdfViewer extends AppCompatActivity {

    Bundle bundle;
    File pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        getData();
        pdfViewer2();
    }

    public void getData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            pdf = (File) bundle.get("filepdf");
        } else {
            Log.i("getIntent", "Bundle vacio");
        }
    }

    public void pdfViewer2() {
        PDFView pdfView = findViewById(R.id.pdfv);

        pdfView.fromFile(pdf)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(2)
                .load();
    }
}