# Health — Aplicación Android para Smartwatch (Galaxy Fit3 / Wearables)

> **Universidad Tecnológica de Nezahualcóyotl (UTN)**  
> **División de Informática y Computación**  
> **Asignatura:** Desarrollo de dispositivos inteligentes — Grupo: ITIC-901  
> **Profesor:** Edgar Díaz Pérez  
> **Equipo de Desarrollo:**
> - Arciniega Ximello José Manuel
> - Correa Gress Nicole
> - Estrada Macías Víctor Hugo
> - Gonzalez Chávez Celestino Jair
> - Manríquez Morales Juan Carlos
> - Méndez González Roberto Carlos

---

## 📌 Descripción del Proyecto

**Health** es una aplicación nativa para Android escrita en **Kotlin** con **Jetpack Compose (Material Design 3)**, diseñada para consultar, centralizar y visualizar las métricas de salud y actividad física registradas por una pulsera inteligente **Samsung Galaxy Fit 3** (así como cualquier otro wearable compatible como **Google Pixel Watch**, **Garmin** o **Xiaomi Band**).

La aplicación utiliza la plataforma oficial de Android **Health Connect** como bus central de datos de salud y desacopla completamente el hardware mediante un patrón agnóstico de fuentes de datos. Cuenta con almacenamiento local persistente en **Room (SQLite)** para funcionamiento sin conexión y un sistema gamificado de **Rachas de Distancia** estilo Duolingo con escudos de congelamiento y rescate de rachas.

---

## ✨ Características Principales

1. **Dashboard de Resumen Diario:**
   * 🚶 **Pasos:** Contador total de pasos del día.
   * 📏 **Distancia:** Distancia acumulada en kilómetros (con dos decimales).
   * 🔥 **Calorías:** Gasto energético activo y basal en kilocalorías (kcal).
   * ❤️ **Frecuencia Cardíaca:** Promedio, pulsación mínima y máxima en lpm.
   * 🌙 **Sueño Nocturno:** Duración de la sesión de descanso en horas y minutos (calculada desde la tarde del día anterior a las 18:00).
   * 🔄 **Sincronización:** Marca de tiempo de la última lectura y refresco manual.

2. **Gamificación de Racha de Distancia (Estilo Duolingo):**
   * Meta de distancia diaria en km personalizable (2, 3, 5, 8 o 10 km).
   * Llama interactiva animada con estados: activa (🔥), en riesgo (⚠️), congelada (❄️) o inactiva.
   * **Escudo de Congelamiento (*Streak Freeze*):** Protege tu racha si un día no puedes salir a caminar. Gana escudos cada 7 días continuos.
   * **Reparar Racha (*Streak Repair*):** Ventana de gracia de 24 horas para rescatar tu récord de días acumulados.

3. **Gráficos Interactivos con Canvas:**
   * **Historial de Actividad (7 y 30 días):** Gráfica interactiva de barras dibujada puramente con Canvas de Compose, con detección táctil de barras para ver la fecha y valor exacto.
   * **Monitor Continuo de Frecuencia Cardíaca:** Curva continua con gradiente suave inferior a lo largo del día y zonas de esfuerzo cardíaco.

4. **Arquitectura Limpia & Modo Sin Conexión (Room SQLite):**
   * Persistencia local que garantiza disponibilidad de datos ante desconexión o falta temporal de permisos.
   * Patrón UDF (Unidirectional Data Flow) con `StateFlow` y Corrutinas.

5. **Soporte Multi-Dispositivo y Multi-Teléfono:**
   * Probado y documentado para **Google Pixel 9a** vinculado a **Samsung Galaxy Fit 3**.
   * Compatible con cualquier smartwatch que sincronice con Health Connect.

---

## 📚 Documentación y Manuales del Proyecto

En el directorio [`docs/`](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs) se encuentran disponibles los manuales técnicos detallados del proyecto:

1. 🚀 **[Manual de Despliegue e Instalación](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs/MANUAL_DESPLIEGUE.md):**
   * Prerrequisitos de entorno (JDK 17/21, Android Studio, SDK API 28-35).
   * Activación de opciones de desarrollador y depuración USB en Google Pixel 9a.
   * Compilación, generación e instalación del archivo APK (`./gradlew assembleDebug` y `adb install`).

2. ⌚ **[Manual de Vinculación de Dispositivos y Sincronización](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs/MANUAL_DISPOSITIVOS_Y_SINCRONIZACION.md):**
   * Flujo de sincronización de la Galaxy Fit 3: Reloj $\rightarrow$ Galaxy Wearable $\rightarrow$ Samsung Health $\rightarrow$ Health Connect $\rightarrow$ App Health.
   * Activación de permisos de escritura de Samsung Health en Health Connect.
   * Patrón agnóstico multi-dispositivo para conectar Pixel Watch, Garmin, etc.

3. 🏛️ **[Manual de Arquitectura y Diseño Técnico](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs/MANUAL_ARQUITECTURA_Y_DISENO.md):**
   * Diagramas de flujo y arquitectura Clean MVVM.
   * Esquema de base de datos Room SQLite y entidades.
   * Estrategias de agregación y deduplicación de Health Connect.
   * Motor de reglas del sistema de rachas.

4. 📱 **[Manual de Uso y Guía del Usuario](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs/MANUAL_DE_USO.md):**
   * Guía visual paso a paso de todas las pantallas de la aplicación.
   * Cómo ajustar metas de distancia, activar congeladores y reparar rachas.
   * Navegación por gráficos de frecuencia cardíaca e historial.

5. 🧪 **[Manual de Pruebas y Aseguramiento de Calidad](file:///home/jos-manuel/Projects/PROY-DI-SMARTWATCH/docs/MANUAL_DE_PRUEBAS.md):**
   * Pruebas automatizadas unitarias (`./gradlew testDebugUnitTest`).
   * Matriz de casos de prueba manuales (CP-01 a CP-16).
   * Guía para simulación de datos con *Health Connect Toolbox*.

---

## 🛠️ Comandos Rápidos de Terminal

```bash
# Ejecutar todas las pruebas unitarias
./gradlew testDebugUnitTest

# Compilar y generar el APK de depuración
./gradlew assembleDebug

# Instalar directamente en el teléfono (Pixel 9a conectado por USB)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Lanzar la aplicación en el dispositivo
adb shell am start -n com.utn.health/.MainActivity
```