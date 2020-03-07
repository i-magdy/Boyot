package org.boyoot.app.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GoogleSheet.class , Contacts.class}, version = 11,exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {


    public abstract GoogleSheetDao googleSheetDao();
    public abstract ContactsDoa contactsDoa();

    private static volatile AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 8;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static AppRoomDatabase getDatabase(final Context context){

        if (null == INSTANCE){
            synchronized (AppRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "boyoot_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }



        return INSTANCE;
    }

    public static AppRoomDatabase getContactsDatabase(final Context context){

        if (null == INSTANCE){
            synchronized (AppRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "boyoot_database")
                            .addCallback(sContactsRoomDatabaseCallback)
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
            databaseWriteExecutor.execute(() -> INSTANCE.googleSheetDao());
        }
    };

    private static RoomDatabase.Callback sContactsRoomDatabaseCallback = new  RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> INSTANCE.contactsDoa());
        }
    };
}
