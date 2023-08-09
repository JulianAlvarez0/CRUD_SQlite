package co.com.upb.crud_sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLHelper (context: MainActivity):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)  {

    companion object{
        private const val DB_VERSION = 1
        private const val DB_NAME = "students.db"
        private const val DB_TABLE = "tbl_students"
        private const val id_est = "id"
        private const val nombre = "nombre"
        private const val correo = "correo"
        private const val curso = "curso"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = (
                "CREATE TABLE $DB_TABLE(" +
                        "$id_est INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$nombre TEXT," +
                        "$correo TEXT," +
                        "$curso TEXT)")

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)

    }

    fun deleteStudentById(id: Int): Int{
        val db = this.writableDatabase

        val success = db.delete(DB_TABLE, "id=$id", null)
        db.close()
        return success
    }

    fun actualizarEst(est: EstModel): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(id_est, est.id)
        contentValues.put(nombre, est.nombre)
        contentValues.put(correo, est.correo)
        contentValues.put(curso, est.curso)

        val success = db.update(DB_TABLE, contentValues, "id="+est.id, null)
        db.close()
        return success
    }

    fun insertEst(est: EstModel): Long{
        //LEER Y ESCRIBIR
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(id_est, est.id)
        contentValues.put(nombre, est.nombre)
        contentValues.put(correo, est.correo)
        contentValues.put(curso, est.curso)

        val success = db.insert(DB_TABLE, null, contentValues)
        db.close()
        return success
    }
    @SuppressLint("Range", "Recycle")
    fun getListEst(): ArrayList<EstModel>{
        val listEst: ArrayList<EstModel> = ArrayList()
        val sql = "SELECT * FROM $DB_TABLE"
        val db = this.readableDatabase
        val recCursor: Cursor

        try{
            recCursor = db.rawQuery(sql, null)
        }catch (e: Exception){
            e.printStackTrace()
            return ArrayList()
        }

        var id : Int?
        var nombre : String = ""
        var correo : String = ""
        var curso : String=""

        //Iniciar un bucle para recorrer  la informacion que llega de la db
        if ( recCursor.moveToFirst()){
            do {
                id = recCursor.getInt(recCursor.getColumnIndex("id"))
                nombre = recCursor.getString(recCursor.getColumnIndex("nombre"))
                correo = recCursor.getString(recCursor.getColumnIndex("correo"))
                curso = recCursor.getString(recCursor.getColumnIndex("curso"))

                val est = EstModel(id, nombre, correo, curso)
                listEst.add(est)
            }while (recCursor.moveToNext())
        }
        return listEst
    }

}
