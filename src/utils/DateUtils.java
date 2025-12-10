package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null)
            return "";
        return fecha.format(FORMATO_FECHA);
    }

    public static String formatearFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null)
            return "";
        return fechaHora.format(FORMATO_FECHA_HORA);
    }

    public static String formatearHora(LocalDateTime fechaHora) {
        if (fechaHora == null)
            return "";
        return fechaHora.format(FORMATO_HORA);
    }

    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null)
            return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
}
