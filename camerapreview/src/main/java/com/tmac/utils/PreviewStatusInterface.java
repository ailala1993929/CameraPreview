package com.tmac.utils;

import android.hardware.camera2.CaptureFailure;

public interface PreviewStatusInterface {

    void onPreviewFailed(String cameraID, CaptureFailure failure);
    void onPictureSaveSuccess(String cameraID, boolean state);

}
