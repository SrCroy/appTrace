package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "conversaciones",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_uno_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "usuario_dos_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"usuario_uno_id", "usuario_dos_id"}, unique = true)
        }
)
public class ConversacionesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ultimo_mensaje_en;
    private int usuario_uno_id;
    private int usuario_dos_id;
    private String creado_en;

    public ConversacionesEntity(int usuario_uno_id, int usuario_dos_id) {
        this.usuario_uno_id = usuario_uno_id;
        this.usuario_dos_id = usuario_dos_id;
    }
}
