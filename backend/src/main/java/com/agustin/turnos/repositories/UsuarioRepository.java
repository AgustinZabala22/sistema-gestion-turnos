package com.agustin.turnos.repositories;

import com.agustin.turnos.model.Rol;
import com.agustin.turnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    List<Usuario> findByRol(Rol rol);
    Optional<Usuario> findByEmail(String mail);
}
