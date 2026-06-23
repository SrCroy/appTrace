package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "comentarios",
        foreignKeys = {
                @ForeignKey(
                        entity = ComentariosEntity.class,
                        parentColumns = "id",
                        childColumns = "padre_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"comentable_tipo", "comentable_id"}),
                @Index("padre_id"),
                @Index("usuario_id")
        }
)
public class ComentariosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String comentable_tipo;
    private long comentable_id;
    private String cuerpo;
    private String estado;
    private Integer padre_id;
    private int usuario_id;
    private String created_at;
    private String updated_at;

    public ComentariosEntity(String comentable_tipo, long comentable_id, String cuerpo,
                             String estado, int usuario_id) {
        this.comentable_tipo = comentable_tipo;
        this.comentable_id = comentable_id;
        this.cuerpo = cuerpo;
        this.estado = estado;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentable_tipo() {
        return comentable_tipo;
    }

    public void setComentable_tipo(String comentable_tipo) {
        this.comentable_tipo = comentable_tipo;
    }

    public long getComentable_id() {
        return comentable_id;
    }

    public void setComentable_id(long comentable_id) {
        this.comentable_id = comentable_id;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPadre_id() {
        return padre_id;
    }

    public void setPadre_id(Integer padre_id) {
        this.padre_id = padre_id;
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
