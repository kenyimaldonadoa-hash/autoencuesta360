package com.autoencuesta360.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val urlEncuesta =
        "https://forms.cloud.microsoft/Pages/ResponsePage.aspx?id=Z8k-Q1T08kmxMtB_gVReAr9AgUq3-TROilvn16wvQ_hUOEM4RzJTMUVBRFRaNEU0MFRJTkpMUEtSTSQlQCN0PWcu"

    private lateinit var estado: TextView

    private fun fechaHoy(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Date())
    }

    private fun encuestaConfirmada(): Boolean {
        val prefs = getSharedPreferences(
            "AutoEncuesta360",
            MODE_PRIVATE
        )
        return prefs.getString("fechaConfirmada", "") == fechaHoy()
    }

    private fun actualizarEstado() {
        estado.text = if (encuestaConfirmada()) {
            "Estado: Confirmada hoy (registro local)"
        } else {
            "Estado: Pendiente de confirmación"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 40)
        }

        val titulo = TextView(this).apply {
            text = "AutoEncuesta 360°"
            textSize = 28f
        }

        val descripcion = TextView(this).apply {
            text = "Sistema de encuestas con confirmación diaria"
            textSize = 16f
            setPadding(0, 15, 0, 30)
        }

        estado = TextView(this).apply {
            textSize = 17f
            setPadding(0, 10, 0, 25)
        }

        val botonAbrir = Button(this).apply {
            text = "ABRIR ENCUESTA DEL DÍA"

            setOnClickListener {
                if (encuestaConfirmada()) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Encuesta confirmada")
                        .setMessage(
                            "Ya registraste una confirmación hoy. ¿Deseas abrir el formulario nuevamente?"
                        )
                        .setPositiveButton("Abrir") { _, _ ->
                            abrirEncuesta()
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                } else {
                    abrirEncuesta()
                }
            }
        }

        val botonConfirmar = Button(this).apply {
            text = "CONFIRMAR ENVÍO DEL DÍA"

            setOnClickListener {
                if (encuestaConfirmada()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ya confirmaste la encuesta de hoy",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Confirmación diaria")
                        .setMessage(
                            "¿Ya completaste y enviaste la encuesta en Microsoft Forms y viste el mensaje de envío exitoso?"
                        )
                        .setPositiveButton("Sí, enviada") { _, _ ->
                            getSharedPreferences(
                                "AutoEncuesta360",
                                MODE_PRIVATE
                            ).edit()
                                .putString("fechaConfirmada", fechaHoy())
                                .apply()

                            actualizarEstado()

                            Toast.makeText(
                                this@MainActivity,
                                "Confirmación guardada",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }
        }

        val informacion = TextView(this).apply {
            text = "Primero abre y envía el formulario. Después confirma el envío aquí."
            textSize = 14f
            setPadding(0, 30, 0, 0)
        }

        layout.addView(titulo)
        layout.addView(descripcion)
        layout.addView(estado)
        layout.addView(botonAbrir)
        layout.addView(botonConfirmar)
        layout.addView(informacion)

        setContentView(layout)
        actualizarEstado()
    }

    override fun onResume() {
        super.onResume()
        if (::estado.isInitialized) {
            actualizarEstado()
        }
    }

    private fun abrirEncuesta() {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlEncuesta)
            )
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "No se pudo abrir Microsoft Forms",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
