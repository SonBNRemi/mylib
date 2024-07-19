package com.sonbn.remi.mylib.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

object AdmobUtils {
    var isDebug = true
    var isShowAds = true

    fun initAdmob(context: Context, isDebug: Boolean = false, isShowAds: Boolean = true) {
        this.isDebug = isDebug
        this.isShowAds = isShowAds
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(context) {}
        }
    }

    suspend fun isInternetAvailable(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val address: InetAddress = InetAddress.getByName("www.google.com")
                !address.equals("")
            } catch (e: Exception) {
                false
            }
        }
    }

//    App Open	ca-app-pub-3940256099942544/9257395921
//    Adaptive Banner	ca-app-pub-3940256099942544/9214589741
//    Fixed Size Banner	ca-app-pub-3940256099942544/6300978111
//    Interstitial	ca-app-pub-3940256099942544/1033173712
//    Interstitial Video	ca-app-pub-3940256099942544/8691691433
//    Rewarded	ca-app-pub-3940256099942544/5224354917
//    Rewarded Interstitial	ca-app-pub-3940256099942544/5354046379
//    Native Advanced	ca-app-pub-3940256099942544/2247696110
//    Native Advanced Video	ca-app-pub-3940256099942544/1044960115
}