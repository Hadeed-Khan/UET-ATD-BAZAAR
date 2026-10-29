package com.devrix.uetatdbazaar.ApplicationClass

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkClass(context: Context)  {

    lateinit var context: Context
    init {
        this.context = context
    }
    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}