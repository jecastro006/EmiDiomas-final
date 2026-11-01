package com.ebc.muffinshop.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ebc.muffinshop.model.AlmacenLecciones
import com.ebc.muffinshop.model.Comprension
import com.ebc.muffinshop.model.Leccion
import com.ebc.muffinshop.model.LeccionUiState
import com.ebc.muffinshop.model.leccionesPrecargadas
import com.ebc.muffinshop.model.leccionEjemplo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// estado y acciones de las lecciones
class LeccionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LeccionUiState())
    val uiState = _uiState.asStateFlow()
    private var almacen: AlmacenLecciones? = null

    // cargar las lecciones del teléfono
    fun cargarLecciones(context: Context) {
        if (almacen != null) return
        almacen = AlmacenLecciones(context)
        val lecciones = almacen!!.cargar(leccionesPrecargadas(context))
        _uiState.update { it.copy(lecciones = lecciones, leccion = lecciones.first()) }
    }

    fun iniciarLeccion(idioma: String) {
        val actual = _uiState.value.lecciones.find { it.esEjemplo } ?: _uiState.value.leccion
        val notas = if (actual.esEjemplo) actual.valoraciones else emptyMap()
        activar(leccionEjemplo(idioma).copy(valoraciones = notas))
    }

    private fun activar(leccion: Leccion) {
        _uiState.update {
            it.copy(leccion = leccion, orden = leccion.tarjetas.indices.toList(),
                posicion = 0, mostrarRespuesta = false, enRepaso = false,
                irATarjetas = true)
        }
    }

    fun abrirGuardada(id: String) {
        _uiState.value.lecciones.find { it.id == id }?.let { activar(it) }
    }

    fun navegacionAtendida() {
        _uiState.update { it.copy(irATarjetas = false) }
    }

    fun salirDeRepaso() {
        _uiState.update {
            it.copy(orden = it.leccion.tarjetas.indices.toList(), posicion = 0,
                mostrarRespuesta = false, enRepaso = false)
        }
    }

    // mostrar u ocultar la respuesta
    fun mostrarRespuesta() {
        _uiState.update { it.copy(mostrarRespuesta = !it.mostrarRespuesta) }
    }

    // clasificación y repetición de tarjetas
    fun calificar(comprension: Comprension) {
        _uiState.update { estado ->
            val tarjetaActual = estado.orden.getOrNull(estado.posicion)
            if (tarjetaActual == null || !estado.mostrarRespuesta) {
                return@update estado
            }

            // guardar la valoración de la palabra
            val valoraciones = estado.valoraciones.toMutableMap()
            valoraciones[tarjetaActual] = comprension
            val leccion = estado.leccion.copy(valoraciones = valoraciones)

            val lecciones = estado.lecciones.toMutableList()
            for (i in lecciones.indices) {
                if (lecciones[i].id == leccion.id) {
                    lecciones[i] = leccion
                }
            }

            // historial y tarjetas pendientes
            val orden = mutableListOf<Int>()
            for (i in estado.orden.indices) {
                val numeroTarjeta = estado.orden[i]
                if (i <= estado.posicion) {
                    orden.add(numeroTarjeta)
                } else if (numeroTarjeta != tarjetaActual) {
                    orden.add(numeroTarjeta)
                }
            }
            // quitar otra repetición pendiente de la palabra

            val siguiente = estado.posicion + 1
            var lugar = orden.size

            if (comprension == Comprension.REPETIR) {
                lugar = siguiente + 1
            } else if (comprension == Comprension.DIFICIL) {
                lugar = siguiente + 5
            }
            // fácil vuelve al final

            if (lugar > orden.size) {
                lugar = orden.size
            }

            if (comprension != Comprension.APRENDIDA) {
                orden.add(lugar, tarjetaActual)
            }

            estado.copy(
                leccion = leccion,
                lecciones = lecciones,
                orden = orden,
                posicion = siguiente,
                mostrarRespuesta = false
            )
        }

        almacen?.guardar(_uiState.value.lecciones)
    }

    fun anterior() {
        _uiState.update {
            it.copy(posicion = (it.posicion - 1).coerceAtLeast(0), mostrarRespuesta = false)
        }
    }

    // preparar las palabras pendientes de repaso
    fun iniciarRepaso() {
        _uiState.update { estado ->
            val pendientes = estado.leccion.tarjetas.indices.filter {
                estado.valoraciones[it] == Comprension.REPETIR || estado.valoraciones[it] == Comprension.DIFICIL
            }
            estado.copy(orden = pendientes, posicion = 0, mostrarRespuesta = false, enRepaso = true)
        }
    }
}
