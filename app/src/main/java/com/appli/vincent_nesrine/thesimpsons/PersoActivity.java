package com.appli.vincent_nesrine.thesimpsons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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


public class PersoActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perso);
        GetPersoServices.startActionGet_All_Perso(this);
        IntentFilter intentFilter = new IntentFilter(PERSO_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PersoUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_perso);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new PersoAdapter(getPersoFromFile()));
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

    public static final String PERSO_UPDATE = "com.appli.vincent_nesrine.thesimpsons.action.PERSO_UPDATE";

    public class PersoUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PersoAdapter persoAdapt = (PersoAdapter) rv.getAdapter();
            persoAdapt.setNewPerso(getPersoFromFile());
            Log.d("PersoActivity", "" + getIntent().getAction());
        }
    }

    public JSONArray getPersoFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"perso.json");
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


    private class PersoAdapter extends RecyclerView.Adapter<PersoAdapter.PersoHolder> {

        private JSONArray perso;

        PersoAdapter(JSONArray persos) {
            this.perso=persos;
        }

        @Override
        public PersoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            JSONObject object = null;
            int categorie = 0;
            try {
                object = (JSONObject) perso.get(i);
                categorie = object.getInt("numero");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PersoHolder persoHold = null;
            if(categorie==1) {
                persoHold = new PersoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_perso_element_princ, viewGroup, false), 1);
            }
            if(categorie==2) {
                persoHold = new PersoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_perso_element_second, viewGroup, false), 2);
            }
            if(categorie==3) {
                persoHold = new PersoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_perso_element_guest, viewGroup, false), 3);
            }
            return persoHold;
        }

        public void setNewPerso(JSONArray persos) {
            this.perso=persos;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(PersoHolder persoHolder, int i) {
            JSONObject object = null;
            try {
                object = perso.getJSONObject(i);
                String nom = object.getString("titre");
                int categorie = object.getInt("numero");

                String contenu = object.getString("contenu");
                String photo = object.getString("photo");


                if (categorie==1) {
                    persoHolder.name.setText(nom + " / Principal\n");
                    persoHolder.contenu.setText(contenu);
                    Picasso.with(PersoActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(persoHolder.image);
                }



                if(categorie==2) {
                    persoHolder.name.setText(nom + " / Secondaire\n");
                    persoHolder.contenu.setText(contenu);
                    Picasso.with(PersoActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(persoHolder.image);
                }


                if(categorie==3) {
                    persoHolder.name.setText(nom + " / Guest\n");
                    persoHolder.contenu.setText(contenu);
                    Picasso.with(PersoActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(persoHolder.image);
                }



            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), "pbJSON", Toast.LENGTH_LONG).show();
                persoHolder.name.setText("erreur");

                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return perso.length();
        }


        public class PersoHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView contenu;
            public ImageView image;

            public PersoHolder(View itemView, int categorie) {
                super(itemView);

                if(categorie==1) {
                    name = (TextView) itemView.findViewById(R.id.rv_perso_element_name_princ);
                    contenu = (TextView) itemView.findViewById(R.id.rv_perso_element_contenu_princ);
                    image = (ImageView) itemView.findViewById(R.id.rv_perso_element_image_princ);
                }

                if(categorie==2) {
                    name = (TextView) itemView.findViewById(R.id.rv_perso_element_name_second);
                    contenu = (TextView) itemView.findViewById(R.id.rv_perso_element_contenu_second);
                    image = (ImageView) itemView.findViewById(R.id.rv_perso_element_image_second);
                }

                if(categorie==3) {
                    name = (TextView) itemView.findViewById(R.id.rv_perso_element_name_guest);
                    contenu = (TextView) itemView.findViewById(R.id.rv_perso_element_contenu_guest);
                    image= (ImageView) itemView.findViewById(R.id.rv_perso_element_image_guest);
                }
            }
        }

    }
}
