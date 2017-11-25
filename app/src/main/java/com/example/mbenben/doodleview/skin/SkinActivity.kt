package com.example.mbenben.doodleview.skin

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mbenben.doodleview.R
import kotlinx.android.synthetic.main.activity_skin.*

class SkinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin)

        setSupportActionBar(toolbar)
        if (supportActionBar!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        }

        navigation.setNavigationItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.skin_day -> {

                }

                R.id.skin_night -> {

                }

                R.id.skin_blue -> {

                }

                R.id.skin_green -> {

                }

                R.id.font_normal -> {

                }

                R.id.font_weiRuan -> {

                }
            }
            true
        }

        recyclerView.layoutManager=LinearLayoutManager(this)
        val list=MutableList<String>(50){
            index ->
            "item"+index
        }
        recyclerView.adapter=MAdapter(this,list)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> drawLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    inner class MAdapter(val context: Context,val stringList: MutableList<String>)
        : RecyclerView.Adapter<MAdapter.MViewHolder>() {

        override fun onBindViewHolder(holder: MViewHolder, position: Int) {
            holder.Text.text=stringList[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MViewHolder {
            return MViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false))
        }

        override fun getItemCount(): Int {
            return stringList.size
        }

        inner class MViewHolder(view :View):RecyclerView.ViewHolder(view){
            val Text:TextView=view.findViewById(R.id.itemText)
        }
    }
}
