package com.tmac.camerapreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.camera2.CaptureFailure;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tmac.utils.DateUtil;
import com.tmac.utils.PreviewStatusInterface;
import com.tmac.view.CameraPreview;

public class SingleCameraCloseAE extends Activity implements PreviewStatusInterface {

    private CameraPreview cameraPreview;
    private Button switch_Camera;
    private String cameraID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_camera_close_ae);
        switch_Camera = findViewById(R.id.switch_id_close_AE);

        cameraPreview = findViewById(R.id.cameraPreview_close_AE);

        openCamera(cameraID);

        switch_Camera.setOnClickListener(new View.OnClickListener() {
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
        });

    }


    private void openCamera(String cameraID){
        if(cameraPreview != null){
            cameraPreview.setCameraID(cameraID);
            cameraPreview.setAEMode(true);
            cameraPreview.setPreViewStatusListener(SingleCameraCloseAE.this);
            cameraPreview.onResume(SingleCameraCloseAE.this);
        }
    }


    @Override
    public void onPreviewFailed(String cameraID, CaptureFailure failure) {
        int reason = 0;
        if(failure != null){
            reason = failure.getReason();
        }
        showAlertDialog(cameraID, reason);

       /* Toast.makeText(this, "error reason:"+reason, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, SingleCameraCloseAE.class);
        startActivity(intent);
        finish();//关闭自己
        overridePendingTransition(0, 0);*/
    }

    @Override
    public void onPictureSaveSuccess(String cameraID, boolean state) {

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
