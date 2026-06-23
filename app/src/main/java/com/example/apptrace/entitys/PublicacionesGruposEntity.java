package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "publicaciones_grupos",
        foreignKeys = {
                @ForeignKey(
                        entity = RutasEntity.class,
                        parentColumns = "id",
                        childColumns = "ruta_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = ActividadesEntity.class,
                        parentColumns = "id",
                        childColumns = "actividad_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = GruposEntity.class,
                        parentColumns = "id",
                        childColumns = "grupo_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("ruta_id"),
                @Index("actividad_id"),
                @Index("grupo_id"),
                @Index("usuario_id")
        }
)
public class PublicacionesGruposEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String contenido;
    private Integer ruta_id;
    private Integer actividad_id;
    private int grupo_id;
    private int usuario_id;
    private String created_at;
    private String updated_at;

    public PublicacionesGruposEntity(String contenido, int grupo_id, int usuario_id) {
        this.contenido = contenido;
        this.grupo_id = grupo_id;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Integer getRuta_id() {
        return ruta_id;
    }

    public void setRuta_id(Integer ruta_id) {
        this.ruta_id = ruta_id;
    }

    public Integer getActividad_id() {
        return actividad_id;
    }

    public void setActividad_id(Integer actividad_id) {
        this.actividad_id = actividad_id;
    }

    public int getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(int grupo_id) {
        this.grupo_id = grupo_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
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
