package com.sonbn.remi.mylib.ads

import android.app.Activity
import android.app.AppOpsManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AdOpen {
    private const val DEBUG_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    private var timeOut = 5 * 1000L

    interface OnAdOpenListener {
        fun onCompleted()
    }

    fun setTimeOut(timeOut: Long) {
        this.timeOut = timeOut
    }

    fun showIfAvailable(activity: Activity, id: String, onAdOpenListener: OnAdOpenListener) {
        val mId = if (AdmobUtils.isDebug) DEBUG_OPEN_ID else id
        if (!AdmobUtils.isShowAds) {
            onAdOpenListener.onCompleted()
            return
        }
        var isTimeOutCalled = false
        CoroutineScope(Dispatchers.IO).launch {
            delay(timeOut)
            withContext(Dispatchers.Main) {
                onAdOpenListener.onCompleted()
            }
            isTimeOutCalled = true
        }
        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, mId, request, object :
            AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(p0: AppOpenAd) {
                super.onAdLoaded(p0)
                if (!isTimeOutCalled) {
                    p0.apply {
                        fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                onAdOpenListener.onCompleted()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                super.onAdFailedToShowFullScreenContent(p0)
                                onAdOpenListener.onCompleted()
                            }
                        }
                        show(activity)
                    }
                }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                onAdOpenListener.onCompleted()
            }
        })
    }
}