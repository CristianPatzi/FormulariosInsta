package com.example.formularios

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.data.DataHolder
class EditProfileActivity (context: Context): SQLiteOpenHelper(context, "UsuariosSQL", null, 1) {

    override fun onCreate(db: SQLiteDatabase?){
        val sqlcreate = "CREATE TABLE Usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, DNI TEXT, nombre TEXT, apellido TEXT, descripcion TEXT, estadoCivil TEXT, genero TEXT, Pais TEXT, Ciudad TEXT, fechaNacimiento DATE)".trimIndent()
        val sqljugadores = """
            INSERT INTO Jugadores (DNI, nombre, apellido) VALUES
            ('18944738S', 'Cristian', 'Patzi Frias'),
            ('18944734P', 'Millan', 'Martinez'),
            ('18944738P', 'Sonia', 'Mendoza'),
            ('378944295Z', 'Sara', 'Cervantes'),
            ('18944738Q', 'Jennifer', 'Sanz'),
            ('18944738T', 'Alberto', 'Rodriguez'),
            ('18944738W', 'Miguel', 'Martinez'),
            ('18944738E', 'Andrea', 'Lopez'),
            ('18944738D', 'Daniel', 'Gonzalez'),
            ('18944738V', 'Angel', 'Saenz')
        """.trimIndent()

        db?.execSQL(sqlcreate)
        db?.execSQL(sqljugadores)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_profile)
//
//        val edtNombre = findViewById<EditText>(R.id.edtNombre)
//        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
//        val spinnerGenero = findViewById<Spinner>(R.id.spinnerGenero)
//        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
//
//        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
//
//        val opciones = arrayOf("Hombre", "Mujer", "Otro")
//        spinnerGenero.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
//
//        btnGuardar.setOnClickListener {
//            DataHolder.nombre = edtNombre.text.toString()
//            DataHolder.descripcion = edtDescripcion.text.toString()
//
//            finish()
//        }
//    }
}
