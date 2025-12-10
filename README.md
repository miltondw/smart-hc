# SMART HC - Historias Clínicas Inteligentes

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-21-green.svg)

## 📋 Descripción

**SMART HC** es un sistema de gestión de historias clínicas inteligentes que permite a los pacientes completar el 80% de su historia clínica desde cualquier dispositivo, facilitando a los médicos el acceso a información clara, organizada y actualizada.

## 🎯 Características Principales

- ✅ **Doble rol de usuarios**: Médicos y Pacientes
- 📝 **Historia clínica completa**: Datos personales, antecedentes, motivo de consulta, signos vitales
- 📊 **Dashboard intuitivo**: Información resumida y accesible
- 💾 **Persistencia local**: Almacenamiento en JSON
- 🎨 **Interfaz moderna**: Diseño minimalista con JavaFX
- 📈 **Seguimiento automatizado**: Para pacientes crónicos
- 🔒 **Autenticación segura**: Sistema de login por roles

## 🏗️ Arquitectura del Proyecto

```
smart-hc/
├── src/
│   ├── domain/                 # Modelos de dominio
│   ├── services/               # Servicios de negocio
│   ├── controllers/            # Controladores JavaFX
│   ├── storage/                # Persistencia de datos
│   ├── utils/                  # Utilidades
│   ├── config/                 # Configuración
│   └── App.java                # Punto de entrada
├── resources/
│   ├── fxml/                   # Vistas FXML
│   └── css/                    # Estilos
├── data/                       # Datos JSON
└── lib/                        # Librerías (Gson)
```

## 📦 Requisitos

- Java JDK 21+
- JavaFX SDK 21
- Gson 2.10.1

## 🚀 Ejecutar la Aplicación

```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp "bin:lib/*" App
```

## 👥 Usuarios de Prueba

**Médico**: medico@smarthc.com / medico123  
**Paciente**: paciente@smarthc.com / paciente123

## 📊 Módulos

### Médico
- Dashboard con estadísticas
- Gestión de pacientes
- Historias clínicas
- Seguimientos crónicos

### Paciente
- Completar historia clínica
- Registrar datos personales
- Antecedentes médicos
- Signos vitales

## 🔄 Flujo de Trabajo

1. **Login** → Seleccionar rol (Médico/Paciente)
2. **Dashboard** → Ver información resumida
3. **Historia Clínica** → Completar/Consultar información
4. **Guardar** → Persistir en JSON

## 🎨 Diseño

- Paleta: Azul profesional (#2196F3)
- Tipografía: System Font
- Efectos: Sombras y transiciones suaves

## 📈 Futuras Mejoras

- Encriptación de contraseñas
- Base de datos real
- API REST
- Reportes PDF
- Telemedicina

---

**SMART HC** - Transformando la gestión de historias clínicas 🏥💙
