# Manual de Despliegue e Instalación
**Proyecto:** Health — Sistema de Salud para Smartwatch  
**Institución:** Universidad Tecnológica de Nezahualcóyotl (UTN) — ITIC-901  
**Dispositivos Objetivo:** Google Pixel 9a (Android 15) & Samsung Galaxy Fit 3  

---

## 1. Prerrequisitos del Entorno de Desarrollo

Para compilar, depurar y generar el paquete APK de la aplicación, el equipo debe contar con:

* **Sistema Operativo:** Linux (Ubuntu 22.04+ / 24.04+), macOS o Windows 11.
* **Java Development Kit (JDK):** OpenJDK 21 o superior.
* **Android Studio:** Ladybug (2024.2.1+) o versión más reciente.
* **Android SDK:**
  * `compileSdk`: 35 (Android 15)
  * `minSdk`: 28 (Android 9.0 Pie)
  * `targetSdk`: 35
  * Build Tools: `35.0.0` o superior
  * Android SDK Platform-Tools (`adb`)

Verificar en terminal:
```bash
java -version
adb version
```

---

## 2. Preparación del Dispositivo Físico (Google Pixel 9a)

El Google Pixel 9a incorpora Android 14/15 con **Health Connect preinstalado como componente central del sistema operativo**.

### Paso 2.1: Activar Opciones para Desarrolladores
1. En tu Pixel 9a, abre **Ajustes** > **Información del teléfono**.
2. Desplázate al final hasta ubicar **Número de compilación**.
3. Pulsa **7 veces consecutivas** sobre *Número de compilación* hasta ver el mensaje flotante: *"¡Ahora eres un desarrollador!"*.
4. Introduce el PIN o huella digital si el sistema lo solicita.

### Paso 2.2: Habilitar Depuración por USB
1. Ve a **Ajustes** > **Sistema** > **Opciones para desarrolladores**.
2. Activa el interruptor **Depuración por USB**.
3. (Opcional) Activa **Instalar aplicaciones por USB**.
4. Conecta el Pixel 9a a tu computadora mediante un cable USB-C de datos de buena calidad.
5. En la pantalla del Pixel 9a aparecerá un cuadro de diálogo:
   > *"¿Permitir depuración por USB desde esta computadora?"*
6. Marca la casilla **"Permitir siempre desde esta computadora"** y presiona **Permitir**.

### Paso 2.3: Verificar Conexión ADB
En la terminal de tu computadora:
```bash
adb devices
```
Deberás observar tu Pixel 9a listado como:
```text
List of devices attached
XXXXXXXXXXXX    device
```

---

## 3. Compilación y Construcción del Proyecto

### Clonación y Ubicación
Navegar al directorio del proyecto:
```bash
cd /home/jos-manuel/Projects/PROY-DI-SMARTWATCH
```

### Ejecutar Pruebas Unitarias Automatizadas
Antes de compilar el APK, validar que todas las pruebas unitarias pasen exitosamente:
```bash
./gradlew testDebugUnitTest
```

### Generar APK de Depuración (Debug APK)
```bash
./gradlew assembleDebug
```
Una vez concluida la tarea con éxito (`BUILD SUCCESSFUL`), el APK generado se encontrará en la ruta:
```text
app/build/outputs/apk/debug/app-debug.apk
```

### Generar APK de Producción (Release APK)
```bash
./gradlew assembleRelease
```

---

## 4. Instalación de la Aplicación en el Teléfono

### Método A: Instalación Directa mediante ADB (Recomendado)
Con el Pixel 9a conectado y autorizado:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
Respuesta esperada en terminal:
```text
Performing Streamed Install
Success
```

Para abrir la app inmediatamente vía ADB:
```bash
adb shell am start -n com.utn.health/.MainActivity
```

### Método B: Ejecución Directa desde Android Studio
1. Abre Android Studio y selecciona **Open** > Carpeta `/home/jos-manuel/Projects/PROY-DI-SMARTWATCH`.
2. Espera a que la sincronización de Gradle finalice.
3. En la barra superior, en el selector de dispositivos, elige tu **Google Pixel 9a**.
4. Haz clic en el botón verde **Run (Shift + F10)**.

### Método C: Transferencia Manual del APK
1. Copia el archivo `app-debug.apk` al almacenamiento interno o envíalo por Drive/Telegram a tu Pixel 9a.
2. Abre la app **Archivos (Files)** en tu Pixel 9a.
3. Toca sobre `app-debug.apk` y autoriza la instalación de apps desde esa fuente si el sistema lo requiere.
4. Presiona **Instalar** y luego **Abrir**.
