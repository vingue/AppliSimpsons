package com.appli.vincent_nesrine.thesimpsons;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetSavoirServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ALL_SAVOIR = "com.appli.vincent_nesrine.thesimpsons.action.GET_ALL_SAVOIR";
    //private static final String ACTION_BAZ = "evrard_guerin.esiea.org.action.BAZ";

    // TODO: Rename parameters
    //private static final String EXTRA_PARAM1 = "evrard_guerin.esiea.org.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "evrard_guerin.esiea.org.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGet_All_Savoir(Context context/*, String param1, String param2*/) {
        Intent intent = new Intent(context, GetSavoirServices.class);
        intent.setAction(ACTION_GET_ALL_SAVOIR);
        context.startService(intent);
    }


    public GetSavoirServices() {
        super("GetSavoirServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_SAVOIR.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                //final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionGet_All_Savoir();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "pbConvert", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void handleActionGet_All_Savoir(/*String param1, String param2*/) {
        // TODO: Handle action Foo
        Log.d("GetSavoirServices", "Thread service name:" + Thread.currentThread().getName());
        URL url = null;
        try {
            //url = new URL("http://veng-the-simpson.byethost9.com/symfony/web/test.json");
            url = new URL("http://projetblogdevoyage.free.fr/web/savoir.json");
            //url = new URL("http://veng-the-simpson.byethost9.com/symfony/web/app.php/test.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            //Toast.makeText(getApplicationContext(), conn.getResponseCode(), Toast.LENGTH_LONG).show();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "savoir.json"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SavoirActivity.SAVOIR_UPDATE));
            }
            //Toast.makeText(getApplicationContext(), "test2", Toast.LENGTH_LONG).show();
        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "pbURL", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "pbIO", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}