package com.example.formularios

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil

import com.example.formularios.databinding.ActivityContactsBinding

lateinit var bindingContacts: ActivityContactsBinding
class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindingContacts = DataBindingUtil.setContentView(this, R.layout.activity_contacts)

        ViewCompat.setOnApplyWindowInsetsListener(bindingContacts.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
        bindingContacts.back.setOnClickListener {
            finish()
        }
        bindingContacts.subirContacts.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                cargarContactosDelDispositivo()
                actualizarPantalla()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            cargarContactosDelDispositivo()
            actualizarPantalla()
        }
    }

    private fun cargarContactosDelDispositivo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        DataHolder.contactos.clear()

        cursor?.use {
            val nombreIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val telefonoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val nombre = it.getString(nombreIndex) ?: "Sin nombre"
                val telefono = it.getString(telefonoIndex) ?: "Sin teléfono"

                DataHolder.contactos.add(
                    DataHolder.Contacto(nombre, telefono)
                )
            }
        }

        Log.d("CONTACTOS", "Total importados: ${DataHolder.contactos.size}")
    }

    private fun actualizarPantalla() {
        val builder = StringBuilder()
        var contador = 0

        for (c in DataHolder.contactos) {
            builder.append("• ${c.nombre} - ${c.telefono}\n")
            contador++
        }

        bindingContacts.contactsTextView.text = builder.toString()
        bindingContacts.TotalTextView.text = "Total Contactos: $contador"
    }
}