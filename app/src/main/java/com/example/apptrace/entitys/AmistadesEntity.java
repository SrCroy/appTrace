package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "amistades",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "solicitante_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "receptor_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"solicitante_id", "receptor_id"}, unique = true)
        }
)
public class AmistadesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String estado;
    private int solicitante_id;
    private int receptor_id;
    private String created_at;
    private String updated_at;

    public AmistadesEntity(String estado, int solicitante_id, int receptor_id) {
        this.estado = estado;
        this.solicitante_id = solicitante_id;
        this.receptor_id = receptor_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getSolicitante_id() {
        return solicitante_id;
    }

    public void setSolicitante_id(int solicitante_id) {
        this.solicitante_id = solicitante_id;
    }

    public int getReceptor_id() {
        return receptor_id;
    }

    public void setReceptor_id(int receptor_id) {
        this.receptor_id = receptor_id;
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
