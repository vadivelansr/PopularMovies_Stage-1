package com.mydomain.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vadivelansr on 10/26/2015.
 */
public class Utilities {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return ((activeNetworkInfo != null) && activeNetworkInfo.isConnectedOrConnecting());
    }


    public static boolean checkNetworkState(Context paramContext)
    {
        try
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo localNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if ((localNetworkInfo != null) && (localNetworkInfo.isConnectedOrConnecting())) {
                return true;
            }
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            int j = networkInfo.length;
            int i = 0;
            while (i < j)
            {
                localNetworkInfo = networkInfo[i];
                if ((localNetworkInfo.getType() == 1) && (localNetworkInfo.isConnected())) {
                    return true;
                }
                if (localNetworkInfo.getType() == 0)
                {
                    boolean bool = localNetworkInfo.isConnected();
                    if (bool) {
                        return true;
                    }
                }
                i += 1;
            }

        }
        catch (Exception e)
        {

        }
        return false;
    }
}
