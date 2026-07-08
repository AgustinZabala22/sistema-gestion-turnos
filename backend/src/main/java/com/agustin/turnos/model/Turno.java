package com.agustin.turnos.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroturno;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Usuario profesional;

    public Turno() {}

    public Turno(Long nroturno, LocalDateTime fecha, EstadoTurno estado, Usuario usuario, Usuario profesional) {
        this.nroturno = nroturno;
        this.fecha = fecha;
        this.estado = estado;
        this.usuario = usuario;
        this.profesional = profesional;
    }

    public Long getNroturno() {
        return nroturno;
    }
    public void setNroturno(Long nroturno) {
        this.nroturno = nroturno;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public EstadoTurno getEstado() {
        return estado;
    }
    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Usuario getProfesional() {
        return profesional;
    }
    public void setProfesional(Usuario profesional) {
        this.profesional = profesional;
    }
}
