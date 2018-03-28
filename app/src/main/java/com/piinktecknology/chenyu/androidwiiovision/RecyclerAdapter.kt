package com.piinktecknology.chenyu.androidwiiovision

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.provider.ContactsContract
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.io.File
import java.net.URI
import java.net.URL
import android.widget.TextView
import android.widget.RelativeLayout

/**
 * Created by chenyu on 27/03/2018.
 */
class RecyclerAdapter(val photoFils : ArrayList<File>)  : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>()  {

    val choosenPhotos = ArrayList<PhotoHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder{
        val inflatedView = parent.inflate(R.layout.recyclerview_item, false)
        return PhotoHolder(inflatedView, choosenPhotos)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        holder.photoPath = photoFils[position].absolutePath
        holder.bindPhoto()
    }

    override fun getItemCount() = photoFils.size

    fun romoveChoosenItem(){
        for(photoItem: RecyclerAdapter.PhotoHolder in choosenPhotos){
            photoFils.removeAt(photoItem.adapterPosition)
            notifyItemRemoved(photoItem.adapterPosition)
            notifyItemRangeChanged(photoItem.adapterPosition,itemCount)
        }
        choosenPhotos.clear()
    }


    class PhotoHolder(v: View, photoList:ArrayList<PhotoHolder>) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        var view: View = v
        var imageView = v.imageViewItem
        var checkBox = v.checkboxItem
        lateinit var photoPath:String
        var photoList = photoList

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            checkBox.visibility = View.INVISIBLE
        }

        fun bindPhoto(){
            Picasso.get().load(File(photoPath)).resize(200,240).into(imageView)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val intent = Intent(context, ImageDisplayActivity::class.java)
            intent.putExtra(PHOTO_KEY, photoPath)
            context.startActivity(intent)
        }

        override fun onLongClick(v: View?): Boolean {
            checkBox.visibility = View.VISIBLE
            checkBox.isChecked = true
            photoList.add(this)
            return true
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }

}
