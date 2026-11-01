package com.ebc.muffinshop

import androidx.test.platform.app.InstrumentationRegistry
import com.ebc.muffinshop.model.AlmacenLecciones
import com.ebc.muffinshop.model.Comprension
import com.ebc.muffinshop.model.leccionesPrecargadas
import org.junit.Assert.*
import org.junit.Test

class LeccionIntegracionTest {
    private val contexto = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun lasLeccionesPrecargadasEstanCompletas() {
        val lecciones = leccionesPrecargadas(contexto)
        assertEquals(2, lecciones.size)
        assertEquals(6, lecciones.first().tarjetas.size)
        assertTrue(lecciones.last().tarjetas.size >= 50)
        lecciones.forEach { leccion ->
            assertEquals(leccion.tarjetas.size, leccion.tarjetas.map { it.palabra.lowercase() }.distinct().size)
            assertTrue(leccion.lectura.isNotBlank())
            assertTrue(leccion.lecturaTraducida.isNotBlank())
            leccion.tarjetas.forEach { tarjeta ->
                assertTrue(tarjeta.ejemplo.isNotBlank())
                tarjeta.traducciones.values.forEach {
                    assertTrue(it.significado.isNotBlank())
                    assertTrue(it.ejemploTraducido.isNotBlank())
                    assertTrue(it.gramatica.isNotBlank())
                }
            }
        }
    }

    @Test
    fun lasClasificacionesSeRecuperanEnOtraInstancia() {
        val archivo = "prueba_guardado"
        contexto.getSharedPreferences(archivo, 0).edit().clear().commit()
        val lecciones = leccionesPrecargadas(contexto).map {
            it.copy(valoraciones = mapOf(0 to Comprension.DIFICIL, 1 to Comprension.APRENDIDA))
        }
        AlmacenLecciones(contexto, archivo).guardar(lecciones)
        val recuperadas = AlmacenLecciones(contexto, archivo).cargar(leccionesPrecargadas(contexto))
        assertEquals(lecciones.map { it.valoraciones }, recuperadas.map { it.valoraciones })
        contexto.getSharedPreferences(archivo, 0).edit().clear().commit()
    }

    @Test
    fun unaClasificacionDesconocidaNoRompeElProgreso() {
        val archivo = "prueba_nota"
        val preferencias = contexto.getSharedPreferences(archivo, 0)
        preferencias.edit().clear().putString("ejemplo_0", "NO_EXISTE").commit()
        val lecciones = AlmacenLecciones(contexto, archivo).cargar(leccionesPrecargadas(contexto))
        assertTrue(lecciones.first().valoraciones.isEmpty())
        preferencias.edit().clear().commit()
    }
}
