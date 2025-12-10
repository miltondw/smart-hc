#!/bin/bash

# Script para compilar SMART HC

echo "🔨 Compilando SMART HC..."

# Variables
JAVAFX_PATH="/home/miltondw/projects/java/smart-hc/javafx/javafx-sdk-21.0.1/lib"
SRC_PATH="src"
BIN_PATH="bin"
LIB_PATH="lib"

# Crear directorio bin si no existe
mkdir -p $BIN_PATH

# Limpiar compilación anterior
rm -rf $BIN_PATH/*

# Compilar
javac --module-path $JAVAFX_PATH \
      --add-modules javafx.controls,javafx.fxml \
      -cp "$LIB_PATH/*:$SRC_PATH" \
      -d $BIN_PATH \
      $(find $SRC_PATH -name "*.java")

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    
    # Copiar recursos (FXML y CSS)
    echo "📦 Copiando recursos..."
    cp -r resources $BIN_PATH/
    echo "✅ Recursos copiados!"
else
    echo "❌ Error en la compilación"
    exit 1
fi
