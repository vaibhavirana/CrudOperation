package com.vebs.crudoperation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.vebs.crudoperation.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi Rana on 16-10-2019.
 */
public class MovieDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;

    public MovieDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

        // we have supplied no upgrade path from version 1 to 2
       // setForcedUpgrade(2);
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
            * Read all Movie from the database.
            *
            * @return a List of Movie
     */
    public List<Movie> getMovieList() {
        List<Movie> list = new ArrayList<>();
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM movie", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Movie(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
       return list;
    }

    /**
     * Read Movie by id from the database.
     *
     * @return a Movie
     */
    public Movie getMovie(int id) {
        Movie movie = null;
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM movie where id="+id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movie =new Movie(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return movie;
    }

    /**
     * add Movie in the database.
     *
     * @return a boolean
     */
    public boolean insertData(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",movie.getTitle());
        contentValues.put("genre",movie.getGenre());
        contentValues.put("year",movie.getYear());
        long result = db.insert("movie",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /**
     * update Movie in the database.
     *
     * @return a boolean
     */

    public boolean updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",movie.getId());
        contentValues.put("title",movie.getTitle());
        contentValues.put("genre",movie.getGenre());
        contentValues.put("year",movie.getYear());
        db.update("movie", contentValues, "ID = ?",new String[] {String.valueOf(movie.getId())});
        return true;
    }

    /**
     * delete Movie in the database.
     *
     * @return a boolean
     */

    public boolean deleteMovie (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("movie", "id = ?",new String[] {String.valueOf(id)}) >0;
    }

    public int getUpgradeVersion() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"MAX (version)"};
        String sqlTables = "upgrades";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        int v = 0;
        c.moveToFirst();
        if (!c.isAfterLast()) {
            v = c.getInt(0);
        }
        c.close();
        return v;
    }


}

