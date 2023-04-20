package com.example.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

import static com.example.viewpager.drawing.path;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class display extends androidx.appcompat.widget.AppCompatImageView {
    public ViewGroup.LayoutParams params;
    public static Bitmap bitmap;
    public static Canvas mcanvas;

    public static ArrayList<Path> drawPathList = new ArrayList<>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public static ArrayList<Integer> sizeBrushList = new ArrayList<>();
    public static Paint drawPaint, canvasPaint;
    public static int current_color = Color.BLACK;
    public static int current_size = 20;

    ScaleGestureDetector scaleDetector;
    public static float scaleFactor = 1.0f;
    float maxZoom = 5.0f;
    float minZoom = 0.5f;
    private static final int INVALID_POINTER_ID = -1;

    public static float mPosX = 0;
    public static float mPosY = 0;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mLastGestureX;
    private float mLastGestureY;
    private int mActivePointerId = INVALID_POINTER_ID;

    public static boolean isZoom = false;
    public static Context displayScreen;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom));

            invalidate();
            return true;
        }

    }


    public display(Context context) {
        super(context);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        init(context);
    }

    public display(Context context, @Nullable AttributeSet atts) {
        super(context, atts);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        init(context);
    }

    public display(Context context, @Nullable AttributeSet atts, int defStyleAttr) {
        super(context, atts, defStyleAttr);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        init(context);
    }

    private void init(Context context) {
        drawPaint = new Paint();

        drawPaint.setColor(current_color);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(current_size);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = ((float) event.getX() / scaleFactor) - mPosX;
        float touchY = ((float) event.getY() / scaleFactor) - mPosY;

        int action = event.getAction();
        if (isZoom) {
            scaleDetector.onTouchEvent(event);

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    if (!scaleDetector.isInProgress()) {
                        final float x = event.getX();
                        final float y = event.getY();

                        mLastTouchX = x;
                        mLastTouchY = y;
                        mActivePointerId = event.getPointerId(0);
                    }
                    break;
                }
                case MotionEvent.ACTION_POINTER_1_DOWN: {
                    if (scaleDetector.isInProgress()) {
                        final float gx = scaleDetector.getFocusX();
                        final float gy = scaleDetector.getFocusY();
                        mLastGestureX = gx;
                        mLastGestureY = gy;
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!scaleDetector.isInProgress()) {
                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(pointerIndex);
                        final float y = event.getY(pointerIndex);

                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        mPosX += dx;
                        mPosY += dy;

                        invalidate();

                        mLastTouchX = x;
                        mLastTouchY = y;
                    } else {
                        final float gx = scaleDetector.getFocusX();
                        final float gy = scaleDetector.getFocusY();

                        final float gdx = gx - mLastGestureX;
                        final float gdy = gy - mLastGestureY;

                        mPosX += gdx;
                        mPosY += gdy;

                        invalidate();

                        mLastGestureX = gx;
                        mLastGestureY = gy;
                    }

                    break;
                }
                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = event.getX(newPointerIndex);
                        mLastTouchY = event.getY(newPointerIndex);
                        mActivePointerId = event.getPointerId(newPointerIndex);
                    } else {
                        final int tempPointerIndex = event.findPointerIndex(mActivePointerId);
                        mLastTouchX = event.getX(tempPointerIndex);
                        mLastTouchY = event.getY(tempPointerIndex);
                    }

                    break;
                }
            }

            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(touchX, touchY);
                    mX = touchX;
                    mY = touchY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(touchX - mX);
                    float dy = Math.abs(touchY - mY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        path.quadTo(mX, mY, (touchX + mX) / 2, (touchY + mY) / 2);
                        mX = touchX;
                        mY = touchY;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    path.lineTo(mX, mY);
//                    mcanvas.drawPath(path, drawPaint);
                    path.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(mPosX, mPosY);

        mcanvas.drawPath(path, drawPaint);
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);

        canvas.restore();
    }

    private void newCanvas(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
        setImageBitmap(bitmap);
    }
}