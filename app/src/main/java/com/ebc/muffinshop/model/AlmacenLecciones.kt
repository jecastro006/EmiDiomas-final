package com.ebc.muffinshop.model

import android.content.Context

// guardar las clasificaciones sin conexión
class AlmacenLecciones(context: Context, archivo: String = "progreso_precargado") {
    private val preferencias = context.applicationContext.getSharedPreferences(archivo, Context.MODE_PRIVATE)

    fun guardar(lecciones: List<Leccion>) {
        val editor = preferencias.edit()
        lecciones.forEach { leccion ->
            leccion.valoraciones.forEach { (numero, nota) ->
                editor.putString("${leccion.id}_$numero", nota.name)
            }
        }
        editor.apply()
    }

    fun cargar(lecciones: List<Leccion>): List<Leccion> {
        return lecciones.map { leccion ->
            val notas = mutableMapOf<Int, Comprension>()
            leccion.tarjetas.indices.forEach { numero ->
                val nombre = preferencias.getString("${leccion.id}_$numero", null)
                val nota = Comprension.entries.find { it.name == nombre }
                if (nota != null) notas[numero] = nota
            }
            leccion.copy(valoraciones = notas)
        }
    }
}
