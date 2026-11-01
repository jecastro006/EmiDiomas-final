package com.ebc.muffinshop

import com.ebc.muffinshop.model.Comprension
import com.ebc.muffinshop.viewmodel.LeccionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LeccionViewModelTest {
    @Test
    fun noSeCalificaSinVerLaRespuestaNiSeDuplicaUnToque() {
        val vm = LeccionViewModel()
        vm.calificar(Comprension.APRENDIDA)
        assertEquals(0, vm.uiState.value.posicion)
        assertTrue(vm.uiState.value.valoraciones.isEmpty())

        vm.mostrarRespuesta()
        vm.calificar(Comprension.APRENDIDA)
        vm.calificar(Comprension.APRENDIDA)
        assertEquals(1, vm.uiState.value.posicion)
        assertEquals(1, vm.uiState.value.valoraciones.size)
    }

    @Test
    fun alTerminarNoSeAgreganTarjetasFueraDeLaLeccion() {
        val vm = LeccionViewModel()
        repeat(6) {
            vm.mostrarRespuesta()
            vm.calificar(Comprension.APRENDIDA)
        }
        vm.mostrarRespuesta()
        vm.calificar(Comprension.REPETIR)
        assertEquals(6, vm.uiState.value.posicion)
        assertEquals(6, vm.uiState.value.valoraciones.size)
        assertTrue(vm.uiState.value.valoraciones.values.all { it == Comprension.APRENDIDA })
    }

    @Test
    fun elRepasoNoSaltaLaSiguientePalabraAlCambiarUnaClasificacion() {
        val vm = LeccionViewModel()
        val notas = listOf(Comprension.REPETIR, Comprension.DIFICIL)
        notas.forEach {
            vm.mostrarRespuesta()
            vm.calificar(it)
        }
        vm.iniciarRepaso()
        assertEquals(listOf(0, 1), vm.uiState.value.orden)
        vm.mostrarRespuesta()
        vm.calificar(Comprension.APRENDIDA)
        assertEquals(1, vm.uiState.value.orden[vm.uiState.value.posicion])
        vm.mostrarRespuesta()
        vm.calificar(Comprension.FACIL)
        vm.iniciarRepaso()
        assertTrue(vm.uiState.value.orden.isEmpty())
        assertEquals(2, vm.uiState.value.valoraciones.size)

        vm.iniciarLeccion("Francés")
        assertEquals("Francés", vm.uiState.value.idioma)
        assertEquals(6, vm.uiState.value.orden.size)
        assertEquals(2, vm.uiState.value.valoraciones.size)
    }

    @Test
    fun repetirVuelveTrasUnaTarjetaYDificilYFacilEsperanMas() {
        for ((nota, espera) in listOf(Comprension.REPETIR to 1,
            Comprension.DIFICIL to 5, Comprension.FACIL to 5)) {
            val vm = LeccionViewModel()
            vm.mostrarRespuesta()
            vm.calificar(nota)
            val estado = vm.uiState.value
            val pendientes = estado.orden.drop(estado.posicion)
            assertEquals(espera, pendientes.indexOf(0))
            assertEquals(1, pendientes.count { it == 0 })
            assertEquals(1, estado.valoraciones.size)
        }
    }

    @Test
    fun facilVuelveEnLaMismaVueltaYAprendidaPermiteTerminar() {
        val vm = LeccionViewModel()
        vm.mostrarRespuesta()
        vm.calificar(Comprension.FACIL)
        repeat(5) {
            vm.mostrarRespuesta()
            vm.calificar(Comprension.APRENDIDA)
        }
        assertEquals(0, vm.uiState.value.orden[vm.uiState.value.posicion])
        vm.mostrarRespuesta()
        vm.calificar(Comprension.APRENDIDA)
        assertEquals(vm.uiState.value.orden.size, vm.uiState.value.posicion)
        assertEquals(6, vm.uiState.value.valoraciones.size)
    }

    @Test
    fun volverAtrasNoDuplicaPendientesYAprendidaQuitaLaRepeticion() {
        val vm = LeccionViewModel()
        vm.mostrarRespuesta()
        vm.calificar(Comprension.FACIL)
        vm.anterior()
        vm.mostrarRespuesta()
        vm.calificar(Comprension.DIFICIL)
        var estado = vm.uiState.value
        assertEquals(1, estado.orden.drop(estado.posicion).count { it == 0 })
        vm.anterior()
        vm.mostrarRespuesta()
        vm.calificar(Comprension.APRENDIDA)
        estado = vm.uiState.value
        assertTrue(0 !in estado.orden.drop(estado.posicion))
        assertEquals(1, estado.valoraciones.size)
    }
}
