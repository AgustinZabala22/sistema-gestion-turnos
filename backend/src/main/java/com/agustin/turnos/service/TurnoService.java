package com.agustin.turnos.service;

import com.agustin.turnos.dto.CrearTurnoRequest;
import com.agustin.turnos.dto.ModificarTurnoRequest;
import com.agustin.turnos.model.EstadoTurno;
import com.agustin.turnos.model.Rol;
import com.agustin.turnos.model.Turno;
import com.agustin.turnos.model.Usuario;
import com.agustin.turnos.repositories.TurnoRepository;
import com.agustin.turnos.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {

    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    public TurnoService(UsuarioRepository usuarioRepository, TurnoRepository turnoRepository ) {
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
    }

    // ====================================================
    // CREACIÓN DE TURNOS
    // ====================================================

    public Turno crearTurno(CrearTurnoRequest request) {
        if (request.getProfesionalEmail() == null ||
                request.getProfesionalEmail().isBlank()) {

            throw new RuntimeException(
                    "Debe ingresar el mail del profesional"
            );
        }

        if (request.getFecha() == null) {

            throw new RuntimeException(
                    "Debe seleccionar una fecha"
            );
        }
        if (request.getFecha().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden crear turnos en fechas pasadas");
        }
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Usuario profesional = usuarioRepository.findByEmail(request.getProfesionalEmail()).orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
        if (profesional.getRol() != Rol.Profesional) {
            throw new RuntimeException("El mail ingresado no pertenece a un profesional");
        }
        boolean ocupado = turnoRepository.existsByProfesionalAndFecha(profesional, request.getFecha());
        if (ocupado) {
            throw new RuntimeException(
                    "El profesional ya tiene un turno en ese horario"
            );
        }
        Turno turno = new Turno();
        turno.setUsuario(usuario);
        turno.setProfesional(profesional);
        turno.setFecha(request.getFecha());
        turno.setEstado(EstadoTurno.PENDIENTE);
        turnoRepository.save(turno);
        return turno;
    }

    // ====================================================
    // CONSULTAS DE TURNOS
    // ====================================================

    public List<Turno> obtenerTurnos() {
         return turnoRepository.findAll();
    }

    public List<Turno> obtenerTurnosOrdenadosxFecha(){
        return turnoRepository.findAllByOrderByFechaAsc();
    }

    public List<Turno> obtenerTurnosPorUsuario(Long usuarioId){
        return turnoRepository.findByUsuarioId(usuarioId);
    }

    public List<Turno> obtenerTurnosPorProfesional(Long profesionalID){ return turnoRepository.findByProfesionalId(profesionalID); }

    public List<Turno> obtenerTurnosPorUsuarioOrdenado(Long usuarioId){
        return turnoRepository.findByUsuarioIdOrderByFechaAsc(usuarioId);
    }

    public List<Turno> obtenerTurnosPorProfesionalOrdenado(Long profesionalID){
        return turnoRepository.findByProfesionalIdOrderByFechaAsc(profesionalID);
    }

    public List<Turno> obtenerTurnosPorEstado(EstadoTurno estado){
        return turnoRepository.findByEstado(estado);
    }

    public List<Turno> obtenerTurnosHoy(){
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        return turnoRepository.findByFechaBetween(inicio, fin);
    }

    public List<Turno> obtenerTurnosHoyxProfesional(Long profesionalID){
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        return turnoRepository.findByProfesionalIdAndFechaBetween(profesionalID, inicio, fin);
    }

    // ====================================================
    // MODIFICACIONES
    // ====================================================

    public Turno modificarTurno(Long nroturno, ModificarTurnoRequest request){
        Turno turno = turnoRepository.findById(nroturno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setFecha(request.getFecha());

        return turnoRepository.save(turno);
    }

    public Turno marcarAtendido(Long nroturno) {
        Turno turno = turnoRepository.findById(nroturno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setEstado(EstadoTurno.ATENDIDO);

        return turnoRepository.save(turno);
    }

    public Turno marcarCancelado(Long nroturno) {
        Turno turno = turnoRepository.findById(nroturno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setEstado(EstadoTurno.CANCELADO);

        return turnoRepository.save(turno);
    }

    public Turno aceptarTurno(Long nroturno) {

        Turno turno = turnoRepository.findById(nroturno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setEstado(EstadoTurno.ACEPTADO);

        return turnoRepository.save(turno);
    }

    public Turno rechazarTurno(Long nroturno) {

        Turno turno = turnoRepository.findById(nroturno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setEstado(EstadoTurno.RECHAZADO);

        return turnoRepository.save(turno);
    }

    public void eliminarTurno(Long nroturno) {
        turnoRepository.deleteById(nroturno);
    }


}
