package com.hypech.case23_avatar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

public class MyPopup extends PopupWindow {
    private View mView;
    private Context mContext;
    private View.OnClickListener mGalleryListener;   // photo gallery listener
    private View.OnClickListener mCaptureListener;  // camera listener

    public MyPopup(Activity context,
                   View.OnClickListener galleryListener,
                   View.OnClickListener captureListener) {
        super(context);
        this.mContext = context;
        this.mGalleryListener = galleryListener;
        this.mCaptureListener = captureListener;
        Init();
    }

    private void Init() {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popup_window, null);

        Button btn_camera = mView.findViewById(R.id.button_camera);
        Button btn_gallery= mView.findViewById(R.id.button_gallery);
        Button btn_cancel = mView.findViewById(R.id.button_cancel);

        btn_gallery.setOnClickListener(mGalleryListener);
        btn_camera.setOnClickListener(mCaptureListener);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // setup the popup window layout
        this.setContentView(mView);

        // animation
        this.setAnimationStyle(R.style.popwindow_anim_style);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

        // set touch
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);

        // clicking the area outside of the popup window will close it.
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mView.findViewById(R.id.ll_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}