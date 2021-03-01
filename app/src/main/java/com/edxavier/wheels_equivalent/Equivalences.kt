package com.edxavier.wheels_equivalent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Equivalences extends AppCompatActivity{
    TextView original;

    float ancho_o;
    float perfil_o;
    float diametro_o;
    double rpm;
    String[] mWidths;
    String[] mPerfiles;
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equivalences2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        original = (TextView) findViewById(R.id.txtOriginal);
        TextView originalSpeed = (TextView) findViewById(R.id.txtSpeed);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_eq);

        analytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWidths = getResources().getStringArray(R.array.ancho_array);
        mPerfiles = getResources().getStringArray(R.array.perfil_array);
        analytics.logEvent("activity_equivalences", null);

        if(!Prefs.getBoolean("ads_removed", false)) {
            //MainActivity.requestAds(this);
            showAds();
        }

        Bundle b = getIntent().getExtras();
        if(b != null) {
            ArrayList<Equivalence> equivalenceArrayList = new ArrayList<>();
            ancho_o = b.getFloat("ancho_o");
            perfil_o = b.getFloat("perfil_o");
            diametro_o = b.getFloat("diametro_o");
            String carga = b.getString("carga_o");
            String vel = b.getString("velocidad_o");

            float [] rines = new float[]{diametro_o -2, diametro_o -1, diametro_o, diametro_o+1,diametro_o+2};
            for (float rin : rines) {
                for (String mWidth : mWidths) {
                    float width = Float.parseFloat(mWidth);
                    for (String mPerfil : mPerfiles) {
                        float perfil = Float.parseFloat(mPerfil);
                        float wdif = ancho_o - width;
                        if(wdif>=-30 && wdif <= 30) {
                            Equivalence equivalence = son_equivalentes(width, perfil, rin);
                            if (equivalence.isEquivalent) {
                                equivalence.rin = rin;
                                equivalence.perfil = perfil;
                                equivalence.width = width;
                                equivalenceArrayList.add(equivalence);
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
            AdapterEq adapterEq  = new AdapterEq(equivalenceArrayList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapterEq);

            original.setText(String.format(getString(R.string.original), String.format( Locale.getDefault(),"%.0f", ancho_o), String.format( Locale.getDefault(),"%.0f", perfil_o), String.format( Locale.getDefault(),"%.0f", diametro_o), carga, vel));
            originalSpeed.setText(getString(R.string.eq_explicacion2_1)
                    +  String.format(Locale.getDefault(), "%.0f" ,rpm) + getString(R.string.eq_explicacion2_2));
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    Equivalence son_equivalentes(float ancho_n,   float perfil_n, float diametro_n){
        Equivalence equivalence = new Equivalence();
        float diametro_total_orig = (float) Math.round( (ancho_o * (perfil_o/100)) * 2 + (diametro_o * 25.4));
        double v = (ancho_n * (perfil_n / 100)) * 2 + (diametro_n * 25.4);

        float diametro_total_nuevo = (float) Math.round(v);
        double diametro_original = ((ancho_o * (perfil_o / 100)) * 2 + (diametro_o * 25.4)) / 1000; //metros
        double diametro_nuevo = v / 1000; //metros

        float diferencia_porc = ((diametro_total_nuevo/diametro_total_orig)-1)*100;
        float diferencia_mm = diametro_total_nuevo - diametro_total_orig;
        equivalence.diference = diferencia_porc;
        equivalence.isEquivalent = diferencia_porc >= -3.0 && diferencia_porc <= 3.0;

        /*
            W = vel/diametro (rad/s)
            rpm = 60*W/2pi
            V = W * diametro (m/s)
            si  tomamos como base 100 kph y trbajamos con medidas del sistema internacional,
            habra de pasar la velociadad a m/s y el diametro a metros.
            ahora supongamos que la rueda original va a 100kph y queremos averiguar las rpm y asi
            averiguar la velocidad de la nueva rueda a esa misma rpm hacemos:
        */
        if(equivalence.isEquivalent) {
            //Convertir 100kph a m/s
            double vel = ((float) 100 * (float) 1000) / (float) 3600; // m/s
            //calcular la velocidad angular
            double w = vel / diametro_original;// rad/seg
            //Averigaur las rpm a 100kph para el diametro original
            rpm = (60 * w) / (Math.PI * 2); //rpm
            //teniendo las rpm del neumatico original a 100kph calculamos la velociada del nuevo a esas mismas rpm
            equivalence.speed = ((w * diametro_nuevo) * 3600) / 1000;

            equivalence.diameter = diametro_nuevo;
        }
        return equivalence;

    }

    private void showAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        final AdView adView =  findViewById(R.id.adView);
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
    }
/*

    @SuppressLint("InflateParams")
    private fun loadNativeAd(){
        val builder = AdLoader.Builder(requireContext(), getString(R.string.admob_native))
        builder.forNativeAd { nativeAd ->
            try {
                if (isAdded) {
                    val adBinding = AdNativeLayoutBinding.inflate(layoutInflater)
                    //val nativeAdview = AdNativeLayoutBinding.inflate(layoutInflater).root
                    binding.nativeAdFrameLayout.removeAllViews()
                    binding.nativeAdFrameLayout.addView(populateNativeAd(nativeAd, adBinding))
                }
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
                adIcon.setVisible()
                nativeAdView.iconView = adIcon
            }
            nativeAd.starRating?.let {
                adStartRating.rating = it.toFloat()
                adStartRating.setVisible()
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
            nativeAd.mediaContent?.let {
                adMedia.setMediaContent(it)
                adMedia.setVisible()
                adMedia.setImageScaleType(ImageView.ScaleType.FIT_XY)
                nativeAdView.mediaView = adMedia
            }
        }
        nativeAdView.setNativeAd(nativeAd)
        return nativeAdView
    }
 */

}
