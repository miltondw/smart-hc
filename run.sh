#!/bin/bash

# Script para ejecutar SMART HC

echo "🚀 Ejecutando SMART HC..."

# Variables
JAVAFX_PATH="/home/miltondw/projects/java/smart-hc/javafx/javafx-sdk-21.0.1/lib"
BIN_PATH="bin"
LIB_PATH="lib"

# Verificar que exista la compilación
if [ ! -d "$BIN_PATH" ] || [ -z "$(ls -A $BIN_PATH)" ]; then
    echo "❌ No se encontró la compilación. Ejecuta ./compile.sh primero"
    exit 1
fi

# Ejecutar
java --module-path $JAVAFX_PATH \
     --add-modules javafx.controls,javafx.fxml \
     --add-opens java.base/java.time=ALL-UNNAMED \
     -cp "$BIN_PATH:$LIB_PATH/*" \
     App

echo ""
echo "👋 Aplicación cerrada"
