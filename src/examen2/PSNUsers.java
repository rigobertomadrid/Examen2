
package examen2;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PSNUsers {
    
    private RandomAccessFile raf;
    private HashTable usuario;
    
    public PSNUsers() throws IOException {
        raf = new RandomAccessFile("psn.psn", "rw");
        this.usuario = new HashTable();
        reloadHashTable();
    }

    private void reloadHashTable() throws IOException {
        usuario = new HashTable(); 
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            long posInicio = raf.getFilePointer(); 
            String nom_usuario = raf.readUTF();
            int puntos = raf.readInt();
            int trofeos = raf.readInt();
            boolean activo = raf.readBoolean();
            usuario.add(nom_usuario, posInicio);
        }
    }

    public boolean buscarUsername(String nom_usuario) {
        return usuario.search(nom_usuario) != -1;
    }

    public boolean isActive(String nom_usuario) throws IOException {
        long posicion = usuario.search(nom_usuario);
        if (posicion != -1) {
            raf.seek(posicion);
            raf.readUTF(); 
            raf.readInt(); 
            raf.readInt(); 
            return raf.readBoolean(); 
        }
        return false;
    }

    public boolean addUser(String nom_usuario) throws IOException {
        if (buscarUsername(nom_usuario)) {
            return false;
        }
        raf.seek(raf.length());
        long posInicio = raf.getFilePointer();
        raf.writeUTF(nom_usuario);
        raf.writeInt(0);
        raf.writeInt(0);
        raf.writeBoolean(true); 
        usuario.add(nom_usuario, posInicio);
        return true;
    }

    public void deactivateUser(String nom_usuario) throws IOException {
        long posicion = usuario.search(nom_usuario);
        if (posicion != -1) {
            raf.seek(posicion);
            raf.readUTF(); 
            raf.readInt(); 
            raf.readInt(); 
            raf.writeBoolean(false); 
        }
    }

    public void addTrophieTo(String nom_usuario, String TrofeoJuego, String Nom_Trofeo, Trophy tipo, String descripcion) throws IOException {
        long posicion = usuario.search(nom_usuario);
        if (posicion != -1 && isActive(nom_usuario)) {
            raf.seek(posicion);
            raf.readUTF();
            int puntos = raf.readInt();
            int trophies = raf.readInt();
            puntos += tipo.points;
            trophies++;
            raf.seek(posicion + nom_usuario.length() + 2); 
            raf.writeInt(puntos);
            raf.writeInt(trophies);

            try (RandomAccessFile trofeo_Archivo = new RandomAccessFile("Trofeos.tfo", "rw")) {
                trofeo_Archivo.seek(trofeo_Archivo.length());
                trofeo_Archivo.writeUTF(nom_usuario);
                trofeo_Archivo.writeUTF(tipo.name());
                trofeo_Archivo.writeUTF(TrofeoJuego);
                trofeo_Archivo.writeUTF(Nom_Trofeo);
                trofeo_Archivo.writeUTF(descripcion); 
                SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                trofeo_Archivo.writeUTF(fechaFormateada.format(new Date()));
            }
        } else {
            throw new IllegalStateException("El usuario esta inactivo");
        }
    }

    public String playerInfo(String nom_usuario) throws IOException {
        long posicion = usuario.search(nom_usuario);
        StringBuilder info = new StringBuilder();
        if (posicion != -1) {
            raf.seek(posicion);
            String user = raf.readUTF(); 
            info.append("Usuario: ").append(user).append("\n");

            int puntos = raf.readInt(); 
            info.append("Puntos: ").append(puntos).append("\n");

            int trophies = raf.readInt(); 
            boolean isActive = raf.readBoolean(); 
            info.append("Activo: ").append(isActive ? "Si" : "No").append("\n");
            info.append("Cantidad de Trofeos: ").append(trophies).append("\n");

            info.append("\nTrofeos:\n");

            try (RandomAccessFile trophyFile = new RandomAccessFile("Trofeos.tfo", "rw")) {
                trophyFile.seek(0);
                while (trophyFile.getFilePointer() < trophyFile.length()) {
                    String userTrophy = trophyFile.readUTF(); 
                    if (userTrophy.equals(nom_usuario)) {
                        String tipo = trophyFile.readUTF();
                        String juego = trophyFile.readUTF();
                        String nombreTrofeo = trophyFile.readUTF();
                        String descripcion = trophyFile.readUTF();
                        String fecha = trophyFile.readUTF();
                        info.append(fecha).append(" - ").append(tipo).append(" - ").append(juego)
                            .append(" - ").append(descripcion).append("\n");
                    } else {
                        trophyFile.readUTF(); 
                        trophyFile.readUTF(); 
                        trophyFile.readUTF(); 
                        trophyFile.readUTF();
                        trophyFile.readUTF(); 
                    }
                }
            } catch (EOFException e) {
            }
        } else {
            return "Usuario no encontrado";
        }
        return info.toString();
    }
}
