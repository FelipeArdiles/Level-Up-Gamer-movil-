# 🧪 Cómo Ejecutar Pruebas en Android Studio

Esta guía te muestra paso a paso cómo ejecutar las pruebas unitarias en Android Studio.

---

## 📍 Ubicación de las Pruebas

### Pruebas Unitarias (test)
**Ubicación**: `app/src/test/java/com/example/level_up_gamer/`

Estas pruebas corren en la JVM (rápidas, sin necesidad de dispositivo):
- ✅ `viewmodel/AuthViewModelTest.kt` - Pruebas con MockK
- ✅ `viewmodel/RegistrationViewModelTest.kt` - Pruebas con MockK
- ✅ `viewmodel/CoroutinesTestExample.kt` - Pruebas con coroutines-test
- ✅ `repository/AuthRepositoryCoroutinesTest.kt` - Pruebas asíncronas
- ✅ `utils/BusinessLogicTest.kt` - Pruebas con Kotest
- ✅ `model/ProductLogicTest.kt` - Pruebas con Kotest

### Pruebas de Instrumentación (androidTest)
**Ubicación**: `app/src/androidTest/java/com/example/level_up_gamer/`

Estas pruebas requieren un dispositivo o emulador Android:
- ✅ `ui/screens/LoginScreenTest.kt` - Pruebas de UI con JUnit5
- ✅ `ui/screens/RegistrationScreenTest.kt` - Pruebas de UI con JUnit5

---

## 🚀 Método 1: Ejecutar una Prueba Individual

### Paso a Paso:

1. **Abre el archivo de prueba** en Android Studio
   - Por ejemplo: `AuthViewModelTest.kt`

2. **Busca el icono de "play" (▶️)** al lado de:
   - La clase (ejecuta todas las pruebas de la clase)
   - Un método `@Test` individual (ejecuta solo esa prueba)

3. **Click en el icono de play**
   - Android Studio ejecutará la prueba

4. **Ver los resultados**:
   - Los resultados aparecen en la parte inferior en la pestaña "Run"
   - ✅ Verde = Prueba pasó
   - ❌ Rojo = Prueba falló

**Ejemplo visual**:
```
class AuthViewModelTest {
    @Test
    fun shouldUpdateEmailWhenChanged() {  // ▶️ Click aquí para ejecutar solo esta prueba
        // ...
    }
}
// ▶️ Click aquí para ejecutar todas las pruebas de la clase
```

---

## 🚀 Método 2: Ejecutar Todas las Pruebas de un Directorio

### Para Pruebas Unitarias (test):

1. **En el panel "Project"** (lado izquierdo)
2. **Navega a**: `app/src/test/java/com/example/level_up_gamer/`
3. **Click derecho** en:
   - Una carpeta (ej: `viewmodel/`) → "Run 'Tests in 'viewmodel'"
   - O en `test/` completo → "Run 'Tests in 'test'"
4. **Android Studio ejecutará todas las pruebas** en esa ubicación

### Para Pruebas de Instrumentación (androidTest):

1. **En el panel "Project"**
2. **Navega a**: `app/src/androidTest/java/com/example/level_up_gamer/`
3. **Click derecho** en:
   - Una carpeta (ej: `ui/screens/`) → "Run 'Tests in 'screens'"
   - O en `androidTest/` completo → "Run 'Tests in 'androidTest'"
4. **IMPORTANTE**: Asegúrate de tener un dispositivo o emulador corriendo

---

## 🚀 Método 3: Usar el Menú de Configuración de Ejecución

### Paso a Paso:

1. **En la barra superior**, busca el dropdown de configuración de ejecución
   - Normalmente dice "app" o el nombre de tu configuración

2. **Click en el dropdown** y selecciona:
   - **"test"** para ejecutar pruebas unitarias
   - **"androidTest"** para ejecutar pruebas de instrumentación

3. **Click en el botón "Run" (▶️)** o presiona `Shift + F10`

4. **Ver los resultados** en la pestaña "Run" en la parte inferior

---

## 🚀 Método 4: Usar Atajos de Teclado

### Atajos Útiles:

- **`Ctrl + Shift + F10`** (Windows/Linux) o **`Cmd + Shift + R`** (Mac):
  - Ejecuta la prueba donde está el cursor

- **`Ctrl + F10`** (Windows/Linux) o **`Cmd + R`** (Mac):
  - Ejecuta la configuración seleccionada

- **`Ctrl + Shift + F9`** (Windows/Linux) o **`Cmd + Shift + D`** (Mac):
  - Depura la prueba donde está el cursor

---

## 🚀 Método 5: Ejecutar desde la Terminal de Android Studio

### Paso a Paso:

1. **Abre la terminal** en Android Studio (parte inferior)
   - O ve a: `View → Tool Windows → Terminal`

2. **Ejecuta los comandos Gradle**:

```bash
# Ejecutar todas las pruebas unitarias
./gradlew test

# Ejecutar todas las pruebas de instrumentación (requiere dispositivo)
./gradlew connectedAndroidTest

# Ejecutar una clase específica
./gradlew test --tests "com.example.level_up_gamer.viewmodel.AuthViewModelTest"

# Ejecutar un método específico
./gradlew test --tests "com.example.level_up_gamer.viewmodel.AuthViewModelTest.shouldUpdateEmailWhenChanged"

# Ver reporte HTML de las pruebas
./gradlew test
# Luego abre: app/build/reports/tests/test/index.html
```

---

## 📊 Ver los Resultados

### Pestaña "Run":

Después de ejecutar las pruebas, verás:

```
✅ shouldUpdateEmailWhenChanged() - PASSED (50ms)
✅ shouldUpdatePasswordWhenChanged() - PASSED (30ms)
❌ shouldAuthenticateSuccessfully() - FAILED (100ms)
   AssertionError: Expected true but was false
```

### Pestaña "Test Results":

- **Vista de árbol**: Muestra la estructura de las pruebas
- **Filtros**: Puedes filtrar por "Passed", "Failed", "Skipped"
- **Búsqueda**: Busca pruebas específicas

---

## 🐛 Depurar Pruebas

### Paso a Paso:

1. **Coloca un breakpoint** en la línea que quieres depurar
   - Click en el margen izquierdo (aparece un punto rojo)

2. **Click derecho** en la prueba → "Debug 'nombreDeLaPrueba'"
   - O usa el atajo: `Ctrl + Shift + F9` (Windows/Linux) o `Cmd + Shift + D` (Mac)

3. **La ejecución se pausará** en el breakpoint
   - Puedes inspeccionar variables
   - Avanzar paso a paso con F8

---

## ⚠️ Solución de Problemas

### Error: "No tests found"

**Solución**:
1. Verifica que el archivo esté en `app/src/test/` o `app/src/androidTest/`
2. Asegúrate de que la clase tenga métodos con `@Test`
3. Reconstruye el proyecto: `Build → Rebuild Project`

### Error: "Device not found" (para androidTest)

**Solución**:
1. Abre el "Device Manager" (Android Studio)
2. Inicia un emulador o conecta un dispositivo físico
3. Verifica que aparezca en la lista de dispositivos

### Error: "JUnit5 not found"

**Solución**:
1. Sincroniza Gradle: `File → Sync Project with Gradle Files`
2. Verifica que las dependencias estén en `build.gradle.kts`

### Las pruebas no aparecen con iconos de play

**Solución**:
1. **Sincroniza Gradle**: `File → Sync Project with Gradle Files`
2. **Reconstruye el proyecto**: `Build → Rebuild Project`
3. **Cierra y vuelve a abrir** el archivo de prueba

---

## 📝 Ejemplos Prácticos

### Ejemplo 1: Ejecutar una Prueba con MockK

1. Abre: `app/src/test/.../AuthViewModelTest.kt`
2. Busca el método: `shouldUpdateEmailWhenChanged()`
3. Click en el icono ▶️ al lado del método
4. Verás los resultados en la pestaña "Run"

### Ejemplo 2: Ejecutar Todas las Pruebas de UI

1. En el panel "Project", navega a: `app/src/androidTest/.../ui/screens/`
2. Click derecho en `screens/` → "Run 'Tests in 'screens'"
3. **Asegúrate de tener un emulador corriendo**
4. Las pruebas se ejecutarán en el dispositivo

### Ejemplo 3: Ejecutar Pruebas con Kotest

1. Abre: `app/src/test/.../BusinessLogicTest.kt`
2. Click en el icono ▶️ al lado de la clase
3. Todas las pruebas de Kotest se ejecutarán
4. Verás resultados organizados por `describe` e `it`

---

## 🎯 Resumen Rápido

| Tipo de Prueba | Ubicación | Cómo Ejecutar |
|----------------|-----------|---------------|
| **MockK** | `test/viewmodel/` | Click ▶️ en el método o clase |
| **coroutines-test** | `test/viewmodel/` | Click ▶️ en el método o clase |
| **Kotest** | `test/utils/` o `test/model/` | Click ▶️ en la clase |
| **JUnit5 UI** | `androidTest/ui/screens/` | Click ▶️ (requiere dispositivo) |

---

## 💡 Tips Útiles

1. **Ejecuta pruebas frecuentemente** mientras desarrollas
2. **Usa "Run" en lugar de "Debug"** para pruebas rápidas
3. **Revisa los resultados** en la pestaña "Run" para ver detalles
4. **Usa filtros** en "Test Results" para encontrar pruebas fallidas rápidamente
5. **Configura ejecuciones** para ejecutar grupos específicos de pruebas

---

¡Ahora estás listo para ejecutar todas las pruebas! 🚀

