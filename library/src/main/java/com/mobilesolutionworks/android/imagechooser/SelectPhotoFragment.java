package com.mobilesolutionworks.android.imagechooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.io.File;

/**
 * Created by yunarta on 27/5/14.
 */
public abstract class SelectPhotoFragment extends DialogFragment
{

    private static final int REQUEST_FOR_IMAGE_CAPTURE = 100;
    private static final int REQUEST_FOR_MEDIA = 101;
    private static final int REQUEST_FOR_CROP_IMAGE = 102;

    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_PHOTO = 1;

    private File captureImageFile;

    private SelectMediaController mediaController;

    private int mode;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mediaController = new SelectMediaController(activity, this);
    }

    public void selectWhich(int method)
    {
        switch (method)
        {
            case TAKE_PHOTO:
            {
                captureImageFile = mediaController.selectCamera(REQUEST_FOR_IMAGE_CAPTURE);
                break;
            }

            default:
            case CHOOSE_PHOTO:
            {
                mediaController.selectMedia(REQUEST_FOR_MEDIA);
                break;
            }
        }
    }

    protected Bundle getCropExtras()
    {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode)
        {
            case REQUEST_FOR_MEDIA:
            {
                if (resultCode == Activity.RESULT_OK && intent != null)
                {
                    File cacheDir = getActivity().getCacheDir();
                    cacheDir.mkdirs();

                    File imageFile = new File(cacheDir, "CROP_IMAGE.jpg");

                    mode = REQUEST_FOR_MEDIA;
                    mediaController.cropMedia(getCropExtras(), intent.getData(), imageFile.getAbsolutePath(), REQUEST_FOR_CROP_IMAGE);
                }
                else
                {
                    onImageAcquired(null);
                }
                break;
            }

            case REQUEST_FOR_IMAGE_CAPTURE:
            {
                if (resultCode == Activity.RESULT_OK/* && intent != null*/)
                {
                    if (captureImageFile.exists())
                    {
                        File cacheDir = getActivity().getCacheDir();
                        cacheDir.mkdirs();

                        File imageFile = new File(cacheDir, "CROP_IMAGE.jpg");

                        mode = REQUEST_FOR_IMAGE_CAPTURE;
                        mediaController.cropMedia(getCropExtras(), captureImageFile.getAbsolutePath(), imageFile.getAbsolutePath(), REQUEST_FOR_CROP_IMAGE);
                    }
                    else
                    {
                        onImageAcquired(null);
                    }
                }
                break;
            }

            case REQUEST_FOR_CROP_IMAGE:
            {
                if (resultCode == Activity.RESULT_OK && intent != null)
                {
                    String filePath = intent.getAction();
                    onImageAcquired(filePath);
                }
                else
                {
                    onImageAcquired(null);
                }
                break;
            }
        }
    }

    protected abstract void onImageAcquired(String filePath);
}
