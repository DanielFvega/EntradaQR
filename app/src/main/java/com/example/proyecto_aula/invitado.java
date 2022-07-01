package com.example.proyecto_aula;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class invitado extends AppCompatActivity {

    LinearLayout botonInvitado;
    LinearLayout segundosdies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado);


        segundosdies = findViewById(R.id.segundosdies);

        segundosdies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "10 segundos", Toast.LENGTH_SHORT).show();
            }
        });

            botonInvitado = findViewById(R.id.botonInvitado);
            botonInvitado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(invitado.this, QrViewInvtado.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    public void volver1(View view) {
        onBackPressed();
    }
}