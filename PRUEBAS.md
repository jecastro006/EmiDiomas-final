# Pruebas de la entrega

## Resultado

- La app compila correctamente
- Pasaron seis pruebas de la lógica de tarjetas
- Pasaron cuatro pruebas en el emulador Medium Phone con Android 17
- El emulador no tenía ninguna red activa durante el recorrido final
- Se comprobó que ambas lecciones incluyen significados ejemplos y gramática
- Se comprobó el guardado y la recuperación de las clasificaciones
- Se probaron tarjetas repaso progreso lectura y el regreso al inicio
- También se comprobó que la pantalla y el progreso se conservan al recrear la actividad
- Después de cerrar por completo y volver a abrir la app se recuperó una palabra aprendida de la lección de 51 tarjetas

Las capturas del manual se tomaron de esta versión ejecutándose en el emulador

## Repetir las pruebas

Para compilar y probar la lógica desde la carpeta del proyecto

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest
```

Para las pruebas en Android conecta un dispositivo o inicia un emulador
El recorrido visual parte de una instalación limpia sin clasificaciones anteriores
Las pruebas de guardado usan sus propios archivos de prueba

```sh
./gradlew :app:connectedDebugAndroidTest
```

## Alcance

Se probó en el emulador indicado y no en todos los modelos de teléfono
La generación con Gemini no está incluida en esta entrega
