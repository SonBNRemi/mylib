package com.sonbn.remi.mylib.ads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdInterstitial {
    private const val DEBUG_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    private const val DEBUG_INTERSTITIAL_VIDEO_ID = "ca-app-pub-3940256099942544/8691691433"

    var isShowing = false
    var isLoading = false
    private var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitial(
        activity: Activity,
        id: String,
        listener: InterstitialAdLoadCallback? = null
    ) {
        if (!AdmobUtils.isShowAds || isLoading || mInterstitialAd != null) {
            return
        }
        isLoading = true
        val mId = if (AdmobUtils.isDebug) DEBUG_INTERSTITIAL_ID else id
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, mId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                listener?.onAdLoaded(interstitialAd)
                isLoading = false
                mInterstitialAd = interstitialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                listener?.onAdFailedToLoad(p0)
                isLoading = false
                mInterstitialAd = null
            }
        })
    }

    fun showInterstitial(
        activity: Activity,
        id: String,
        onAdInterstitialListener: OnAdInterstitialListener? = null
    ) {
        val mId = if (AdmobUtils.isDebug) DEBUG_INTERSTITIAL_ID else id
        if (!AdmobUtils.isShowAds || isShowing) {
            onAdInterstitialListener?.onCompleted()
            return
        }
        if (mInterstitialAd == null) {
            loadInterstitial(activity, mId, null)
            onAdInterstitialListener?.onCompleted()
            return
        }
        isShowing = true
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                onAdInterstitialListener?.onCompleted()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                onAdInterstitialListener?.onCompleted()
                isShowing = false
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                onAdInterstitialListener?.onCompleted()
                isShowing = false
                mInterstitialAd = null
            }
        }
        mInterstitialAd?.show(activity)
    }

    interface OnAdInterstitialListener {
        fun onCompleted()
    }
}