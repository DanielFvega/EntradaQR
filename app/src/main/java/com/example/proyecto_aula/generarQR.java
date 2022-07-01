package com.example.proyecto_aula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.proyecto_aula.Objetos.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class generarQR extends AppCompatActivity {
    Button btn_generarqr;
    EditText et_nombre, et_documento, et_codigo;
    ImageView img_verQR;
    CardView Datos;
    LinearLayout relativeLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Persona> listPersona = new ArrayList<Persona>();
    private Persona personaActual = new Persona();
    private boolean flagControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_qr);
        btn_generarqr = findViewById(R.id.btn_generador);
        et_nombre = findViewById(R.id.et_nombre);
        et_documento = findViewById(R.id.et_documento);
        et_codigo = findViewById(R.id.et_codigo);
        img_verQR = findViewById(R.id.img_verQR);
        relativeLayout = findViewById(R.id.relativeLayout);
        Datos = findViewById(R.id.datos);
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.bottom_to_up);
        Datos.setAnimation(animacion1);
        inicializarFirebase();


        btn_generarqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_nombre.getText().toString().equals("") && !et_documento.getText().toString().equals("") && !et_codigo.getText().toString().equals("")) {
                    //String dato es para guardar en el qr lo que se ingresó. Seria buena idea que hubiera otra app que pudiera conectarse a la base de datos y revisar el codigo.
                    String dato = et_nombre.getText().toString() + "/-/" + et_documento.getText().toString() + "/-/" + et_codigo.getText().toString();
                    //String codigo es para consultar en base de datos si existe el estudiante o administrativo, nada de gente rara en la U.
                    String codigo = et_codigo.getText().toString();
                    if (Integer.parseInt(codigo) > 999999 || Integer.parseInt(codigo) < 100000) {
                        toastIncorrecto("CODIGO INVÁLIDO");
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(et_codigo.getWindowToken(), 0);
                        return;
                    } else {
                        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    listPersona.clear();
                                    for (DataSnapshot objSnapshot : snapshot.getChildren()){
                                        Persona p = objSnapshot.getValue(Persona.class);
                                        listPersona.add(p);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (validarCodigo(listPersona)){
                            //Codigo exclusivo para agregar nuevos usuarios a la base de datos, tene cuidado Daniel :v
                            /*Persona p = new Persona();
                            p.setNombre(et_nombre.getText().toString());
                            p.setCodigo(et_codigo.getText().toString());
                            p.setDocumento(et_documento.getText().toString());
                            databaseReference.child("Persona").child(p.getCodigo()).setValue(p);
                            String user = dato + " " + codigo;*/
                            try {
                                //relativeLayout.setVisibility(View.GONE);
                                dato = personaActual.getNombre() + "/" + personaActual.getCodigo();
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.encodeBitmap(dato, BarcodeFormat.QR_CODE, 750, 750);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                                byte[] b = baos.toByteArray();
                                Intent intent = new Intent(generarQR.this, QrView.class);
                                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("nombre", personaActual.getNombre());
                                editor.putString("documento", personaActual.getDocumento());
                                editor.putString("imagen", Base64.encodeToString(b, Base64.DEFAULT));
                                editor.apply();
                                startActivity(intent);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (flagControl)
                                toastIncorrecto("NO EXISTE EL CODIGO");
                            else
                                flagControl = true;
                        }
                    }
                } else
                    toastIncorrecto("CAMPOS VACIOS ");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(et_nombre.getWindowToken(), 0);
            }
        });
    }

    private boolean validarCodigo(List<Persona> listPersona) {
        for (int i = 0; i < listPersona.size();i++){
            if(listPersona.get(i).getCodigo().equals(et_codigo.getText().toString())) {
                //y si usamos esta variable para guardar los datos? el problema es que entonces introducirlos manual el nombre y documento no tendria sentido...
                personaActual = listPersona.get(i);
                return true;
            }
        }
        for (int i = 0; i < listPersona.size();i++){
            if(listPersona.get(i).getCodigo().equals(et_codigo.getText().toString())) {
                //y si usamos esta variable para guardar los datos? el problema es que entonces introducirlos manual el nombre y documento no tendria sentido...
                personaActual = listPersona.get(i);
                return true;
            }
        }
        return false;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.toastincorrecto, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.mensajetoast);
        txtMensaje.setText(msg);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void volver(View view) {
        onBackPressed();
    }
}