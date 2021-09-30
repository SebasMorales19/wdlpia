package com.example.wdlpia;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    DatabaseReference nDatabase;

    DatabaseReference mHistorial;

    String Email;
    String  id;
    String id2;


    int Cl = 0;
    int Op = 1;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               // Toast.makeText(Home.this, "Suscrito a enviar a todos", Toast.LENGTH_SHORT).show();
            }
        });

        Switch Puerta  = findViewById(R.id.SPuerta);

        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef    = database.getReference("Door");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nDatabase = FirebaseDatabase.getInstance().getReference();

        // Para el historial
        mHistorial =  FirebaseDatabase.getInstance().getReference();








        Puerta.setOnCheckedChangeListener((buttonView, b) -> {
                // el push es para crear el identificador
            if(b){
                Date currentTime = Calendar.getInstance().getTime();
                String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
                Log.d("myLOG0", currentTime.toString());
                String pamensajeOp = "Abieron Puerta: " + formattedDate + "  " + currentTime + "  " + Email;
               final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth =  c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                Toast.makeText(this, pamensajeOp, Toast.LENGTH_LONG).show();
                    String sYear = mYear +"";
                    String sMonth = mMonth + "";
                    String sDay = mDay+"";

                //aqui escribimos en Histrial
                Map<String, Object> historialOp =  new HashMap<>();
                //historialOp.put(sYear ,mYear);
                //historialOp.put(sMonth, mMonth);
                //historialOp.put(sDay,mDay);
                //Toast.makeText(this, sYear + "" + sMonth+ "" + sDay, Toast.LENGTH_SHORT).show();

                historialOp.put("AccesoP", pamensajeOp);
               //Con este creabamos el nodo Historial en caso de no exiistir
                //y con el push metiamos un nuevo id para la acción
                //mHistorial.child("Historial").push().setValue(historialOp);
                mHistorial.child("Historial").child(sYear).child(sMonth).child(sDay).push().setValue(historialOp);

                myRef.setValue(Op);
                llamarpuertaabierta();






            }else{
                Date currentTime = Calendar.getInstance().getTime();
                String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
                Log.d("myLOG0", currentTime.toString());
                String pamensajeCl = "Cerraron Puerta: " + formattedDate + "  " + currentTime + "  " + Email;
                Toast.makeText(this, pamensajeCl, Toast.LENGTH_LONG).show();
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth =  c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                String sYear = mYear +"";
                String sMonth = mMonth + "";
                String sDay = mDay+"";

                Map<String, Object> historialCl =  new HashMap<>();

                historialCl.put("AccesoP", pamensajeCl);
                //mHistorial.child("Historial").push().setValue(historialCl);
                mHistorial.child("Historial").child(sYear).child(sMonth).child(sDay).push().setValue(historialCl);
                myRef.setValue(Cl);
                llamarpuertaCerrada();

            }
        });


        getUserInfo();

    }

    private void llamarpuertaCerrada() {
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json  = new JSONObject();
        try {
           // String token = "cu317oJlSIedeJfiB6asPg:APA91bHVuh31QR5-vaqj4SOcC1jWqwPVWAneN_meZTP-DeL753_mI7IQcKjJ2STen_vimc5vq1ysewALAWy-dMXnU1iYUi0gFQxpfuBj0qZzJkOYcngBo6x7QBgFStKZ3VQYw0cu8h9g";
            json.put("to", "/topics/"+ "enviaratodos");
            JSONObject notificacion =  new JSONObject();
            notificacion.put("titulo", "Puerta Cerrada");
            notificacion.put("detalle", "Se ha cerrado la puerta, para ver más detalles ver la bitácora");
            json.put("data", notificacion);


            String URL ="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request =  new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String,String> header =  new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAUmsa63Y:APA91bHtAXLFtCGzOorKJO0kC006m4i_Vy5WNjg0rNMzz_HvTixOl281z67xUcqr8ntyS4oZY6ZNPB5NIyHA348qg_1WFQhqPvoz9-eiHLelPzZu_iVu-xD-UNlZWpVdE2bYzB6H1NZk");
                    return header;
                }
            };
            myrequest.add(request);

        }catch (JSONException e){

            e.printStackTrace();

        }
    }

    private void llamarpuertaabierta() {

        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json  = new JSONObject();
        try {

           // String token = "cu317oJlSIedeJfiB6asPg:APA91bHVuh31QR5-vaqj4SOcC1jWqwPVWAneN_meZTP-DeL753_mI7IQcKjJ2STen_vimc5vq1ysewALAWy-dMXnU1iYUi0gFQxpfuBj0qZzJkOYcngBo6x7QBgFStKZ3VQYw0cu8h9g";
            json.put("to", "/topics/"+ "enviaratodos");
            JSONObject notificacion =  new JSONObject();
            notificacion.put("titulo", "Puerta Abierta");
            notificacion.put("detalle", "Se ha abierto la puerta, para ver más detalles ver la bitácora");
            json.put("data", notificacion);


            String URL ="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request =  new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String,String> header =  new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAUmsa63Y:APA91bHtAXLFtCGzOorKJO0kC006m4i_Vy5WNjg0rNMzz_HvTixOl281z67xUcqr8ntyS4oZY6ZNPB5NIyHA348qg_1WFQhqPvoz9-eiHLelPzZu_iVu-xD-UNlZWpVdE2bYzB6H1NZk");
                    return header;

                }
            };
            myrequest.add(request);

        }catch (JSONException e){

            e.printStackTrace();

        }

    }

    //Método para mostrar y ocultar el menú
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.overflow, menu);

        return true;
    }

    //Método para asignar las funciones correspndientes a las opciones.
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.cerrar_sesion){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
            Toast.makeText(this, "Se cerro la sesion", Toast.LENGTH_SHORT).show();


        } else if(id == R.id.perfil){
            startActivity(new Intent(Home.this, ProfileActivity.class));
            finish();
        }else if (id  ==  R.id.Bitacora){
            startActivity(new Intent(Home.this, Historial.class));
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    private void getUserInfo(){
// Esto lo puedo usar para poder hacer el agregar cuentas que puedan usar la puerta
        id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                  Email = dataSnapshot.child("email").getValue().toString();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}