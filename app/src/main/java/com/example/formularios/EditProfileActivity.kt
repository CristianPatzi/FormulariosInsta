package com.example.formularios

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private val PICK_PROFILE = 200
    private lateinit var imgPerfil: ImageView
    private var uriPerfil: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnActualizarFoto = findViewById<Button>(R.id.btnActualizarFoto)
        imgPerfil = findViewById(R.id.imagenPerfil)

        // 👉 Mostrar foto si ya existe
        if (DataHolder.fotoPerfil != null) {
            imgPerfil.setImageURI(DataHolder.fotoPerfil)
            uriPerfil = DataHolder.fotoPerfil
        }

        // 👉 Elegir nueva foto con permisos persistentes
        btnActualizarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            startActivityForResult(intent, PICK_PROFILE)
        }

        // 👉 Guardar cambios
        btnGuardar.setOnClickListener {

            DataHolder.nombre = edtNombre.text.toString()
            DataHolder.descripcion = edtDescripcion.text.toString()

            // Spinner
            val spinner = findViewById<Spinner>(R.id.spinnerGenero)
            DataHolder.genero = spinner.selectedItem.toString()

            // RadioGroup
            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                DataHolder.opcionSeleccionada = radioButton.text.toString()
            }

            // Checkbox
            val cbPerfilPublico = findViewById<CheckBox>(R.id.cbPerfilPublico)
            DataHolder.perfilPublico = cbPerfilPublico.isChecked

            // Foto
            DataHolder.fotoPerfil = uriPerfil

            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data

            if (requestCode == PICK_PROFILE && uri != null) {

                // 🔥 Guardar permiso persistente
                val flags = data.flags and
                        (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                try {
                    contentResolver.takePersistableUriPermission(uri, flags)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                imgPerfil.setImageURI(uri)
                uriPerfil = uri
            }
        }
    }
}
