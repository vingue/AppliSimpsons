package com.appli.vincent_nesrine.thesimpsons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class GoodiesActivity extends ActionBarActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodies);
        GetGoodiesServices.startActionGet_All_Goodies(this);
        IntentFilter intentFilter = new IntentFilter(GOODIES_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new GoodiesUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_goodies);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new GoodiesAdapter(getGoodiesFromFile()));
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

    public static final String GOODIES_UPDATE = "com.appli.vincent_nesrine.thesimpsons.action.GOODIES_UPDATE";

    public class GoodiesUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            GoodiesAdapter goodiesAdapt = (GoodiesAdapter) rv.getAdapter();
            goodiesAdapt.setNewGoodies(getGoodiesFromFile());
            Log.d("SecondeActivity", "" + getIntent().getAction());
        }
    }

    public JSONArray getGoodiesFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"goodies.json");
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

    private class GoodiesAdapter extends RecyclerView.Adapter<GoodiesAdapter.GoodiesHolder> {

        private JSONArray goodies;

        GoodiesAdapter(JSONArray goodie) {
            this.goodies=goodie;
        }

        @Override
        public GoodiesHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GoodiesHolder goodiesHold = new GoodiesHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_goodies_element, viewGroup, false));
            return goodiesHold;
        }

        public void setNewGoodies(JSONArray goodie) {
            this.goodies=goodie;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(GoodiesHolder goodiesHolder, int i) {
            JSONObject object = null;
            try {
                object = goodies.getJSONObject(i);
                String nom = object.getString("title");
                goodiesHolder.name.setText(nom);
                String contenu = object.getString("contenu");
                goodiesHolder.contenu.setText(contenu);
                String photo = object.getString("photo");
                Picasso.with(GoodiesActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(goodiesHolder.image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return goodies.length();
        }


        public class GoodiesHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView contenu;
            public ImageView image;

            public GoodiesHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_goodies_element_name);
                contenu = (TextView) itemView.findViewById(R.id.rv_goodies_element_content);
                image = (ImageView) itemView.findViewById(R.id.rv_goodies_element_image);
            }
        }

    }
}
