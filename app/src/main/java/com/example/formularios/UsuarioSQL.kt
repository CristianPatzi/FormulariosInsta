package com.example.advideojuego

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class JugadoresSQLite (context: Context): SQLiteOpenHelper(context, "JugadoresSQL", null, 1) {
    override fun onCreate(db: SQLiteDatabase?){
        val sqlcreate = "CREATE TABLE Jugadores (id INTEGER PRIMARY KEY AUTOINCREMENT, DNI TEXT, nombre TEXT, apellido TEXT)".trimIndent()

        db?.execSQL(sqlcreate)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }
    fun anadriDato(DNI: String, nombre: String, apellido: String){
        val datos = ContentValues()
        datos.put("DNI", DNI)
        datos.put("nombre", nombre)
        datos.put("apellido", apellido)
        var db = this.writableDatabase
        db.insert("Jugadores",null, datos)
        db.close()
    }

    fun modificarDato( DNI: String, nombre: String, apellido: String){
        var args = arrayOf(DNI.toString())
        val datos = ContentValues()
        datos.put("DNI", DNI)
        datos.put("nombre", nombre)
        datos.put("apellido", apellido)

        var db = this.writableDatabase
        db.update("Jugadores", datos, "DNI=?", args)
        db.close()
    }
    fun borrarDato(DNI: String):Int{
        var args = arrayOf(DNI.toString())
        var db = this.writableDatabase
        var borrados = db.delete("Jugadores", "DNI=?", args)
        db.close()
        return borrados
    }
    fun cargarDatos(){
        var contador = 0
        binding.textView2.text = ""
        val db: SQLiteDatabase = jugador.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Jugadores", null)
        if(cursor.moveToFirst()){
            do{
                binding.textView2.append(cursor.getInt(1).toString()+", ")
                binding.textView2.append(cursor.getString(2).toString()+", ")
                binding.textView2.append(cursor.getString(3).toString()+"\n")
                contador = contador+1
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        binding.textTotal.text = "Nº de Jugadores: ${contador}"
    }
}