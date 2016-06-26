package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jiangzuomeng.fleetingtime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanlu on 16/6/27.
 */
public class CreateAlbumDialog extends Dialog {

    private Context context;
    private EditText albumName;
    private List<ImageButton> albumPrivate = new ArrayList<>();
    private List<ImageButton> albumSyn= new ArrayList<>();
    private List<ImageButton> albumWifi = new ArrayList<>();

    private Boolean isPrivate;
    private Boolean isSyn = false;
    private Boolean isWifi = false;

    private Button create;
    private Button cancel;

    private View view;

    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doCreate();

        public void doCancel();

    }


    public CreateAlbumDialog(Context context,int theme) {
        super(context,theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void show() {
        setContentView(view);
        super.show();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);

        view=inflater.inflate(R.layout.dialog_creat_album,null);

        albumName=(EditText) view.findViewById(R.id.create_album_name);

        ImageButton btn1 = (ImageButton)view.findViewById(R.id.btn_lock);
        ImageButton btn2 = (ImageButton)view.findViewById(R.id.btn_public);
        albumPrivate.add(btn1);
        albumPrivate.add(btn2);


        ImageButton btn3 = (ImageButton)view.findViewById(R.id.btn_syn);
        albumSyn.add(btn3);

        ImageButton btn4 = (ImageButton)view.findViewById(R.id.btn_wifi);
        albumWifi.add(btn3);

        create = (Button)view.findViewById(R.id.create_album);
        cancel = (Button)view.findViewById(R.id.create_cancel);

        create.setOnClickListener(new clickListener());
        cancel.setOnClickListener(new clickListener());

        for(ImageButton btn: albumPrivate) {
            btn.setOnClickListener(btnClick);
        }
        for (ImageButton btn : albumSyn) {
            btn.setOnClickListener(btnClick);
        }
        for (ImageButton btn : albumWifi) {
            btn.setOnClickListener(btnClick);
        }

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_lock:
                    v.setBackgroundResource(R.drawable.ic_lock_white_24dp);
                    albumPrivate.get(1).setBackgroundResource(R.drawable.ic_public_white_24dp);
                    break;

                case R.id.btn_public:
                    v.setBackgroundResource(R.drawable.ic_public_white_click_24dp);
                    albumPrivate.get(0).setBackgroundResource(R.drawable.ic_lock_transwhite_24dp);
                    break;

                case R.id.btn_wifi:
                    if(!isWifi) {
                        v.setBackgroundResource(R.drawable.ic_wifi_white_click_24dp);
                    } else {
                        v.setBackgroundResource(R.drawable.ic_wifi_white_24dp);
                    }
                    isWifi = !isWifi;
                    break;

                case R.id.btn_syn:
                    if(!isSyn) {
                        v.setBackgroundResource(R.drawable.ic_face_white_click_24dp);
                    } else {
                        v.setBackgroundResource(R.drawable.ic_face_white_24dp);
                    }
                    isSyn = !isSyn;
                    break;



            }
        }
    };

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.create_album:
                    clickListenerInterface.doCreate();
                    break;
                case R.id.create_cancel:
                    clickListenerInterface.doCancel();
                    break;

            }
        }
    }
}
