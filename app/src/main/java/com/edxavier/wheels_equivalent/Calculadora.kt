package com.edxavier.wheels_equivalent

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.edxavier.wheels_equivalent.db.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.pixplicity.easyprefs.library.Prefs
import com.raizlabs.android.dbflow.config.FlowManager
import kotlinx.android.synthetic.main.activity_calculadora.*
import kotlinx.android.synthetic.main.card_nuevo.*
import kotlinx.android.synthetic.main.card_original.*
import kotlinx.android.synthetic.main.card_resultados.*
import kotlinx.android.synthetic.main.dialog_inputs.view.*
import java.util.*


class Calculadora : AppCompatActivity(), BillingProcessor.IBillingHandler {

    var bp: BillingProcessor? = null
    var PRODUCT = "remove_ads"

    private var analytics: FirebaseAnalytics? = null
    private var anchurasAdapter: ArrayAdapter<Ancho>? = null
    private var perfilesAdapter: ArrayAdapter<Perfil>? = null
    private var rinesAdapter: ArrayAdapter<Rin>? = null
    private var cargasAdapter: ArrayAdapter<Carga>? = null
    private var velocidadesAdapter: ArrayAdapter<Velocidad>? = null
    private var ancho_o: Float? = null
    private var perfil_o: Float = 0.toFloat()
    private var diametro_o: Float = 0.toFloat()
    private var icargaO: String? = null
    private var ivelocidadO: String? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onBillingInitialized() {
        try {
            Prefs.putBoolean("ads_removed", bp!!.isPurchased(PRODUCT))
            if (!Prefs.getBoolean("ads_removed", false)) {
                //Toast.makeText(this, "SHOW ADS ", Toast.LENGTH_LONG).show()
                requestInterstical()
                loadBanner()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR on Billing Initialized " + e.message, Toast.LENGTH_LONG).show()
        }

    }

    override fun onPurchaseHistoryRestored() {}

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {}

    override fun onBillingError(errorCode: Int, error: Throwable?) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MatAppTheme)
        setContentView(R.layout.activity_calculadora)
        bottom_bar.replaceMenu(R.menu.menu_main)
        setSupportActionBar(bottom_bar)
        analytics = FirebaseAnalytics.getInstance(this)

        bp = BillingProcessor.newBillingProcessor(this, BuildConfig.APP_BILLING_PUB_KEY, BuildConfig.MERCHANT_ID, this)
        bp?.initialize()

        InitData()

        preestablecerDatos()
        calcular_equivalencia()

        btnShowEquivalences.setOnClickListener {
            //float ancho_o = Float.valueOf(ancho_orig.getSelectedItem().toString());
            //float perfil_o = Float.valueOf(perfil_orig.getSelectedItem().toString());
            //float diametro_o = Float.valueOf(diametro_orig.getSelectedItem().toString());
            val bundle = Bundle()
            bundle.putFloat("ancho_o", ancho_o!!)
            bundle.putFloat("perfil_o", perfil_o)
            bundle.putFloat("diametro_o", diametro_o)
            bundle.putString("carga_o", icargaO)
            bundle.putString("velocidad_o", ivelocidadO)
            val intent = Intent(this, Equivalences::class.java)
            intent.putExtras(bundle)
            startActivityForResult(intent, 0)
        }

        fab_add.setOnClickListener { fab ->
            val dialog = MaterialDialog(this@Calculadora)
                    .positiveButton(text = "Guardar").positiveButton { mDialog ->
                        val customView = mDialog.getCustomView()
                        if(customView.input_perfil.text.toString().isNotEmpty()){
                            val perfil = customView.input_perfil.text.toString().toInt()
                            FlowManager.getModelAdapter<Perfil>(Perfil::class.java).save(Perfil(perfil))
                        }
                        if(customView.input_ancho.text.toString().isNotEmpty()) {
                            val ancho = customView.input_ancho.text.toString().toInt()
                            FlowManager.getModelAdapter<Ancho>(Ancho::class.java).save(Ancho(ancho))
                        }
                        if(customView.input_rin.text.toString().isNotEmpty()) {
                            val rin = customView.input_rin.text.toString().toInt()
                            FlowManager.getModelAdapter<Rin>(Rin::class.java).save(Rin(rin))
                        }

                        // Use the view instance, e.g. to set values or setup listeners
                        preestablecerDatos()
                    }
                    .customView(R.layout.dialog_inputs, scrollable = true)
            dialog.show()
        }

    }

    private fun preestablecerDatos() {
        perfilesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.cargarPerfiles())
        anchurasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.cargarAnchos())
        rinesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.cargarRines())
        cargasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.cargarCargas())
        velocidadesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.cargarVelocidades())



        original_perfil2.setAdapter(perfilesAdapter!!)
        original_ancho2.setAdapter(anchurasAdapter!!)
        original_diametro2.setAdapter(rinesAdapter!!)
        original_carga.setAdapter(cargasAdapter!!)
        original_velocidad.setAdapter(velocidadesAdapter!!)

        nuevo_perfil2.setAdapter(perfilesAdapter!!)
        nuevo_ancho2.setAdapter(anchurasAdapter!!)
        nuevo_diametro2.setAdapter(rinesAdapter!!)
        nuevo_carga.setAdapter(cargasAdapter!!)
        nuevo_velocidad.setAdapter(velocidadesAdapter!!)

        original_perfil2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia() }
        original_ancho2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        original_diametro2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        original_carga.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        original_velocidad.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}

        nuevo_perfil2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        nuevo_ancho2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        nuevo_diametro2.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        nuevo_carga.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}
        nuevo_velocidad.setOnItemSelectedListener { _, _, _, _ ->  calcular_equivalencia()}

        val npi = Prefs.getInt("npi", 4)
        val nai = Prefs.getInt("nai", 4)
        val ndi = Prefs.getInt("ndi", 4)
        val nci = Prefs.getInt("nci", 4)
        val nvi = Prefs.getInt("nvi", 4)

        val opi = Prefs.getInt("opi", 4)
        val oai = Prefs.getInt("oai", 4)
        val odi = Prefs.getInt("odi", 4)
        val oci = Prefs.getInt("oci", 4)
        val ovi = Prefs.getInt("ovi", 4)

        try {
            original_diametro2.selectedIndex = odi
            original_perfil2.selectedIndex = opi
            original_ancho2.selectedIndex = oai
            original_carga.selectedIndex = oci
            original_velocidad.selectedIndex = ovi
            /**/
            nuevo_diametro2.selectedIndex = ndi
            nuevo_perfil2.selectedIndex = npi
            nuevo_ancho2.selectedIndex = nai
            nuevo_carga.selectedIndex = nci
            nuevo_velocidad.selectedIndex = nvi
        } catch (ignored: Exception) {
        }
    }

    fun loadBanner(){
        if (!Prefs.getBoolean("ads_removed", false)) {

            adView.visibility = View.GONE
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adView.visibility = View.VISIBLE
                }
            }
        } else {
            adView.visibility = View.GONE
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
      return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_contact) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "edxavier05@gmail.com", null))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            intent.putExtra(Intent.EXTRA_TEXT, "Escriba sus comentarios, sugerencias o reporte de error")

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
            }

            analytics?.logEvent("action_contact", null)
            return true
        }
        if (id == R.id.action_share) {
            analytics?.logEvent("action_share", null)
            //bp.consumePurchase(PRODUCT);
            //Prefs.putBoolean("ads_removed", false);
            try {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Vuelos EAAI")
                var sAux = "\nMe gustaría recomendarte esta aplicación\n\n"
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + packageName + " \n\n"
                i.putExtra(Intent.EXTRA_TEXT, sAux)
                startActivity(Intent.createChooser(i, "Compartir usando"))
            } catch (e: Exception) {
            }

            return true
        }
        if (id == R.id.action_rate) {
            analytics?.logEvent("action_rate", null)
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
            }

            return true
        }
        if (id == R.id.privacy_policy) {
            analytics?.logEvent("privacy_policy_view", null)
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/edxavier/Wheels_equivalent/blob/master/Privacy%20Policy.md")))
            return true
        }
        if (id == R.id.action_help) {
            val intent = Intent(this, Help::class.java)
            startActivityForResult(intent, 0)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    internal fun InitData() {
        InitDB.InitPerfil()
        InitDB.InitAncho()
        InitDB.InitRines()
        InitDB.InitCargas()
        InitDB.InitVelocidades()
    }

    internal fun calcular_equivalencia() {

        Prefs.putInt("opi", original_perfil2.selectedIndex)
        Prefs.putInt("oai", original_ancho2.selectedIndex)
        Prefs.putInt("odi", original_diametro2.selectedIndex)
        Prefs.putInt("oci", original_carga.selectedIndex)
        Prefs.putInt("ovi", original_velocidad.selectedIndex)

        Prefs.putInt("npi", nuevo_perfil2.selectedIndex)
        Prefs.putInt("nai", nuevo_ancho2.selectedIndex)
        Prefs.putInt("ndi", nuevo_diametro2.selectedIndex)
        Prefs.putInt("nci", nuevo_carga.selectedIndex)
        Prefs.putInt("nvi", nuevo_velocidad.selectedIndex)

        ancho_o = java.lang.Float.valueOf(anchurasAdapter!!.getItem(original_ancho2.selectedIndex)!!.toString())
        perfil_o = java.lang.Float.valueOf(perfilesAdapter!!.getItem(original_perfil2.selectedIndex)!!.toString())
        diametro_o = java.lang.Float.valueOf(rinesAdapter!!.getItem(original_diametro2.selectedIndex)!!.toString())
        icargaO = cargasAdapter!!.getItem(original_carga.selectedIndex)!!.toString()
        ivelocidadO = velocidadesAdapter!!.getItem(original_velocidad.selectedIndex)!!.toString()

        val vcargaO = cargasAdapter!!.getItem(original_carga.selectedIndex)!!.valor
        val vvelocidadO = velocidadesAdapter!!.getItem(original_velocidad.selectedIndex)!!.valor

        val ancho_n = java.lang.Float.valueOf(anchurasAdapter!!.getItem(nuevo_ancho2.selectedIndex)!!.toString())
        val perfil_n = java.lang.Float.valueOf(perfilesAdapter!!.getItem(nuevo_perfil2.selectedIndex)!!.toString())
        val diametro_n = java.lang.Float.valueOf(rinesAdapter!!.getItem(nuevo_diametro2.selectedIndex)!!.toString())
        val icargaN = cargasAdapter!!.getItem(nuevo_carga.selectedIndex)!!.toString()
        val ivelocidadN = velocidadesAdapter!!.getItem(nuevo_velocidad.selectedIndex)!!.toString()

        val vcargaN = cargasAdapter!!.getItem(nuevo_carga.selectedIndex)!!.valor
        val vvelocidadN = velocidadesAdapter!!.getItem(nuevo_velocidad.selectedIndex)!!.valor


        val diametro_total_orig = Math.round(ancho_o!! * (perfil_o / 100) * 2 + diametro_o * 25.4).toFloat()
        val diametro_total_nuevo = Math.round(ancho_n * (perfil_n / 100) * 2 + diametro_n * 25.4).toFloat()

        val diferencia_porc = (diametro_total_nuevo / diametro_total_orig - 1) * 100
        //val diferencia_mm = diametro_total_nuevo - diametro_total_orig


        val conc_velocidad = findViewById<TextView>(R.id.conclusion2)

        txt_medidas_nuevo.text = String.format(getString(R.string.nuevo), String.format(Locale.getDefault(), "%.0f", ancho_n), String.format(Locale.getDefault(), "%.0f", perfil_n), String.format(Locale.getDefault(), "%.0f", diametro_n), icargaN, ivelocidadN)
        txt_medidas_original.text = String.format(getString(R.string.original), String.format(Locale.getDefault(), "%.0f", ancho_o), String.format(Locale.getDefault(), "%.0f", perfil_o), String.format(Locale.getDefault(), "%.0f", diametro_o), icargaO, ivelocidadO)



        txt_diametro_original.text = String.format(getString(R.string.diameter), String.format(Locale.getDefault(), "%.0f", diametro_total_orig), String.format(Locale.getDefault(), "%.0f", java.lang.Float.valueOf(vcargaO)), vvelocidadO)
        txt_diametro_nuevo.text = String.format(getString(R.string.diameter), String.format(Locale.getDefault(), "%.0f", diametro_total_nuevo), String.format(Locale.getDefault(), "%.0f", java.lang.Float.valueOf(vcargaN)), vvelocidadN)

        var concl = resources.getString(R.string.dif_porc) + " " + String.format(Locale.getDefault(), "%.2f", diferencia_porc)

        if (diferencia_porc >= 0)
            concl += resources.getString(R.string.dif_porc_mas)
        else if (diferencia_porc < 0)
            concl += resources.getString(R.string.dif_porc_menos)

        if (diferencia_porc >= -3.0 && diferencia_porc <= 3.0) {
            concl += " " + resources.getString(R.string.dif_porc_si)
            conclusion.setTextColor(resources.getColor(R.color.md_green_100))
            conc_velocidad.setTextColor(resources.getColor(R.color.md_green_100))
        } else {
            concl += " " + resources.getString(R.string.dif_porc_no)
            conclusion.setTextColor(resources.getColor(R.color.md_yellow_100))
            conc_velocidad.setTextColor(resources.getColor(R.color.md_yellow_100))
        }

        conclusion.text = concl

        /*
            W = vel/diametro (rad/s)
            rpm = 60*W/2pi
            V = W * diametro (m/s)
            si  tomamos como base 100 kph y trbajamos con medidas del sistema internacional,
            habra de pasar la velociadad a m/s y el diametro a metros.
            ahora supongamos que la rueda original va a 100kph y queremos averiguar las rpm y asi
            averiguar la velocidad de la nueva rueda a esa misma rpm hacemos:
        */
        val vel = (100.toFloat() * 1000.toFloat() / 3600.toFloat()).toDouble() // m/s
        val diametro_original = (ancho_o!! * (perfil_o / 100) * 2 + diametro_o * 25.4) / 1000 //metros
        val diametro_nuevo = (ancho_n * (perfil_n / 100) * 2 + diametro_n * 25.4) / 1000 //metros
        val w = vel / diametro_original// rad/seg
        val rpm = 60 * w / (Math.PI * 2) //rpm

        //teniendo las rpm del neumatico original a 100kph calculamos la velociada del nuevo a esas mismas rpm
        val velocidad = w * diametro_nuevo * 3600 / 1000 //kmh
        txt_rpm.text = String.format(getString(R.string.rpm), String.format(Locale.getDefault(), "%.1f", rpm), String.format(Locale.getDefault(), "%.1f", 100.0f))
        txt_rpm_new.text = String.format(getString(R.string.rpm), String.format(Locale.getDefault(), "%.1f", rpm), String.format(Locale.getDefault(), "%.1f", velocidad))

        //txt_vel.setText(String.format(getString(R.string.velocity), String.format(Locale.getDefault(),"%.1f", 100.0f)));

        //txt_velN.setText(String.format(getString(R.string.velocity), String.format(Locale.getDefault(),"%.1f", velocidad)));

        conc_velocidad.text = getString(R.string.conc_vel) + " " + String.format("%.1f", velocidad) + " kph"

    }


    fun isTimeToAds(): Boolean {
        val ne = Prefs.getInt("num_show_interstical", 0)
        //val sa = Prefs.getInt("show_after", 0)
        //Log.e("EDER", String.valueOf(ne));
        //Log.e("EDER", String.valueOf(sa));
        Prefs.putInt("num_show_interstical", ne + 1)
        return if (Prefs.getInt("num_show_interstical", 0) >= Prefs.getInt("show_after", 5)) {
            Prefs.putInt("num_show_interstical", 0)
            val r = Random()
            val rnd = r.nextInt(7 - 5) + 7
            Prefs.putInt("show_after", rnd)
            true
        } else
            false

    }


    fun showInterstical() {
        mInterstitialAd?.let {
            if (mInterstitialAd!!.isLoaded) {
                mInterstitialAd!!.show()
            }
        }

    }

    fun requestInterstical() {
        val adRequest = AdRequest.Builder()
                .addTestDevice("0B307F34E3DDAF6C6CAB28FAD4084125")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mInterstitialAd = InterstitialAd(applicationContext)
        mInterstitialAd!!.setAdUnitId(applicationContext.resources.getString(R.string.id_interstical))

        if (!mInterstitialAd!!.isLoaded())
            mInterstitialAd!!.loadAd(adRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //showInterstical()
        if (isTimeToAds()) {
            showInterstical()
            requestInterstical()
        }

    }
}
