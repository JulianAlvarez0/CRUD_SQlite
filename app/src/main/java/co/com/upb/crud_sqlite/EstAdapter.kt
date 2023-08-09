package co.com.upb.crud_sqlite

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstAdapter:RecyclerView.Adapter<EstAdapter.EstViewHolder>() {

    private var estList : ArrayList<EstModel> = ArrayList()
    private var onClickItem: ((EstModel) -> Unit)? = null
    private var onClickDeleteItem: ((EstModel) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_lista_est, parent, false)
        return EstViewHolder(view)
    }

    override fun getItemCount() = estList.size

    override fun onBindViewHolder(holder: EstViewHolder, position: Int) {
        val itemList = estList[position]
        holder.bindView(itemList)
        holder.itemView.setOnClickListener { onClickItem?.invoke(itemList)}
        holder.btnEliminar.setOnClickListener { onClickDeleteItem?.invoke(itemList) }


    }
    fun addItems(items: ArrayList<EstModel>){
        this.estList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (EstModel) -> Unit){
        this.onClickItem = callback

    }

    fun setOnClickDeleteItem(callback: (EstModel) -> Unit){
        this.onClickDeleteItem = callback

    }

    inner class EstViewHolder(var view:View): RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvidEst)
        private var nombreEst = view.findViewById<TextView>(R.id.tvNombreEst)
        private var correoEst = view.findViewById<TextView>(R.id.tvCorreoEst)
        private var cursoEst = view.findViewById<TextView>(R.id.tvCursoEst)
        var btnEliminar = view.findViewById<Button>(R.id.btnEliminar)

        fun bindView(est: EstModel){
            id.text = est.id.toString()
            nombreEst.text = est.nombre
            correoEst.text = est.correo
            cursoEst.text = est.curso
        }
    }
}