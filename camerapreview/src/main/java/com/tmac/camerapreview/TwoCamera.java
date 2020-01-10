package com.tmac.camerapreview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CaptureFailure;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tmac.utils.DateUtil;
import com.tmac.utils.PreviewStatusInterface;
import com.tmac.view.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TwoCamera extends Activity implements PreviewStatusInterface{


    private CameraPreview cameraPreview;
    private CameraPreview cameraPreview1;
    private static final String TAG = "CC-TwoCamera";

    private Button open0AE;
    private Button close0AE;
    private Button open1AE;
    private Button close1AE;
    private Button takePhoto;

    private String savePath = null;
    private Handler handlerCapture = new Handler();
    private CameraPreview.ImageSaver imageSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_camera);

        open0AE = findViewById(R.id.openAE_0);
        open1AE = findViewById(R.id.openAE_1);
        close0AE = findViewById(R.id.closeAE_0);
        close1AE = findViewById(R.id.closeAE_1);
        takePhoto = findViewById(R.id.takePhoto);

        cameraPreview = findViewById(R.id.cameraPreview);
        cameraPreview.setCameraID("0");
        cameraPreview.setPreViewStatusListener(this);

        initEvents();
    }



    private void initEvents() {
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File rgbFile = new File(v.getContext().getExternalFilesDir("rgb"), "rgbPic.jpg");
                cameraPreview.setOutPutDir(rgbFile);
                cameraPreview.takePicture();
                /*File fisheyeFile = new File(v.getContext().getExternalFilesDir("fisheye"), "fisheyePic.jpg");
                cameraPreview1.setOutPutDir(fisheyeFile);
                cameraPreview1.takePicture();*/
            }
        });
        open0AE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraPreview.getAEMode()){
                    cameraPreview.onPause();
                    cameraPreview.setCameraID("0");//RGB
                    cameraPreview.setAEMode(false);
                    cameraPreview.setPreViewStatusListener(TwoCamera.this);
                    cameraPreview.onResume(TwoCamera.this);
                }
            }
        });

        close0AE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cameraPreview.getAEMode()){
                    cameraPreview.onPause();
                    cameraPreview.setCameraID("0");
                    cameraPreview.setAEMode(true);
                    cameraPreview.setPreViewStatusListener(TwoCamera.this);
                    cameraPreview.onResume(TwoCamera.this);
                }
            }
        });

        open1AE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraPreview1.getAEMode()){
                    cameraPreview1.onPause();
                    cameraPreview1.setCameraID("1");//Fisheye
                    cameraPreview1.setAEMode(false);
                    cameraPreview1.setPreViewStatusListener(TwoCamera.this);
                    cameraPreview1.onResume(TwoCamera.this);
                }
            }
        });

        close1AE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cameraPreview1.getAEMode()){
                    cameraPreview1.onPause();
                    cameraPreview1.setCameraID("1");
                    cameraPreview1.setAEMode(true);
                    cameraPreview1.setPreViewStatusListener(TwoCamera.this);
                    cameraPreview1.onResume(TwoCamera.this);
                }
            }
        });

    }

    private Handler handler = new Handler();


    @Override
    protected void onResume() {
        super.onResume();
        cameraPreview.onResume(this);
        //cameraPreview1.onResume(TwoCamera.this);

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onPreviewFailed(String cameraID, CaptureFailure failure) {
        int reason = 0;
        if(failure != null){
            reason = failure.getReason();
        }
//        showAlertDialog(cameraID, reason);
    }

    private List<String> mCameraIDList = new ArrayList<>();
    @Override
    public void onPictureSaveSuccess(String cameraID, boolean state) {
        mCameraIDList.add(cameraID);
        /*if(mCameraIDList.size() > 0 && mCameraIDList.contains("0") && mCameraIDList.contains("1")){
            Log.d(TAG, "camera finish ......... ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }*/

        if(cameraID.equals("0")){
            Log.d(TAG, "cameraID = " + cameraID);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cameraPreview.onPause();
                    Log.d(TAG, "onPause" );
                    /*cameraPreview1 = findViewById(R.id.cameraPreview1);
                    cameraPreview1.setCameraID("1");
                    cameraPreview1.setPreViewStatusListener(TwoCamera.this);
                    cameraPreview1.onResume(TwoCamera.this);*/
                }
            });
        }else if (cameraID.equals("1")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            });
        }
    }


    private AlertDialog dialog;
    private void showAlertDialog(String cameraID, int reason){
        String cameraType = "";
        if(cameraID.equals("0")){
            cameraType = "RGB";
        }else if(cameraID.equals("1")){
            cameraType = "Fish eye";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Camera Error")
                .setMessage("Date: " + DateUtil.getCurDate("HH:mm:ss") + "\n" + cameraType + " Camera" + " preview error!" + "\n" + "Reason : " + reason)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraPreview.onPause();
        //cameraPreview1.onPause();
        mCameraIDList.clear();
        mCameraIDList = null;
    }
}
