# Guía de Pruebas Unitarias - LevelUpGamer

Este documento explica cómo funcionan las pruebas unitarias integradas en el proyecto y cómo ejecutarlas.

## 📋 Resumen de Requisitos Cumplidos

### ✅ Pantallas Obligatorias
- ✅ Login y Registro (`LoginScreen`, `RegistrationScreen`)
- ✅ Perfil de usuario (`ProfileScreen`)
- ✅ Edición de perfil (`EditProfileScreen`)
- ✅ Carga de productos (`ProductFormScreen`)
- ✅ Edición de productos (`ProductFormScreen` con modo edición)
- ✅ Carrito de compras (`CartScreen`)

### ✅ Requisitos Técnicos
- ✅ **API REST gratuita**: Integrada (`GameApiService` - API de juegos)
- ✅ **Base de datos SQLite**: Implementada con Room (`AppDatabase`)
- ✅ **Material 3**: Diseño visual funcional con Material 3
- ✅ **Validación de formularios con animación**: Implementada con `AnimatedVisibility` en Login y Registro

### ✅ Pruebas de Software (Mínimo 2 tipos diferentes)
- ✅ **JUnit5** para tests de UI con ComposeTestRule
- ✅ **MockK** para testear sin dependencias reales
- ✅ **coroutines-test** para simular asincronía
- ✅ **Kotest** para lógica de negocio

---

## 🧪 Tipos de Pruebas Implementadas

### 1. Pruebas de UI con JUnit5 y ComposeTestRule

**Ubicación**: `app/src/androidTest/java/com/example/level_up_gamer/ui/screens/`

**Archivos**:
- `LoginScreenTest.kt`
- `RegistrationScreenTest.kt`

**¿Qué hacen?**
- Verifican que los elementos de la UI se muestren correctamente
- Comprueban que las interacciones del usuario funcionen
- Validan que los componentes de Material 3 se rendericen adecuadamente

**Ejemplo**:
```kotlin
@Test
fun shouldDisplayLoginButton() {
    composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    composeTestRule.onNodeWithText("Iniciar Sesión").assertIsEnabled()
}
```

**Ventajas**:
- Pruebas reales de la interfaz de usuario
- Verificación visual de componentes
- Detección de problemas de renderizado

---

### 2. Pruebas con MockK (Sin Dependencias Reales)

**Ubicación**: `app/src/test/java/com/example/level_up_gamer/viewmodel/`

**Archivos**:
- `AuthViewModelTest.kt`
- `RegistrationViewModelTest.kt`

**¿Qué hacen?**
- Prueban la lógica de los ViewModels sin usar la base de datos real
- Simulan el comportamiento de dependencias (repositorios, servicios)
- Verifican que el estado de la UI se actualice correctamente

**Ejemplo**:
```kotlin
// Crear un mock del repositorio
val mockRepository = mockk<AuthRepository>()

// Configurar el comportamiento esperado
coEvery { mockRepository.login(email, password) } returns mockUser

// Probar el ViewModel
viewModel.login()

// Verificar que se llamó al repositorio
coVerify { mockRepository.login(email, password) }
```

**Ventajas**:
- ⚡ **Rápidas**: No hay I/O real (sin base de datos, sin red)
- 🎯 **Aisladas**: Solo prueban la lógica del ViewModel
- 🔧 **Control total**: Puedes simular cualquier escenario (éxito, error, etc.)

**Conceptos clave**:
- **Mock**: Objeto simulado que imita el comportamiento de una dependencia real
- **coEvery**: Define qué debe devolver un método suspendido cuando se llama
- **coVerify**: Verifica que un método se llamó con los parámetros correctos

---

### 3. Pruebas de Operaciones Asíncronas con coroutines-test

**Ubicación**: `app/src/test/java/com/example/level_up_gamer/viewmodel/`

**Archivos**:
- `CoroutinesTestExample.kt`
- `AuthRepositoryCoroutinesTest.kt`

**¿Qué hacen?**
- Prueban operaciones asíncronas (coroutines) sin esperar delays reales
- Controlan el tiempo virtual para ejecutar pruebas rápidamente
- Verifican que las coroutines se ejecuten en el orden correcto

**Ejemplo**:
```kotlin
runTest(testDispatcher) {
    // Simular una operación que tarda 500ms
    coEvery { repository.login(email, password) } coAnswers {
        delay(500)
        mockUser
    }
    
    viewModel.login()
    
    // Avanzar el tiempo virtual (no espera realmente 500ms)
    advanceTimeBy(500)
    
    // Verificar el resultado
    assertTrue(uiState.isAuthenticated)
}
```

**Ventajas**:
- ⏱️ **Control de tiempo**: Avanzas el tiempo sin esperar realmente
- 🚀 **Rápidas**: Las pruebas no esperan delays reales
- 🎯 **Deterministas**: Siempre se ejecutan igual

**Conceptos clave**:
- **StandardTestDispatcher**: Dispatcher de prueba que permite controlar el tiempo
- **advanceTimeBy()**: Avanza el tiempo virtual sin esperar realmente
- **runTest()**: Ejecuta las pruebas en un contexto de coroutines controlado

---

### 4. Pruebas de Lógica de Negocio con Kotest

**Ubicación**: `app/src/test/java/com/example/level_up_gamer/`

**Archivos**:
- `utils/BusinessLogicTest.kt`
- `model/ProductLogicTest.kt`

**¿Qué hacen?**
- Prueban funciones de lógica de negocio (validaciones, cálculos)
- Usan property-based testing para generar datos automáticamente
- Verifican reglas de negocio con múltiples casos

**Ejemplo**:
```kotlin
describe("Validación de Email") {
    it("debería validar emails correctos") {
        val validEmails = listOf("test@example.com", "user@domain.com")
        validEmails.forEach { email ->
            isValidEmail(email) shouldBe true
        }
    }
    
    // Property-based testing: genera emails aleatorios
    it("debería validar emails generados aleatoriamente") {
        checkAll(Arb.email()) { email ->
            isValidEmail(email) shouldBe true
        }
    }
}
```

**Ventajas**:
- 📝 **Legible**: Código más expresivo y fácil de leer
- 🎲 **Property testing**: Genera datos automáticamente para encontrar casos edge
- 🎯 **Múltiples estilos**: DescribeSpec, FunSpec, etc.

**Conceptos clave**:
- **DescribeSpec**: Estilo de pruebas con `describe` e `it`
- **shouldBe**: Aserción más legible que `assertEquals`
- **checkAll**: Property-based testing que genera datos automáticamente
- **Arb.email()**: Generador de emails aleatorios para pruebas

---

## 🚀 Cómo Ejecutar las Pruebas

### Desde Android Studio

1. **Pruebas Unitarias (test)**:
   - Click derecho en `app/src/test` → "Run 'Tests in 'test'"
   - O ejecuta una clase de prueba individual

2. **Pruebas de Instrumentación (androidTest)**:
   - Click derecho en `app/src/androidTest` → "Run 'Tests in 'androidTest'"
   - Requiere un dispositivo o emulador Android

### Desde la Terminal

```bash
# Ejecutar todas las pruebas unitarias
./gradlew test

# Ejecutar todas las pruebas de instrumentación
./gradlew connectedAndroidTest

# Ejecutar una clase específica
./gradlew test --tests "com.example.level_up_gamer.viewmodel.AuthViewModelTest"
```

---

## 📊 Estructura de Pruebas

```
app/
├── src/
│   ├── test/                    # Pruebas unitarias (JVM)
│   │   └── java/com/example/level_up_gamer/
│   │       ├── viewmodel/       # Pruebas con MockK
│   │       │   ├── AuthViewModelTest.kt
│   │       │   ├── RegistrationViewModelTest.kt
│   │       │   └── CoroutinesTestExample.kt
│   │       ├── repository/      # Pruebas con coroutines-test
│   │       │   └── AuthRepositoryCoroutinesTest.kt
│   │       ├── utils/           # Pruebas con Kotest
│   │       │   └── BusinessLogicTest.kt
│   │       └── model/
│   │           └── ProductLogicTest.kt
│   │
│   └── androidTest/             # Pruebas de instrumentación (Android)
│       └── java/com/example/level_up_gamer/
│           └── ui/screens/      # Pruebas de UI con JUnit5
│               ├── LoginScreenTest.kt
│               └── RegistrationScreenTest.kt
```

---

## 🎓 Conceptos Importantes

### Mock vs Real
- **Mock**: Objeto simulado para pruebas rápidas y aisladas
- **Real**: Objeto real que usa dependencias reales (más lento, pero más completo)

### Test vs AndroidTest
- **test**: Pruebas que corren en la JVM (rápidas, sin Android)
- **androidTest**: Pruebas que requieren Android (más lentas, pero prueban la UI real)

### Property-Based Testing
- Genera datos automáticamente para encontrar casos edge
- Ejemplo: Genera 100 emails aleatorios y verifica que todos sean válidos

---

## ✅ Verificación de Requisitos

| Requisito | Estado | Ubicación |
|-----------|--------|-----------|
| JUnit5 para UI | ✅ | `androidTest/ui/screens/` |
| MockK para ViewModels | ✅ | `test/viewmodel/` |
| coroutines-test | ✅ | `test/viewmodel/CoroutinesTestExample.kt` |
| Kotest para lógica | ✅ | `test/utils/BusinessLogicTest.kt` |

**Total: 4 tipos de pruebas diferentes** (requisito mínimo: 2) ✅

---

## 📝 Notas Adicionales

- Las pruebas con MockK son las más rápidas y aisladas
- Las pruebas de UI requieren un dispositivo/emulador
- coroutines-test permite probar código asíncrono de forma determinista
- Kotest ofrece mejor legibilidad y property-based testing

---

## 🔧 Dependencias Agregadas

Todas las dependencias necesarias están en `app/build.gradle.kts`:

```kotlin
// JUnit5
testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

// Kotest
testImplementation("io.kotest:kotest-runner-junit5:5.8.0")

// MockK
testImplementation("io.mockk:mockk:1.13.8")

// coroutines-test
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
```

---

¡Todas las pruebas están listas para ejecutarse! 🎉

