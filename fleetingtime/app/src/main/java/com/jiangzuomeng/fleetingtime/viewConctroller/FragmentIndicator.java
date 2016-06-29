package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiangzuomeng.fleetingtime.R;


/**
 * Created by 关璐 on 2016/5/20.
 */
public class FragmentIndicator extends LinearLayout implements OnClickListener{
    private int mDefaultIndicator = 0;

    private static int mCurIndicator;

    private static View[] mIndicators;

    private OnIndicateListener mOnIndicateListener;

    private static final String TAG_TEXT_0 = "text_tag_0";
    private static final String TAG_TEXT_1 = "text_tag_1";
    private static final String TAG_TEXT_2 = "text_tag_2";

    private static final String TAG_ICON_0 = "icon_tag_0";
    private static final String TAG_ICON_1 = "icon_tag_1";
    private static final String TAG_ICON_2 = "icon_tag_2";

    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_GRAY = 0xFF888888;

    private  static final int SELECTED = 0xffff9800;
    private  static final int UNSELECTED = 0xff9e9e9e;

    private static final  int BG = 0xfff9f9f9;

    private static final int HIGH = 100;

    FragmentIndicator(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public FragmentIndicator(Context context, AttributeSet attrs) {
        super(context,attrs);

        mCurIndicator = mDefaultIndicator;
        setOrientation(LinearLayout.HORIZONTAL);
        init();

    }


    private View createIndicator(int stringResID, int stringColor,String textTag,
                                 String iconTag,int iconResID){
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);

        view.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,HIGH,1));
        view.setGravity(Gravity.CENTER_HORIZONTAL);

        //line
        ImageView line = new ImageView(getContext());
        line.setBackgroundColor(0xffdedede);
        line.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,3));
        view.addView(line);

        //icon

        ImageView iconView = new ImageView(getContext());
        iconView.setTag(iconTag);
        iconView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 1));
        iconView.setImageResource(iconResID);
        iconView.setPadding(0, 3, 0, -7);
        //text
        TextView textView = new TextView(getContext());
        textView.setTag(textTag);
        textView.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        textView.setTextColor(UNSELECTED);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setText(stringResID);
        textView.setPadding(0,0,0,0);

        view.addView(iconView);
        view.addView(textView);
        return view;
    }


    private void init() {
        mIndicators = new View[3];
        //默认第一个界面
        mIndicators[0] = createIndicator(R.string.nearby, Color.BLACK,TAG_TEXT_0,
                TAG_ICON_0,R.drawable.ic_nearby_light_24dp);
        mIndicators[0].setBackgroundColor(BG);
        mIndicators[0].setTag(Integer.valueOf(0));
        mIndicators[0].setOnClickListener(this);
        TextView text=(TextView)mIndicators[0].findViewWithTag(TAG_TEXT_0);
        text.setTextColor(SELECTED);
        addView(mIndicators[0]);

        mIndicators[1] = createIndicator(R.string.album,Color.BLACK,TAG_TEXT_1,
                TAG_ICON_1,R.drawable.ic_album_gray_24dp);
        mIndicators[1].setBackgroundColor(BG);
        mIndicators[1].setTag(Integer.valueOf(1));
        mIndicators[1].setOnClickListener(this);
        addView(mIndicators[1]);


        mIndicators[2] = createIndicator(R.string.circle,Color.BLACK,TAG_TEXT_2,
                TAG_ICON_2,R.drawable.ic_circle_gray_24dp);
        mIndicators[2].setBackgroundColor(BG);
        mIndicators[2].setTag(Integer.valueOf(2));
        mIndicators[2].setOnClickListener(this);
        addView(mIndicators[2]);

    }

    public static void setIndicator(int which) {
            mIndicators[mCurIndicator].setBackgroundColor(BG);
        TextView prevText;
        ImageView prevIcon;

        switch(mCurIndicator) {
            case 0:
                prevText=(TextView)mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_0);
                prevIcon=(ImageView)mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_0);
                prevText.setTextColor(UNSELECTED);
                prevIcon.setImageResource(R.drawable.ic_nearby_gray_24dp);
                break;
            case 1:
                prevText=(TextView)mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_1);
                prevIcon=(ImageView)mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_1);
                prevText.setTextColor(UNSELECTED);
                prevIcon.setImageResource(R.drawable.ic_album_gray_24dp);
                break;
            case 2:
                prevText=(TextView)mIndicators[mCurIndicator].findViewWithTag(TAG_TEXT_2);
                prevIcon=(ImageView)mIndicators[mCurIndicator].findViewWithTag(TAG_ICON_2);
                prevText.setTextColor(UNSELECTED);
                prevIcon.setImageResource(R.drawable.ic_circle_gray_24dp);
                break;
        }


        mIndicators[which].setBackgroundColor(BG);
        TextView curText;
        ImageView curIcon;

        switch(which) {
            case 0:
                curText=(TextView)mIndicators[which].findViewWithTag(TAG_TEXT_0);
                curIcon=(ImageView)mIndicators[which].findViewWithTag(TAG_ICON_0);
                curText.setTextColor(SELECTED);
                curIcon.setImageResource(R.drawable.ic_nearby_light_24dp);
                break;
            case 1:
                curText=(TextView)mIndicators[which].findViewWithTag(TAG_TEXT_1);
                curIcon=(ImageView)mIndicators[which].findViewWithTag(TAG_ICON_1);
                curText.setTextColor(SELECTED);
                curIcon.setImageResource(R.drawable.ic_album_light_24dp);
                break;
            case 2:
                curText=(TextView)mIndicators[which].findViewWithTag(TAG_TEXT_2);
                curIcon=(ImageView)mIndicators[which].findViewWithTag(TAG_ICON_2);
                curText.setTextColor(SELECTED);
                curIcon.setImageResource(R.drawable.ic_circle_light_24dp);
                break;
        }
        mCurIndicator = which;
    }


    public interface OnIndicateListener {
        public void OnIndicate(View v, int which);
    }

    public void setOnIndicateListener(OnIndicateListener listener) {
        mOnIndicateListener = listener;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(mOnIndicateListener !=null) {
            int tag = (Integer)v.getTag();
            switch(tag) {
                case 0:
                    if(mCurIndicator !=0) {
                        mOnIndicateListener.OnIndicate(v, 0);
                        setIndicator(0);
                    }
                    break;

                case 1:
                    if(mCurIndicator !=1) {
                        mOnIndicateListener.OnIndicate(v, 1);
                        setIndicator(1);
                    }
                    break;

                case 2:
                    if(mCurIndicator !=2) {
                        mOnIndicateListener.OnIndicate(v, 2);
                        setIndicator(2);
                    }
                    break;

                default:

                    break;
            }
        }

    }


}
