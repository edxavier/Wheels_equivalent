package com.edxavier.wheels_equivalent

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryPurchasesParams
import com.edxavier.wheels_equivalent.db.WheelsDB
import com.edxavier.wheels_equivalent.db.entities.Ancho
import com.edxavier.wheels_equivalent.db.entities.Perfil
import com.edxavier.wheels_equivalent.db.entities.Rin
import com.edxavier.wheels_equivalent.screens.*
import com.edxavier.wheels_equivalent.ui.theme.Wheels_equivalentTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
class MainCompose : ComponentActivity(), PurchasesUpdatedListener, PurchasesResponseListener {

    private lateinit var billingClient: BillingClient
    // private val viewModel by viewModels<WheelViewModel>()
    private lateinit var viewModel: WheelViewModel
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MatAppTheme)
        billingClient =  BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()
        startBillingConnection()
        lifecycleScope.launch (Dispatchers.IO){
            viewModel = WheelViewModel(this@MainCompose)
            viewModel.initializeData(this@MainCompose)
        }
        if (!Prefs.getBoolean("ads_removed", false)) {
            requestInterstical()
        }
        setContent {
            Wheels_equivalentTheme(darkTheme = false, dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                val ctx = LocalContext.current
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                var selection by remember { mutableIntStateOf(0) }
                var expanded by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = {
                                    Text(
                                        text=ctx.getString(R.string.app_name),
                                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                                    )
                                },
                                scrollBehavior = scrollBehavior,
                                actions = {
                                    IconButton(onClick = {  showDialog() }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = null
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(ctx.getString(R.string.menu_help))
                                            },
                                            onClick = {
                                                expanded = false
                                                ctx.startActivity(Intent(ctx, Help::class.java))
                                                if (isTimeToAds() && !Prefs.getBoolean("ads_removed", false)) {
                                                    showInterstitial()
                                                    requestInterstical()
                                                }
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(ctx.getString(R.string.menu_share))
                                            },
                                            onClick = {
                                                expanded = false
                                                shareApp()
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = {
                                                Text(ctx.getString(R.string.menu_rate))
                                            },
                                            onClick = {
                                                expanded = false
                                                rateApp()
                                            },
                                        )
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomBar = {
                        if (!Prefs.getBoolean("ads_removed", false)) {
                            MyBannerAd(adSize = getAdSize())
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val state by viewModel.uiState.collectAsState()
                        val mContext = LocalContext.current
                        val scrollState  = rememberScrollState()
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Header()
                            CardWheelData(
                                cardTitle = mContext.getString(R.string.paso1),
                                cardSubTitle = "${state.stdWheel}",
                                data = state.data,
                                codePrefix = "o",
                                onCardItemSelected = { i, c ->
                                    saveIndexPref(index = i, code = c)
                                    if(selection==1){
                                        viewModel.getSuggestions()
                                    }
                                }
                            )
                            MultiToggleButton(
                                defaultSelected = 0,
                                toggleOptions = listOf(
                                    ctx.getString(R.string.compare), ctx.getString(R.string.suggestions)
                                ),
                                onToggleChange = { sel ->
                                    selection = sel
                                    if(sel == 1){
                                        viewModel.getSuggestions()
                                        if (isTimeToAds()) {
                                            showInterstitial()
                                            requestInterstical()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AnimatedVisibility(visible = selection == 0) {
                                Column() {
                                    CardWheelData(
                                        cardTitle = mContext.getString(R.string.paso2),
                                        cardSubTitle = "${state.newWheel}",
                                        data = state.data,
                                        codePrefix = "n",
                                        onCardItemSelected = { i, c ->
                                            saveIndexPref(index = i, code = c)
                                        }
                                    )
                                    ResultsCard(
                                        percDiff = state.percDifference,
                                        speedDiff = state.speedDifference,
                                        stdWheel = state.stdWheel,
                                        newWheel = state.newWheel,
                                        stdWheelDiameter = state.stdWheelDiameter,
                                        newWheelDiameter = state.newWheelDiameter
                                    )
                                }
                            }
                            AnimatedVisibility(visible = selection == 1) {
                                if(state.loading)
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(84.dp)
                                            .padding(12.dp)
                                    )
                                else
                                    SuggestionsScreen(viewModel)
                            }
                            /*if (!Prefs.getBoolean("ads_removed", false)) {
                                Spacer(modifier = Modifier.height(12.dp))
                                NativeMediumAd()
                                Spacer(modifier = Modifier.height(12.dp))
                            }*/
                        }

                    }
                }
            }
        }
    }

    private fun saveIndexPref(index:Int, code:String){
        Prefs.putInt(code, index)
        viewModel.setWheelsData()
    }
    private fun getAdSize(): AdSize {
        //Determine the screen width to use for the ad width.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    private fun showDialog(){
        val dao = WheelsDB.getInstance(this).wheelsDao()
        val dialog = MaterialDialog(this@MainCompose)
            .positiveButton(text = "Guardar").positiveButton { mDialog ->
                lifecycleScope.launch(Dispatchers.IO){
                    val customView = mDialog.getCustomView()
                    val input_perfil = customView.findViewById<EditText>(R.id.input_perfil)
                    val input_ancho = customView.findViewById<EditText>(R.id.input_ancho)
                    val input_rin = customView.findViewById<EditText>(R.id.input_rin)
                    if(input_perfil.text.toString().isNotEmpty()){
                        val perfil = input_perfil.text.toString().toInt()
                        dao.saveProfile(Perfil(perfil))
                    }
                    if(input_ancho.text.toString().isNotEmpty()) {
                        val ancho = input_ancho.text.toString().toInt()
                        dao.saveWidth(Ancho(ancho))
                    }
                    if(input_rin.text.toString().isNotEmpty()) {
                        val rin = input_rin.text.toString().toInt()
                        dao.saveRim(Rin(rin))
                    }
                    // Use the view instance, e.g. to set values or setup listeners
                    viewModel.loadData()
                }
            }
            .customView(R.layout.dialog_inputs, scrollable = true)
        dialog.show()
    }

    private fun shareApp(){
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
    }

    private fun rateApp(){
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
    }
    private fun isTimeToAds(): Boolean {
        val ne = Prefs.getInt("num_show_interstical", 0)
        Prefs.putInt("num_show_interstical", ne + 1)
        return if (Prefs.getInt("num_show_interstical", 0) >= Prefs.getInt("show_after", 2)) {
            Prefs.putInt("num_show_interstical", 0)
            val r = Random()
            val rnd = r.nextInt(4 - 2) + 2
            Prefs.putInt("show_after", rnd)
            true
        } else
            false

    }
    private fun showInterstitial() {
        mInterstitialAd?.show(this)
    }
    private fun requestInterstical() {
        val adUnitId = applicationContext.resources.getString(R.string.id_interstical)
        InterstitialAd.load(this, adUnitId, AdRequest.Builder().build(), object:
            InterstitialAdLoadCallback(){
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                mInterstitialAd = p0
            }
        })
    }
    fun startBillingConnection() {

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                startBillingConnection()
            }
        })
    }
    fun queryPurchases() {
        val params2 = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
        billingClient.queryPurchasesAsync(params2.build(), this)
    }
    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        Log.e("EDER",  "onPurchasesUpdated")
        p1?.forEach {
            print(it.purchaseState)
        }
    }

    override fun onQueryPurchasesResponse(p0: BillingResult, p1: MutableList<Purchase>) {
        p1.forEach {
            if(it.purchaseState == Purchase.PurchaseState.PURCHASED){
                Prefs.putBoolean("ads_removed", true)
            }
        }
    }
}


