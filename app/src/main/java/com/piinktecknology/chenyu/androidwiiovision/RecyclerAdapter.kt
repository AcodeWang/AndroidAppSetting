package com.piinktecknology.chenyu.androidwiiovision

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.io.File
import java.net.URI
import java.net.URL
import android.widget.TextView




/**
 * Created by chenyu on 27/03/2018.
 */
class RecyclerAdapter(val photoFils : Array<File>)  : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder{
        val inflatedView = parent.inflate(R.layout.recyclerview_item, false)
        return PhotoHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        holder.photoPath = photoFils[position].absolutePath
        holder.bindPhoto()
    }

    override fun getItemCount() = photoFils.size

    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private var view: View = v
        var imageView = v.imageView
        lateinit var photoPath:String

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
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
            Log.d("RecyclerView", "LongCLICK!")
            return true
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }

}