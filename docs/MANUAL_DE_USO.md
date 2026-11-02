# Manual de Uso y Guía del Usuario
**Proyecto:** Health — Monitor de Salud y Rachas para Smartwatch  
**Institución:** Universidad Tecnológica de Nezahualcóyotl (UTN) — ITIC-901  
**Materia:** Desarrollo para dispositivos inteligentes  

---

## 1. Inicio y Onboarding

Al abrir la aplicación por primera vez, el usuario es recibido con una interfaz limpia diseñada bajo **Material Design 3**:

```text
+------------------------------------------+
|                 [ ❤️ ]                   |
|           Bienvenido a Health            |
|  Tu centro de salud inteligente para     |
|   smartwatch (Galaxy Fit 3 y más)        |
|                                          |
|  ✓ Pasos, distancia y calorías           |
|  ✓ Frecuencia cardíaca continua          |
|  ✓ Calidad de descanso y sueño           |
|  ✓ 100% Privado y local (Sin nube)       |
|                                          |
|  [    CONCEDER PERMISOS DE SALUD    ]    |
|  [       POLÍTICA DE PRIVACIDAD     ]    |
+------------------------------------------+
```

1. Pulsa el botón **"Conceder Permisos de Salud"**.
2. En la pantalla del sistema de Android Health Connect, activa las cinco métricas:
   * **Pasos** (*Steps*)
   * **Distancia** (*Distance*)
   * **Calorías** (*Total calories burned*)
   * **Frecuencia cardíaca** (*Heart rate*)
   * **Sueño** (*Sleep*)
3. Pulsa **Permitir**. La app cargará inmediatamente tu Dashboard principal.

---

## 2. Pantalla Principal (Dashboard)

El Dashboard organiza la información diaria en tarjetas limpias e interactivas:

```text
+-------------------------------------------------------+
|  Health                   [ 🔥 ] [ 📊 ] [ ⌚ ] [ 🛡️ ]  |
+-------------------------------------------------------+
|  🟢 Última sinc: 14:25:10               [ 🔄 ]        |
+-------------------------------------------------------+
|  +-------------------------------------------------+  |
|  |  ( 🔥 )  7 días de racha                        |  |
|  |  Meta: 3.0 km hoy (Faltan 0.60 km)              |  |
|  |  🛡️ 2 escudos de congelamiento disponibles       |  |
|  +-------------------------------------------------+  |
|                                                       |
|  Métricas de Hoy                                      |
|  +-------------------------------------------------+  |
|  |  Pasos                          [ 🚶 ] 8,432     |  |
|  +-------------------------------------------------+  |
|  |  Distancia                      [ 📏 ] 5.82 km   |  |
|  +-------------------------------------------------+  |
|  |  Calorías quemadas              [ 🔥 ] 480 kcal  |  |
|  +-------------------------------------------------+  |
|  |  Frecuencia Cardíaca (Promedio) [ ❤️ ] 72 lpm    |  |
|  |  > Toca para ver gráfica de muestras del día    |  |
|  +-------------------------------------------------+  |
|  |  Sueño de anoche                [ 🌙 ] 7h 25m    |  |
|  +-------------------------------------------------+  |
|                                                       |
|  [     VER HISTORIAL COMPLETO (7 Y 30 DÍAS)     ]     |
+-------------------------------------------------------+
```

### Funciones Principales del Dashboard:
* **Barra de Estado de Sincronización:** Indica la hora exacta de la última lectura exitosa.
  * Si la lectura proviene de la caché de la base de datos Room, mostrará: `🟡 Caché local • Última sinc: HH:mm`.
  * El botón **[ 🔄 ]** fuerza una nueva consulta a Health Connect.
* **Tocar la tarjeta de Distancia:** Abre directamente la pantalla de configuración de rachas.
* **Tocar la tarjeta de Frecuencia Cardíaca:** Abre el monitor continuo con la gráfica de muestras en tiempo real.
* **Deslizar para actualizar:** Permite refrescar arrastrando la pantalla hacia abajo.

---

## 3. Centro de Racha de Distancia (Gamificación Estilo Duolingo)

Accesible tocando la llama **[ 🔥 ]** de la barra superior o la tarjeta de racha:

### 3.1. Estado de la Llama
* **Llama Naranja Ardiente (🔥):** Racha activa; meta del día cumplida (+1 día sumado a tu récord).
* **Llama Azul Gélido (❄️):** Racha protegida por un **Escudo de Congelamiento**. Tus días no se pierden hoy.
* **Llama Ámbar de Advertencia:** Racha en riesgo. Aún no has alcanzado la meta diaria fijada.
* **Llama Gris:** Racha inactiva o en cero días.

### 3.2. Configuración de Meta Diaria
Puedes personalizar la distancia mínima requerida seleccionando uno de los accesos rápidos:
* **2 km:** Paseo ligero / recuperación.
* **3 km:** Meta recomendada diaria (por defecto).
* **5 km:** Entrenamiento moderado o caminata activa.
* **8 km o 10 km:** Atletas y corredores de alto rendimiento.

### 3.3. Recomendación Inteligente de Meta según IMC (Opcional)
Si deseas una meta adaptada a tu perfil físico, puedes calcular tu **Índice de Masa Corporal (IMC)**:
1. En la pantalla de racha, despliega la tarjeta **"Recomendación por IMC (Opcional)"**.
2. Ingresa tu **Peso en kilogramos** (ej. 70.0 kg) y tu **Estatura en centímetros** (ej. 175 cm).
3. Pulsa **"Calcular y Guardar IMC"**.
4. La app categorizará tu estado y te ofrecerá una meta recomendada basada en pautas de salud:
   * **Bajo peso (< 18.5):** Meta sugerida de **2.5 km** (acondicionamiento paulatino).
   * **Peso saludable (18.5 - 24.9):** Meta sugerida de **5.0 km** (equivalente a los 6,000-8,000 pasos diarios recomendados por la OMS).
   * **Sobrepeso (25.0 - 29.9):** Meta sugerida de **3.5 km** (quema de grasa cuidando el impacto articular).
   * **Obesidad (≥ 30.0):** Meta sugerida de **2.5 km** (bajo impacto para proteger rodillas y tobillos).
5. Pulsa el botón **"🎯 Aplicar meta recomendada"** para fijar la distancia calculada en tu racha activa inmediatamente.

### 3.4. Activar Escudo de Congelamiento (*Streak Freeze*)
* Tienes un inventario de hasta **2 escudos**.
* Si sabes que hoy no podrás salir o estás indispuesto, pulsa **"Congelar Racha Hoy"**. El escudo se consumirá y mantendrá tu récord intacto hasta mañana.
* **Recompensa:** Por cada 7 días continuos completados, ganas automáticamente 1 escudo adicional.

### 3.5. Reparar Racha Perdida (*Streak Repair*)
* Si por descuido olvidaste caminar y no tenías escudos activos, aparecerá un aviso destacado:
  > *"¡Salva tu Racha! Perdiste tu racha de X días ayer. Tienes una ventana de gracia para repararla hoy."*
* Pulsa el botón **"Reparar Racha"** dentro de las 24 horas siguientes para restaurar tus días consecutivos.

---

## 4. Monitor Detallado de Frecuencia Cardíaca

Muestra las pulsaciones por minuto (lpm) registradas por el sensor óptico del Galaxy Fit 3 a lo largo del día:
* **Métricas Clave:** Mínima en reposo, Promedio del día y Máxima registrada.
* **Gráfica de Línea Canvas:** Dibuja la curva continua con gradiente suave inferior y líneas guía.
* **Guía de Zonas Cardíacas:**
  * *Zona de Reposo (< 60 lpm):* Sueño o descanso profundo.
  * *Zona Moderada (60 - 100 lpm):* Estado habitual activo.
  * *Zona Aeróbica (100 - 140 lpm):* Caminata a paso rápido o trote ligero.
  * *Zona Intensa / Pico (> 140 lpm):* Ejercicio cardiovascular vigoroso.

---

## 5. Historial de Actividad (7 y 30 Días)

Permite visualizar la evolución de tu actividad física en el tiempo:
1. **Selector de Periodo:** Alterna entre **"7 Días"** (última semana) y **"30 Días"** (último mes).
2. **Selector de Métrica:** Pulsa sobre cualquier filtro (*Pasos, Distancia, Calorías, Frecuencia Cardíaca, Sueño*).
3. **Interacción con la Gráfica de Barras:**
   * Toca cualquier barra del gráfico para resaltar ese día.
   * La tarjeta superior indicará la fecha exacta (ej. *Jue 29 Oct*) y el total registrado con su unidad correspondiente.
4. **Resumen Estadístico:** Muestra el promedio diario del periodo y el día con el valor récord alcanzado.

---

## 6. Estado de Conexión y Diagnóstico de Dispositivos

Accesible desde el icono del reloj **[ ⌚ ]**:
* **Disponibilidad de Health Connect:** Confirma que el SDK está operativo.
* **Lista de Permisos:** Verifica uno a uno qué permisos están concedidos (con check verde) o cuáles faltan.
* **Fuentes y Smartwatches Detectados:** Reconoce la app emisora (ej. *Samsung Health • Samsung Galaxy Fit3* o *Fitbit • Pixel Watch*).
* **Guía rápida de solución de problemas.**
