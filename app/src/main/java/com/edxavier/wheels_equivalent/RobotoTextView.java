package com.edxavier.wheels_equivalent;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Eder Xavier Rojas on 06/10/2015.
 */
public class RobotoTextView extends TextView {
    Typeface roboto;

    public RobotoTextView(Context context) {
        super(context);
        setRobotoRegular();
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRobotoRegular();
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRobotoRegular();
    }

    public void setRobotoRegular(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        this.setTypeface(roboto);
    }
    public void setRobotoCondensedRegular(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        this.setTypeface(roboto);
    }
    public void setRobotoLight(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        this.setTypeface(roboto);
    }

    public void setRobotoItalic(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Italic.ttf");
        this.setTypeface(roboto);
    }

    public void setRobotoBold(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        this.setTypeface(roboto);
    }

    public void setRobotoMedium(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        this.setTypeface(roboto);
    }
    public void setRobotoThin(){
        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        this.setTypeface(roboto);
    }

}
