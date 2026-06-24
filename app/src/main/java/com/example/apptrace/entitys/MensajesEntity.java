package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "mensajes",
        foreignKeys = {
                @ForeignKey(
                        entity = ConversacionesEntity.class,
                        parentColumns = "id",
                        childColumns = "conversacion_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "remitente_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("conversacion_id"),
                @Index("remitente_id")
        }
)
public class MensajesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cuerpo;
    private String leido_en;
    private int conversacion_id;
    private int remitente_id;
    private String created_at;
    private String updated_at;

    public MensajesEntity(String cuerpo, int conversacion_id, int remitente_id) {
        this.cuerpo = cuerpo;
        this.conversacion_id = conversacion_id;
        this.remitente_id = remitente_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getLeido_en() {
        return leido_en;
    }

    public void setLeido_en(String leido_en) {
        this.leido_en = leido_en;
    }

    public int getConversacion_id() {
        return conversacion_id;
    }

    public void setConversacion_id(int conversacion_id) {
        this.conversacion_id = conversacion_id;
    }

    public int getRemitente_id() {
        return remitente_id;
    }

    public void setRemitente_id(int remitente_id) {
        this.remitente_id = remitente_id;
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
