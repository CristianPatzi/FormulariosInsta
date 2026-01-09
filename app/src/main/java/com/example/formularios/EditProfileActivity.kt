package com.example.formularios

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val spinnerGenero = findViewById<Spinner>(R.id.spinnerGenero)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Spinner
        val opciones = arrayOf("Hombre", "Mujer", "Otro")
        spinnerGenero.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        btnGuardar.setOnClickListener {
            DataHolder.nombre = edtNombre.text.toString()
            DataHolder.descripcion = edtDescripcion.text.toString()

            finish()
        }
    }
}
