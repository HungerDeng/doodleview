package com.example.mbenben.doodleview.tangram

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.example.mbenben.doodleview.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * Created by MBENBEN on 2017/9/1.
 * 设置Adapter
 * 方式1：继承 自 DelegateAdapter
 * 方式2：继承 自 VirtualLayoutAdapter
 */
open class MyAdapter(val context: Context,val layoutHelper: LayoutHelper,val count:Int,
                val listItem :ArrayList< HashMap<String,Any> >,
                val layoutParams:RecyclerView.LayoutParams=RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300))
    : DelegateAdapter.Adapter<MyAdapter.ViewHolder>() {

    //用于设置Item的点击事件
    var listener: ItemClickListener? = null

    override fun onCreateLayoutHelper(): LayoutHelper {
        return layoutHelper
    }

    //绑定Item的数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text=listItem[position]["ItemTitle"] as String
        holder.image.setImageResource(listItem[position]["ItemImage"] as Int)
    }

    //把ViewHolder绑定Item布局
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return count
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val text:TextView = itemView.findViewById(R.id.itemText)
        val image:ImageView = itemView.findViewById(R.id.itemImage)

        init {
            itemView.setOnClickListener {
                view ->
                /*if (listener!=null) listener!!.onItemClick(view,position)*/
                if (aa!=null)
                aa!!(view,position)
            }
        }
    }

    interface ItemClickListener{
        fun onItemClick(view:View,position:Int)
    }

    var aa :((View,Int)-> Unit)? = null
    fun setItemClickListener( aa : (View,Int)-> Unit){
        this.aa = aa
    }

}