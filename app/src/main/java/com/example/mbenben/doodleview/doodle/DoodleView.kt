package com.example.mbenben.doodleview.doodle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.File
import java.io.FileOutputStream
import android.view.View


/**
 * Created by MBENBEN on 2017/8/31.
 */

//会报空指针的错误，还没Debug出来
//learn from：http://www.jianshu.com/p/f05f71446771

class DoodleView : SurfaceView,SurfaceHolder.Callback {
    val TAG="DoodleView"

    private  val  DEFAULT_WIDTH=400
    private  val  DEFAULT_HEIGHT=400
    var mWidth=0
    var mHeight=0

    private val mPaint : Paint =Paint()

    private var mSurfaceHolder:SurfaceHolder = this.holder

    //画笔的粗细
    var currentSize=5
    //默认画笔为黑色
    var currentColor=Color.BLACK
    //当前所选画笔的形状
    var currentAction:BaseAction?=null

    enum class ActionType {
        Point,Path,Line,Rect,Circle,FillEcRect,FilledCircle
    }
    var mActionType=ActionType.Path

    val mBitmap:Bitmap by lazy {  Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888) }

    val mBaseActionList:MutableList<BaseAction> by lazy {
        mutableListOf<BaseAction>()
    }

    constructor(context: Context):
            super(context)

    constructor(context: Context, attrs: AttributeSet):
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    init {
        mSurfaceHolder.addCallback(this)
        this.isFocusable = true

        mPaint.color=Color.WHITE
        mPaint.strokeWidth= currentSize.toFloat()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        Log.d(TAG, "surfaceCreated ")
        val canvas=mSurfaceHolder.lockCanvas()
        Log.d(TAG+"creat", (canvas==null).toString())
        canvas.drawColor(Color.WHITE)
        mSurfaceHolder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action=event!!.action
        if (action==MotionEvent.ACTION_CANCEL){
            return false
        }

        val touchX=event.rawX
        val touchY=event.rawY

        when(action){
            MotionEvent.ACTION_DOWN ->
                    setCurrentAction(touchX,touchY)
            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, ":onTouchEvent ")

                val canvas=mSurfaceHolder.lockCanvas()
                Log.d(TAG, (canvas==null).toString())

                canvas.drawColor(Color.WHITE)
                for (i in mBaseActionList.listIterator()){
                    i.draw(canvas)
                }
                currentAction!!.move(touchX,touchY)
                currentAction!!.draw(canvas)
                mSurfaceHolder.unlockCanvasAndPost(canvas)
            }
            MotionEvent.ACTION_UP -> {
                mBaseActionList.add(currentAction!!)
                currentAction=null
            }
        }
        return true
    }

    private fun setCurrentAction(x:Float,y:Float){
        when(mActionType){
            ActionType.Point -> currentAction=MyPoint(x,y,currentColor)
            ActionType.Path -> currentAction=MyPath(x,y,currentSize,currentColor)
            ActionType.Line -> currentAction=MyLine(x,y,currentSize,currentColor)
            ActionType.Rect -> currentAction=MyRect(x,y,currentSize,currentColor)
            ActionType.Circle -> currentAction=MyCircle(x,y,currentSize,currentColor)
            ActionType.FillEcRect -> currentAction=MyFillRect(x,y,currentSize,currentColor)
            ActionType.FilledCircle -> currentAction=MyFillCircle(x,y,currentSize,currentColor)
        }
    }

    /**
     * 将当前画布转化成Bitmap
     */
    private fun getBitmap():Bitmap{
        val  canvas=Canvas(mBitmap)
        doDraw(canvas)
        return mBitmap
    }

    /**
     * 保存涂鸦后的图片
     */
    fun saveBitmap(doodleView: DoodleView):String{

        val path :String = Environment.getExternalStorageDirectory().absolutePath+
                "/doodleView/" + System.currentTimeMillis()+ ".png"
        if(!File(path).exists()){
            File(path).parentFile.mkdir()
        }
        savePicByPng(doodleView.getBitmap(),path)
        return path
    }

    /**
     * 将一个Bitmap保存在一个指定的路径中
     */
    fun savePicByPng(bitmap: Bitmap,filePath:String){
        val fileOutputStream = FileOutputStream (filePath)
        bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    /**
     * 开始进行绘图
     */
    private fun  doDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        for (i in mBaseActionList){
            i.draw(canvas)
        }
        canvas.drawBitmap(mBitmap,0F,0F,mPaint)
    }

    //返回上一步
    fun back():Boolean{
        if (mBaseActionList.isNotEmpty()){
            mBaseActionList.removeAt(mBaseActionList.size - 1)
            val canvas=mSurfaceHolder.lockCanvas()
            canvas.drawColor(Color.WHITE)
            for (i in mBaseActionList.iterator()){
                i.draw(canvas)
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas)
            return true
        }
        return false
    }

    fun reset(){
        if (mBaseActionList.isNotEmpty()){
            mBaseActionList.clear()
            val canvas=mSurfaceHolder.lockCanvas()
            canvas.drawColor(Color.WHITE)
            for ( i in mBaseActionList.iterator()){
                i.draw(canvas)
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = height
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == View.MeasureSpec.AT_MOST && heightSpecMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)
        } else if (widthSpecMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, heightSpecSize)
        } else if (heightSpecMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_HEIGHT)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        doDraw(canvas!!)
    }

}


