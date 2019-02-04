package com.edxavier.wheels_equivalent;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.edxavier.wheels_equivalent.db.Ancho;
import com.edxavier.wheels_equivalent.db.Carga;
import com.edxavier.wheels_equivalent.db.DataHelper;
import com.edxavier.wheels_equivalent.db.Perfil;
import com.edxavier.wheels_equivalent.db.Rin;
import com.edxavier.wheels_equivalent.db.Velocidad;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, BillingProcessor.IBillingHandler, MaterialSpinner.OnItemSelectedListener {
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppCompatImageView image;
    Button showEquiv;
    MaterialSpinner original_perfil2, original_ancho2, original_diametro2, original_carga, original_velocidad;
    MaterialSpinner nuevo_perfil2, nuevo_ancho2, nuevo_diametro2, nuevo_carga, nuevo_velocidad;

    public BillingProcessor bp;
    public static String PRODUCT = "remove_ads";
    private FirebaseAnalytics analytics;
    Menu toolbar_menu;
    AdView mAdView, mAdView2;
    private Float ancho_o;
    private float perfil_o;
    private float diametro_o;
    private String icargaO;
    private String ivelocidadO;
    private ArrayAdapter<Ancho> anchurasAdapter;
    private ArrayAdapter<Perfil> perfilesAdapter;
    private ArrayAdapter<Rin> rinesAdapter;
    private ArrayAdapter<Carga> cargasAdapter;
    private ArrayAdapter<Velocidad> velocidadesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitData();
        analytics = FirebaseAnalytics.getInstance(this);
        image =  findViewById(R.id.image);
        showEquiv = (Button) findViewById(R.id.btnShowEquivalences);
        //image.setImageResource(R.drawable.pic3);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        bp = BillingProcessor.newBillingProcessor(this, BuildConfig.APP_BILLING_PUB_KEY, BuildConfig.MERCHANT_ID, this);
        bp.initialize();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        //setPalette();
        original_perfil2 =  findViewById(R.id.original_perfil2);
        original_ancho2 =  findViewById(R.id.original_ancho2);
        original_diametro2 =  findViewById(R.id.original_diametro2);
        original_carga =  findViewById(R.id.original_carga);
        original_velocidad =  findViewById(R.id.original_velocidad);

        nuevo_perfil2 =  findViewById(R.id.nuevo_perfil2);
        nuevo_ancho2 =  findViewById(R.id.nuevo_ancho2);
        nuevo_diametro2 =  findViewById(R.id.nuevo_diametro2);
        nuevo_carga =  findViewById(R.id.nuevo_carga);
        nuevo_velocidad =  findViewById(R.id.nuevo_velocidad);


        // String[] ANCHO = getResources().getStringArray(R.array.ancho_array);
        // String[] DIAMETRO = getResources().getStringArray(R.array.diametro_array);
        // String[] PERFIL = getResources().getStringArray(R.array.perfil_array);

        perfilesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataHelper.cargarPerfiles());
        anchurasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataHelper.cargarAnchos());
        rinesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataHelper.cargarRines());
        cargasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataHelper.cargarCargas());
        velocidadesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataHelper.cargarVelocidades());

        original_perfil2.setAdapter(perfilesAdapter);
        original_ancho2.setAdapter(anchurasAdapter);
        original_diametro2.setAdapter(rinesAdapter);
        original_carga.setAdapter(cargasAdapter);
        original_velocidad.setAdapter(velocidadesAdapter);

        original_perfil2.setOnItemSelectedListener(this);
        original_ancho2.setOnItemSelectedListener(this);
        original_diametro2.setOnItemSelectedListener(this);
        original_carga.setOnItemSelectedListener(this);
        original_velocidad.setOnItemSelectedListener(this);

        nuevo_perfil2.setAdapter(perfilesAdapter);
        nuevo_ancho2.setAdapter(anchurasAdapter);
        nuevo_diametro2.setAdapter(rinesAdapter);
        nuevo_carga.setAdapter(cargasAdapter);
        nuevo_velocidad.setAdapter(velocidadesAdapter);

        nuevo_perfil2.setOnItemSelectedListener(this);
        nuevo_ancho2.setOnItemSelectedListener(this);
        nuevo_diametro2.setOnItemSelectedListener(this);
        nuevo_carga.setOnItemSelectedListener(this);
        nuevo_velocidad.setOnItemSelectedListener(this);

        int npi =  Prefs.getInt("npi", 4);
        int nai =  Prefs.getInt("nai", 4);
        int ndi =  Prefs.getInt("ndi", 4);
        int nci =  Prefs.getInt("nci", 4);
        int nvi =  Prefs.getInt("nvi", 4);

        int opi =  Prefs.getInt("opi", 4);
        int oai =  Prefs.getInt("oai", 4);
        int odi =  Prefs.getInt("odi", 4);
        int oci =  Prefs.getInt("oci", 4);
        int ovi =  Prefs.getInt("ovi", 4);

        try {
            original_diametro2.setSelectedIndex(odi);
            original_perfil2.setSelectedIndex(opi);
            original_ancho2.setSelectedIndex(oai);
            original_carga.setSelectedIndex(oci);
            original_velocidad.setSelectedIndex(ovi);
            /**/
            nuevo_diametro2.setSelectedIndex(ndi);
            nuevo_perfil2.setSelectedIndex(npi);
            nuevo_ancho2.setSelectedIndex(nai);
            nuevo_carga.setSelectedIndex(nci);
            nuevo_velocidad.setSelectedIndex(nvi);
        }catch (Exception ignored){}
        calcular_equivalencia();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(this);



        mAdView = findViewById(R.id.adView);
        mAdView2 =  findViewById(R.id.adView2);

        if(!Prefs.getBoolean("ads_removed", false)) {
            MainActivity.requestAds(this);
            mAdView.setVisibility(View.GONE);
            mAdView2.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if(Prefs.getBoolean("ads_removed", false)) {
                        mAdView.setVisibility(View.GONE);
                        mAdView2.setVisibility(View.GONE);
                    }else {
                        mAdView.setVisibility(View.VISIBLE);
                        mAdView2.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else {
            mAdView.setVisibility(View.GONE);
            mAdView2.setVisibility(View.GONE);
        }

        showEquiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //float ancho_o = Float.valueOf(ancho_orig.getSelectedItem().toString());
                //float perfil_o = Float.valueOf(perfil_orig.getSelectedItem().toString());
                //float diametro_o = Float.valueOf(diametro_orig.getSelectedItem().toString());
                Bundle bundle = new Bundle();
                bundle.putFloat("ancho_o", ancho_o);
                bundle.putFloat("perfil_o", perfil_o);
                bundle.putFloat("diametro_o", diametro_o);
                bundle.putString("carga_o", icargaO);
                bundle.putString("velocidad_o", ivelocidadO);
                Intent intent = new Intent(MainActivity.this, Equivalences.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar_menu = menu;
        MenuItem item = menu.findItem(R.id.action_remove_ads);
        if(Prefs.getBoolean("ads_removed", false)) {
            item.setVisible(false);
            mAdView.setVisibility(View.GONE);
            mAdView2.setVisibility(View.GONE);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_contact){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","edxavier05@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT   , "Escriba sus comentarios, sugerencias o reporte de error");

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
            analytics.logEvent("action_contact", null);
            return true;
        }
         if (id == R.id.action_remove_ads) {
             analytics.logEvent("action_remove_ads", null);
             if(bp!=null)
                 bp.purchase(this, PRODUCT);
             else
                 Toast.makeText(this, getString(R.string.rm_ad_err), Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_share) {
            analytics.logEvent("action_share", null);
            //bp.consumePurchase(PRODUCT);
            //Prefs.putBoolean("ads_removed", false);
            try
            { Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Vuelos EAAI");
                String sAux = "\nMe gustaría recomendarte esta aplicación\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+ getPackageName()+" \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Compartir usando"));
            }
            catch(Exception e)
            { }

            return true;
        }
        if (id == R.id.action_rate) {
            analytics.logEvent("action_rate", null);
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
            return true;
        }
        if (id == R.id.privacy_policy) {
            analytics.logEvent("privacy_policy_view", null);
            startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/edxavier/Wheels_equivalent/blob/master/Privacy%20Policy.md")));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void calcular_equivalencia(){

        Prefs.putInt("opi", original_perfil2.getSelectedIndex());
        Prefs.putInt("oai", original_ancho2.getSelectedIndex());
        Prefs.putInt("odi", original_diametro2.getSelectedIndex());
        Prefs.putInt("oci", original_carga.getSelectedIndex());
        Prefs.putInt("ovi", original_velocidad.getSelectedIndex());

        Prefs.putInt("npi", nuevo_perfil2.getSelectedIndex());
        Prefs.putInt("nai", nuevo_ancho2.getSelectedIndex());
        Prefs.putInt("ndi", nuevo_diametro2.getSelectedIndex());
        Prefs.putInt("nci", nuevo_carga.getSelectedIndex());
        Prefs.putInt("nvi", nuevo_velocidad.getSelectedIndex());

        ancho_o = Float.valueOf(anchurasAdapter.getItem(original_ancho2.getSelectedIndex()).toString());
        perfil_o = Float.valueOf(perfilesAdapter.getItem(original_perfil2.getSelectedIndex()).toString());
        diametro_o = Float.valueOf(rinesAdapter.getItem(original_diametro2.getSelectedIndex()).toString());
        icargaO = cargasAdapter.getItem(original_carga.getSelectedIndex()).toString();
        ivelocidadO = velocidadesAdapter.getItem(original_velocidad.getSelectedIndex()).toString();

        String vcargaO = cargasAdapter.getItem(original_carga.getSelectedIndex()).valor;
        String vvelocidadO = velocidadesAdapter.getItem(original_velocidad.getSelectedIndex()).getValor();

        float ancho_n = Float.valueOf(anchurasAdapter.getItem(nuevo_ancho2.getSelectedIndex()).toString());
        float perfil_n = Float.valueOf(perfilesAdapter.getItem(nuevo_perfil2.getSelectedIndex()).toString());
        float diametro_n = Float.valueOf(rinesAdapter.getItem(nuevo_diametro2.getSelectedIndex()).toString());
        String icargaN = cargasAdapter.getItem(nuevo_carga.getSelectedIndex()).toString();
        String ivelocidadN = velocidadesAdapter.getItem(nuevo_velocidad.getSelectedIndex()).toString();

        String vcargaN = cargasAdapter.getItem(nuevo_carga.getSelectedIndex()).valor;
        String vvelocidadN = velocidadesAdapter.getItem(nuevo_velocidad.getSelectedIndex()).getValor();


        float diametro_total_orig = (float) Math.round( (ancho_o * (perfil_o/100)) * 2 + (diametro_o * 25.4));
        float diametro_total_nuevo = (float) Math.round( (ancho_n * (perfil_n/100)) * 2 + (diametro_n * 25.4));

        float diferencia_porc = ((diametro_total_nuevo/diametro_total_orig)-1)*100;
        float diferencia_mm = diametro_total_nuevo - diametro_total_orig;

        TextView diametroTO =  findViewById(R.id.txt_diametro_original);
        TextView diametroTN =  findViewById(R.id.txt_diametro_nuevo);
        TextView conclusion =  findViewById(R.id.conclusion);

        TextView medidaO =  findViewById(R.id.txt_medidas_original);
        TextView medidaN =  findViewById(R.id.txt_medidas_nuevo);

        TextView txt_rpm =  findViewById(R.id.txt_rpm);
        //TextView txt_vel =  findViewById(R.id.txt_velocidad);

        TextView txt_rpmN =  findViewById(R.id.txt_rpm_new);
        //TextView txt_velN =  findViewById(R.id.txt_velocidad_new);

        TextView conc_velocidad =  findViewById(R.id.conclusion2);

        medidaN.setText(String.format(getString(R.string.nuevo), String.format( Locale.getDefault(),"%.0f", ancho_n), String.format( Locale.getDefault(),"%.0f", perfil_n), String.format( Locale.getDefault(),"%.0f", diametro_n), icargaN, ivelocidadN));
        medidaO.setText(String.format(getString(R.string.original), String.format( Locale.getDefault(),"%.0f", ancho_o), String.format( Locale.getDefault(),"%.0f", perfil_o), String.format( Locale.getDefault(),"%.0f", diametro_o), icargaO, ivelocidadO));



        diametroTO.setText(String.format(getString(R.string.diameter), String.format(Locale.getDefault(),"%.0f",diametro_total_orig), String.format(Locale.getDefault(),"%.0f", Float.valueOf(vcargaO)), vvelocidadO ));
        diametroTN.setText(String.format(getString(R.string.diameter), String.format(Locale.getDefault(),"%.0f",diametro_total_nuevo), String.format(Locale.getDefault(),"%.0f",Float.valueOf(vcargaN)), vvelocidadN ));

        String concl = getResources().getString(R.string.dif_porc) +" "+ String.format(Locale.getDefault(),"%.2f", diferencia_porc);

        if(diferencia_porc >= 0)
            concl +=  getResources().getString(R.string.dif_porc_mas);
        else if(diferencia_porc < 0)
            concl += getResources().getString(R.string.dif_porc_menos);

        if(diferencia_porc >= -3.0 && diferencia_porc <= 3.0) {
            concl += " "+ getResources().getString(R.string.dif_porc_si);
            conclusion.setTextColor(getResources().getColor(R.color.md_teal_100));
            conc_velocidad.setTextColor(getResources().getColor(R.color.md_teal_100));
        }else {
            concl += " "+ getResources().getString(R.string.dif_porc_no);
            conclusion.setTextColor(getResources().getColor(R.color.md_yellow_300));
            conc_velocidad.setTextColor(getResources().getColor(R.color.md_yellow_300));
        }

        conclusion.setText(concl);

        /*
            W = vel/diametro (rad/s)
            rpm = 60*W/2pi
            V = W * diametro (m/s)
            si  tomamos como base 100 kph y trbajamos con medidas del sistema internacional,
            habra de pasar la velociadad a m/s y el diametro a metros.
            ahora supongamos que la rueda original va a 100kph y queremos averiguar las rpm y asi
            averiguar la velocidad de la nueva rueda a esa misma rpm hacemos:
        */
        double vel = ((float)100 * (float)1000)/ (float)3600; // m/s
        double diametro_original = ((ancho_o * (perfil_o/100)) * 2 + (diametro_o * 25.4))/1000; //metros
        double diametro_nuevo = ((ancho_n * (perfil_n/100)) * 2 + (diametro_n * 25.4))/1000; //metros
        double w = vel / diametro_original;// rad/seg
        double rpm = (60 * w)/(Math.PI*2); //rpm

        //teniendo las rpm del neumatico original a 100kph calculamos la velociada del nuevo a esas mismas rpm
        double velocidad = ((w * diametro_nuevo)*3600)/1000; //kmh
        txt_rpm.setText(String.format(getString(R.string.rpm), String.format(Locale.getDefault(),"%.1f", rpm), String.format(Locale.getDefault(),"%.1f", 100.0f)));
        txt_rpmN.setText(String.format(getString(R.string.rpm), String.format(Locale.getDefault(),"%.1f", rpm), String.format(Locale.getDefault(),"%.1f", velocidad)));

        //txt_vel.setText(String.format(getString(R.string.velocity), String.format(Locale.getDefault(),"%.1f", 100.0f)));

        //txt_velN.setText(String.format(getString(R.string.velocity), String.format(Locale.getDefault(),"%.1f", velocidad)));

        conc_velocidad.setText(getString(R.string.conc_vel) + " " + String.format("%.1f", velocidad) + " kph");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calcular_equivalencia();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    public static void requestAds(Context context) {
        int ne =  Prefs.getInt("num_show_interstical", 0);
        Prefs.putInt("num_show_interstical", ne + 1);
        if(Prefs.getInt("num_show_interstical", 0) == Prefs.getInt("show_after", 7)) {
            Prefs.putInt("num_show_interstical", 0);
            Random r = new Random();
            int Low = 6;int High = 12;
            int rnd = r.nextInt(High-Low) + Low;
            Prefs.putInt("show_after", rnd);

            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            final InterstitialAd mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.id_interstical));
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();
                }
            });
            mInterstitialAd.loadAd(adRequest);

        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        analytics.logEvent("ads_removed", null);
        Prefs.putBoolean("ads_removed", true);
        //Toast.makeText(this, "onProductPurchased ", Toast.LENGTH_LONG).show();
        if(toolbar_menu!=null) {
            MenuItem item = toolbar_menu.findItem(R.id.action_remove_ads);
            if (item != null)
                item.setVisible(false);
        }
        mAdView.setVisibility(View.GONE);
        mAdView2.setVisibility(View.GONE);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, getString(R.string.on_billing_error), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "ERROR: "+ error.getMessage() +" "+String.valueOf(errorCode), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {
        try {
            Prefs.putBoolean("ads_removed", bp.isPurchased(PRODUCT));
            if (Prefs.getBoolean("ads_removed", false)) {
                if(toolbar_menu!=null) {
                    MenuItem item = toolbar_menu.findItem(R.id.action_remove_ads);
                    if (item != null)
                        item.setVisible(false);
                }
                mAdView.setVisibility(View.GONE);
                mAdView2.setVisibility(View.GONE);
            }
            //Log.e("EDER_ads_removed", String.valueOf(Prefs.getBoolean("ads_removed", false)));
        }catch (Exception e){
            FirebaseCrash.logcat(Log.WARN, "onBillingInitialized", e.getMessage());
            Toast.makeText(this, "ERROR on Billing Initialized "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        calcular_equivalencia();
    }

    void InitData(){
        InitDB.InitPerfil();
        InitDB.InitAncho();
        InitDB.InitRines();
        InitDB.InitCargas();
        InitDB.InitVelocidades();
    }
}
