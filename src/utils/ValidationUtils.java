package utils;

public class ValidationUtils {

    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        return telefono.matches("\\d{10}");
    }

    public static boolean validarCampoNoVacio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }

    public static boolean validarRangoNumerico(double valor, double min, double max) {
        return valor >= min && valor <= max;
    }

    public static boolean validarTemperatura(double temperatura) {
        return validarRangoNumerico(temperatura, 30.0, 45.0);
    }

    public static boolean validarPresion(int presion) {
        return validarRangoNumerico(presion, 40, 250);
    }

    public static boolean validarFrecuenciaCardiaca(int frecuencia) {
        return validarRangoNumerico(frecuencia, 30, 220);
    }

    public static boolean validarSaturacionOxigeno(double saturacion) {
        return validarRangoNumerico(saturacion, 50, 100);
    }
}
