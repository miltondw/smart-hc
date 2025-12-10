# 🚀 Guía de Instalación y Ejecución - SMART HC

## ✅ Instalación Completada

JavaFX 21 y Gson 2.10.1 ya están instalados en el proyecto.

## 📦 Estructura de Dependencias

```
smart-hc/
├── javafx/
│   └── javafx-sdk-21.0.1/
│       └── lib/              ← JavaFX SDK
├── lib/
│   └── gson-2.10.1.jar       ← Gson para JSON
├── compile.sh                ← Script de compilación
├── run.sh                    ← Script de ejecución
└── start.sh                  ← Compilar + Ejecutar (TODO EN UNO)
```

## 🔨 Compilar el Proyecto

```bash
./compile.sh
```

## 🚀 Ejecutar la Aplicación

**Opción 1: Todo en un comando (RECOMENDADO)**
```bash
./start.sh
```

**Opción 2: Paso a paso**
```bash
./compile.sh
./run.sh
```

## 👥 Usuarios de Prueba

### Médico
- **Email**: `medico@smarthc.com`
- **Contraseña**: `medico123`

### Paciente
- **Email**: `paciente@smarthc.com`
- **Contraseña**: `paciente123`

## 📋 Requisitos del Sistema

- ✅ Java 21 (OpenJDK 21.0.9 instalado)
- ✅ JavaFX 21.0.1 (instalado en `javafx/`)
- ✅ Gson 2.10.1 (instalado en `lib/`)
- ✅ Linux Ubuntu 24.04
- ✅ Entorno gráfico (X11)

## ⚡ Inicio Rápido

```bash
# Ejecutar aplicación
./start.sh

# Ingresar con usuario de prueba
# Médico: medico@smarthc.com / medico123
# Paciente: paciente@smarthc.com / paciente123
```

## 🎯 Funcionalidades Disponibles

### Para Médicos
- ✅ Ver dashboard con estadísticas
- ✅ Consultar lista de pacientes
- ✅ Ver historias clínicas completas
- ✅ Gestionar seguimientos

### Para Pacientes
- ✅ Ver progreso de historia clínica
- ✅ Completar datos personales
- ✅ Registrar antecedentes médicos
- ✅ Ingresar motivo de consulta
- ✅ Registrar signos vitales

## 🐛 Solución de Problemas

### Limpiar y recompilar
```bash
rm -rf bin/*
rm -f data/*.json
./start.sh
```

### Error de permisos en scripts
```bash
chmod +x compile.sh run.sh start.sh
```

### Ver archivos JSON generados
```bash
ls -lh data/
cat data/usuarios.json
```

## 🔧 Configuraciones Técnicas

### Flags de Java utilizados:
- `--module-path`: Ruta a JavaFX SDK
- `--add-modules`: Módulos JavaFX necesarios (controls, fxml)
- `--add-opens java.base/java.time=ALL-UNNAMED`: Permite a Gson serializar LocalDate/LocalDateTime

### Adaptadores Gson personalizados:
- `LocalDateTimeAdapter`: Serialización de LocalDateTime
- `LocalDateAdapter`: Serialización de LocalDate  
- `UsuarioAdapter`: Manejo de clase abstracta Usuario

## 📊 Estado del Proyecto

- ✅ Arquitectura MVC implementada
- ✅ Modelos del dominio completos
- ✅ Servicios de negocio funcionales
- ✅ Controladores JavaFX operativos
- ✅ Vistas FXML diseñadas
- ✅ Persistencia JSON configurada
- ✅ Estilos CSS aplicados
- ✅ Validaciones implementadas
- ✅ Compilación exitosa
- ✅ Aplicación ejecutable

---

**SMART HC** - Sistema de Historias Clínicas Inteligentes 🏥💙
