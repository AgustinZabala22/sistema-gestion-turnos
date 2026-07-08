package com.agustin.turnos.repositories;

import com.agustin.turnos.model.EstadoTurno;
import com.agustin.turnos.model.Turno;
import com.agustin.turnos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByUsuarioId(Long idUsuario);
    List<Turno> findByProfesionalId(Long idProfesional);
    List<Turno> findByEstado(EstadoTurno estado);
    List<Turno> findAllByOrderByFechaAsc();
    List<Turno> findByUsuarioIdOrderByFechaAsc(Long idUsuario);
    List<Turno> findByFechaBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);
    List<Turno> findByProfesionalIdAndFechaBetween(Long profesionalId, LocalDateTime fechaAfter, LocalDateTime fechaBefore);
    List<Turno> findByProfesionalIdOrderByFechaAsc(Long profesionalId);
    boolean existsByProfesionalAndFecha(Usuario profesional, LocalDateTime fecha);
}
