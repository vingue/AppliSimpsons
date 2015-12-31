package com.appli.vincent_nesrine.thesimpsons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class EpisodeActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        GetEpisodeServices.startActionGet_All_Episode(this);
        IntentFilter intentFilter = new IntentFilter(EPISODE_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new EpisodeUpdate(),intentFilter);
        rv = (RecyclerView)findViewById(R.id.rv_episode);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new EpisodeAdapter(getEpisodeFromFile()));
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

    public static final String EPISODE_UPDATE = "com.appli.vincent_nesrine.thesimpsons.action.EPISODE_UPDATE";

    public class EpisodeUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            EpisodeAdapter episodeAdapt = (EpisodeAdapter) rv.getAdapter();
            episodeAdapt.setNewEpisode(getEpisodeFromFile());
            Log.d("SecondeActivity", "" + getIntent().getAction());
        }
    }

    public JSONArray getEpisodeFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"episode.json");
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

    private class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

        private JSONArray episode;

        EpisodeAdapter(JSONArray episodes) {
            this.episode=episodes;
        }

        @Override
        public EpisodeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            EpisodeHolder episodeHold = new EpisodeHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_episode_element, viewGroup, false));
            return episodeHold;
        }

        public void setNewEpisode(JSONArray episodes) {
            this.episode=episodes;
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(EpisodeHolder episodeHolder, int i) {
            JSONObject object = null;
            try {
                object = episode.getJSONObject(i);
                String nom = object.getString("titre");
                episodeHolder.name.setText(nom);
                String contenu = object.getString("contenu");
                episodeHolder.contenu.setText(contenu);
                String saison = object.getString("numero");
                episodeHolder.saison.setText("Saison: "+saison);
                String photo = object.getString("photo");
                Picasso.with(EpisodeActivity.this).load("http://projetblogdevoyage.free.fr/web/img/droid/" + photo).resize(100,100).into(episodeHolder.image);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return episode.length();
        }


        public class EpisodeHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView contenu;
            public TextView saison;
            public ImageView image;

            public EpisodeHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_episode_element_name);
                contenu = (TextView) itemView.findViewById(R.id.rv_episode_element_content);
                saison = (TextView) itemView.findViewById(R.id.rv_episode_element_saison);
                image = (ImageView) itemView.findViewById(R.id.rv_episode_element_image);
            }
        }

    }
}
