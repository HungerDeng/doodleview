package com.example.mbenben.doodleview.doodle


import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import kotlin.properties.Delegates
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.Toast
import com.example.mbenben.doodleview.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG="MainActivity"
    var  a:Int by Delegates.observable(0){
        _,_,new ->
        Log.d(TAG, new.toString())
        if (new%10==0){
            Toast.makeText(this,new.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private var mColorDialog:AlertDialog ?=null
    private var mPaintDialog:AlertDialog ?=null
    private var mShapeDialog:AlertDialog ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dooleView.currentSize=5

        seekBar.max=100
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                a=p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //Toast.makeText(this@MainActivity, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

        }
        })

        img.setOnClickListener { dooleView.back() }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return dooleView.onTouchEvent(event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main_color -> showColorDialog()
            R.id.main_size -> showSizeDialog()
            R.id.main_action -> showShapeDialog()
            R.id.main_reset -> dooleView.reset()
            R.id.main_save -> {
                val path=dooleView.saveBitmap(dooleView)
                Log.d(TAG, "onOptionsItemSelected: "+path)
            }
        }
        return true
    }

    /**
     * 显示选择画笔颜色的对话框
     */
    private fun showColorDialog() {
        if (mColorDialog==null){
            mColorDialog=AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(arrayOf("红色","黄色","蓝色"),0)
                    { dialog, which ->
                        when(which){
                            0 -> dooleView.currentColor= Color.parseColor("#ff0000")
                            1 -> dooleView.currentColor=Color.parseColor("#FFFCF913")
                            2 -> dooleView.currentColor=Color.parseColor("#0000ff")
                        }
                        dialog.dismiss()
                    }.create()
        }
        mColorDialog!!.show()
    }

    /**
     * 显示选择画笔粗细的对话框
     */
    private fun showSizeDialog() {
        if (mPaintDialog==null){
            mPaintDialog=AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(arrayOf("细","中","粗"),0)
                    {dialog,which ->
                        when(which){
                            0 -> dooleView.currentSize=dp2px(5)
                            1 -> dooleView.currentSize=dp2px(10)
                            2 -> dooleView.currentSize=dp2px(15)
                        }
                        dialog.dismiss()
                    }.create()
        }
        mPaintDialog!!.show()
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private fun showShapeDialog() {
        if (mShapeDialog==null){
            mShapeDialog=AlertDialog.Builder(this)
                    .setTitle("选择画笔形状")
                    .setSingleChoiceItems(arrayOf("路径", "直线", "矩形", "圆形","实心矩形", "实心圆"),0)
                    { dialog,which ->
                        when(which){
                            0 -> dooleView.mActionType=DoodleView.ActionType.Path
                            1 -> dooleView.mActionType=DoodleView.ActionType.Line
                            2 -> dooleView.mActionType=DoodleView.ActionType.Rect
                            3 -> dooleView.mActionType=DoodleView.ActionType.Circle
                            4 -> dooleView.mActionType=DoodleView.ActionType.FillEcRect
                            5 -> dooleView.mActionType=DoodleView.ActionType.FilledCircle
                        }
                        dialog.dismiss()
                    }.create()
        }
        mShapeDialog!!.show()
    }

    private fun  dp2px(i: Int): Int {
        val scale=resources.displayMetrics.density
        return (i*scale+0.5f).toInt()
    }

}
