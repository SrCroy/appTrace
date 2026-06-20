package com.example.apptrace.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reportes",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuariosEntity.class,
                        parentColumns = "id",
                        childColumns = "reportador_id",
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
                @Index(value = {"reportable_tipo", "reportable_id"}),
                @Index("reportador_id"),
                @Index("revisado_por")
        }
)
public class ReportesEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String reportable_tipo;
    private long reportable_id;
    private String motivo;
    private String detalles;
    private String estado;
    private String revisado_en;
    private int reportador_id;
    private Integer revisado_por;
    private String created_at;
    private String updated_at;

    public ReportesEntity(String reportable_tipo, long reportable_id, String motivo,
                          String estado, int reportador_id) {
        this.reportable_tipo = reportable_tipo;
        this.reportable_id = reportable_id;
        this.motivo = motivo;
        this.estado = estado;
        this.reportador_id = reportador_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportable_tipo() {
        return reportable_tipo;
    }

    public void setReportable_tipo(String reportable_tipo) {
        this.reportable_tipo = reportable_tipo;
    }

    public long getReportable_id() {
        return reportable_id;
    }

    public void setReportable_id(long reportable_id) {
        this.reportable_id = reportable_id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRevisado_en() {
        return revisado_en;
    }

    public void setRevisado_en(String revisado_en) {
        this.revisado_en = revisado_en;
    }

    public int getReportador_id() {
        return reportador_id;
    }

    public void setReportador_id(int reportador_id) {
        this.reportador_id = reportador_id;
    }

    public Integer getRevisado_por() {
        return revisado_por;
    }

    public void setRevisado_por(Integer revisado_por) {
        this.revisado_por = revisado_por;
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
