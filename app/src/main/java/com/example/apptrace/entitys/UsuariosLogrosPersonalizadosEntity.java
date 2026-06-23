package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "usuarios_logros_personalizados",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = LogrosPersonalizadosEntity.class,
                        parentColumns = "id",
                        childColumns = "logro_personalizado_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "otorgado_por",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = {"usuario_id", "logro_personalizado_id"}, unique = true),
                @Index("otorgado_por")
        }
)
public class UsuariosLogrosPersonalizadosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int usuario_id;
    private int logro_personalizado_id;
    private Integer otorgado_por;
    private String obtenido_en;

    public UsuariosLogrosPersonalizadosEntity(int usuario_id, int logro_personalizado_id,
                                              String obtenido_en) {
        this.usuario_id = usuario_id;
        this.logro_personalizado_id = logro_personalizado_id;
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

    public int getLogro_personalizado_id() {
        return logro_personalizado_id;
    }

    public void setLogro_personalizado_id(int logro_personalizado_id) {
        this.logro_personalizado_id = logro_personalizado_id;
    }

    public Integer getOtorgado_por() {
        return otorgado_por;
    }

    public void setOtorgado_por(Integer otorgado_por) {
        this.otorgado_por = otorgado_por;
    }

    public String getObtenido_en() {
        return obtenido_en;
    }

    public void setObtenido_en(String obtenido_en) {
        this.obtenido_en = obtenido_en;
    }
}
