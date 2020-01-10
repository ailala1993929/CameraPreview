package com.tmac.camerapreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.camera2.CaptureFailure;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tmac.utils.DateUtil;
import com.tmac.utils.PreviewStatusInterface;
import com.tmac.view.CameraPreview;

import java.io.File;

public class SingleCamera extends Activity implements PreviewStatusInterface {

    private CameraPreview cameraPreview;
    //private Button switch_Camera;
    private Button takePhoto;
    private String cameraID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_camera);
        //switch_Camera = findViewById(R.id.switch_id);
        takePhoto = findViewById(R.id.takePhoto);
        cameraPreview = findViewById(R.id.cameraPreview);
        openCamera(cameraID);
        initEvents();
    }

    private void initEvents() {
        /*switch_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPreview.onPause();
                if(cameraID.equals("0")){
                    cameraID = "1";
                }else if(cameraID.equals("1")){
                    cameraID = "0";
                }
                openCamera(cameraID);
            }
        });*/
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File rgbFile = new File(view.getContext().getExternalFilesDir("rgb"), "rgbPic.jpg");
                String aa=view.getContext().getExternalFilesDir("rgb").toString();
                Toast.makeText(SingleCamera.this,"path:"+aa,Toast.LENGTH_SHORT).show();
                cameraPreview.setOutPutDir(rgbFile);
                cameraPreview.takePicture();
            }
        });
    }

    private void openCamera(String cameraID){
        if(cameraPreview != null){
            cameraPreview.setCameraID(cameraID);
            cameraPreview.setPreViewStatusListener(SingleCamera.this);
            cameraPreview.onResume(SingleCamera.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraPreview.onResume(this);
    }

    @Override
    public void onPreviewFailed(String cameraID, CaptureFailure failure) {
        int reason = 0;
        if(failure != null){
            reason = failure.getReason();
        }
        //showAlertDialog(cameraID, reason);
    }

    @Override
    public void onPictureSaveSuccess(String cameraID, boolean state) {
        Log.e("AAAA", "onPictureSaveSuccess");
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
    protected void onDestroy() {
        super.onDestroy();
        cameraPreview.onPause();
    }
}
