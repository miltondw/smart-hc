# Documentación UML - SMART HC

## Diagramas del Sistema

### 1. Diagrama de Casos de Uso

```
                        SMART HC - Sistema de Historias Clínicas
                        
Actores:
- Paciente
- Médico

┌─────────────────────────────────────────────────────────────────┐
│                        SMART HC SYSTEM                           │
│                                                                  │
│  ┌─────────────────────┐                                        │
│  │  Iniciar Sesión     │ ◄────────── Paciente                   │
│  └─────────────────────┘             Médico                     │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────────┐                                        │
│  │ Completar Historia  │ ◄────────── Paciente                   │
│  │    Clínica          │                                        │
│  └─────────────────────┘                                        │
│           │                                                      │
│           ├──► Registrar Datos Personales                       │
│           ├──► Registrar Antecedentes                           │
│           ├──► Registrar Motivo de Consulta                     │
│           └──► Registrar Signos Vitales                         │
│                                                                  │
│  ┌─────────────────────┐                                        │
│  │ Ver Historia        │ ◄────────── Médico                     │
│  │ Clínica             │                                        │
│  └─────────────────────┘                                        │
│                                                                  │
│  ┌─────────────────────┐                                        │
│  │ Gestionar           │ ◄────────── Médico                     │
│  │ Seguimiento         │                                        │
│  └─────────────────────┘                                        │
│           │                                                      │
│           ├──► Crear Seguimiento                                │
│           ├──► Actualizar Seguimiento                           │
│           └──► Completar Seguimiento                            │
│                                                                  │
│  ┌─────────────────────┐                                        │
│  │ Consultar           │ ◄────────── Médico                     │
│  │ Pacientes           │                                        │
│  └─────────────────────┘                                        │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

### 2. Diagrama de Clases

```
┌──────────────────────────────────────────────────────────────────┐
│                        DIAGRAMA DE CLASES                         │
└──────────────────────────────────────────────────────────────────┘

┌─────────────────────────┐
│     <<abstract>>        │
│       Usuario           │
├─────────────────────────┤
│ - id: String            │
│ - nombre: String        │
│ - apellido: String      │
│ - email: String         │
│ - password: String      │
│ - telefono: String      │
│ - tipo: TipoUsuario     │
│ - fechaCreacion: Date   │
│ - activo: boolean       │
├─────────────────────────┤
│ + getNombreCompleto()   │
└─────────────────────────┘
         △
         │
    ┌────┴────┐
    │         │
┌───▼──────┐  ┌──▼──────────┐
│ Paciente │  │   Médico    │
├──────────┤  ├─────────────┤
│ - numHC  │  │ - matricula │
│ - fechaN │  │ - especial. │
│ - sangre │  │ - instituc. │
│ - direc. │  │ - horarios  │
│ - ciudad │  │ - consult.  │
│ - genero │  └─────────────┘
│ - estCiv │       │
│ - ocupac │       │ 1
│ - contEm │       │
│ - telEm  │       │
│ - peso   │       │
│ - altura │       │
├──────────┤       │
│ + getIMC │       │
│ + getEdad│       │
└──────────┘       │
     │ 1           │
     │             │
     │ 1..1        │ *
     ▼             ▼
┌──────────────────────────┐
│   HistoriaClinica        │
├──────────────────────────┤
│ - id: String             │
│ - pacienteId: String     │
│ - fechaCreacion: Date    │
│ - fechaActualiz: Date    │
│ - antecedentes: Ant.     │◄────┐
│ - motivoConsulta: String │     │
│ - enfermedadActual: Str  │     │
│ - signosVitales: List    │◄─┐  │
│ - adjuntos: List         │  │  │
│ - seguimientos: List     │◄┐│  │
│ - porcentaje: double     │ ││  │
│ - completada: boolean    │ ││  │
├──────────────────────────┤ ││  │
│ + calcularPorcentaje()   │ ││  │
│ + agregarSignos()        │ ││  │
│ + agregarSeguimiento()   │ ││  │
└──────────────────────────┘ ││  │
                             ││  │
                    ┌────────┘│  │
                    │         │  │
        ┌───────────▼─┐   ┌───▼──────────┐   ┌──▼──────────────┐
        │ Seguimiento │   │ SignosVitales│   │  Antecedentes   │
        ├─────────────┤   ├──────────────┤   ├─────────────────┤
        │ - id        │   │ - id         │   │ - enfPers: List │
        │ - pacienteId│   │ - pacienteId │   │ - enfFam: List  │
        │ - medicoId  │   │ - fecha      │   │ - cirugias: List│
        │ - fecha     │   │ - temp       │   │ - alergias: List│
        │ - diagnost. │   │ - presionS   │   │ - medics: List  │
        │ - tratamien.│   │ - presionD   │   │ - vacunas: List │
        │ - evolucion │   │ - frecCard   │   │ - habitos       │
        │ - proxCita  │   │ - frecResp   │   ├─────────────────┤
        │ - esCronico │   │ - saturacO2  │   │ + isCompleto()  │
        │ - estado    │   │ - peso       │   │ + agregar*()    │
        │ - notas     │   │ - altura     │   └─────────────────┘
        ├─────────────┤   │ - observac.  │
        │ + completar │   ├──────────────┤
        └─────────────┘   │ + calcIMC()  │
                          │ + esNormal() │
                          └──────────────┘


┌──────────────────────────────────────────────────────────────────┐
│                        SERVICIOS                                  │
└──────────────────────────────────────────────────────────────────┘

┌─────────────────────┐   ┌──────────────────────────┐
│   AuthService       │   │ HistoriaClinicaService   │
├─────────────────────┤   ├──────────────────────────┤
│ - instance          │   │ - instance               │
│ - usuarioActual     │   │ - storage                │
│ - storage           │   ├──────────────────────────┤
├─────────────────────┤   │ + obtenerHistoria()      │
│ + login()           │   │ + actualizarHistoria()   │
│ + logout()          │   │ + agregarSignos()        │
│ + isLoggedIn()      │   │ + agregarAntecedente()   │
│ + getUsuarioActual()│   │ + agregarMotivo()        │
│ + registrar()       │   │ + obtenerTodas()         │
└─────────────────────┘   └──────────────────────────┘

┌──────────────────────┐   ┌─────────────────────────┐
│ SeguimientoService   │   │     DataStorage         │
├──────────────────────┤   ├─────────────────────────┤
│ - instance           │   │ - instance              │
│ - storage            │   │ - usuarios: List        │
├──────────────────────┤   │ - historias: List       │
│ + crearSeguimiento() │   │ - seguimientos: List    │
│ + actualizar()       │   ├─────────────────────────┤
│ + obtenerPorPaciente│   │ + guardarUsuario()      │
│ + obtenerPorMedico() │   │ + buscarPorEmail()      │
│ + obtenerActivos()   │   │ + guardarHistoria()     │
│ + obtenerCronicos()  │   │ + guardarSeguimiento()  │
│ + completar()        │   │ + cargarDatos()         │
└──────────────────────┘   │ + inicializarPrueba()   │
                           └─────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                      CONTROLADORES                                │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────┐   ┌──────────────────────────┐
│  LoginController     │   │ DashboardMedicoController│
├──────────────────────┤   ├──────────────────────────┤
│ - authService        │   │ - authService            │
│ - emailField         │   │ - historiaService        │
│ - passwordField      │   │ - seguimientoService     │
│ - errorLabel         │   │ - medicoActual           │
├──────────────────────┤   │ - tablaPacientes         │
│ + loginMedico()      │   ├──────────────────────────┤
│ + loginPaciente()    │   │ + initialize()           │
│ + irARegistro()      │   │ + cargarEstadisticas()   │
│ + cambiarEscena()    │   │ + verPacientes()         │
└──────────────────────┘   │ + verHistorias()         │
                           │ + verSeguimientos()      │
                           │ + cerrarSesion()         │
                           └──────────────────────────┘

┌────────────────────────────┐   ┌─────────────────────────────┐
│ DashboardPacienteController│   │ HistoriaClinicaController   │
├────────────────────────────┤   ├─────────────────────────────┤
│ - authService              │   │ - authService               │
│ - historiaService          │   │ - historiaService           │
│ - pacienteActual           │   │ - paciente                  │
│ - historiaClinica          │   │ - historiaClinica           │
│ - progressBar              │   │ - campos formulario         │
├────────────────────────────┤   ├─────────────────────────────┤
│ + initialize()             │   │ + initialize()              │
│ + cargarInformacion()      │   │ + cargarPaciente()          │
│ + actualizarAlertas()      │   │ + cargarDatos()             │
│ + completarDatos()         │   │ + guardarHistoria()         │
│ + registrarAntecedentes()  │   │ + validarCampos()           │
│ + registrarSignos()        │   │ + calcularIMC()             │
│ + cerrarSesion()           │   │ + volver()                  │
└────────────────────────────┘   └─────────────────────────────┘
```

### 3. Diagrama de Secuencia - Login y Completar Historia

```
Paciente    LoginCtrl    AuthService    DataStorage    DashboardCtrl    HistoriaCtrl
    │            │            │              │                │               │
    ├──login────►│            │              │                │               │
    │            ├─validate──►│              │                │               │
    │            │            ├─buscarEmail─►│                │               │
    │            │            │◄─Usuario─────┤                │               │
    │            │            │              │                │               │
    │            │◄─success───┤              │                │               │
    │            ├─loadView──────────────────►│                │               │
    │◄─Dashboard─┴────────────────────────────┤                │               │
    │                                         │                │               │
    ├──completarHistoria────────────────────►│                │               │
    │                                         ├─loadView──────►│               │
    │◄─FormularioHistoria─────────────────────┴────────────────┤               │
    │                                                           │               │
    ├──llenarDatos─────────────────────────────────────────────►│               │
    ├──guardar─────────────────────────────────────────────────►│               │
    │                                                           ├─actualizar───►│
    │                                                           │               ├─save─►DataStorage
    │                                                           │               │◄─ok───┤
    │◄─confirmación─────────────────────────────────────────────┴───────────────┤
```

### 4. Diagrama de Actividad - Proceso Completo

```
                    ┌─────────┐
                    │ Inicio  │
                    └────┬────┘
                         │
                         ▼
                  ┌─────────────┐
                  │   Login     │
                  └──────┬──────┘
                         │
            ┌────────────┴────────────┐
            │                         │
            ▼                         ▼
    ┌──────────────┐        ┌──────────────┐
    │   Médico     │        │  Paciente    │
    └──────┬───────┘        └──────┬───────┘
           │                       │
           ▼                       ▼
    ┌──────────────┐        ┌──────────────┐
    │  Dashboard   │        │  Dashboard   │
    │   Médico     │        │  Paciente    │
    └──────┬───────┘        └──────┬───────┘
           │                       │
           ▼                       ▼
    ┌──────────────┐        ┌──────────────┐
    │Ver Pacientes │        │  Completar   │
    │   y HC       │        │  Información │
    └──────┬───────┘        └──────┬───────┘
           │                       │
           ▼                       ├──► Datos Personales
    ┌──────────────┐               │
    │  Consultar   │               ├──► Antecedentes
    │  Historia    │               │
    └──────┬───────┘               ├──► Motivo Consulta
           │                       │
           ▼                       ├──► Signos Vitales
    ┌──────────────┐               │
    │  Gestionar   │               ▼
    │ Seguimiento  │        ┌──────────────┐
    └──────┬───────┘        │   Guardar    │
           │                └──────┬───────┘
           │                       │
           └───────────┬───────────┘
                       │
                       ▼
                ┌─────────────┐
                │  Cerrar     │
                │  Sesión     │
                └──────┬──────┘
                       │
                       ▼
                   ┌───────┐
                   │  Fin  │
                   └───────┘
```

### 5. Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────┐
│                     SMART HC SYSTEM                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              Presentation Layer (JavaFX)                 │   │
│  ├─────────────────────────────────────────────────────────┤   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐              │   │
│  │  │  Login   │  │Dashboard │  │Historia  │              │   │
│  │  │  View    │  │  Views   │  │ Clínica  │              │   │
│  │  └────┬─────┘  └────┬─────┘  └────┬─────┘              │   │
│  │       │             │             │                     │   │
│  │       └─────────────┴─────────────┘                     │   │
│  │                     │                                   │   │
│  │                     ▼                                   │   │
│  │  ┌─────────────────────────────────────────┐           │   │
│  │  │          Controllers                     │           │   │
│  │  │  - LoginController                       │           │   │
│  │  │  - DashboardMedicoController             │           │   │
│  │  │  - DashboardPacienteController           │           │   │
│  │  │  - HistoriaClinicaController             │           │   │
│  │  └─────────────────┬────────────────────────┘           │   │
│  └────────────────────┼─────────────────────────────────────┘   │
│                       │                                         │
│  ┌────────────────────┼─────────────────────────────────────┐   │
│  │              Business Layer                              │   │
│  ├────────────────────┼─────────────────────────────────────┤   │
│  │                    ▼                                     │   │
│  │  ┌─────────────────────────────────────────┐            │   │
│  │  │            Services                      │            │   │
│  │  │  - AuthService                           │            │   │
│  │  │  - HistoriaClinicaService                │            │   │
│  │  │  - SeguimientoService                    │            │   │
│  │  └─────────────────┬────────────────────────┘            │   │
│  │                    │                                     │   │
│  │                    ▼                                     │   │
│  │  ┌─────────────────────────────────────────┐            │   │
│  │  │           Domain Models                  │            │   │
│  │  │  - Usuario, Paciente, Médico             │            │   │
│  │  │  - HistoriaClinica                       │            │   │
│  │  │  - Antecedentes, SignosVitales           │            │   │
│  │  │  - Seguimiento                           │            │   │
│  │  └─────────────────┬────────────────────────┘            │   │
│  └────────────────────┼─────────────────────────────────────┘   │
│                       │                                         │
│  ┌────────────────────┼─────────────────────────────────────┐   │
│  │              Data Layer                                  │   │
│  ├────────────────────┼─────────────────────────────────────┤   │
│  │                    ▼                                     │   │
│  │  ┌─────────────────────────────────────────┐            │   │
│  │  │         DataStorage (Singleton)          │            │   │
│  │  │  - usuarios.json                         │            │   │
│  │  │  - historias_clinicas.json               │            │   │
│  │  │  - seguimientos.json                     │            │   │
│  │  └──────────────────────────────────────────┘            │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              Utilities & Config                          │   │
│  ├──────────────────────────────────────────────────────────┤   │
│  │  - AlertUtils                                            │   │
│  │  - DateUtils                                             │   │
│  │  - ValidationUtils                                       │   │
│  │  - LocalDateTimeAdapter (Gson)                           │   │
│  │  - AppConfig                                             │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

## Patrones de Diseño Implementados

### 1. Singleton
- **AuthService**: Instancia única para gestionar autenticación
- **HistoriaClinicaService**: Servicio único para historias clínicas
- **SeguimientoService**: Servicio único para seguimientos
- **DataStorage**: Almacenamiento único de datos

### 2. MVC (Model-View-Controller)
- **Models**: Clases en `domain/`
- **Views**: Archivos FXML en `resources/fxml/`
- **Controllers**: Clases en `controllers/`

### 3. Service Layer
- Separación de lógica de negocio en servicios
- Abstracción del acceso a datos

### 4. Data Access Object (DAO)
- DataStorage actúa como DAO para persistencia

## Principios SOLID Aplicados

### S - Single Responsibility
Cada clase tiene una responsabilidad única:
- AuthService: solo autenticación
- HistoriaClinicaService: solo gestión de historias
- Controladores: solo gestión de UI

### O - Open/Closed
- Usuario es una clase abstracta extensible
- Paciente y Médico extienden Usuario sin modificarlo

### L - Liskov Substitution
- Paciente y Médico pueden sustituir a Usuario

### I - Interface Segregation
- Servicios específicos en lugar de uno monolítico

### D - Dependency Inversion
- Controladores dependen de abstracciones (servicios)
- No dependen de implementaciones concretas

## Relaciones entre Clases

### Herencia
- `Paciente extends Usuario`
- `Medico extends Usuario`

### Composición
- `HistoriaClinica` compone `Antecedentes`
- `HistoriaClinica` compone `List<SignosVitales>`
- `HistoriaClinica` compone `List<Seguimiento>`

### Agregación
- `Medico` agrega `List<Paciente>` (conceptual)
- `Paciente` agrega `HistoriaClinica`

### Dependencia
- Controladores dependen de Servicios
- Servicios dependen de DataStorage
- Todos dependen de modelos del dominio

---

**Nota**: Esta documentación describe la arquitectura UML completa del sistema SMART HC. Para más detalles sobre la implementación, consultar el código fuente en los respectivos paquetes.
