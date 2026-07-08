package com.agustin.turnos.service;

import com.agustin.turnos.dto.CrearUsuarioRequest;
import com.agustin.turnos.dto.LoginRequest;
import com.agustin.turnos.model.Usuario;
import com.agustin.turnos.model.Rol;
import com.agustin.turnos.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ====================================================
    // CREACIÓN DE USUARIOS
    // ====================================================

    private void validarUsuario(CrearUsuarioRequest request) {

        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El email ya está asignado a un usuario"
            );
        }
    }

    public Usuario crearUsuario(CrearUsuarioRequest request) {
        validarUsuario(request);

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setRol(Rol.Usuario);
        String password = passwordEncoder.encode(request.getPassword());
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario crearProfesional(CrearUsuarioRequest request) {
        validarUsuario(request);

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setRol(Rol.Profesional);
        String password = passwordEncoder.encode(request.getPassword());
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
        return usuario;
    }

    // ====================================================
    // AUTENTICACIÓN
    // ====================================================

    public Usuario login(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Debe ingresar un email");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Debe ingresar una contraseña");
        }
        Usuario usuario = usuarioRepository.findByEmail(request.email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new RuntimeException("Usuario no encontrado");
        }
        return usuario;
    }

    // ====================================================
    // CONSULTAS
    // ====================================================


    public List<Usuario> listarUsuariosPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> listarUsuariosTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuario(Long idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        return usuario.orElse(null);
    }

    public Usuario buscarUsuarioPorMail(String mail){
        Optional<Usuario> usuario = usuarioRepository.findByEmail(mail);
        return usuario.orElse(null);
    }

    public void eliminarUsuario(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
