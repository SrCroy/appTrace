package com.example.apptrace.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.apptrace.entitys.ActividadesEntity;
import com.example.apptrace.entitys.AmistadesEntity;
import com.example.apptrace.entitys.ComentariosEntity;
import com.example.apptrace.entitys.ConversacionesEntity;
import com.example.apptrace.entitys.GruposEntity;
import com.example.apptrace.entitys.LogrosEntity;
import com.example.apptrace.entitys.LogrosPersonalizadosEntity;
import com.example.apptrace.entitys.MensajesEntity;
import com.example.apptrace.entitys.MiembrosGruposEntity;
import com.example.apptrace.entitys.PublicacionArchivosEntity;
import com.example.apptrace.entitys.PublicacionesEntity;
import com.example.apptrace.entitys.PublicacionesGruposEntity;
import com.example.apptrace.entitys.PuntosGpsEntity;
import com.example.apptrace.entitys.ReaccionesEntity;
import com.example.apptrace.entitys.ReportesEntity;
import com.example.apptrace.entitys.RutasEntity;
import com.example.apptrace.entitys.UsuariosEntity;
import com.example.apptrace.entitys.UsuariosLogrosEntity;
import com.example.apptrace.entitys.UsuariosLogrosPersonalizadosEntity;

@Database(
        entities = {
                UsuariosEntity.class,
                LogrosEntity.class,
                RutasEntity.class,
                GruposEntity.class,
                ActividadesEntity.class,
                PuntosGpsEntity.class,
                MiembrosGruposEntity.class,
                UsuariosLogrosEntity.class,
                PublicacionesGruposEntity.class,
                PublicacionesEntity.class,
                PublicacionArchivosEntity.class,
                AmistadesEntity.class,
                ConversacionesEntity.class,
                MensajesEntity.class,
                ReportesEntity.class,
                ReaccionesEntity.class,
                ComentariosEntity.class,
                LogrosPersonalizadosEntity.class,
                UsuariosLogrosPersonalizadosEntity.class
        },
        version = 1
)
public abstract class AppDataBase extends RoomDatabase {
    private static volatile AppDataBase INSTANCE;

    public static  AppDataBase obtenerDatos(final Context context){
        if (INSTANCE == null){
            synchronized (AppDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "db_trace"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
