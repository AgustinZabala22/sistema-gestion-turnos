package com.agustin.turnos.controller;

import com.agustin.turnos.dto.CrearTurnoRequest;
import com.agustin.turnos.dto.ModificarTurnoRequest;
import com.agustin.turnos.model.EstadoTurno;
import com.agustin.turnos.model.Turno;
import com.agustin.turnos.service.TurnoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // ====================================================
    // CREACION
    // ====================================================

    @PostMapping
    public Turno crearTurno(@RequestBody CrearTurnoRequest request) {
        return turnoService.crearTurno(request);
    }

    // ====================================================
    // CONSULTAS
    // ====================================================

    @GetMapping
    public List<Turno> obtenerTurnos() {
        return turnoService.obtenerTurnos();
    }

    @GetMapping("/ordenfecha")
    public List<Turno> obtenerTurnosOrdenxFecha() {
        return turnoService.obtenerTurnosOrdenadosxFecha();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Turno> obtenerTurnoPorUsuario(@PathVariable Long idUsuario) {
        List<Turno> turnos = turnoService.obtenerTurnosPorUsuario(idUsuario);
        if (turnos.isEmpty()){
            throw new RuntimeException("Turno con id" + idUsuario + " no existe");
        }
        return turnos;
    }

    @GetMapping("/profesional/{idProfesional}")
    public List<Turno> obtenerTurnoPorProfesional(@PathVariable Long idProfesional) {
        List<Turno> turnos = turnoService.obtenerTurnosPorProfesional(idProfesional);
        if (turnos.isEmpty()){
            throw new RuntimeException("Turno con id profesional " + idProfesional + " no existe");
        }
        return turnos;
    }

    @GetMapping("/usuario/ordenfecha/{idUsuario}")
    public List<Turno> obtenerTurnosUsuarioOrdenFecha(@PathVariable Long idUsuario) {
        List<Turno> turnos = turnoService.obtenerTurnosPorUsuarioOrdenado(idUsuario);
        if(turnos.isEmpty()){
            throw new RuntimeException("Turno con id" + idUsuario + " no existe");
        }
        return turnos;
    }

    @GetMapping("/profesional/ordenado/{idProfesional}")
    public List<Turno> obtenerTurnosPorProfesionalOrdenFecha(@PathVariable Long idProfesional) {
        List<Turno> turnos = turnoService.obtenerTurnosPorProfesionalOrdenado(idProfesional);
        if(turnos.isEmpty()){
            throw new RuntimeException("Turno con id" + idProfesional + " no existe");
        }
        return turnos;
    }

    @GetMapping("/estado/{estado}")
    public List<Turno> obtenerTurnosPorEstado(@PathVariable EstadoTurno estado) {
        List<Turno> turnos = turnoService.obtenerTurnosPorEstado(estado);
        if (turnos.isEmpty()){
            throw new RuntimeException("Turnos en" + estado + " no existen");
        }
        return turnos;
    }

    @GetMapping("/hoy")
    public List<Turno> obtenerTurnosHoy() {
        List<Turno> turnos = turnoService.obtenerTurnosHoy();
        if (turnos.isEmpty()){
            throw new RuntimeException("Hoy no hay turnos");
        }
        return turnos;
    }

    @GetMapping("/profesional/hoy/{idProfesional}")
    public List<Turno> obtenerTurnosPorProfesionalHoy(@PathVariable Long idProfesional) {
        List<Turno> turnos = turnoService.obtenerTurnosHoyxProfesional(idProfesional);
        if (turnos.isEmpty()){
            throw new RuntimeException("Hoy no hay turnos");
        }
        return turnos;
    }

    // ====================================================
    // MODIFICACIONES
    // ====================================================

    @PutMapping("/{nroturno}")
    public Turno modificarTurno(@PathVariable Long nroturno, @RequestBody ModificarTurnoRequest request) {
        Turno t = turnoService.modificarTurno(nroturno, request);
        if (t == null) {
            throw new RuntimeException("Turno con id" + nroturno + " no existe");
        }
        return t;
    }

    @PutMapping("/{nroturno}/atendido")
    public Turno marcarAtendido(@PathVariable Long nroturno) {
        return turnoService.marcarAtendido(nroturno);
    }

    @PutMapping("/{nroturno}/cancelado")
    public Turno marcarCancelado(@PathVariable Long nroturno) {
        return turnoService.marcarCancelado(nroturno);
    }

    @PutMapping("/{nroturno}/aceptado")
    public Turno aceptarTurno(@PathVariable Long nroturno) {
        return turnoService.aceptarTurno(nroturno);
    }

    @PutMapping("/{nroturno}/rechazado")
    public Turno rechazarTurno(@PathVariable Long nroturno) {
        return turnoService.rechazarTurno(nroturno);
    }



    @DeleteMapping("/{nroturno}")
    public void eliminarTurno(@PathVariable Long nroturno) {
        turnoService.eliminarTurno(nroturno);
    }
}
