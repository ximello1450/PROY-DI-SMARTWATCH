# Manual de Pruebas y Aseguramiento de Calidad (QA)
**Proyecto:** Health — Sistema de Salud para Smartwatch  
**Institución:** Universidad Tecnológica de Nezahualcóyotl (UTN) — ITIC-901  
**Dispositivos de Prueba:** Google Pixel 9a (Android 15) & Samsung Galaxy Fit 3  

---

## 1. Estrategia de Pruebas

El plan de aseguramiento de calidad combina **pruebas automatizadas unitarias** para la lógica de negocio y cálculos críticos, con una **matriz de pruebas funcionales manuales en hardware real** que verifica la comunicación entre el reloj Samsung Galaxy Fit 3, la app Samsung Health, Health Connect y la aplicación *Health*.

---

## 2. Pruebas Automatizadas Unitarias

Las pruebas unitarias se ejecutan directamente en la JVM sin necesidad de emulador ni dispositivo físico:

```bash
./gradlew testDebugUnitTest
```

### Casos de Prueba Automatizados Implementados:
| Clase de Prueba | Objetivo del Test | Estado |
| :--- | :--- | :--- |
| `HealthDateRangesTest` | Verifica que el rango diario inicie a las 00:00 y termine en el instante actual o 23:59:59. | **Aprobado** |
| `HealthDateRangesTest` | Valida que la ventana de sueño inicie a las 18:00 del día anterior para abarcar la noche completa. | **Aprobado** |
| `StreakEngineTest` | Comprueba el cálculo del porcentaje de progreso y distancia restante (`StreakState`). | **Aprobado** |
| `StreakEngineTest` | Valida la activación de la meta cumplida cuando `distancia >= metaKm`. | **Aprobado** |
| `StreakEngineTest` | Valida que un escudo de congelamiento (`isFrozenToday = true`) proteja la racha contra reseteos. | **Aprobado** |
| `MetricConversionTest` | Valida la conversión exacta de metros a kilómetros con 2 decimales (`3450 m -> 3.45 km`). | **Aprobado** |
| `MetricConversionTest` | Valida el formateo de minutos de sueño a horas y minutos (`445 min -> 7h 25m`). | **Aprobado** |
| `MetricConversionTest` | Valida el redondeo de calorías en kcal sin decimales. | **Aprobado** |
| `MetricConversionTest` | Valida que las métricas nulas produzcan cadenas de reserva (`"--"`) sin lanzar excepciones. | **Aprobado** |
| `BmiRecommendationTest` | Valida el cálculo del IMC y las metas sugeridas por categoría (Normal: 5.0 km, Sobrepeso: 3.5 km, Obesidad/Bajo peso: 2.5 km). | **Aprobado** |

---

## 3. Matriz de Casos de Prueba Manuales (CP-01 a CP-16)

Esta matriz amplía los casos originales de la documentación académica (CP-01 a CP-10) incorporando los casos específicos para el **sistema de rachas gamificado** y el **soporte multi-dispositivo agnóstico** (CP-11 a CP-16).

| ID | Caso de Prueba | Procedimiento de Verificación | Resultado Esperado | Criterio de Aceptación |
| :--- | :--- | :--- | :--- | :--- |
| **CP-01** | Sin Health Connect instalado o no disponible | Abrir la aplicación en un entorno sin soporte de Health Connect. | Muestra pantalla de estado con mensaje claro y guía para habilitarlo o actualizarlo desde Google Play. La app no falla ni se cierra. | RNF-07 / HU-01 |
| **CP-02** | Concesión de los 5 permisos de lectura | Abrir la app por primera vez y presionar "Conceder Permisos de Salud". Marcar las 5 casillas. | El Dashboard carga exitosamente mostrando tarjetas con datos reales de pasos, distancia, calorías, FC y sueño. | RF-02 / HU-02 |
| **CP-03** | Rechazo de permisos individuales (ej. Calorías) | Revocar el permiso de calorías en Ajustes y volver a abrir la app. | La tarjeta de Calorías indica "Permiso pendiente". Las demás 4 tarjetas continúan operando normalmente. | RF-06 / HU-02 |
| **CP-04** | Caminar con el reloj y sincronizar | Realizar una caminata de 100 metros portando el Galaxy Fit 3. Abrir Samsung Health para sincronizar y luego pulsar [ 🔄 ] en la app. | El contador de pasos y distancia se actualiza reflejando el incremento registrado por el reloj. | RF-03 / HU-04 |
| **CP-05** | Día sin datos registrados | Consultar métricas a las 00:01 am o un día sin actividad física. | Las métricas muestran "0" o "Sin datos registrados", sin cierres inesperados. | RF-06 / HU-03 |
| **CP-06** | Modo sin conexión / Fallo temporal de Health Connect | Activar Modo Avión o revocar temporalmente permisos de Health Connect tras haber sincronizado previamente. | La app muestra el último resumen guardado en la base de datos Room con el distintivo: "🟡 Caché local • Última sinc". | RF-10 / HU-10 |
| **CP-07** | Historial de 7 y 30 días con gráficos de barras | Abrir la pantalla de Historial y alternar entre "7 Días" y "30 Días". Tocar una de las barras. | El gráfico Canvas muestra una barra por día. Al tocar una barra, se actualiza la tarjeta superior con la fecha y valor exacto. | RF-08 / HU-08 |
| **CP-08** | Modo oscuro y accesibilidad de texto | Cambiar el Pixel 9a a Tema Oscuro y habilitar texto grande en Ajustes de Pantalla. | La interfaz adapta sus contrastes a la paleta nocturna sin textos solapados ni cortados. | RNF-04 |
| **CP-09** | Frecuencia cardíaca continua con sensor óptico | Acceder a la pantalla de detalle de Frecuencia Cardíaca tras usar el Galaxy Fit 3 durante el día. | Muestra el valor mínimo, promedio y máximo en lpm, junto con la curva de línea Canvas con gradiente continuo de las muestras. | RF-11 / HU-11 |
| **CP-10** | Sesión nocturna de sueño | Dormir con el Galaxy Fit 3 puesto. Al despertar y sincronizar Samsung Health, abrir la app. | La tarjeta de sueño muestra el total en formato "Xh Ym" capturando la sesión desde las 18:00 del día anterior. | RF-12 / HU-12 |
| **CP-11** | Cumplimiento de meta diaria de distancia y suma de racha | Configurar meta en 3.0 km. Completar una caminata de 3.2 km con el reloj y sincronizar. | El indicador circular de la llama se llena al 100%, la llama se enciende en naranja brillante y la racha aumenta en +1. | HU-Racha-01 |
| **CP-12** | Activación del Escudo de Congelamiento (*Streak Freeze*) | En la pantalla de racha, pulsar "Congelar Racha Hoy" teniendo escudos disponibles. | El contador de escudos se reduce en 1, la llama adopta color azul gélido (❄️) y la racha queda protegida contra reseteos si no se camina. | HU-Racha-02 |
| **CP-13** | Reparación de Racha (*Streak Repair*) en periodo de gracia | Simular día anterior sin cumplir meta. Al día siguiente abrir la pantalla de racha. | Aparece el banner de advertencia "¡Salva tu Racha!". Al pulsar "Reparar Racha", se restauran los días acumulados. | HU-Racha-03 |
| **CP-14** | Configuración personalizada de meta de distancia | Seleccionar el chip de "5 km" en la pantalla de racha. Cerrar y volver a abrir la aplicación. | La nueva meta de 5.0 km permanece guardada en la base de datos Room y los porcentajes se recalculan contra 5 km. | HU-Racha-04 |
| **CP-15** | Detección agnóstica de fuentes de salud | Conectar otro wearable o simular datos desde Google Fit / Fitbit. Abrir la pantalla de Estado de Conexión. | `HealthDeviceResolver` identifica y lista el nombre de la app y tipo de dispositivo detectado sin requerir cambios de código. | RNF-MultiDev |
| **CP-16** | Recompensa de escudo de congelamiento | Mantener la racha activa durante 7 días consecutivos. | El sistema otorga automáticamente +1 escudo de congelamiento al inventario del usuario. | HU-Racha-05 |
| **CP-17** | Cálculo opcional de IMC y meta recomendada de distancia | En la pantalla de racha, ingresar peso (ej. 70 kg) y estatura (ej. 175 cm). Tocar "Calcular y Guardar IMC" y luego "Aplicar meta recomendada". | Calcula IMC (22.9 kg/m², Saludable), sugiere 5.0 km/día y actualiza la meta activa de la racha a 5.0 km persistiendo los datos. | HU-Racha-06 |

---

## 4. Guía para Pruebas con "Health Connect Toolbox"

Si deseas realizar pruebas de simulación sin necesidad de caminar físicamente con el reloj o antes de vincular el Galaxy Fit 3:

1. Instala la app oficial de Google **Health Connect Toolbox** desde Google Play Store.
2. Abre la Toolbox y autoriza permisos de escritura en Health Connect.
3. Inserta registros de prueba:
   * **Steps:** 5,000 pasos.
   * **Distance:** 3.5 km.
   * **Heart Rate:** Muestras de 68 lpm, 75 lpm, 90 lpm, 110 lpm.
   * **Sleep Session:** Sesión de 7 horas y 30 minutos.
4. Abre nuestra aplicación **Health** y pulsa **[ 🔄 ]**: observa cómo el Dashboard, el gráfico de FC y la racha reaccionan de inmediato a los datos simulados.
