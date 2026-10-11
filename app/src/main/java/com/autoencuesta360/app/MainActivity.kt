package com.autoencuesta360.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 40)
        }

        val titulo = TextView(this).apply {
            text = "AutoEncuesta 360°"
            textSize = 26f
        }

        val descripcion = TextView(this).apply {
            text = "Sistema de encuestas con confirmación diaria"
            textSize = 16f
        }

        val boton = Button(this).apply {
            text = "Confirmar encuesta del día"
            setOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    "Confirmación de prueba",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        layout.addView(titulo)
        layout.addView(descripcion)
        layout.addView(boton)

        setContentView(layout)
    }
}
