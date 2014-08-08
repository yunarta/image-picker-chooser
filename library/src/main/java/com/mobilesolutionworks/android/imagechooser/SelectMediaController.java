package com.mobilesolutionworks.android.imagechooser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import com.mobilesolutionworks.android.cropimage.camera.CropImageIntentBuilder;

import java.io.File;
import java.util.Formatter;

/**
 * Created by yunarta on 27/5/14.
 */
public class SelectMediaController {

    private Activity mContext;
    private Fragment mFragment;

    public SelectMediaController(Activity context, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    public File selectCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        SharedPreferences preferences = mContext.getSharedPreferences("camera", Context.MODE_PRIVATE);

        int number = preferences.getInt("number", 1);
        preferences.edit().putInt("number", number + 1).commit();

        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        dcim.mkdir();

        Formatter mFmt = new Formatter(java.util.Locale.US);
        File file = new File(dcim, mFmt.format("IMG_%04d.jpg", number).toString());

        Uri uri = Uri.fromFile(file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        mFragment.startActivityForResult(intent, requestCode);

        return file;
    }

    public void selectMedia(int requestCode) {
        final Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }

        mFragment.startActivityForResult(Intent.createChooser(intent, "Select picture"), requestCode);
    }

    public void cropMedia(Bundle bundle, Uri imagePath, String savePath, int requestCode) {
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, Uri.fromFile(new File(savePath)));
        cropImage.setSourceImage(imagePath);

        Intent intent = cropImage.getIntent(mContext);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        mFragment.startActivityForResult(intent, requestCode);
    }

    public void cropMedia(Bundle bundle, String imagePath, String savePath, int requestCode) {
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, Uri.fromFile(new File(savePath)));
        cropImage.setSourceImage(Uri.fromFile(new File(imagePath)));

        Intent intent = cropImage.getIntent(mContext);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        mFragment.startActivityForResult(intent, requestCode);
    }
}
