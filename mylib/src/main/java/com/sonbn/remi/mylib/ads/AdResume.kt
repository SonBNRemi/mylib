package com.sonbn.remi.mylib.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.lang.ref.WeakReference
import java.util.Date

object AdResume {
    private var isShowingAd = false
    private var isLoadingAd = false
    private var appOpenAd: AppOpenAd? = null
    private const val DEBUG_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
    private var mId = DEBUG_OPEN_ID
    private var loadTime: Long = 0
    private var mApplication: Application? = null

    fun init(application: Application, id: String) {
        if (mApplication != null || !AdmobUtils.isShowAds) return

        this.mApplication = application
        this.mId = if (AdmobUtils.isDebug) DEBUG_OPEN_ID else id

        getActivity()?.let { loadAd(it) }
        mApplication?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

            }

            override fun onActivityStarted(p0: Activity) {
                setActivity(p0)
            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }

        })

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                getActivity()?.let {showAdIfAvailable(it)}
            }
        })
    }

    private var activityReference: WeakReference<Activity>? = null
    private fun setActivity(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    private fun getActivity(): Activity? {
        return activityReference?.get()
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }


    private fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context, mId, request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                }
            })
    }

    fun showAdIfAvailable(activity: Activity){
        if (isShowingAd) {
            return
        }

        if (!isAdAvailable()) {
            loadAd(activity)
            return
        }

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false

                loadAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false

                loadAd(activity)
            }

            override fun onAdShowedFullScreenContent() {

            }
        }
        isShowingAd = true
        appOpenAd?.show(activity)
    }

}