# Guía para una IA: Cómo integrar Mapbox Maps SDK en un proyecto de Android Studio

Tu tarea como IA es guiar a un usuario paso a paso para integrar **Mapbox Maps SDK for Android** en su aplicación.  
El usuario trabaja con Android Studio y quiere mostrar un mapa en su app, evitando usar Google Maps porque pide tarjeta.

A continuación se describen **todas las instrucciones** que debes seguir y explicar.

---

## 0. Contexto y objetivo

- Objetivo: integrar **Mapbox** en una app Android y mostrar un mapa centrado en una ubicación (ej. Santiago de Chile).
- Entorno: Android Studio, proyecto Android (puede ser nuevo o existente).
- Lenguaje sugerido: **Kotlin** (aunque puede adaptarse a Java si el usuario lo pide).
- Estilo de explicación: claro, paso a paso, en español.

---

## 1. Crear cuenta en Mapbox y obtener el token

Indica al usuario:

1. Ir a la web de **Mapbox** y crear una cuenta gratuita o iniciar sesión.
2. En el panel de Mapbox, ir a la sección **Tokens**.
3. Copiar su **Public Access Token**, que normalmente empieza con `pk...`.

Este token se usará para autenticar el SDK dentro de la app Android.

---

## 2. Guardar el token en `res/values/mapbox_access_token.xml`

El usuario debe:

1. En Android Studio, ir a la carpeta:  
   `app/src/main/res/values/`
2. Crear un archivo nuevo de recursos de valores:  
   `New → Values Resource File → Name: mapbox_access_token.xml`
3. Pegar este contenido, reemplazando el texto en mayúsculas con su token:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools">
    <string
        name="mapbox_access_token"
        translatable="false"
        tools:ignore="UnusedResources">
        TU_TOKEN_PUBLICO_DE_MAPBOX_AQUI
    </string>
</resources>
```

Este string (`R.string.mapbox_access_token`) se usará más adelante.

---

## 3. Configurar el repositorio de Mapbox en `settings.gradle`

En el archivo `settings.gradle` del **proyecto** (no el del módulo), dentro de `dependencyResolutionManagement`, el usuario debe asegurarse de tener algo como:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Repositorio de Mapbox
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
    }
}
```

> Nota para la IA: Si el proyecto está en Groovy, la sintaxis de `maven { url "..." }` cambia un poco, pero la idea es la misma.

---

## 4. Añadir la dependencia de Mapbox en `app/build.gradle`

En el archivo `app/build.gradle` (módulo de la app), el usuario debe:

1. Asegurarse de que `minSdk` sea **21** o superior:

```kotlin
android {
    defaultConfig {
        applicationId = "com.ejemplo.mapboxapp"
        minSdk = 21
        targetSdk = 34
        // ...
    }
}
```

2. Agregar la dependencia de Mapbox en `dependencies`:

```kotlin
dependencies {
    implementation("com.mapbox.maps:android:11.16.6")
    // Si en algún caso necesita soporte NDK 27, podría usar:
    // implementation("com.mapbox.maps:android-ndk27:11.16.6")
}
```

3. Ejecutar **Sync Project with Gradle Files**.

---

## 5. (Opcional) Permisos de ubicación en `AndroidManifest.xml`

Si el usuario quiere mostrar la **ubicación actual** del dispositivo, indícale que agregue estos permisos arriba del `<application>` en `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Aclárale que, en Android 6+, también deberá pedir los permisos en tiempo de ejecución (runtime permissions).

Para simplemente mostrar un mapa estático, estos permisos **no son obligatorios**.

---

## 6. Ejemplo con Views: `MapView` en una `Activity` (Kotlin)

La forma más sencilla sin Jetpack Compose es crear el `MapView` programáticamente en una Activity.

### 6.1. Crear o editar `MainActivity.kt`

Ejemplo de Activity minimal con Mapbox:

```kotlin
package com.ejemplo.mapboxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.MapInitOptions

class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Obtener el access token desde los resources
        val token = getString(R.string.mapbox_access_token)

        // 2. Crear las opciones de recursos con el token
        val resourceOptions = ResourceOptions.Builder()
            .accessToken(token)
            .build()

        // 3. Crear las opciones de inicialización del mapa
        val mapInitOptions = MapInitOptions(
            context = this,
            resourceOptions = resourceOptions
        )

        // 4. Crear el MapView programáticamente
        mapView = MapView(this, mapInitOptions)

        // 5. Configurar la cámara inicial (Santiago, Chile como ejemplo)
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-70.6693, -33.4489)) // lng, lat
                .zoom(11.0)
                .pitch(0.0)
                .bearing(0.0)
                .build()
        )

        // 6. Establecer el MapView como contenido de la Activity
        setContentView(mapView)
    }

    // 7. Manejar el ciclo de vida del MapView
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
```

Puntos clave que debes recalcar al usuario:

- El `access token` se obtiene desde `R.string.mapbox_access_token`.
- Es importante llamar a los métodos de ciclo de vida (`onStart`, `onStop`, etc.) para evitar problemas de memoria.
- La cámara se centra con coordenadas `lng, lat` (longitud, latitud).

---

## 7. Ejemplo con Jetpack Compose (opcional)

Si el usuario usa **Jetpack Compose**, también puede integrar Mapbox así:

1. Activar Compose en `app/build.gradle`:

```kotlin
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2" // o la versión que use el proyecto
    }
}
```

2. Añadir la extensión de Mapbox para Compose:

```kotlin
dependencies {
    implementation("com.mapbox.maps:android:11.16.6")
    implementation("com.mapbox.extension:maps-compose:11.16.6")
}
```

3. Usar `MapboxMap` en la `MainActivity`:

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el token global de Mapbox antes de usar el mapa
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)

        setContent {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = rememberMapViewportState {
                    setCameraOptions {
                        zoom(11.0)
                        center(Point.fromLngLat(-70.6693, -33.4489)) // lng, lat
                    }
                }
            )
        }
    }
}
```

Aclara al usuario que:

- Aquí se usa Compose en lugar de Views.
- `MapboxOptions.accessToken` se configura antes de renderizar el mapa.

---

## 8. Resumen para la IA

Cuando un usuario te pida **“cómo integrar Mapbox con Android Studio”**, debes:

1. Explicar que Mapbox requiere:
   - Crear cuenta.
   - Obtener **public access token**.
2. Mostrar cómo guardar el token en `res/values/mapbox_access_token.xml`.
3. Explicar cómo agregar el repositorio de Mapbox en `settings.gradle`.
4. Indicar la dependencia `com.mapbox.maps:android:11.16.6` (o la versión estable actual).
5. Dar un ejemplo funcional con:
   - `MapView` en una `Activity` (Views).
   - Opcionalmente, ejemplo con Compose.
6. Mencionar que los permisos de ubicación son necesarios **solo** si se quiere la ubicación del usuario.

Tu respuesta siempre debe ir acompañada de **código listo para copiar/pegar** y comentarios simples para que el usuario entienda cada paso.
