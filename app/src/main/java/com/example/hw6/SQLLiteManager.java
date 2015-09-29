package com.example.hw6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ыг on 24.09.2015.
 */
public class SQLLiteManager extends SQLiteOpenHelper {

public static final int DATABASE_VERSION = 1;//int to secure
    public static final String DATABASE_NAME = "Advertisements";

    public SQLLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {

        //create table analog
        //query to execute
        String CreateQuery = "CREATE TABLE IF NOT EXISTS "+AdvDBTableObject.TABLE_NAME+
                " ( "+AdvDBTableObject.ID_COLUMN_KEY+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                AdvDBTableObject.PLACE_COLUMN_KEY+" VARCHAR, "
                +AdvDBTableObject.DATE_COLUMN_KEY+" VARCHAR, "
                +AdvDBTableObject.TIME_COLUMN_KEY+" VARCHAR, "+
                AdvDBTableObject.IMAGE_COLUMN_KEY+" BLOB );";

        MyDataBase.execSQL(CreateQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrading the very table
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME+"; ");
        //send to upper method oncreate
        this.onCreate(db);

    }

    public  void addAdvertisementToTable(AdvDBTableObject newAddToAdd){

//reference to a class we are  in
        SQLiteDatabase myDataBase = this.getWritableDatabase();

//object to strore column-key-values pairs
        ContentValues myContentValuesPairs = new ContentValues();

        myContentValuesPairs.put(newAddToAdd.PLACE_COLUMN_KEY, newAddToAdd.getPlace());
        myContentValuesPairs.put(newAddToAdd.DATE_COLUMN_KEY, newAddToAdd.getDate());
        myContentValuesPairs.put(newAddToAdd.TIME_COLUMN_KEY, newAddToAdd.getTime());
        myContentValuesPairs.put(newAddToAdd.IMAGE_COLUMN_KEY,ImageToBytesAndBackConverter.bitmapToByteArray(newAddToAdd.getImage()));

        myDataBase.insert(newAddToAdd.TABLE_NAME, null, myContentValuesPairs);

        myDataBase.close();



    }


    public  void deleteAdvertisemsnt(AdvDBTableObject addToDeleteFromDatabase){

        //from this DB
        SQLiteDatabase myDb = this.getWritableDatabase();

        myDb.delete(addToDeleteFromDatabase.TABLE_NAME, addToDeleteFromDatabase.ID_COLUMN_KEY + " = ?",
                new String[]{String.valueOf(addToDeleteFromDatabase.getId())});//array of strings

        myDb.close();
        Log.d("delete Add", addToDeleteFromDatabase.toString());



    }

public AdvDBTableObject getAdvertisement( int idOfAddToCheck){


    SQLiteDatabase db = this.getReadableDatabase();

    // my cursor query/TABLE_NAME/COLUMNS_TO_ITERATE/SELECTION/THE_VERY_VALUE/
    Cursor cursor = db.query(AdvDBTableObject.TABLE_NAME,
            AdvDBTableObject.COLUMNS_ARRAY,
            " id = ?",
            new String[]{String.valueOf(idOfAddToCheck)},
            null,
            null,
            null,
            null);



    if(cursor !=null){//if there is  smt to go to
        cursor.moveToFirst();//first iteration
    }

//creating the very obj to return
    AdvDBTableObject addWeGot  = new AdvDBTableObject();
//filling the velues  of the obj
    addWeGot.setId(Integer.parseInt(cursor.getString(0)));
    addWeGot.setPlace(cursor.getString(1));
    addWeGot.setDate(cursor.getString(2));
    addWeGot.setTime(cursor.getString(3));
    addWeGot.setImage(ImageToBytesAndBackConverter.byteArrayToBitmap(cursor.getBlob(4)));

    Log.d("getAdvertisement(" + idOfAddToCheck + ")", addWeGot.toString());
    return addWeGot;
}

    public ArrayList<AdvDBTableObject> getAllAdds(){

        ArrayList<AdvDBTableObject> ListOfAdds= new ArrayList<>();
        String queryToExecute = "SELECT * FROM " + AdvDBTableObject.TABLE_NAME ;

        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor myCursor = myDb.rawQuery(queryToExecute, null);

        AdvDBTableObject AddListSingleItem = null;

        if(myCursor.moveToFirst()) {
            do {
                AddListSingleItem.setId(Integer.parseInt(myCursor.getString(0)));
                AddListSingleItem.setPlace(myCursor.getString(1));
                AddListSingleItem.setDate(myCursor.getString(2));
                AddListSingleItem.setTime(myCursor.getString(3));
                AddListSingleItem.setImage(ImageToBytesAndBackConverter.byteArrayToBitmap(myCursor.getBlob(4)));
            } while (myCursor.moveToFirst());
        }//if
          Log.d("get all ads",ListOfAdds.toString());
          return ListOfAdds;

    }


    public int updateDatabase( AdvDBTableObject addToUpdate){

        SQLiteDatabase myDb = this.getWritableDatabase();
        //my value pairs
        ContentValues myValues = new ContentValues();
        myValues.put(addToUpdate.PLACE_COLUMN_KEY, addToUpdate.getPlace());
        myValues.put(addToUpdate.DATE_COLUMN_KEY, addToUpdate.getDate());
        myValues.put(addToUpdate.TIME_COLUMN_KEY, addToUpdate.getTime());
        myValues.put(addToUpdate.IMAGE_COLUMN_KEY, ImageToBytesAndBackConverter.bitmapToByteArray(addToUpdate.getImage()));
//my row
        int idOfUpdate = myDb.update(addToUpdate.TABLE_NAME,myValues,addToUpdate.ID_COLUMN_KEY + " = ?",
                new String[]{String.valueOf(addToUpdate.getId())});



        myDb.close();

        Log.d("database update",addToUpdate.toString());

        return idOfUpdate;

    }


}
