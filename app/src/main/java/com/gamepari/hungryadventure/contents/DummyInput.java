package com.gamepari.hungryadventure.contents;

/**
 * Created by gamepari on 2/17/15.
 */
public class DummyInput {

    public static final String makeDummyCity() {

        String sqlQuery7 = "INSERT INTO " + HungryDatabase.TABLE_CITY +
                "(" +
                "_id," +
                "local_name," +
                "eng_name," +
                "img_path," +
                "unlocked" +

                ") VALUES " +

                "(" +
                "0," +
                "'서울'," +
                "'Seoul'," +
                "'thumb_seoul.png'," +
                "1" +
                ")," +

                "(" +
                "1," +
                "'東京'," +
                "'Tokyo'," +
                "'thumb_tokyo.png'," +
                "0" +
                ");";
        ;

        return sqlQuery7;

    }

    public static final String makeDummyFood() {

        String sqlQuery4 = "INSERT INTO " + HungryDatabase.TABLE_FOODS +
                " (local_name, eng_name, steps, img_path, city_id, calories, cost)" +
                " VALUES " +

                "(" +
                "'갈비'," +
                "'Galbi'," +
                "300," +
                "'item_galbi.png'," +
                "0," +
                "600," +
                "'7000 ~ 12000 won'" +
                ")," +


                "(" +
                "'갈비2'," +
                "'Galbi2'," +
                "300," +
                "'item_galbi.png'," +
                "0," +
                "600," +
                "'7000 ~ 12000 won'" +
                ")," +

                "(" +
                "'갈비3'," +
                "'Galbi3'," +
                "300," +
                "'item_galbi.png'," +
                "0," +
                "600," +
                "'7000 ~ 12000 won'" +
                ");";


        return sqlQuery4;
    }


}
