package com.wiio.androidwiiovision

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.io.File
import com.wiio.androidwiiovision.R

/**
 * Created by chenyu on 27/03/2018.
 */
class RecyclerAdapter(val photoFils : ArrayList<File>)  : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>()  {

    val choosenPhotos = ArrayList<PhotoHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item, false)
        return PhotoHolder(inflatedView, choosenPhotos)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.photoPath = photoFils[position].absolutePath
        holder.bindPhoto()
    }

    override fun getItemCount() = photoFils.size

    fun romoveChoosenItem(){
        for(photoItem: PhotoHolder in choosenPhotos){
            photoFils.removeAt(photoItem.adapterPosition)
            notifyItemRemoved(photoItem.adapterPosition)
            notifyItemRangeChanged(photoItem.adapterPosition,itemCount)
        }
        choosenPhotos.clear()
    }


    class PhotoHolder(v: View, choosenPhotos:ArrayList<PhotoHolder>) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        var view: View = v
        var imageView = v.imageViewItem
        var checkBox = v.checkboxItem
        lateinit var photoPath:String
        var choosenPhotos = choosenPhotos

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            checkBox.visibility = View.INVISIBLE
        }

        fun bindPhoto(){
            Picasso.get().load(File(photoPath)).resize(200,240).into(imageView)
            imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val intent = Intent(context, ImageDisplayActivity::class.java)
            intent.putExtra(PHOTO_KEY, photoPath)
            context.startActivity(intent)
        }

        override fun onLongClick(v: View?): Boolean {

            if(checkBox.visibility == View.VISIBLE){
                checkBox.visibility = View.INVISIBLE
                choosenPhotos.remove(this)
                imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
            }
            else{
                checkBox.visibility = View.VISIBLE
                choosenPhotos.add(this)
                imageView.clearColorFilter()
            }

            checkBox.isChecked = !checkBox.isChecked
            return true
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }

}
