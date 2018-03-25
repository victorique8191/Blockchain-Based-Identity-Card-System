/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.face.photo;

import java.io.FileNotFoundException;
import android.content.ContentResolver;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.samples.vision.face.patch.SafeFaceDetector;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.InputStream;

import static android.R.attr.data;

/**
 * Demonstrates basic usage of the GMS vision face detector by running face landmark detection on a
 * photo and displaying the photo with associated landmarks in the UI.
 */
public class PhotoViewerActivity extends Activity {
    private static final String TAG = "PhotoViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int[] grade={0,0};
        if (requestCode==1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                            .build();

                    Detector<Face> safeDetector = new SafeFaceDetector(detector);

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Face> faces = safeDetector.detect(frame);

                    if (!safeDetector.isOperational()) {
                        Log.w(TAG, "Face detector dependencies are not yet available.");

                        IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                        boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                        if (hasLowStorage) {
                            Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                            Log.w(TAG, getString(R.string.low_storage_error));
                        }
                    }

                    FaceView overlay = (FaceView) findViewById(R.id.faceView);
                    overlay.setContent(bitmap, faces);

                    safeDetector.release();
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }

        if (requestCode==2) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                            .build();

                    Detector<Face> safeDetector = new SafeFaceDetector(detector);

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Face> faces = safeDetector.detect(frame);

                    if (!safeDetector.isOperational()) {
                        Log.w(TAG, "Face detector dependencies are not yet available.");

                        IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                        boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                        if (hasLowStorage) {
                            Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                            Log.w(TAG, getString(R.string.low_storage_error));
                        }
                    }

                    FaceView overlay = (FaceView) findViewById(R.id.faceView2);
                    overlay.setContent(bitmap, faces);

                    safeDetector.release();
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


        //InputStream stream = getResources().openRawResource(R.raw.f20170312_201332);
        //Bitmap bitmap = BitmapFactory.decodeStream(stream);
    }
}
