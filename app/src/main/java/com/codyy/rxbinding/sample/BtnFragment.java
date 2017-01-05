package com.codyy.rxbinding.sample;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codyy.rx.permissions.Permission;
import com.codyy.rx.permissions.RxPermissions;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


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
                .compose(mRxPermissions.ensureEach(Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE))
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        result(getContext(), permission);
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
