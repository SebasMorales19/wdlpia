package com.example.wdlpia;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class wdlmss extends FirebaseMessagingService {


    @Override//con este token nos da el id del telefono y enviamos una notificacion especifica

    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.e("token", "mi token es: " + s);
        guardartoken(s);

    }

    private void guardartoken(String s) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        //busca la manera de buscar el id del usuario con el que estoy registado para mandar el nombre como nodo hijo
        ref.child("Sebastian").setValue(s);

    }


    @Override
    //recibe  todas las notificaciones y datos que se envian
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //de Esta menera recibimos información con los valores por default QUE USA FIREBASE




        //para recibir información de manera clave valor

        if(remoteMessage.getData().size()>0){
            String titulo =  remoteMessage.getData().get("titulo");
            String detalle =  remoteMessage.getData().get("detalle");
            mayorqueoreo(titulo,detalle);
        }

    }
//Este metodo se ejecuta solo cuando la version de android es mayor  a la de orero
    private void mayorqueoreo(String titulo, String detalle) {
       String id = "mensaje";
        NotificationManager nm  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this,id);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc  = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
           .setWhen(System.currentTimeMillis())
            .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clicknoti())
                .setContentInfo("nuevo");

        Random random =  new Random();
        int idNotify =  random.nextInt( 8000);
        assert nm != null;
        nm.notify(idNotify,builder.build());

    }
//para que al hacer click en la notificacion me mande a una pantalla y le ponemos banderas para ver si ya estamos dentro
    public PendingIntent clicknoti(){

        Intent nf =  new Intent(getApplicationContext(), Home.class);
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0,nf, 0);
    }
}
