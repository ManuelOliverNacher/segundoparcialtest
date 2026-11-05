# Planta Norte | Examen práctico de Ingeniería de Software (4.º año)

Proyecto Android nativo en Kotlin: panel IIoT **de demostración**, con una pantalla híbrida XML + Jetpack Compose, MVVM, Retrofit, SQLite y sensores Android. El proyecto **contiene decisiones de diseño heredadas** y tiene dos funcionalidades que todavía no están terminadas.

**Importante:** los equipos y las lecturas de fábrica son simulados. El canal público de ThingSpeak se utiliza exclusivamente para ejercitar el consumo de API: sus datos **no corresponden** a una instalación industrial real. En caso de fallo de red, la app sigue siendo utilizable con datos de prueba.

## Requisitos de preparación (a cargo del docente, antes de la prueba)

- Android Studio con Android SDK 35 instalado y JDK 17 (o superior compatible con Gradle 8.11.1).
- Conexión a Internet **para descargar dependencias la primera vez**; después la app puede ejecutarse sin acceso a la API.
- Emulador o dispositivo Android 8.0 / API 26 en adelante.
- Generar el wrapper oficial de Gradle ejecutando **una vez** `./gradlew --version` (Linux/macOS) o `gradlew.bat --version` (Windows). Los scripts obtienen el `gradle-wrapper.jar` oficial de Gradle 8.11.1 y verifican su SHA-256 antes de usarlo. La distribución de Gradle también se descargará si no estaba en caché.
- Abrir **esta carpeta** en Android Studio y esperar la sincronización Gradle. Iniciar en el emulador y comprobar las tres tarjetas.

La descarga del wrapper se implementa en los scripts para no incluir un binario que no pueda verificarse durante la generación de este paquete. Para que los estudiantes no descarguen herramientas durante el examen, el docente debe **preparar, sincronizar y distribuir** el proyecto con `gradle/wrapper/gradle-wrapper.jar` ya generado (o proporcionar estaciones previamente configuradas).

## Comportamiento ya implementado

1. Carga inicial de tres equipos desde SQLite; si la base está vacía, inserta lecturas de prueba.
2. Botón **Sincronizar telemetría**: consulta Retrofit/ThingSpeak y persiste un nuevo lote de lecturas en SQLite. Si la red falla, inserta otro lote simulado.
3. Buscador de equipos por nombre.
4. Indicadores de temperatura, humedad, vibración, origen y estado de alerta.
5. Sensores físicos del teléfono: luz ambiental y acelerómetro, cuando el dispositivo los ofrece.
6. Cabecera y botón tradicionales XML; panel y tarjetas Compose integrados mediante `ComposeView`.

## Cómo explorar el código

```text
app/src/main/java/ar/edu/examen/iiot/
  MainActivity.kt                 # actividad XML y sensores Android
  model/Reading.kt                # entidad de lectura y reglas existentes
  data/ThingSpeakApi.kt           # Retrofit + DTO de API externa
  data/PlantDatabase.kt           # SQLiteOpenHelper
  data/PlantRepository.kt         # acceso a datos
  ui/PlantViewModel.kt            # estado de pantalla
  ui/DashboardScreen.kt           # tarjetas y controles Compose
```

Para conocer el trabajo evaluado, leer `CONSIGNA_ESTUDIANTE.md`.

## Prueba de humo

- Ejecutar; deben verse Compresor A, Bomba B, Horno C y por lo menos una alerta.
- Escribir `Bomba` en buscar; solo debe quedar la tarjeta correspondiente. Borrar para ver las tres.
- Pulsar **Sincronizar** dos veces y comprobar el cambio de fecha/hora o de origen del Compresor A. Si hay red válida, la cabecera indicará ThingSpeak; si no, indicará simulación.
- Pulsar **Umbral** y **Ver historial**: se muestra el aviso de funcionalidad pendiente, sin que la app se cierre.
- En un emulador sin sensor de luz se mostrará “no disponible”, no un error.

## Stack fijado para el ejercicio

- Android Gradle Plugin 8.9.2 + Gradle 8.11.1 + Kotlin 2.1.20
- Jetpack Compose BOM 2025.05.00, Material 3, Lifecycle/ViewModel
- Retrofit 2.11.0 + Gson; SQLiteOpenHelper nativo

No contiene API keys, credenciales ni conexiones con maquinaria real.
