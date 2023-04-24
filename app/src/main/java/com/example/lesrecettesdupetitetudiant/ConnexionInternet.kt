package com.example.lesrecettesdupetitetudiant

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo.State

class ConnexionInternet {
    companion object {
        fun isConnectedInternet(activity: Activity): Boolean {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                val networkState = networkInfo.state
                if (networkState == State.CONNECTED) {
                    return true
                } else return false
            } else return false
        }
    }
}
