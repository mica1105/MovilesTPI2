package com.mika.movilestpi2;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.Date;

import static android.Manifest.permission.READ_SMS;

public class Sms extends Service {

    int x = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ContextCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Conceder Permisos", Toast.LENGTH_LONG).show();
        }

        Runnable tarea = new Runnable() {
            public void run(){
                try {
                    Uri sms = Telephony.Sms.CONTENT_URI;
                    ContentResolver cr = getContentResolver();

                    while (true) {
                        Cursor c = cr.query(sms, null, null, null, null);
                        if (c.getCount() > 0) {
                             int i = 0;
                             while (c.moveToNext() && i < 5) {
                                 i++;
                                 String nro = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                                 String fecha = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.DATE));
                                 long dateLong = Long.parseLong(fecha);
                                 Date date = new Date(dateLong);
                                 String contenido = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.BODY));
                                 Log.d("salida", "Sms del Número " + nro + ". Recibido el: " + date.toString() + ". Contenido: " + contenido);
                             }
                        }
                        Thread.sleep(9000);
                    }
                } catch(InterruptedException e) {
                Log.e("Error: ", e.getMessage());
                e.printStackTrace();
                }
            }
        };
        Thread trabajador = new Thread(tarea);
        trabajador.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
