package examen2;

public class Entry {

    String nom_usuario;
    long posicion;
    Entry siguiente;

    public Entry(String username, long posicion) {
        this.nom_usuario = username;
        this.posicion = posicion;
        this.siguiente = null;
    }

    public String getUsername() {
        return nom_usuario;
    }

    public long getPosicion() {
        return posicion;
    }

    public Entry getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Entry siguiente) {
        this.siguiente = siguiente;
    }
}
