package com.intellij.listadetareas.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Date;

@WebServlet(value = "/registroServlet")
public class RegistroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        String fecha = req.getParameter("fecha");

        System.out.println(nombre + " " + descripcion + " " + fecha);
    }
}