package com.edxavier.wheels_equivalent.screens

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.edxavier.wheels_equivalent.R
import com.edxavier.wheels_equivalent.databinding.AdNativeLayoutBinding
import com.edxavier.wheels_equivalent.visible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun MyBannerAd(modifier: Modifier = Modifier, adSize: AdSize) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.

                setAdSize(adSize)
                // adUnitId = "ca-app-pub-3940256099942544/6300978111"
                adUnitId = context.getString(R.string.id_banner)
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun NativeMediumAd() {
    AndroidView(factory = { context ->
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val adNativeInCall = AdNativeLayoutBinding.inflate(inflater)
        // Initialize NativeAdView
        val adView = adNativeInCall.root.also { adView ->
            adView.advertiserView = adNativeInCall.adAdvertiser
            adView.bodyView = adNativeInCall.adBodyText
            adView.callToActionView = adNativeInCall.adBtnCallToAction
            adView.headlineView = adNativeInCall.adHeadline
            adView.iconView = adNativeInCall.adIcon
            adView.starRatingView = adNativeInCall.adStartRating
        }

        // Request Ad
        val builder = AdLoader
            .Builder(context, context.getString(R.string.id_native))
            .withAdListener(object :AdListener(){
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adView.visible()
                    adNativeInCall.nativeLinearLayout.visible()
                }
            })

        builder.forNativeAd { nativeAd ->
            adNativeInCall.adHeadline.text = nativeAd.headline
            nativeAd.advertiser?.let {
                adNativeInCall.adAdvertiser.text = it
                adNativeInCall.adAdvertiser.visible()
            }
            nativeAd.icon?.let {
                // adIcon.setImageDrawable(it.drawable)
                adNativeInCall.adIcon.load(it.drawable){transformations(RoundedCornersTransformation(radius = 8f))}
                adNativeInCall.adIcon.visible()
            }
            nativeAd.starRating?.let {
                adNativeInCall.adStartRating.rating = it.toFloat()
                adNativeInCall.adStartRating.visible()
            }
            nativeAd.callToAction?.let {
                adNativeInCall.adBtnCallToAction.text = it
            }
            nativeAd.body?.let {
                adNativeInCall.adBodyText.text = it
            }
            adView.setNativeAd(nativeAd)
        }
        val adLoader = builder.build()
        adLoader.loadAd(AdRequest.Builder().build())
        return@AndroidView adView
    })
}
/*
*/
