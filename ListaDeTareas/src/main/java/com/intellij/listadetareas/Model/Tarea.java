public class Tarea {
    /* Tarea con nombre, descripción, fecha y pendiente (boolean) */

    private String nombre;
    private String descripcion;
    private String fecha;
    // true = pendiente, false = hecho
    private boolean pendiente;

    // Constructor completo con todos los atributos
    public Tarea(String nombre, String descripcion, String fecha, boolean pendiente) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.pendiente = pendiente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    // Vamos a sobreescribir el método String
    @Override
    public String toString() {
        String p = this.pendiente ? "Pendiente" : "Hecho";
        return "nombre: " + this.nombre + "\n" + " descripción: " + this.descripcion + "\n" + " fecha: " + this.fecha
                + "\n"
                + " pendiente: " + p + "\n";
    }
}