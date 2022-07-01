package com.example.proyecto_aula;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio2 extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio2);
        logo = findViewById(R.id.logoUFPSO);

        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.bottom_to_up);
        logo.setAnimation(animacion1);

        Button entrar = findViewById(R.id.entrarUsuario);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio2.this, generarQR.class);
                startActivity(intent);
            }
        });

        LinearLayout btn_invitado = findViewById(R.id.btn_invitado);
        btn_invitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio2.this, invitado.class);
                startActivity(intent);
            }
        });
    }
}