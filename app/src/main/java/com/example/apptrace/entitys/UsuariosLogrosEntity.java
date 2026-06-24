package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "usuarios_logros",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = LogrosEntity.class,
                        parentColumns = "id",
                        childColumns = "logro_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"usuario_id", "logro_id"}, unique = true)
        }
)
public class UsuariosLogrosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int usuario_id;
    private int logro_id;
    private String obtenido_en;

    public UsuariosLogrosEntity(int usuario_id, int logro_id, String obtenido_en) {
        this.usuario_id = usuario_id;
        this.logro_id = logro_id;
        this.obtenido_en = obtenido_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getLogro_id() {
        return logro_id;
    }

    public void setLogro_id(int logro_id) {
        this.logro_id = logro_id;
    }

    public String getObtenido_en() {
        return obtenido_en;
    }

    public void setObtenido_en(String obtenido_en) {
        this.obtenido_en = obtenido_en;
    }
}
