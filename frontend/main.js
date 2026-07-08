// ===============================
// CONFIG
// ===============================

const API_URL = "http://localhost:8080";

// ===============================
// HELPERS
// ===============================

async function manejarRespuesta(res) {

    if (!res.ok) {

        const errorData = await res.json();

        throw new Error(errorData.message);
    }

    return res.json();
}

// ===============================
// VISTAS
// ===============================

function mostrarVista(vista) {

    document.getElementById("loginview").style.display = "none";
    document.getElementById("registerView").style.display = "none";
    document.getElementById("userView").style.display = "none";
    document.getElementById("profesionalView").style.display = "none";

    document.getElementById(vista).style.display = "block";
}


// ===============================
// USUARIO LOGUEADO
// ===============================

function obtenerUsuarioLogueado() {
    return JSON.parse(sessionStorage.getItem("usuario"));
}

function logout() {

    if (!confirm("¿Estás seguro que deseas cerrar sesión?")) {
        return;
    }

    sessionStorage.removeItem("usuario");

    document.getElementById("logoutBtn").style.display = "none";

    mostrarVista("loginview");
}


// ===============================
// REGISTRO
// ===============================

function registrarse() {

    const nombre = document.getElementById("regNombre").value;
    const email = document.getElementById("regEmail").value;
    const rol = document.getElementById("regRol").value;
    const password = document.getElementById("regPassword").value;

    if (!nombre.trim()) {
        alert("Debe ingresar un nombre");
        return;
    }

    if (!email.trim()) {
        alert("Debe ingresar un email");
        return;
    }

    if (!email.includes("@")) {
    alert("Debe ingresar un email válido");
    return;
    }

    if (!password.trim()) {
        alert("Debe ingresar una contraseña");
        return;
    }

    let url = "";

    if (rol === "usuario") {
        url = `${API_URL}/usuarios`;
    } else {
        url = `${API_URL}/usuarios/profesional`;
    }

    fetch(url, {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            nombre,
            email,
            password
        })
    })

    .then(manejarRespuesta)

    .then(() => {

        alert("Registro exitoso");

        mostrarVista("loginview");
    })

    .catch(err => {

        console.error(err);

        alert(err.message);
    });
}


// ===============================
// LOGIN
// ===============================

function login() {

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    if (!email.trim()) {
        alert("Debe ingresar un email");
        return;
    }

    if (!email.includes("@")) {
    alert("Debe ingresar un email válido");
    return;
    }

    if (!password.trim()) {
        alert("Debe ingresar una contraseña");
        return;
    }

    fetch(`${API_URL}/usuarios/login`, {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            email,
            password
        })
    })

    .then(manejarRespuesta)

    .then(usuario => {

        sessionStorage.setItem("usuario", JSON.stringify(usuario));

        document.getElementById("logoutBtn").style.display = "block";

        alert(`Bienvenido ${usuario.nombre}`);

        if (usuario.rol === "Profesional") {
            mostrarVista("profesionalView");
        } else {
            mostrarVista("userView");
        }
    })

    .catch(err => {

        console.error(err);

        alert("Error al iniciar sesión");
    });
}


// ===============================
// SOLICITAR TURNO
// ===============================

function solicitarTurno() {

    const usuario = obtenerUsuarioLogueado();

    const emailProfesional =
        document.getElementById("emailProfesional").value;

    const fecha = document.getElementById("fechaTurno").value;
    const hora = document.getElementById("horaTurno").value;

    if (!emailProfesional.trim()) {
        alert("Debe ingresar el mail del profesional");
        return;
    }
    if (!emailProfesional.includes("@")) {
    alert("Debe ingresar un email válido");
    return;
    }

    if (!fecha) {
        alert("Debe seleccionar una fecha");
        return;
    }

    if (!hora) {
        alert("Debe seleccionar un horario");
        return;
    }


    const fechaCompleta = fecha + "T" + hora;

    fetch(`${API_URL}/turnos`, {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            usuarioId: usuario.id,
            profesionalEmail: emailProfesional,
            fecha: fechaCompleta
        })
    })

    .then(manejarRespuesta)

    .then(() => {

        alert("Turno solicitado con éxito");

        document.getElementById("emailProfesional").value = "";
        document.getElementById("fechaTurno").value = "";
        document.getElementById("horaTurno").value = "";
    })

    .catch(err => {

        console.error(err);

        // 🔥 mostrar mensaje real
        alert(err.message);
    });
}


// ===============================
// VER MIS TURNOS
// ===============================

function verMisTurnos() {

    const usuario = obtenerUsuarioLogueado();

    fetch(`${API_URL}/turnos/usuario/${usuario.id}`)

    .then(res => res.json())

    .then(turnos => {

        const contenedor =
            document.getElementById("misTurnosList");

        renderizarTurnos(
            turnos,
            contenedor,
            "usuario"
        );
    })

    .catch(err => console.error(err));
}


// ===============================
// VER TURNOS PROFESIONAL
// ===============================

function verTurnosSolicitados() {

    const profesional = obtenerUsuarioLogueado();

    fetch(`${API_URL}/turnos/profesional/${profesional.id}`)

    .then(res => res.json())

    .then(turnos => {

        const contenedor =
            document.getElementById("turnosSolicitadosList");

        renderizarTurnos(
            turnos,
            contenedor,
            "profesional"
        );
    })

    .catch(err => console.error(err));
}


// ===============================
// RENDERIZAR TURNOS
// ===============================

function renderizarTurnos(turnos, contenedor, tipo) {

    contenedor.innerHTML = "";

    turnos.forEach(turno => {

        const card = document.createElement("div");

        card.classList.add("turno-card");

        const fechaFormateada =
            new Date(turno.fecha).toLocaleString();

        let nombrePersona = "";

        if (tipo === "usuario") {
            nombrePersona = turno.profesional.nombre;
        } else {
            nombrePersona = turno.usuario.nombre;
        }

        let botonAccion = "";

        // usuario puede cancelar
        if (
            tipo === "usuario" &&
            turno.estado === "PENDIENTE"
        ) {

            botonAccion = `
                <button
                    class="btn-cancelar"
                    onclick="cancelarTurno(${turno.nroturno})"
                >
                    Cancelar
                </button>
            `;
        }

        // profesional puede aceptar o rechazar
        if (
            tipo === "profesional" &&
            turno.estado === "PENDIENTE"
        ) {

            botonAccion = `
                <button
                    class="btn-aceptar"
                    onclick="aceptarTurno(${turno.nroturno})"
                >
                    Aceptar
                </button>

                <button
                    class="btn-rechazar"
                    onclick="rechazarTurno(${turno.nroturno})"
                >
                    Rechazar
                </button>
            `;
        }

        // profesional puede atender
        if (
            tipo === "profesional" &&
            turno.estado === "ACEPTADO"
        ) {

            botonAccion = `
                <button
                    class="btn-atender"
                    onclick="atenderTurno(${turno.nroturno})"
                >
                    Atender
                </button>
            `;
        }

        card.innerHTML = `

            <p>
                <strong>
                    👤 ${tipo === "usuario" ? "Profesional" : "Usuario"}:
                </strong>

                ${nombrePersona}
            </p>

            <p>
                <strong>📅 Fecha:</strong>
                ${fechaFormateada}
            </p>

            <p class="estado ${turno.estado.toLowerCase()}">
                ${turno.estado}
            </p>

            ${botonAccion}
        `;

        contenedor.appendChild(card);
    });
}


// ===============================
// CANCELAR TURNO
// ===============================

function cancelarTurno(turnoId) {

    const confirmar =
        confirm("¿Deseas cancelar este turno?");

    if (!confirmar) {
        return;
    }

    fetch(`${API_URL}/turnos/${turnoId}/cancelado`, {

        method: "PUT"
    })

    .then(() => {

        alert("Turno cancelado");

        verMisTurnos();
    })

    .catch(err => console.error(err));
}

// ===============================
// ACEPTAR TURNO
// ===============================

function aceptarTurno(turnoId) {

    const confirmar =
        confirm("¿Deseas aceptar este turno?");

    if (!confirmar) {
        return;
    }

    fetch(`${API_URL}/turnos/${turnoId}/aceptado`, {

        method: "PUT"
    })

    .then(() => {

        alert("Turno aceptado");

        verTurnosSolicitados();
    })

    .catch(err => console.error(err));
}

// ===============================
// RECHAZAR TURNO
// ===============================

function rechazarTurno(turnoId) {

    const confirmar =
        confirm("¿Deseas rechazar este turno?");

    if (!confirmar) {
        return;
    }

    fetch(`${API_URL}/turnos/${turnoId}/rechazado`, {

        method: "PUT"
    })

    .then(() => {

        alert("Turno rechazado");

        verTurnosSolicitados();
    })

    .catch(err => console.error(err));
}


// ===============================
// ATENDER TURNO
// ===============================

function atenderTurno(turnoId) {

    const confirmar =
        confirm("¿Deseas marcar este turno como atendido?");

    if (!confirmar) {
        return;
    }

    fetch(`${API_URL}/turnos/${turnoId}/atendido`, {

        method: "PUT"
    })

    .then(() => {

        alert("Turno atendido");

        verTurnosSolicitados();
    })

    .catch(err => console.error(err));
}

function cargarHorarios() {

    const select = document.getElementById("horaTurno");

    for (let hora = 0; hora < 24; hora++) {

        for (let minuto = 0; minuto < 60; minuto += 15) {

            const horaFormateada = String(hora).padStart(2, "0");
            const minutoFormateado = String(minuto).padStart(2, "0");

            const horario = `${horaFormateada}:${minutoFormateado}`;

            const option = document.createElement("option");

            option.value = horario;
            option.textContent = horario;

            select.appendChild(option);
        }
    }
}

function configurarFechaMinima() {

    const inputFecha = document.getElementById("fechaTurno");

    const hoy = new Date();

    const año = hoy.getFullYear();
    const mes = String(hoy.getMonth() + 1).padStart(2, "0");
    const dia = String(hoy.getDate()).padStart(2, "0");

    const fechaMinima = `${año}-${mes}-${dia}`;

    inputFecha.min = fechaMinima;
}

function inicializarAplicacion() {

    const usuario = obtenerUsuarioLogueado();

    if (!usuario) {
        mostrarVista("loginview");
        return;
    }

    document.getElementById("logoutBtn").style.display = "block";

    if (usuario.rol === "Profesional") {
        mostrarVista("profesionalView");
    } else {
        mostrarVista("userView");
    }
}

cargarHorarios();
configurarFechaMinima();
inicializarAplicacion();