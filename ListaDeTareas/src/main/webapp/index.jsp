<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar tarea</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<form action="registroServlet" method="post">
    <label for="nombre">Introduce el nombre</label>
    <input id="nombre" type="text" maxlength="50" name="nombre" placeholder="Ingresa el nombre">
    <br>
    <br>
    <label for="descripcion">Introduce la descripción</label>
    <input id="descripcion" type="text" maxlength="120" name="descripcion" placeholder="Ingresa la descripción">
    <br>
    <br>
    <label for="fecha">Selecciona la fecha</label>
    <input id="fecha" type="date" name="fecha">
    <br>
    <br>
    <button type="submit">Registrar tarea</button>
</form>
<script src="js/bootstrap.js"></script>
</body>
</html>