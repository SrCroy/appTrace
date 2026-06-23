package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "miembros_grupos",
        foreignKeys = {
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
                @Index(value = {"grupo_id", "usuario_id"}, unique = true)
        }
)
public class MiembrosGruposEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String rol;
    private String unido_en;
    private int grupo_id;
    private int usuario_id;
    public MiembrosGruposEntity(String rol, int grupo_id, int usuario_id) {
        this.rol = rol;
        this.grupo_id = grupo_id;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUnido_en() {
        return unido_en;
    }

    public void setUnido_en(String unido_en) {
        this.unido_en = unido_en;
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
}
