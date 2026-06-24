package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "publicacion_archivos",
        foreignKeys = {
                @ForeignKey(
                        entity = PublicacionesEntity.class,
                        parentColumns = "id",
                        childColumns = "publicacion_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("publicacion_id")
        }
)
public class PublicacionArchivosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String url;
    private String tipo;
    private int orden;
    private int publicacion_id;

    public PublicacionArchivosEntity(String url, String tipo, int orden, int publicacion_id) {
        this.url = url;
        this.tipo = tipo;
        this.orden = orden;
        this.publicacion_id = publicacion_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getPublicacion_id() {
        return publicacion_id;
    }

    public void setPublicacion_id(int publicacion_id) {
        this.publicacion_id = publicacion_id;
    }
}
