package com.appli.vincent_nesrine.thesimpsons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class QuoteActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        GetQuoteServices.startActionGet_All_Quote(this);
        IntentFilter intentFilter = new IntentFilter(QUOTE_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new QuoteUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_quote);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new QuoteAdapter(getQuoteFromFile()));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static final String QUOTE_UPDATE = "com.appli.vincent_nesrine.thesimpsons.action.QUOTE_UPDATE";

    public class QuoteUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            QuoteAdapter quoteAdapt = (QuoteAdapter) rv.getAdapter();
            quoteAdapt.setNewQuote(getQuoteFromFile());
            Log.d("QuoteActivity", "" + getIntent().getAction());
        }
    }

    public JSONArray getQuoteFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"quote.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteHolder> {

        private JSONArray quote;

        QuoteAdapter(JSONArray quotes) {
            this.quote=quotes;
        }

        @Override
        public QuoteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            QuoteHolder quoteHold = new QuoteHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_quote_element, viewGroup, false));
            return quoteHold;
        }

        public void setNewQuote(JSONArray quotes) {
            this.quote=quotes;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(QuoteHolder quoteHolder, int i) {
            JSONObject object = null;
            try {
                object = quote.getJSONObject(i);
                String nom = object.getString("citation");
                quoteHolder.name.setText("\""+nom+"\"");

                String perso = object.getString("personnage");


                if(perso.equals("")){
                    quoteHolder.perso.setText("Pas d'auteur précisé");
                }
                else{
                    quoteHolder.perso.setText("  -"+perso);
                }
                String source = object.getString("source");
                if(source.equals("") || perso.equals("homer")){
                    quoteHolder.source.setText("Pas de source précisée");
                }

                else{
                    quoteHolder.source.setText(source);
                }

                if(source.equals("Saison 14 - Episode 2")){
                    quoteHolder.name.setText("\"Tais-toi lcerveau ou je te tue avec un coton tige!\"");
                    quoteHolder.source.setText("Saison 8 - Episode 18");
                }
            } catch (JSONException e){
                //Toast.makeText(getApplicationContext(), "pbJSON", Toast.LENGTH_LONG).show();
                quoteHolder.name.setText("erreur");
                quoteHolder.perso.setText("erreur");
                quoteHolder.source.setText("erreur");
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return quote.length();
        }


        public class QuoteHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView perso;
            public TextView source;

            public QuoteHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_quote_element_contenu);
                perso = (TextView) itemView.findViewById(R.id.rv_quote_element_perso);
                source = (TextView) itemView.findViewById(R.id.rv_quote_element_source);
            }
        }

    }
}
