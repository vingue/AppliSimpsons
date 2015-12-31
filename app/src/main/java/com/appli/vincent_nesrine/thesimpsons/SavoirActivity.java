package com.appli.vincent_nesrine.thesimpsons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SavoirActivity extends ActionBarActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savoir);
        GetSavoirServices.startActionGet_All_Savoir(this);
        IntentFilter intentFilter = new IntentFilter(SAVOIR_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new SavoirUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_savoir);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new SavoirAdapter(getSavoirFromFile()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetSavoirServices.startActionGet_All_Savoir(this);
        IntentFilter intentFilter = new IntentFilter(SAVOIR_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new SavoirUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_savoir);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new SavoirAdapter(getSavoirFromFile()));
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

    public static final String SAVOIR_UPDATE = "com.appli.vincent_nesrine.thesimpsons.action.SAVOIR_UPDATE";

    public class SavoirUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SavoirAdapter savoirAdapt = (SavoirAdapter) rv.getAdapter();
            savoirAdapt.setNewSavoir(getSavoirFromFile());
            Log.d("SecondeActivity", "" + getIntent().getAction());
        }
    }

    public JSONArray getSavoirFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"savoir.json");
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

    private class SavoirAdapter extends RecyclerView.Adapter<SavoirAdapter.SavoirHolder> {

        private JSONArray savoir;

        SavoirAdapter(JSONArray savoirs) {
            this.savoir=savoirs;
        }

        @Override
        public SavoirHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            SavoirHolder savoirHold = new SavoirHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_savoir_element, viewGroup, false));
            return savoirHold;
        }

        public void setNewSavoir(JSONArray savoirs) {
            this.savoir=savoirs;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(SavoirHolder savoirHolder, int i) {
            JSONObject object = null;
            try {
                object = savoir.getJSONObject(i);
                String nom = object.getString("title");
                savoirHolder.name.setText(nom);
                String contenu = object.getString("contenu");
                savoirHolder.contenu.setText(contenu);
                String photo = object.getString("photo");
                Picasso.with(SavoirActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(savoirHolder.image);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return savoir.length();
        }


        public class SavoirHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView contenu;
            public ImageView image;

            public SavoirHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_savoir_element_name);
                contenu = (TextView) itemView.findViewById(R.id.rv_savoir_element_content);
                image = (ImageView) itemView.findViewById(R.id.rv_savoir_element_image);
            }
        }

    }
}
