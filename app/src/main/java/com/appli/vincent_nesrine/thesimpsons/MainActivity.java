package com.appli.vincent_nesrine.thesimpsons;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), getString(R.string.welcome), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(Html.fromHtml("<b>" + "Les créateurs:" + "</b>" + "<br></br><br></br>" + "Nesrine EVRARD" + "<br></br>" + "Vincent GUERIN" + "<br></br><br></br>" + "<b>" + "Pourquoi cette application ?" + "</b>" + "<br></br><br></br>" + "Etant tous les 2 de grands fans de la série américaine " + "<i>"+"\"The Simpsons\""+"</i>"+" et ayant déjà développé et mis un site en ligne sur ce sujet, nous avons par la suite décider de réaliser l'application de ce site." + "<br></br><br></br>" + "Vous pourrez maintenant tout savoir sur les Simpson en un clique sans devoir aller sur votre ordinateur ou pire à la bibiliothèque !"));
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ouh pinaise !",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goodiesList(View v) {
        //notification_test();
        Intent second= new Intent(this, GoodiesActivity.class);
        startActivity(second);
    }

    public void persoList(View v) {
        //notification_test();
        Intent second= new Intent(this, PersoActivity.class);
        startActivity(second);
    }

    public void savoirList(View v) {
        //notification_test();
        Intent second= new Intent(this, SavoirActivity.class);
        startActivity(second);
    }

    public void episodeList(View v) {
        //notification_test();
        Intent second= new Intent(this, EpisodeActivity.class);
        startActivity(second);
    }

    public void quoteList(View v) {
        //notification_test();
        Intent second= new Intent(this, QuoteActivity.class);
        startActivity(second);
    }

    public void serieAffiche(View v) {
        //notification_test();
        Intent second= new Intent(this, SerieActivity.class);
        startActivity(second);
    }


}
