package com.ebc.muffinshop

import android.graphics.Bitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Rule
import org.junit.Test

class RecorridoAppTest {
    @get:Rule val pantalla = createAndroidComposeRule<MainActivity>()

    private fun pulsar(texto: String) {
        pantalla.onNodeWithText(texto).performScrollTo().performClick()
        pantalla.waitForIdle()
    }

    private fun captura(nombre: String) {
        pantalla.waitForIdle()
        android.os.SystemClock.sleep(600)
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        val carpeta = File(instrumentacion.targetContext.getExternalFilesDir(null), "capturas")
        carpeta.mkdirs()
        val imagen = instrumentacion.uiAutomation.takeScreenshot()
        File(carpeta, "$nombre.png").outputStream().use {
            imagen.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        imagen.recycle()
    }

    @Test
    fun estudiarRepetirRepasarYLeerSinInternet() {
        captura("inicio")
        pulsar("Empezar práctica corta")
        pantalla.onNodeWithText("ciao").assertIsDisplayed()
        pulsar("Mostrar significado")
        captura("tarjeta")
        pulsar("Repetir")
        pulsar("Mostrar significado")
        pulsar("Difícil")
        pantalla.onNodeWithText("Repaso").performClick()
        pantalla.onNodeWithText("2 tarjetas para repasar").assertIsDisplayed()
        captura("repaso")
        pantalla.onNodeWithText("Progreso").performClick()
        captura("progreso")
        pantalla.onNodeWithText("Repaso").performClick()
        pulsar("Empezar repaso")
        repeat(2) {
            pulsar("Mostrar significado")
            pulsar("Aprendida")
        }
        pantalla.onNodeWithText("Lección").performClick()
        repeat(6) {
            pulsar("Mostrar significado")
            pulsar("Aprendida")
        }
        pulsar("Ver lectura")
        captura("lectura")
        pulsar("Volver al inicio")
        pulsar("Estudiar lección")
        pantalla.onNodeWithText("Tarjeta 1 de 51").assertIsDisplayed()
        pulsar("Mostrar significado")
        captura("leccion_completa")
        pulsar("Aprendida")
        pantalla.activityRule.scenario.recreate()
        pantalla.waitForIdle()
        pantalla.onNodeWithText("Progreso").performClick()
        pantalla.onNodeWithText("Aprendidas: 1 de 51").assertIsDisplayed()
    }
}
