# 🏦 Sistema de Gestión Bancaria
### Aplicación de Consola en Java | Vargas Fabián

![Java](https://img.shields.io/badge/Java-8%2B-orange?style=flat-square&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?style=flat-square&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Connector-green?style=flat-square)
![Pattern](https://img.shields.io/badge/Pattern-MVC%20%7C%20DAO-purple?style=flat-square)
![POO](https://img.shields.io/badge/Paradigma-POO-red?style=flat-square)

---

## 📋 Descripción General

**Sistema de Gestión Bancaria** es una aplicación de consola desarrollada en Java que simula las operaciones fundamentales de un sistema bancario. Permite a los clientes registrarse, iniciar sesión y gestionar distintos tipos de cuentas bancarias (Ahorro, Crédito y Débito), aplicando operaciones como depósitos, retiros, transferencias y generación de intereses.

El proyecto fue diseñado con énfasis en buenas prácticas de programación orientada a objetos, patrones de diseño establecidos y una arquitectura en capas clara y mantenible.

---

## 🗂️ Estructura del Proyecto

```
src/
├── vargas/fabian/
│   ├── bl/
│   │   ├── entities/          # Capa de Modelo (entidades del dominio)
│   │   │   ├── Cliente.java
│   │   │   ├── Cuenta.java            ← Clase abstracta base
│   │   │   ├── CuentaAhorro.java
│   │   │   ├── CuentaCredito.java
│   │   │   └── CuentaDebito.java
│   │   ├── dao/               # Acceso a datos (patrón DAO)
│   │   │   ├── DAOCliente.java
│   │   │   ├── DAOCuentaAhorro.java
│   │   │   ├── DAOCuentaCredito.java
│   │   │   └── DAOCuentaDebito.java
│   │   └── logic/             # Lógica de negocio (capa de servicio)
│   │       ├── GestorCliente.java
│   │       ├── GestorCuentaAhorro.java
│   │       ├── GestorCuentaCredito.java
│   │       └── GestorCuentaDebito.java
│   ├── tl/
│   │   └── Controlador.java   # Controlador principal (MVC)
│   ├── ui/
│   │   └── Menu.java          # Vista / Interfaz de usuario (MVC)
│   ├── dl/
│   │   ├── AccesoBD.java      # Operaciones genéricas con la BD
│   │   └── Conector.java      # Singleton de conexión
│   └── utils/
│       └── Utilidades.java    # Lectura de credenciales desde archivo
│       └── bd.properties      # ⚠️ Excluido del repositorio (.gitignore)
│
└── Main.java
```

---

## 📐 Diagrama de Clases (Simplificado)

```
                        ┌──────────────────┐
                        │   <<abstract>>   │
                        │     Cuenta       │
                        │──────────────────│
                        │ - numeroCuenta   │
                        │ - saldo          │
                        │ - tasaInteres    │
                        │──────────────────│
                        │ + retirar()      │  ← abstracto
                        │ + depositar()    │  ← abstracto
                        │ + generarInt..() │  ← abstracto
                        └────────┬─────────┘
                                 │ hereda
            ┌────────────────────┼───────────────────┐
            ▼                    ▼                   ▼
  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
  │  CuentaAhorro    │  │  CuentaCredito   │  │  CuentaDebito    │
  │──────────────────│  │──────────────────│  │──────────────────│
  │ saldoMínimo:₡100 │  │ limiteCredito    │  │ (sin límite min) │
  │──────────────────│  │──────────────────│  │──────────────────│
  │ + retirar()      │  │ + retirar()      │  │ + retirar()      │
  │ + depositar()    │  │ + depositar()    │  │ + depositar()    │
  │ + generarInt..() │  │ + generarInt..() │  │ + generarInt..() │
  └──────────────────┘  └──────────────────┘  └──────────────────┘
            │                    │                   │
            ▼                    ▼                   ▼
  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
  │ DAOCuentaAhorro  │  │ DAOCuentaCredito │  │ DAOCuentaDebito  │
  └──────────────────┘  └──────────────────┘  └──────────────────┘
            │                    │                   │
            ▼                    ▼                   ▼
  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
  │GestorCuentaAhorro│  │GestorCuenta...   │  │GestorCuenta...   │
  └──────────────────┘  └──────────────────┘  └──────────────────┘
                                 │
                        ┌────────▼─────────┐
                        │   Controlador    │
                        └────────┬─────────┘
                                 │
                        ┌────────▼─────────┐
                        │      Menú        │
                        └──────────────────┘

  ┌──────────────────────────────────────────────────────┐
  │                      Cliente                         │
  │ - cédula | nombre | contraseña | fechaNacimiento     │
  │ - tiene varias Cuentas (1..N)                        │
  └──────────────────────────────────────────────────────┘

  ┌──────────────────────────────────────────────────────┐
  │           Conector  (Singleton)                      │
  │                                                      │
  │  instancia única ──► AccesoBD ──► DAOs               │
  └──────────────────────────────────────────────────────┘
```

---

## ⚙️ Funcionalidades Principales

### 👤 Gestión de Clientes
| Funcionalidad | Descripción |
|---|---|
| Registro | Crea un nuevo cliente con nombre, cédula, contraseña y fecha de nacimiento |
| Validación de edad | Un **trigger en la base de datos** rechaza registros de menores de 18 años automáticamente |
| Inicio de sesión | Autenticación mediante cédula y contraseña |

### 💳 Tipos de Cuenta
| Tipo | Característica Principal |
|---|---|
| **Ahorro** | Requiere un saldo mínimo de **₡100** en todo momento |
| **Crédito** | Permite saldo negativo (deuda) hasta un límite de crédito definido |
| **Débito** | Operaciones directas sin saldo mínimo ni crédito disponible |

### 💰 Operaciones Bancarias

- **Apertura de cuentas**: El cliente puede abrir uno o más tipos de cuenta.
- **Retiro**: Sujeto a validaciones según el tipo de cuenta (saldo mínimo en Ahorro, límite en Crédito).
- **Depósito / Abono**: Depósito de fondos en Ahorro y Débito; abono para pagar deuda en Crédito.
- **Transferencias**:
  - `Ahorro → Ahorro`
  - `Ahorro → Débito`
  - `Débito → Ahorro`
  - `Débito → Débito`
- **Generación de intereses**: Cada cuenta aplica su propia tasa de interés periódicamente.
- **Visualización de cuentas**: Lista todas las cuentas asociadas al cliente activo.
- **Eliminación de cuenta**: Proceso con confirmación explícita antes de proceder.

---

## 🧩 Patrones de Diseño Implementados

### 🗃️ DAO (Data Access Object)
Separa completamente la lógica de acceso a la base de datos de la lógica de negocio. Cada entidad tiene su propio DAO encargado de las operaciones CRUD.

```
GestorCuentaAhorro  →  DAOCuentaAhorro  →  Base de Datos MySQL
```

> **Beneficio**: Si se cambia de base de datos (p. ej., de MySQL a PostgreSQL), únicamente se modifican los DAOs sin tocar la lógica de negocio.

---

### 🔧 Gestor / Service Layer
Actúa como intermediario entre el `Controlador` y los DAOs. Contiene las reglas de negocio (validaciones de saldo, límites, etc.).

```
Controlador  →  GestorCuentaAhorro  →  DAOCuentaAhorro
```

> **Beneficio**: El controlador queda limpio de lógica compleja; cada gestor es responsable de una sola entidad.

---

### 🔒 Singleton (Conector)
La clase `Conector` garantiza que exista **una única instancia** de la conexión a la base de datos durante toda la ejecución de la aplicación.

```java
// Ejemplo conceptual del patrón Singleton
public class Conector {
    private static Conector instancia;

    private Conector() { /* inicializa conexión */ }

    public static Conector getInstancia() {
        if (instancia == null) {
            instancia = new Conector();
        }
        return instancia;
    }
}
```

> **Beneficio**: Evita múltiples conexiones abiertas simultáneamente, optimizando el uso de recursos.

---

### 🏗️ MVC (Model - View - Controller)
| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Modelo** | `bl.entities` | Representación de los datos del dominio |
| **Vista** | `ui.Menu` | Presentación de menús e interacción con el usuario |
| **Controlador** | `tl.Controlador` | Coordina la vista con la lógica de negocio |

> **Beneficio**: Separación de responsabilidades clara; facilita el mantenimiento y escalabilidad del sistema.

---

## 🧠 Principios POO Aplicados

### 🔷 Abstracción
La clase `Cuenta` es abstracta y define el contrato que deben cumplir todos los tipos de cuenta sin especificar la implementación concreta.

### 🔷 Herencia
`CuentaAhorro`, `CuentaCredito` y `CuentaDebito` heredan de `Cuenta`, reutilizando atributos y comportamientos comunes.

```
Cuenta (abstracta)
  ├── CuentaAhorro
  ├── CuentaCredito
  └── CuentaDebito
```

### 🔷 Polimorfismo
Los métodos `retirar()`, `depositar()` y `generarIntereses()` se comportan de manera diferente según el tipo de cuenta, aunque comparten la misma firma definida en `Cuenta`.

```java
// Ejemplo de polimorfismo en tiempo de ejecución
Cuenta cuenta = new CuentaAhorro(...);
cuenta.retirar(5000); // ejecuta la lógica específica de CuentaAhorro
```

### 🔷 Encapsulamiento
Todos los atributos de las entidades son **privados** y se exponen únicamente a través de métodos `get` y `set`, protegiendo la integridad de los datos.

---

## 🛠️ Instalación y Configuración

### Prerrequisitos

- Java JDK 8 o superior
- MySQL Server 5.7 o superior
- IDE compatible (IntelliJ IDEA, Eclipse, NetBeans)
- Git (opcional)

### Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/sistema-bancario.git
cd sistema-bancario
```

### Paso 2 — Configurar la base de datos

1. Abrir MySQL y ejecutar el script de creación:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
2. Verificar que la base de datos `banco_db` se haya creado correctamente.

### Paso 3 — Configurar las credenciales

Crear el archivo `src/resources/bd.properties` con el siguiente contenido:

```properties
db.url=jdbc:mysql://localhost:3306/banco_db
db.user=tu_usuario
db.password=tu_contraseña
```

> ⚠️ **Importante**: Este archivo está incluido en `.gitignore` y **nunca debe subirse al repositorio**.

### Paso 4 — Compilar el proyecto

Desde el IDE, compilar el proyecto asegurándose de que el driver JDBC de MySQL (`mysql-connector-java.jar`) esté en el classpath.

---

## ▶️ Cómo Ejecutar el Programa

### Desde el IDE
1. Abrir el proyecto en el IDE de preferencia.
2. Localizar la clase principal (la que contiene `main`).
3. Ejecutar con `Run`.

### Desde consola
```bash
javac -cp .;lib/mysql-connector-java.jar -d out src/vargas/fabian/**/*.java
java -cp out;lib/mysql-connector-java.jar vargas.fabian.ui.Menu
```

> En sistemas Linux/macOS reemplazar `;` por `:` en el classpath.

---

## 🗄️ Estructura de la Base de Datos

### Tablas Principales

```sql
-- Tabla de clientes
CREATE TABLE Cliente (
    cedula       VARCHAR(20)  PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    contrasena   VARCHAR(255) NOT NULL,
    fecha_nac    DATE         NOT NULL
);

-- Tabla de cuentas (base)
CREATE TABLE Cuenta (
    numero_cuenta INT          PRIMARY KEY AUTO_INCREMENT,
    saldo         DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tasa_interes  DECIMAL(5,4)  NOT NULL,
    tipo          ENUM('AHORRO','CREDITO','DEBITO') NOT NULL,
    cedula_cliente VARCHAR(20) NOT NULL,
    FOREIGN KEY (cedula_cliente) REFERENCES Cliente(cedula)
);

-- Tabla de cuentas de ahorro
CREATE TABLE CuentaAhorro (
    numero_cuenta INT PRIMARY KEY,
    saldo_minimo  DECIMAL(15,2) NOT NULL DEFAULT 100.00,
    FOREIGN KEY (numero_cuenta) REFERENCES Cuenta(numero_cuenta)
);

-- Tabla de cuentas de crédito
CREATE TABLE CuentaCredito (
    numero_cuenta   INT PRIMARY KEY,
    limite_credito  DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (numero_cuenta) REFERENCES Cuenta(numero_cuenta)
);

-- Tabla de cuentas de débito
CREATE TABLE CuentaDebito (
    numero_cuenta INT PRIMARY KEY,
    FOREIGN KEY (numero_cuenta) REFERENCES Cuenta(numero_cuenta)
);
```

### 🔔 Trigger: Validación de Edad Mínima

```sql
DELIMITER $$

CREATE TRIGGER validar_edad_cliente
BEFORE INSERT ON Cliente
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.fecha_nac, CURDATE()) < 18 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente debe ser mayor de 18 años.';
    END IF;
END$$

DELIMITER ;
```

> Este trigger actúa como capa de seguridad a nivel de base de datos, independiente de la validación en la capa de negocio.

---

## 💡 Ejemplos de Uso

### Registrar un nuevo cliente
```
========== MENÚ PRINCIPAL ==========
1. Iniciar sesión
2. Registrar nuevo cliente
3. Salir
Seleccione una opción: 2

Ingrese su nombre: Ana González
Ingrese su cédula: 1-1234-5678
Ingrese su contraseña: ****
Ingrese su fecha de nacimiento (dd/mm/aaaa): 15/03/1995

✔ Cliente registrado exitosamente.
```

### Abrir una cuenta de ahorro
```
========== MENÚ DE CUENTAS ==========
1. Ver mis cuentas
2. Abrir nueva cuenta
...
Seleccione: 2

Tipo de cuenta:
1. Ahorro  2. Crédito  3. Débito
Seleccione: 1

Saldo inicial (mínimo ₡100): 5000

✔ Cuenta de Ahorro abierta. Número: 10042
```

### Realizar una transferencia
```
========== TRANSFERENCIA ==========
Cuenta origen: 10042
Cuenta destino: 10055
Monto a transferir: ₡2000

¿Confirmar transferencia? (S/N): S
✔ Transferencia realizada exitosamente.
   Saldo actual: ₡3000
```

---

## 🚀 Posibles Mejoras Futuras

| Mejora | Descripción |
|---|---|
| 🌐 Interfaz gráfica | Migrar la vista de consola a una GUI con JavaFX o Swing |
| 🔐 Cifrado de contraseñas | Implementar hashing con BCrypt o SHA-256 para almacenar contraseñas |
| 📊 Historial de transacciones | Registrar y mostrar el historial completo de movimientos por cuenta |
| 🔄 Conexión pooling | Reemplazar el Singleton simple por un pool de conexiones (HikariCP) |
| 🧪 Pruebas unitarias | Agregar cobertura de tests con JUnit 5 para gestores y DAOs |
| 📧 Notificaciones | Enviar confirmaciones por correo al realizar operaciones importantes |
| 🛡️ Manejo avanzado de excepciones | Implementar excepciones personalizadas por tipo de error bancario |
| 🌍 Internacionalización | Soporte para múltiples idiomas y formatos de moneda |

---

## 👨‍💻 Autor

**Fabián Vargas**
Proyecto desarrollado como parte de un curso de Programación Orientada a Objetos.

---

> 📌 *Este proyecto fue construido con fines académicos y demuestra la aplicación de patrones de diseño y principios de POO en un contexto bancario simulado.*
