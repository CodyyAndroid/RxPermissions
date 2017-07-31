/*
 * 阔地教育科技有限公司版权所有(codyy.com/codyy.cn)
 * Copyright (c) 2017, Codyy and/or its affiliates. All rights reserved.
 */

package com.codyy.rxbinding.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * PermissionsUtils
 * Created by eachann on 2016/8/4.
 */
public class PermissionsUtils {
    /**
     * 弹窗提示
     * @param context
     * @param message
     */
    public static void warn(final Context context, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告").setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                openSettings(context);
            }
        }).create().show();
    }

    private static void openSettings(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }
}
