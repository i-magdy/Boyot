package org.boyoot.app.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GoogleSheet.class}, version = 7,exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {


    public abstract GoogleSheetDao googleSheetDao();

    private static volatile AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static AppRoomDatabase getDatabase(final Context context){

        if (null == INSTANCE){
            synchronized (AppRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "boyoot_app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }



        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new  RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                    INSTANCE.googleSheetDao();
            });
        }
    };
}
