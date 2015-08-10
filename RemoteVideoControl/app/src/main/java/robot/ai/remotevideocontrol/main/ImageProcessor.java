package robot.ai.remotevideocontrol.main;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.atap.tangoservice.TangoCameraIntrinsics;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Created by scott on 7/20/15.
 */
public class ImageProcessor {
    private VideoCapture mCamera = null;
    private int mCameraId;
    private Size mPreviewSize;
    private Size mSmallSize;
    private boolean mDoProcess;
    private Mat mCurrentFrame;
    private Mat mCurrentSmall;
    private Bitmap mCurrentBitmap;

    public ImageProcessor() {
        this.mCameraId = TangoCameraIntrinsics.TANGO_CAMERA_COLOR;
        this.mSmallSize = new Size(455, 256);
        this.mDoProcess = true;
    }

    public void setupCamera() {

        if (mCamera != null) {
            VideoCapture camera = mCamera;
            mCamera = null; // Make it null before releasing...
            camera.release();
        }

        mCamera = new VideoCapture(mCameraId);

        List<Size> previewSizes = mCamera.getSupportedPreviewSizes();
        double smallestPreviewSize = 1280 * 720; // We should be smaller than this...
        double smallestWidth = 480; // Let's not get smaller than this...
        Log.v("CameraSetup", "Smallest Preview Sizes");
        for (Size previewSize : previewSizes) {
            Log.v("CameraSetup", "Width x Height " + previewSize.width + "x" + previewSize.height);
            if (previewSize.area() < smallestPreviewSize && previewSize.width >= smallestWidth) {
                mPreviewSize = previewSize;
            }
        }

        mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, mPreviewSize.width);
        mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, mPreviewSize.height);
        mCurrentFrame = new Mat(mPreviewSize, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGB);
        mCurrentSmall = new Mat(mSmallSize, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGB);
        mCurrentBitmap = Bitmap.createBitmap(mCurrentSmall.width(), mCurrentSmall.height(), Bitmap.Config.RGB_565);
    }

    public void updateFrame() {
        Log.v("ImageProc", "updateing frame");
        long start = System.currentTimeMillis();
        boolean grabbed = mCamera.grab();
        if (grabbed) {
            long end = System.currentTimeMillis();
            Log.v("ImageProc", "Frame Grabbed in " + (end-start));
            start = System.currentTimeMillis();
            mCamera.retrieve(mCurrentFrame,
                    Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGB);
            end = System.currentTimeMillis();
            Log.v("ImageProc", "Frame Retrieved in " + (end-start));
            start = System.currentTimeMillis();
            Imgproc.resize(mCurrentFrame, mCurrentSmall, mSmallSize);
            end = System.currentTimeMillis();
            Log.v("ImageProc", "Frame Resized from " + mCurrentFrame.width() + "x" + mCurrentFrame.height() + " to " + mCurrentSmall.width() + "x" + mCurrentSmall.height());
            Log.v("ImageProc", "Resizing time " + (end-start));
            start = System.currentTimeMillis();
            Utils.matToBitmap(mCurrentSmall, mCurrentBitmap);
            end = System.currentTimeMillis();
            Log.v("ImageProc", "Frame mapped to bitmap in " + (end-start));

            Log.v("ImageProc", "Frame Updated");
        } else {
            Log.v("ImageProc", "Frame Not Updated");
        }
    }

    public Bitmap getmCurrentBitmap() {
        Log.v("ImageProc", "Getting Bitmap " + mCurrentBitmap.getByteCount());
        return mCurrentBitmap;
    }

    public boolean ismDoProcess() {
        return mDoProcess;
    }

    public void setmDoProcess(boolean mDoProcess) {
        this.mDoProcess = mDoProcess;
    }
}
