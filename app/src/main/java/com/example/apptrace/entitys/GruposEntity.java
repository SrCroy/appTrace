package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "grupos",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "propietario_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = @Index("propietario_id")
)
public class GruposEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String descripcion;
    private String avatar;
    private String privacidad;
    private int total_miembros;
    private int propietario_id;
    private String created_at;
    private String updated_at;
    public GruposEntity(String nombre, String privacidad, int total_miembros, int propietario_id) {
        this.nombre = nombre;
        this.privacidad = privacidad;
        this.total_miembros = total_miembros;
        this.propietario_id = propietario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public int getTotal_miembros() {
        return total_miembros;
    }

    public void setTotal_miembros(int total_miembros) {
        this.total_miembros = total_miembros;
    }

    public int getPropietario_id() {
        return propietario_id;
    }

    public void setPropietario_id(int propietario_id) {
        this.propietario_id = propietario_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
