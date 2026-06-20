package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reacciones",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"usuario_id", "reaccionable_tipo", "reaccionable_id"}, unique = true),
                @Index(value = {"reaccionable_tipo", "reaccionable_id"})
        }
)
public class ReaccionesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String reaccionable_tipo;
    private long reaccionable_id;
    private int usuario_id;
    private String creado_en;

    public ReaccionesEntity(String reaccionable_tipo, long reaccionable_id, int usuario_id) {
        this.reaccionable_tipo = reaccionable_tipo;
        this.reaccionable_id = reaccionable_id;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReaccionable_tipo() {
        return reaccionable_tipo;
    }

    public void setReaccionable_tipo(String reaccionable_tipo) {
        this.reaccionable_tipo = reaccionable_tipo;
    }

    public long getReaccionable_id() {
        return reaccionable_id;
    }

    public void setReaccionable_id(long reaccionable_id) {
        this.reaccionable_id = reaccionable_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getCreado_en() {
        return creado_en;
    }

    public void setCreado_en(String creado_en) {
        this.creado_en = creado_en;
    }
}
