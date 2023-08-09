package co.com.upb.crud_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private lateinit var etNombreEst : EditText
    private lateinit var etCorreoEst : EditText
    private lateinit var etCursoEst : EditText
    private lateinit var btnlistar : Button
    private lateinit var btnActualizar : Button

    private lateinit var SQLiteHelper: SQLHelper
    private lateinit var estModel: EstModel
    private var adapter : EstAdapter? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        SQLiteHelper = SQLHelper(this)

        findViewById<Button>(R.id.btnGuardar).setOnClickListener { addEst() }
        btnlistar.setOnClickListener{getStudents()}
        btnActualizar.setOnClickListener { updateStudent() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.nombre, Toast.LENGTH_SHORT).show()

            etNombreEst.setText(it.nombre)
            etCorreoEst.setText(it.correo)
            etCursoEst.setText(it.curso)
            estModel = it
        }

        adapter?.setOnClickItem {
            it.id?.let { it1 -> deleteStudent(it1) }
        }

    }

    private fun getStudents() {
        val estList = SQLiteHelper.getListEst()
        adapter?.addItems(estList)
    }
    private  fun clearText(){
        etNombreEst.setText("")
        etCorreoEst.setText("")
        etCursoEst.setText("")
        etNombreEst.requestFocus()

    }

    private fun deleteStudent(id: Int){
        if (id == null) return

        var builder = AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar permanentemente el estudiante?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si"){ dialog,_->
            SQLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog,_ ->
            dialog.dismiss()

        }
        val alert = builder.create()
        alert.show()
    }

    private fun updateStudent(){
        val nombre = etNombreEst.text.toString()
        val correo = etCorreoEst.text.toString()
        val curso = etCursoEst.text.toString()

        if (nombre == estModel?.nombre && correo == estModel?.correo && curso == estModel?.curso) {
            Toast.makeText(this, "Valores iguales no se actualiza el estudiamte", Toast.LENGTH_LONG)
                .show()
            return
        }

        if(estModel == null) return
        val estAct = EstModel(estModel!!.id, nombre, correo, curso)
        val status = SQLiteHelper.actualizarEst(estAct)
        if(status > -1){
            clearText()
            getStudents()
        }else{
            Toast.makeText(this, "Valores iguales no se actualiza el estudiamte", Toast.LENGTH_LONG).show()
        }
    }

    private fun addEst() {
        val nombre = findViewById<EditText>(R.id.etNombreEst)?.text.toString()
        val correo = findViewById<EditText>(R.id.etCorreoEst)?.text.toString()
        val curso = findViewById<EditText>(R.id.etCursoEst)?.text.toString()

        if (nombre.isNullOrEmpty() || curso.isNullOrEmpty()) {
            Toast.makeText(this, "por favor ingrese los campos requeridos", Toast.LENGTH_LONG)
                .show()
        } else {
            val est = EstModel(null, nombre, correo, curso)
            val status = SQLiteHelper.insertEst(est)

            if (status >= 1) {
                Toast.makeText(this, "Se agrego correctamente el estudiante.", Toast.LENGTH_LONG).show()
                clearText()

            } else {
                Toast.makeText(
                    this,
                    "ocurrio un error al guardar el estudiante  ",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EstAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        etNombreEst = findViewById(R.id.etNombreEst)
        etCorreoEst = findViewById(R.id.etCorreoEst)
        etCursoEst = findViewById(R.id.etCursoEst)
        btnlistar = findViewById(R.id.btnListar)
        recyclerView = findViewById(R.id.recyclerView)
        btnActualizar = findViewById(R.id.btnAct)
    }
}







