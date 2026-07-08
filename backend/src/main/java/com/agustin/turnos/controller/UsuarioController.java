package com.agustin.turnos.controller;

import com.agustin.turnos.dto.CrearUsuarioRequest;
import com.agustin.turnos.dto.LoginRequest;
import com.agustin.turnos.model.Rol;
import com.agustin.turnos.model.Usuario;
import com.agustin.turnos.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ====================================================
    // REGISTRO/LOGIN
    // ====================================================

    @PostMapping
    public Usuario crearUsuario(@RequestBody CrearUsuarioRequest request) {
        return usuarioService.crearUsuario(request);
    }

    @PostMapping("/profesional")
    public Usuario crearProfesional(@RequestBody CrearUsuarioRequest request) {
        return usuarioService.crearProfesional(request);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    // ====================================================
    // CONSULTAS
    // ====================================================

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarUsuariosTodos();
    }

    @GetMapping("/profesionales")
    public List<Usuario> listarProfesionales() {
        return usuarioService.listarUsuariosPorRol(Rol.Profesional);
    }

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() { return usuarioService.listarUsuariosPorRol(Rol.Usuario);}

    @GetMapping("/{idUsuario}")
    public Usuario buscarUsuarioPorId(@PathVariable Long idUsuario) {
        return usuarioService.buscarUsuario(idUsuario);
    }

    @GetMapping("/mail/{mail}")
    public Usuario buscarUsuarioPorEmail(@PathVariable String mail) { return usuarioService.buscarUsuarioPorMail(mail);}

    @DeleteMapping("/{idUsuario}")
    public void eliminarUsuario(@PathVariable Long idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
    }
}
