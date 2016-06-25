package com.jiangzuomeng.fleetingtime.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * Created by guanlu on 16/5/23.
 */
public class GalleryFlow extends Gallery {

    private  Camera mCamera = new Camera();

    private int mMaxRotationAngle = 60;

    private int mMaxZoom = -120;

    private int mCoveflowCenter = 0;

    public GalleryFlow(Context context) {

        this(context,null);
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);

        this.setStaticTransformationsEnabled(true);
        this.setChildrenDrawingOrderEnabled(true);
    }

    public int getMaxRotationAngle() {
        return  mMaxRotationAngle;
    }

    public  void  setMaxRotationAngle (int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
        return  mMaxZoom;
    }
    public void setMaxZoom(int maxZoom){
        mMaxZoom = maxZoom;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = getSelectedItemPosition()-getFirstVisiblePosition();
        if(selectedIndex <0) {
            return i;
        }
        if(i < selectedIndex) {
            return  i;
        }
        else if(i>=selectedIndex) {
            return childCount-1-i+selectedIndex;
        } else {
            return i;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int getCenterOfView(View view)
    {
        return view.getLeft() + view.getWidth() / 2;
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t)
    {
        super.getChildStaticTransformation(child, t);

        final int childCenter = getCenterOfView(child);
        final int childWidth  = child.getWidth();

        int rotationAngle = 0;
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        // If the child is in the center, we do not rotate it.
        if (childCenter == mCoveflowCenter)
        {
            transformImageBitmap(child, t, 0);
        }
        else
        {
            // Calculate the rotation angle.
            rotationAngle = (int)(((float)(mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

            // Make the angle is not bigger than maximum.
            if (Math.abs(rotationAngle) > mMaxRotationAngle)
            {
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
            }

            transformImageBitmap(child, t, rotationAngle);
        }

        return true;
    }

    private int getCenterOfCoverflow()
    {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    private void transformImageBitmap(View child, Transformation t, int rotationAngle)
    {
        mCamera.save();

        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getHeight();
        final int imageWidth  = child.getWidth();
        final int rotation    = Math.abs(rotationAngle);

        // Zoom on Z axis.
        mCamera.translate(0, 0, mMaxZoom);

        if (rotation < mMaxRotationAngle)
        {
            float zoomAmount = (float)(mMaxZoom + rotation * 1.5f);
            mCamera.translate(0, 0, zoomAmount);
        }

        // Rotate the camera on Y axis.
        mCamera.rotateY(rotationAngle);
        // Get the matrix from the camera, in fact, the matrix is S (scale) transformation.
        mCamera.getMatrix(imageMatrix);

        // The matrix final is T2 * S * T1, first translate the center point to (0, 0),
        // then scale, and then translate the center point to its original point.
        // T * S * T

        // S * T1
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        // (T2 * S) * T1
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

        mCamera.restore();
    }
}
