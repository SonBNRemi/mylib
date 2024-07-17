package com.sonbn.remi.mylib.ads

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.sonbn.remi.mylib.R
import com.sonbn.remi.mylib.ext.gone

object AdNative {
    private const val DEBUG_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"

    fun loadNative(activity: Activity, id: String) {
        val mId = if (AdmobUtils.isDebug) DEBUG_NATIVE_ID else id
        if (!AdmobUtils.isShowAds) {
            return
        }
        val adLoader = AdLoader.Builder(activity, mId)
            .forNativeAd { nativeAd ->

            }
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun showNative(
        nativeAd: NativeAd,
        nativeAdView: NativeAdView,
        viewGroup: ViewGroup
    ) {
        val mNativeAdView = setNativeAdView(nativeAd, nativeAdView)
        viewGroup.removeAllViews()
        viewGroup.addView(mNativeAdView)
    }

    fun loadAndShowNative(
        activity: Activity,
        id: String,
        shimmer: ShimmerFrameLayout,
        nativeAdView: NativeAdView,
        viewGroup: ViewGroup
    ) {
        val mId = if (AdmobUtils.isDebug) DEBUG_NATIVE_ID else id
        if (!AdmobUtils.isShowAds) {
            return
        }
        shimmer.startShimmer()
        viewGroup.addView(shimmer)
        val adLoader = AdLoader.Builder(activity, mId)
            .forNativeAd { nativeAd ->
                showNative(nativeAd, nativeAdView, viewGroup)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    viewGroup.gone()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun setNativeAdView(nativeAd: NativeAd, layoutNative: NativeAdView): NativeAdView {
        val nativeAdView: NativeAdView? = layoutNative.findViewById(R.id.native_ad_view)
        val primaryView: TextView? = layoutNative.findViewById(R.id.primary)
        val secondaryView: TextView? = layoutNative.findViewById(R.id.secondary)
        val ratingBar: RatingBar? = layoutNative.findViewById(R.id.rating_bar)
        val tertiaryView: TextView? = layoutNative.findViewById(R.id.body)
        val iconView: ImageView? = layoutNative.findViewById(R.id.icon)
        val mediaView: MediaView? = layoutNative.findViewById(R.id.media_view)
        val callToActionView: Button? = layoutNative.findViewById(R.id.cta)
        val background: ConstraintLayout? = layoutNative.findViewById(R.id.background)

        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        val headline = nativeAd.headline
        val body = nativeAd.body
        val cta = nativeAd.callToAction
        val starRating = nativeAd.starRating
        val icon = nativeAd.icon
        val secondaryText: String?

        nativeAdView!!.callToActionView = callToActionView
        nativeAdView.headlineView = primaryView
        nativeAdView.mediaView = mediaView
        secondaryView!!.visibility = View.VISIBLE
        if (adHasOnlyStore(nativeAd)) {
            nativeAdView.storeView = secondaryView
            secondaryText = store
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView.advertiserView = secondaryView
            secondaryText = advertiser
        } else {
            secondaryText = ""
        }

        primaryView!!.text = headline
        callToActionView!!.text = cta

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            secondaryView.visibility = View.GONE
            ratingBar!!.visibility = View.VISIBLE
            ratingBar.rating = starRating.toFloat()
            nativeAdView.starRatingView = ratingBar
        } else {
            secondaryView.text = secondaryText
            secondaryView.visibility = View.VISIBLE
            ratingBar!!.visibility = View.GONE
        }

        if (icon != null) {
            iconView!!.visibility = View.VISIBLE
            iconView.setImageDrawable(icon.drawable)
        } else {
            iconView!!.visibility = View.GONE
        }

        if (tertiaryView != null) {
            tertiaryView.text = body
            nativeAdView.bodyView = tertiaryView
        }

        nativeAdView.setNativeAd(nativeAd)
        return nativeAdView
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }
}