package com.example.wdlpia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Historial extends AppCompatActivity {

    private List<accs> listacceso  = new ArrayList<accs>();
    ArrayAdapter<accs> arrayAdapteraccs;

    ListView listV_AccesoP;
    String VarParaMes="";
    String VarParaDia="";

    private ImageButton mDelete;

    DatabaseReference mHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Spinner combomeses;
        Spinner combodias;


        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth =  c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String sYear = mYear +"";
       String sMonth = mMonth + "";
        String sDay = mDay+"";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        ///////////////////////////////////////////////////////////////////////////////
//Identificamos cuales seran nuestros Spinners con su ID
         combomeses =  findViewById(R.id.spinnerMeses);
        combodias = findViewById(R.id.spinnerDias);

//ARRAYS ADAPTERS , SIRVEN PARA PODER DESPLEGAR LAS OPCCIONES EN ESTE CASO PARA MESES

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.combo_meses, android.R.layout.simple_spinner_item);
        combomeses.setAdapter(adapter);
        //Para el spiner DIAS
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.combo_dias, android.R.layout.simple_spinner_item);
        combodias.setAdapter(adapter1);

        combodias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ObjtDia = parent.getItemAtPosition(position).toString();
                VarParaDia = ObjtDia;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // PARA DARLE FUNCIONALIDAD A CADA ITEM DEL ARRAY
        combomeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String ObjtMes = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selccione:  " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(parent.getContext(), "Este es el ObjtMes : " + ObjtMes, Toast.LENGTH_SHORT).show();
                 if (ObjtMes.equals("Enero")){
                    VarParaMes = "0";
                }else if (ObjtMes.equals("Febrero")){
                    VarParaMes= "1";
                }else if (ObjtMes.equals("Marzo")){
                    VarParaMes = "2";
                }else if (ObjtMes.equals("Abril")){
                    VarParaMes = "3";
                }else if (ObjtMes.equals("Mayo")){
                    VarParaMes = "4";
                }else if (ObjtMes.equals("Junio")){
                    VarParaMes = "5";
                }else if (ObjtMes.equals("Julio")){
                    VarParaMes = "6";
                }else if (ObjtMes.equals("Agosto")){
                    VarParaMes = "7";
                }else if (ObjtMes.equals("Septiembre")){
                    VarParaMes = "8";
                }else if (ObjtMes.equals("Octubre")){
                    VarParaMes = "9";
                }else if (ObjtMes.equals("Noviembre")){
                    VarParaMes = "10";
                }
                else if (ObjtMes .equals("Diciembre")){
                       VarParaMes = "11";
                    // Toast.makeText(Historial.this, "HAHAH SI FUNCIONA OBT MES  "  + VarParaMes + "" + VarParaDia , Toast.LENGTH_SHORT).show();
                }

                //Aquí moveremos el print de cada nodo
                mHistorial =  FirebaseDatabase.getInstance().getReference();
                mHistorial.child("Historial").child(sYear).child(VarParaMes).child(VarParaDia).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnaptshot: snapshot.getChildren() ){
                            accs p = objSnaptshot.getValue(accs.class);
                            listacceso.add(p);

                            arrayAdapteraccs = new ArrayAdapter<accs>(Historial.this, android.R.layout.simple_list_item_1, listacceso);
                            listV_AccesoP.setAdapter(arrayAdapteraccs);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });        listV_AccesoP = findViewById(R.id.mensaje);
        mDelete = findViewById(R.id.bttDelete);

        //ACCIONES PARA EL SPINNER DE DIAS
       /* combodias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ObjtDia = parent.getItemAtPosition(position).toString();
                VarParaDia= ObjtDia;
                Toast.makeText(Historial.this, "HAHAH SI FUNCIONA OBT MES  "  + VarParaDia , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        //Este es para eliminar el historial
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistorial.child("Historial").removeValue();
                Intent intent = new Intent(Historial.this, Home.class);
                startActivity(intent);
                finish();
                Toast.makeText(Historial.this, "Se ha eliminado la bitácora exitosamente", Toast.LENGTH_SHORT).show();

            }
        });




        //Conexion con el nodo principal del proyecto

        //Aqui es donde se imprime en la arraylist



       /* mHistorial.child("Historial").child("2020").child(VarParaMes).child("6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objSnaptshot: snapshot.getChildren() ){
                    accs p = objSnaptshot.getValue(accs.class);
                    listacceso.add(p);

                    arrayAdapteraccs = new ArrayAdapter<accs>(Historial.this, android.R.layout.simple_list_item_1, listacceso);
                    listV_AccesoP.setAdapter(arrayAdapteraccs);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/






    }








    public void Return3(View view) {

        Intent intent = new Intent(Historial.this, Home.class);
        startActivity(intent);
        finish();
    }

}