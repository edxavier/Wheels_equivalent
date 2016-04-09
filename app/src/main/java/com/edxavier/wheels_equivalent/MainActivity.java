package com.edxavier.wheels_equivalent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    Tracker tracker;
    Spinner  ancho_orig, perfil_orig, diametro_orig, ancho_new, perfil_new, diametro_new;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracker = ((MyApplication) getApplication()).getTracker();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        // Set screen name.
        tracker.setScreenName("Inicio");
        tracker.send(new HitBuilders.ScreenViewBuilder()
                .setNewSession()
                .build());
        // Habilitar las funciones de display.
        tracker.enableAdvertisingIdCollection(true);


        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(R.drawable.pic3);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setPalette();

        RobotoTextView titulo = (RobotoTextView)findViewById(R.id.titulo);
        titulo.setRobotoBold();

        RobotoTextView titulo2 = (RobotoTextView)findViewById(R.id.subtitulo1);
        titulo2.setRobotoMedium();
        RobotoTextView titulo3 = (RobotoTextView)findViewById(R.id.subtitulo2);
        titulo3.setRobotoMedium();

        ancho_orig = (Spinner) findViewById(R.id.original_ancho);
        ancho_orig.setSelection(2);
        perfil_orig = (Spinner) findViewById(R.id.original_perfil);
        perfil_orig.setSelection(9);
        diametro_orig = (Spinner) findViewById(R.id.original_diametro);
        diametro_orig.setSelection(4);

        ancho_new = (Spinner) findViewById(R.id.nuevo_ancho);
        ancho_new.setSelection(2);
        perfil_new = (Spinner) findViewById(R.id.nuevo_perfil);
        perfil_new.setSelection(9);
        diametro_new = (Spinner) findViewById(R.id.nuevo_diametro);
        diametro_new.setSelection(4);

        calcular_equivalencia();

        ancho_orig.setOnItemSelectedListener(this);
        perfil_orig.setOnItemSelectedListener(this);
        diametro_orig.setOnItemSelectedListener(this);

        ancho_new.setOnItemSelectedListener(this);
        perfil_new.setOnItemSelectedListener(this);
        diametro_new.setOnItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(this);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setPalette() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.primary_dark);
                int primary = getResources().getColor(R.color.primary);
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == R.id.action_help) {

             tracker.send(new HitBuilders.EventBuilder()
                     .setCategory("action")
                     .setAction("menu_help")
                     .setLabel("Ayuda desde menu")
                     .build());
             Intent intent = new Intent(this, Help.class);
             startActivity(intent);
            return true;
        }
        if (id == R.id.action_share) {
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("action")
                    .setAction("Share app")
                    .setLabel("Compartir app")
                    .build());
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
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("action")
                    .setAction("Rate app")
                    .setLabel("Calificar app")
                    .build());
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

        return super.onOptionsItemSelected(item);
    }


    void calcular_equivalencia(){

        float ancho_o = Float.valueOf(ancho_orig.getSelectedItem().toString());
        float perfil_o = Float.valueOf(perfil_orig.getSelectedItem().toString());
        float diametro_o = Float.valueOf(diametro_orig.getSelectedItem().toString());

        float ancho_n = Float.valueOf(ancho_new.getSelectedItem().toString());
        float perfil_n = Float.valueOf(perfil_new.getSelectedItem().toString());
        float diametro_n = Float.valueOf(diametro_new.getSelectedItem().toString());

        float diametro_total_orig = (float) Math.round( (ancho_o * (perfil_o/100)) * 2 + (diametro_o * 25.4));
        float diametro_total_nuevo = (float) Math.round( (ancho_n * (perfil_n/100)) * 2 + (diametro_n * 25.4));

        float diferencia_porc = ((diametro_total_nuevo/diametro_total_orig)-1)*100;
        float diferencia_mm = diametro_total_nuevo - diametro_total_orig;

        RobotoTextView diametroTO = (RobotoTextView) findViewById(R.id.txt_diametro_original);
        RobotoTextView diametroTN = (RobotoTextView) findViewById(R.id.txt_diametro_nuevo);
        RobotoTextView conclusion = (RobotoTextView) findViewById(R.id.conclusion);

        RobotoTextView medidaO = (RobotoTextView) findViewById(R.id.txt_medidas_original);
        RobotoTextView medidaN = (RobotoTextView) findViewById(R.id.txt_medidas_nuevo);

        RobotoTextView txt_rpm = (RobotoTextView) findViewById(R.id.txt_rpm);
        RobotoTextView txt_vel = (RobotoTextView) findViewById(R.id.txt_velocidad);

        RobotoTextView txt_rpmN = (RobotoTextView) findViewById(R.id.txt_rpm_new);
        RobotoTextView txt_velN = (RobotoTextView) findViewById(R.id.txt_velocidad_new);

        RobotoTextView conc_velocidad = (RobotoTextView) findViewById(R.id.conclusion2);

        medidaN.setText(getResources().getString(R.string.nuevo)+"\n" + ancho_new.getSelectedItem().toString()+"/"+perfil_new.getSelectedItem().toString()+" R"+diametro_new.getSelectedItem().toString());
        medidaO.setText(getResources().getString(R.string.original)+" \n" + ancho_orig.getSelectedItem().toString()+"/"+perfil_orig.getSelectedItem().toString()+" R"+diametro_orig.getSelectedItem().toString());
        medidaO.setRobotoBold();
        medidaN.setRobotoBold();

        diametroTO.setText("D = " + String.valueOf(diametro_total_orig) + " mm");
        diametroTO.setRobotoBold();
        diametroTN.setText("D = " + String.valueOf(diametro_total_nuevo) + " mm");
        diametroTN.setRobotoBold();

        String concl = getResources().getString(R.string.dif_porc) +" "+ String.format("%.2f", diferencia_porc);

        if(diferencia_porc >= 0)
            concl +=  getResources().getString(R.string.dif_porc_mas);
        else if(diferencia_porc < 0)
            concl += getResources().getString(R.string.dif_porc_menos);

        if(diferencia_porc >= -3.0 && diferencia_porc <= 3.0) {
            concl += " "+ getResources().getString(R.string.dif_porc_si);
            conclusion.setTextColor(getResources().getColor(R.color.md_green_500));
            conc_velocidad.setTextColor(getResources().getColor(R.color.md_green_500));
        }else {
            concl += " "+ getResources().getString(R.string.dif_porc_no);
            conclusion.setTextColor(getResources().getColor(R.color.md_red_500));
            conc_velocidad.setTextColor(getResources().getColor(R.color.md_red_500));
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
        txt_rpm.setText("rpm = " + String.format("%.1f", rpm));
        txt_rpm.setRobotoBold();
        txt_rpmN.setText("rpm = " + String.format("%.1f", rpm));
        txt_rpmN.setRobotoBold();

        txt_vel.setText("V = 100.0 kph");
        txt_vel.setTextColor(getResources().getColor(R.color.md_blue_900));
        txt_vel.setRobotoBold();

        txt_velN.setText("V = " + String.format("%.1f", velocidad) + " kph");
        txt_velN.setRobotoBold();
        txt_velN.setTextColor(getResources().getColor(R.color.md_blue_900));

        conc_velocidad.setText(getResources().getString(R.string.conc_vel) + " " + String.format("%.1f", velocidad) + " kph");
        conc_velocidad.setRobotoBold();

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
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("action")
                .setAction("fab_help")
                .setLabel("Ayuda desde fab boton")
                .build());
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }
}
