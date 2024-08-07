package com.sonbn.remi.mylib.ads

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.sonbn.remi.mylib.databinding.ShimmerBannerBinding
import com.sonbn.remi.mylib.ext.gone

object AdBanner {
    private const val DEBUG_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
    fun showBanner(activity: Activity, id: String, viewGroup: ViewGroup){
        if (!AdmobUtils.isShowAds){
            viewGroup.visibility = View.GONE
            return
        }
        val mId = if (AdmobUtils.isDebug) DEBUG_BANNER_ID else id
        val adRequest = AdRequest.Builder().build()
        val adView = AdView(activity)
        val layoutShimmer: ShimmerFrameLayout = ShimmerBannerBinding.inflate(LayoutInflater.from(activity)).shimmer
        layoutShimmer.startShimmer()
        viewGroup.addView(layoutShimmer)
        adView.apply {
            adUnitId = mId
            setAdSize(getAdSize(activity))
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    viewGroup.apply {
                        removeAllViews()
                        addView(adView)
                    }
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    viewGroup.gone()
                }
            }
        }
    }

    fun showBannerCollapsible(activity: Activity, id: String, viewGroup: ViewGroup, type: BannerCollapsibleType = BannerCollapsibleType.TOP){
        if (!AdmobUtils.isShowAds){
            viewGroup.visibility = View.GONE
            return
        }
        val mId = if (AdmobUtils.isDebug) DEBUG_BANNER_ID else id
        val adView = AdView(activity)
        val extras = Bundle().apply {
            putString("collapsible", type.value)
        }
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        val layoutShimmer: ShimmerFrameLayout = ShimmerBannerBinding.inflate(LayoutInflater.from(activity)).shimmer
        layoutShimmer.startShimmer()
        viewGroup.addView(layoutShimmer)
        adView.apply {
            adUnitId = mId
            setAdSize(getAdSize(activity))
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    viewGroup.apply {
                        removeAllViews()
                        addView(adView)
                    }
                }
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    viewGroup.gone()
                }
            }
        }
    }
    enum class BannerCollapsibleType(val value: String) {
        TOP("top"),
        BOTTOM("bottom")
    }

    private fun getAdSize(mActivity: Activity): AdSize {
        val display = mActivity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
    }

}