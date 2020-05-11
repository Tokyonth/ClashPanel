package com.tokyonth.clashpanel.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class PackageCheck {

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pkgInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pkgInfo.size(); i++) {
            String pn = pkgInfo.get(i).packageName;
            if (pn.equals("com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }


}
