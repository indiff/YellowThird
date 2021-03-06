package com.pear.android.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘工具
 */

public class SoftInputUtils {

    /**
     * 隐藏键盘
     * */
    public static void hideSoftInput(Context context, EditText input) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

}
