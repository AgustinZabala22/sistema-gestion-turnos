package com.agustin.turnos.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

public class CrearTurnoRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private String profesionalEmail;

    @NotNull
    private LocalDateTime fecha;

    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioID) {
        this.usuarioId = usuarioID;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getProfesionalEmail() {
        return profesionalEmail;
    }
    public void setProfesionalEmail(String profesionalEmail) {
        this.profesionalEmail = profesionalEmail;
    }
}
