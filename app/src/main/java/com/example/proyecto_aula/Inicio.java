package com.example.proyecto_aula;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Inicio.this, Inicio2.class);
                startActivity(intent);
                finish();
            }
        };
        // tiempo de espera
        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 4000);
    }
}