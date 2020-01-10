package com.tmac.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by yunduo on 18-8-29.
 */

public class CropUtils {
    private static String mFile;

    public static String getPath() {
        //resize image to thumb
        if (mFile == null) {
            mFile = Environment.getExternalStorageDirectory() + "/" + "temp.jpg";
        }
        return mFile;
    }

    /**
     * 调用系统照片的裁剪功能
     */
    public static Intent invokeSystemCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        intent.putExtra("scale", true);

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPath())));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        return intent;
    }
}
