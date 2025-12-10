package services;

import domain.*;
import storage.DataStorage;
import java.util.Optional;

public class AuthService {
    private static AuthService instance;
    private Usuario usuarioActual;
    private DataStorage storage;

    private AuthService() {
        this.storage = DataStorage.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(String email, String password, Usuario.TipoUsuario tipo) {
        Optional<Usuario> usuario = storage.buscarUsuarioPorEmail(email);

        if (usuario.isPresent() &&
                usuario.get().getPassword().equals(password) &&
                usuario.get().getTipo() == tipo &&
                usuario.get().isActivo()) {

            this.usuarioActual = usuario.get();
            return true;
        }
        return false;
    }

    public void logout() {
        this.usuarioActual = null;
    }

    public boolean isLoggedIn() {
        return usuarioActual != null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Paciente getPacienteActual() {
        if (usuarioActual instanceof Paciente) {
            return (Paciente) usuarioActual;
        }
        return null;
    }

    public Medico getMedicoActual() {
        if (usuarioActual instanceof Medico) {
            return (Medico) usuarioActual;
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) {
        // Verificar que no exista el email
        if (storage.buscarUsuarioPorEmail(usuario.getEmail()).isPresent()) {
            return false;
        }

        return storage.guardarUsuario(usuario);
    }

    public boolean cambiarPassword(String passwordActual, String nuevaPassword) {
        if (usuarioActual != null && usuarioActual.getPassword().equals(passwordActual)) {
            usuarioActual.setPassword(nuevaPassword);
            return storage.actualizarUsuario(usuarioActual);
        }
        return false;
    }
}
