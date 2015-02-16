package com.gamepari.hungryadventure.foods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gamepari on 2/16/15.
 */
public class FoodDatabase extends SQLiteOpenHelper {

    public static final String DB_HUNGRY = "db_hungry";

    private static final String TABLE_FOODS = "tb_foods";
    private static final String TABLE_COUNTRY = "tb_country";
    private static final String TABLE_USER = "tb_user";

    public FoodDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlQuery = "CREATE TABLE " +
                TABLE_FOODS +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "required_steps INTEGER," +
                "asset_path TEXT," +
                "unlock_date TEXT," +
                "country_name TEXT" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery);

        String sqlQuery2 = "CREATE TABLE " +
                TABLE_COUNTRY +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "asset_path TEXT" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery2);


        String sqlQuery3 = "CREATE TABLE " +
                TABLE_USER +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "asset_path TEXT" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery3);


        String sqlQuery4 = "INSERT INTO " + TABLE_FOODS +
                " (eng_name, local_name, required_steps, asset_path, country_name) " +
                " VALUES (" +
                "Galbi," +
                "갈비," +
                "200," +
                "item_galbi.png," +
                "Seoul" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery4);

        /*
        String sqlQuery5 = "INSERT INTO " + TABLE_FOODS + " VALUES (" +
                "Bossam," +
                "보쌈," +
                "500," +
                "item_bossam.png," +
                "NULL," +
                "Seoul" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery5);

        String sqlQuery6 = "INSERT INTO " + TABLE_FOODS + " VALUES (" +
                "Galbitang," +
                "갈비탕," +
                "900," +
                "item_galbitang.png," +
                "NULL," +
                "Seoul" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery6);
        */


        String sqlQuery7 = "INSERT INTO " + TABLE_COUNTRY +
                " VALUES (" +
                "서울," +
                "Seoul," +
                "thumb_seoul" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery7);

        String sqlQuery8 = "INSERT INTO " + TABLE_COUNTRY + " VALUES (" +
                "도쿄," +
                "Tokyo," +
                "thumb_seoul" +
                ");";

        sqLiteDatabase.execSQL(sqlQuery8);

    }

    public List<ModelFood> getFoods(String country) {

        SQLiteDatabase database = getReadableDatabase();

        String query = "SELECT * FROM " +
                TABLE_FOODS +
                " WHERE country_id = " + country + ";";

        Cursor cursor = database.rawQuery(query, null);

        List<ModelFood> listFoods = new ArrayList<>();

        while (cursor.moveToNext()) {

            ModelFood food = new ModelFood(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getString(4), null,
                    cursor.getInt(6));

            Time time = new Time();
            time.parse(cursor.getString(5));

            food.setmUnlockDate(time);

            listFoods.add(food);

        }

        database.close();

        return listFoods;

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
