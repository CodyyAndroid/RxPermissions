package com.codyy.rxbinding.sample;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codyy.rx.permissions.Permission;
import com.codyy.rx.permissions.RxPermissions;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * RxPermissions
 * Created by lijian on 2017/01/05.
 */

public class BtnFragment extends Fragment {
    View mView;
    RxPermissions mRxPermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxPermissions = new RxPermissions(getChildFragmentManager());
        mRxPermissions.setLogging(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.btn_fragment, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RxView.clicks(mView.findViewById(R.id.button))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mRxPermissions.ensure(Manifest.permission.CAMERA))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept( Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            RxPermissions.showDialog(getContext(),getContext().getPackageName(),"未授予相机使用权限");
                        }
                    }
                });
        RxView.clicks(mView.findViewById(R.id.button2))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept( Object o) throws Exception {
                        switch (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
                            case PermissionChecker.PERMISSION_DENIED:
                                Toast.makeText(getContext(), "PERMISSION_DENIED拒绝", Toast.LENGTH_SHORT).show();
                                PermissionsUtils.warn(getContext(), "相机权限获取失败");
                                break;
                            case PermissionChecker.PERMISSION_DENIED_APP_OP:
                                Toast.makeText(getContext(), "PERMISSION_DENIED_APP_OP拒绝", Toast.LENGTH_SHORT).show();
                                break;
                            case PermissionChecker.PERMISSION_GRANTED:
                                Toast.makeText(getContext(), "成功", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void result(Context context, Permission permission) {
        if (permission.granted) {
            // `permission.name` is granted !
            Toast.makeText(context, permission.name + ":granted", Toast.LENGTH_SHORT).show();
        } else if (permission.shouldShowRequestPermissionRationale) {
            // Denied permission without ask never again
            Toast.makeText(context, permission.name + ":denied", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, permission.name + ":never again", Toast.LENGTH_SHORT).show();
            // Denied permission with ask never again
            // Need to go to the settings
        }

    }
}
