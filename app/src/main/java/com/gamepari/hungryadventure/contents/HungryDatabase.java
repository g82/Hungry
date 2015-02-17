package com.gamepari.hungryadventure.contents;

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
public class HungryDatabase extends SQLiteOpenHelper {

    public static final String DB_HUNGRY = "db_hungry";

    public static final String TABLE_FOODS = "tb_foods";
    public static final String TABLE_CITY = "tb_city";
    private static final String TABLE_USER = "tb_user";

    public HungryDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    private static final String makeFoodTableQuery() {

        String sqlQuery = "CREATE TABLE " +
                TABLE_FOODS +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "steps INTEGER," +
                "img_path TEXT," +
                "unlock_time TEXT," +
                "city_id INTEGER," +
                "calories INTEGER," +
                "cost TEXT" +
                ");";

        return sqlQuery;
    }

    private static final String makeCityTableQuery() {

        String sqlQuery2 = "CREATE TABLE " +
                TABLE_CITY +
                " (" +
                "_id INTEGER PRIMARY KEY," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "img_path TEXT," +
                "unlocked INTEGER" +
                ");";

        return sqlQuery2;
    }

    private static final String makeUserTableQuery() {

        String sqlQuery3 = "CREATE TABLE " +
                TABLE_USER +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "local_name TEXT," +
                "eng_name TEXT," +
                "asset_path TEXT" +
                ");";

        return sqlQuery3;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(makeCityTableQuery());

        sqLiteDatabase.execSQL(makeFoodTableQuery());
        sqLiteDatabase.execSQL(makeUserTableQuery());

        sqLiteDatabase.execSQL(DummyInput.makeDummyCity());
        sqLiteDatabase.execSQL(DummyInput.makeDummyFood());

    }

    public List<ModelCity> getCities() {

        SQLiteDatabase database = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CITY + ";";

        Cursor cursor = database.rawQuery(query, null);

        List<ModelCity> listCities = new ArrayList<>();

        while (cursor.moveToNext()) {


            ModelCity city = new ModelCity(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    (cursor.getInt(4) == 1 ? true : false));
            listCities.add(city);
        }

        database.close();

        return listCities;
    }

    public List<ModelFood> getFoods(String country) {

        SQLiteDatabase database = getReadableDatabase();

        String query = "SELECT * FROM " +
                TABLE_FOODS + "," + TABLE_CITY +
                " WHERE tb_city.eng_name = '" + country + "' AND tb_foods.city_id=tb_city._id;";

        Cursor cursor = database.rawQuery(query, null);

        List<ModelFood> listFoods = new ArrayList<>();

        while (cursor.moveToNext()) {

            ModelFood food = new ModelFood(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getString(4), null,
                    cursor.getInt(6), cursor.getInt(7), cursor.getInt(8));

            String timeStr = cursor.getString(5);

            if (timeStr != null) {

                Time time = new Time();
                time.parse(timeStr);
                food.setmUnlockTime(time);

            }

            listFoods.add(food);

        }

        database.close();

        return listFoods;

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
