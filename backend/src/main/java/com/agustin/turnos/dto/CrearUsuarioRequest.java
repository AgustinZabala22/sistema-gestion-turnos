package com.agustin.turnos.dto;

import org.antlr.v4.runtime.misc.NotNull;

public class CrearUsuarioRequest {

    @NotNull
    private String nombre;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
