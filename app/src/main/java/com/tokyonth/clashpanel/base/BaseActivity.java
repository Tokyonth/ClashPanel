package com.tokyonth.clashpanel.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private PermCheck permCheck;
    private boolean callPerm = false;

    public interface PermCheck {
        void isPerm(boolean bool);
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            Toast.makeText(this, "获取到权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission(PermCheck permCheck) {
        callPerm = true;
        this.permCheck = permCheck;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
            permCheck.isPerm(false);
        } else {
            permCheck.isPerm(true);
        }
    }

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissions, 0);
            if (callPerm)
            permCheck.isPerm(false);
        } else {
            if (callPerm)
            permCheck.isPerm(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                if (callPerm)
                permCheck.isPerm(true);
            } else {
                if (callPerm)
                permCheck.isPerm(false);
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
