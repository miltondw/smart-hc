#!/bin/bash

# Script para descargar e instalar JavaFX SDK 21.0.1

echo "📦 Instalando JavaFX SDK 21.0.1..."

JAVAFX_VERSION="21.0.1"
JAVAFX_DIR="javafx"
INSTALL_DIR="$JAVAFX_DIR/javafx-sdk-$JAVAFX_VERSION"

# Detectar sistema operativo
OS=$(uname -s)
ARCH=$(uname -m)

case "$OS" in
    Linux*)
        if [ "$ARCH" = "x86_64" ]; then
            JAVAFX_URL="https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip"
            JAVAFX_FILE="openjfx-21.0.1_linux-x64_bin-sdk.zip"
        else
            echo "❌ Arquitectura no soportada: $ARCH"
            exit 1
        fi
        ;;
    Darwin*)
        if [ "$ARCH" = "arm64" ]; then
            JAVAFX_URL="https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_osx-aarch64_bin-sdk.zip"
            JAVAFX_FILE="openjfx-21.0.1_osx-aarch64_bin-sdk.zip"
        else
            JAVAFX_URL="https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_osx-x64_bin-sdk.zip"
            JAVAFX_FILE="openjfx-21.0.1_osx-x64_bin-sdk.zip"
        fi
        ;;
    MINGW*|MSYS*|CYGWIN*)
        JAVAFX_URL="https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_windows-x64_bin-sdk.zip"
        JAVAFX_FILE="openjfx-21.0.1_windows-x64_bin-sdk.zip"
        ;;
    *)
        echo "❌ Sistema operativo no soportado: $OS"
        exit 1
        ;;
esac

# Verificar si ya existe
if [ -d "$INSTALL_DIR" ]; then
    echo "✅ JavaFX SDK ya está instalado en $INSTALL_DIR"
    exit 0
fi

# Crear directorio
mkdir -p "$JAVAFX_DIR"

# Descargar JavaFX
echo "📥 Descargando JavaFX desde $JAVAFX_URL..."
cd "$JAVAFX_DIR"

if command -v wget > /dev/null; then
    wget -O "$JAVAFX_FILE" "$JAVAFX_URL"
elif command -v curl > /dev/null; then
    curl -L -o "$JAVAFX_FILE" "$JAVAFX_URL"
else
    echo "❌ Error: wget o curl no están instalados"
    exit 1
fi

# Extraer
echo "📦 Extrayendo JavaFX..."
if command -v unzip > /dev/null; then
    unzip -q "$JAVAFX_FILE"
else
    echo "❌ Error: unzip no está instalado"
    exit 1
fi

# Limpiar archivo descargado
rm "$JAVAFX_FILE"

cd ..

# Verificar instalación
if [ -d "$INSTALL_DIR/lib" ]; then
    echo "✅ JavaFX SDK instalado correctamente en $INSTALL_DIR"
    echo ""
    echo "🎯 Ahora puedes ejecutar:"
    echo "   ./start.sh"
else
    echo "❌ Error en la instalación de JavaFX"
    exit 1
fi
