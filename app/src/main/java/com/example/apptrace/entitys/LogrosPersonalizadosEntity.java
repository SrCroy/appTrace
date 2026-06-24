package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "logros_personalizados",
        foreignKeys = {
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
                        childColumns = "propuesto_por",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "revisado_por",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = {"grupo_id", "estado"}),
                @Index("actividad_id"),
                @Index("propuesto_por"),
                @Index("revisado_por")
        }
)
public class LogrosPersonalizadosEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String descripcion;
    private String icono_url;
    private String tipo_disparador;
    private Double valor_disparador;
    private Integer actividad_id;
    private String estado;
    private String comentario_revision;
    private int grupo_id;
    private int propuesto_por;
    private Integer revisado_por;
    private String revisado_en;
    private String created_at;
    private String updated_at;

    public LogrosPersonalizadosEntity(String nombre, String descripcion, String icono_url,
                                      String tipo_disparador, String estado, int grupo_id,
                                      int propuesto_por) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono_url = icono_url;
        this.tipo_disparador = tipo_disparador;
        this.estado = estado;
        this.grupo_id = grupo_id;
        this.propuesto_por = propuesto_por;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono_url() {
        return icono_url;
    }

    public void setIcono_url(String icono_url) {
        this.icono_url = icono_url;
    }

    public String getTipo_disparador() {
        return tipo_disparador;
    }

    public void setTipo_disparador(String tipo_disparador) {
        this.tipo_disparador = tipo_disparador;
    }

    public Double getValor_disparador() {
        return valor_disparador;
    }

    public void setValor_disparador(Double valor_disparador) {
        this.valor_disparador = valor_disparador;
    }

    public Integer getActividad_id() {
        return actividad_id;
    }

    public void setActividad_id(Integer actividad_id) {
        this.actividad_id = actividad_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario_revision() {
        return comentario_revision;
    }

    public void setComentario_revision(String comentario_revision) {
        this.comentario_revision = comentario_revision;
    }

    public int getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(int grupo_id) {
        this.grupo_id = grupo_id;
    }

    public int getPropuesto_por() {
        return propuesto_por;
    }

    public void setPropuesto_por(int propuesto_por) {
        this.propuesto_por = propuesto_por;
    }

    public Integer getRevisado_por() {
        return revisado_por;
    }

    public void setRevisado_por(Integer revisado_por) {
        this.revisado_por = revisado_por;
    }

    public String getRevisado_en() {
        return revisado_en;
    }

    public void setRevisado_en(String revisado_en) {
        this.revisado_en = revisado_en;
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
