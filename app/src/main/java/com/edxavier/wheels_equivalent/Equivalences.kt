package com.edxavier.wheels_equivalent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edxavier.wheels_equivalent.databinding.ActivityCalculadoraBinding
import com.edxavier.wheels_equivalent.databinding.ActivityEquivalences2Binding
import com.edxavier.wheels_equivalent.databinding.AdNativeLayoutBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.pixplicity.easyprefs.library.Prefs
import java.util.*
import kotlin.collections.ArrayList

class Equivalences : AppCompatActivity() {

    lateinit var binding: ActivityEquivalences2Binding
    var original: TextView? = null
    var ancho_o = 0f
    var perfil_o = 0f
    var diametro_o = 0f
    var rpm = 0.0
    var mWidths: MutableList<String> = ArrayList()
    var mPerfiles: MutableList<String> = ArrayList()

    private var analytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquivalences2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_equivalences2)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        original = findViewById<View>(R.id.txtOriginal) as TextView
        val originalSpeed = findViewById<View>(R.id.txtSpeed) as TextView
        val container = findViewById<View>(R.id.container) as LinearLayout
        val recyclerView = findViewById<View>(R.id.recycler_eq) as RecyclerView
        analytics = FirebaseAnalytics.getInstance(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mWidths = resources.getStringArray(R.array.ancho_array).toMutableList()
        mPerfiles = resources.getStringArray(R.array.perfil_array).toMutableList()
        analytics!!.logEvent("activity_equivalences", null)
        if (!Prefs.getBoolean("ads_removed", false)) {
            //MainActivity.requestAds(this);
            loadNativeAd()
            //showAds()
        }
        val b = intent.extras
        if (b != null) {
            val equivalenceArrayList = ArrayList<Equivalence>()
            ancho_o = b.getFloat("ancho_o")
            perfil_o = b.getFloat("perfil_o")
            diametro_o = b.getFloat("diametro_o")
            val carga = b.getString("carga_o")
            val vel = b.getString("velocidad_o")
            val rines = floatArrayOf(diametro_o - 2, diametro_o - 1, diametro_o, diametro_o + 1, diametro_o + 2)
            for (rin in rines) {
                for (mWidth in mWidths) {
                    val width = mWidth.toFloat()
                    for (mPerfil in mPerfiles) {
                        val perfil = mPerfil.toFloat()
                        val wdif = ancho_o - width
                        if (wdif >= -30 && wdif <= 30) {
                            val equivalence = son_equivalentes(width, perfil, rin)
                            if (equivalence.isEquivalent) {
                                equivalence.rin = rin
                                equivalence.perfil = perfil
                                equivalence.width = width
                                equivalenceArrayList.add(equivalence)
                                //Log.e("EDER_EQ", String.valueOf(width) + "/" + String.valueOf(perfil) + " " + String.valueOf(rin));
                                //LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                //View v = vi.inflate(R.layout.row_equivalences, null);
                                //TextView suggestedTire = (TextView) v.findViewById(R.id.txtSuggested_tire);
                                //TextView diffTire = (TextView) v.findViewById(R.id.txtDifference);
                                //TextView speedTire = (TextView) v.findViewById(R.id.txtSpeed);
                                //suggestedTire.setText(String.format(Locale.getDefault(), "%.0f/%.0f R%.0f", width, perfil, rin));
                                //diffTire.setText(String.format(Locale.getDefault(), "%.1f", equivalence.diference));
                                //speedTire.setText(String.format(Locale.getDefault(), "%.1f kph", equivalence.speed));
                                //container.addView(v);
                            }
                        }
                    }
                }
            }
            val adapterEq = AdapterEq(equivalenceArrayList, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapterEq
            original!!.text = String.format(getString(R.string.original), String.format(Locale.getDefault(), "%.0f", ancho_o), String.format(Locale.getDefault(), "%.0f", perfil_o), String.format(Locale.getDefault(), "%.0f", diametro_o), carga, vel)
            originalSpeed.text = (getString(R.string.eq_explicacion2_1)
                    + String.format(Locale.getDefault(), "%.0f", rpm) + getString(R.string.eq_explicacion2_2))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun son_equivalentes(ancho_n: Float, perfil_n: Float, diametro_n: Float): Equivalence {
        val equivalence = Equivalence()
        val diametro_total_orig = Math.round(ancho_o * (perfil_o / 100) * 2 + diametro_o * 25.4).toFloat()
        val v = ancho_n * (perfil_n / 100) * 2 + diametro_n * 25.4
        val diametro_total_nuevo = Math.round(v).toFloat()
        val diametro_original = (ancho_o * (perfil_o / 100) * 2 + diametro_o * 25.4) / 1000 //metros
        val diametro_nuevo = v / 1000 //metros
        val diferencia_porc = (diametro_total_nuevo / diametro_total_orig - 1) * 100
        val diferencia_mm = diametro_total_nuevo - diametro_total_orig
        equivalence.diference = diferencia_porc
        equivalence.isEquivalent = diferencia_porc >= -3.0 && diferencia_porc <= 3.0

        /*
            W = vel/diametro (rad/s)
            rpm = 60*W/2pi
            V = W * diametro (m/s)
            si  tomamos como base 100 kph y trbajamos con medidas del sistema internacional,
            habra de pasar la velociadad a m/s y el diametro a metros.
            ahora supongamos que la rueda original va a 100kph y queremos averiguar las rpm y asi
            averiguar la velocidad de la nueva rueda a esa misma rpm hacemos:
        */if (equivalence.isEquivalent) {
            //Convertir 100kph a m/s
            val vel = (100.toFloat() * 1000.toFloat() / 3600.toFloat()).toDouble() // m/s
            //calcular la velocidad angular
            val w = vel / diametro_original // rad/seg
            //Averigaur las rpm a 100kph para el diametro original
            rpm = 60 * w / (Math.PI * 2) //rpm
            //teniendo las rpm del neumatico original a 100kph calculamos la velociada del nuevo a esas mismas rpm
            equivalence.speed = w * diametro_nuevo * 3600 / 1000
            equivalence.diameter = diametro_nuevo
        }
        return equivalence
    }

    private fun showAds() {
        val adRequest = AdRequest.Builder()
                .build()
        val adView = findViewById<AdView>(R.id.adView)
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = View.VISIBLE
            }
        }
    }


    @SuppressLint("InflateParams")
    private fun loadNativeAd(){
        val builder = AdLoader.Builder(this, getString(R.string.id_native))
        builder.forNativeAd { nativeAd ->
            try {
                val adBinding = AdNativeLayoutBinding.inflate(layoutInflater)
                //val nativeAdview = AdNativeLayoutBinding.inflate(layoutInflater).root
                binding.nativeAdFrameLayout.removeAllViews()
                binding.nativeAdFrameLayout.addView(populateNativeAd(nativeAd, adBinding))
            }catch (e:Exception){}
        }

        val adLoader = builder.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAd(nativeAd: NativeAd, adView: AdNativeLayoutBinding): NativeAdView {
        val nativeAdView = adView.root
        with(adView){
            adHeadline.text = nativeAd.headline
            nativeAdView.headlineView = adHeadline
            nativeAd.advertiser?.let {
                adAdvertiser.text = it
                nativeAdView.advertiserView = adAdvertiser
            }
            nativeAd.icon?.let {
                adIcon.setImageDrawable(it.drawable)
                //adIcon.load(it.drawable){transformations(RoundedCornersTransformation(radius = 8f))}
                adIcon.visibility = View.VISIBLE
                nativeAdView.iconView = adIcon
            }
            nativeAd.starRating?.let {
                adStartRating.rating = it.toFloat()
                adStartRating.visibility = View.VISIBLE
                nativeAdView.starRatingView = adStartRating
            }
            nativeAd.callToAction?.let {
                adBtnCallToAction.text = it
                nativeAdView.callToActionView = adBtnCallToAction
            }
            nativeAd.body?.let {
                adBodyText.text = it
                nativeAdView.bodyView = adBodyText
            }
        }
        nativeAdView.setNativeAd(nativeAd)
        return nativeAdView
    }

}