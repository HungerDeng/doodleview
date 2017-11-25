package com.example.mbenben.doodleview.doodle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.properties.Delegates

/**
 * Created by MBENBEN on 2017/8/31.
 */
abstract class BaseAction (var color: Int  =Color.WHITE) {

    abstract fun draw(canvas: Canvas)

    abstract fun move(mx:Float,my:Float)
}

class MyPoint (private val px:Float,private val py:Float,color: Int):BaseAction(color){

    override fun draw(canvas: Canvas) {
        val paint=Paint()
        paint.color=color
        canvas.drawPoint(px,py, paint)
    }

    override fun move(mx: Float, my: Float) {
    }
}

class MyPath:BaseAction{

    private var mPath by Delegates.notNull<Path>()
    private var size=1

    constructor(){
        mPath=Path()
    }

    constructor(mx: Float,my: Float,size:Int,color: Int):super(color){
        mPath=Path()
        this.size=size
        mPath.moveTo(mx,my)
        mPath.lineTo(mx,my)
    }

    override fun draw(canvas: Canvas) {
        val paint=Paint()
        paint.isAntiAlias=true
        paint.isDither=true
        paint.color= color
        paint.strokeWidth=size.toFloat()
        paint.style=Paint.Style.STROKE
        paint.strokeJoin=Paint.Join.ROUND
        paint.strokeCap=Paint.Cap.ROUND
        canvas.drawPath(mPath,paint)
    }

    override fun move(mx: Float, my: Float) {
        mPath.lineTo(mx,my)
    }

}

class MyLine:BaseAction{

    private var startX:Float=0F
    private var startY:Float=0F
    private var stopX:Float=0F
    private var stopY:Float=0F
    private var size=0
    constructor(){

    }

    constructor(x:Float,y:Float,size: Int,color: Int):super(color){
        startX=x
        startY=y
        stopX=x
        stopY=y
        this.size=size
    }

    override fun draw(canvas: Canvas) {
        val paint=Paint()
        paint.isAntiAlias=true
        paint.style=Paint.Style.STROKE
        paint.color=color
        paint.strokeWidth=size.toFloat()
        canvas.drawLine(startX,startY,stopX,stopY,paint)
    }

    override fun move(mx: Float, my: Float) {
        stopX=mx
        stopY=my
    }
}


/**
 * 方框
 */
internal class MyRect : BaseAction {
    private var startX= 0F
    private var startY= 0F
    private var stopX= 0F
    private var stopY = 0F
    private var size: Int=0

    constructor() {
        this.startX = 0f
        this.startY = 0f
        this.stopX = 0f
        this.stopY = 0f
    }

    constructor(x: Float, y: Float, size: Int, color: Int) : super(color) {
        this.startX = x
        this.startY = y
        this.stopX = x
        this.stopY = y
        this.size = size
    }

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = size.toFloat()
        canvas.drawRect(startX, startY, stopX, stopY, paint)
    }

    override fun move(mx: Float, my: Float) {
        stopX = mx
        stopY = my
    }
}

/**
 * 圆框
 */
internal class MyCircle : BaseAction {
    private var startX= 0F
    private var startY= 0F
    private var stopX= 0F
    private var stopY = 0F
    private var radius=0F
    private var size: Int=0

    constructor() {
        startX = 0f
        startY = 0f
        stopX = 0f
        stopY = 0f
        radius = 0f
    }

    constructor(x: Float, y: Float, size: Int, color: Int) : super(color) {
        this.startX = x
        this.startY = y
        this.stopX = x
        this.stopY = y
        this.radius = 0f
        this.size = size
    }

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = size.toFloat()
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint)
    }

    override fun move(mx: Float, my: Float) {
        stopX = mx
        stopY = my
        radius = (Math.sqrt(((mx - startX) * (mx - startX) + (my - startY) * (my - startY)).toDouble()) / 2).toFloat()
    }
}

internal class MyFillRect : BaseAction {
    private var startX= 0F
    private var startY= 0F
    private var stopX= 0F
    private var stopY = 0F
    private var size: Int=0

    constructor() {
        this.startX = 0f
        this.startY = 0f
        this.stopX = 0f
        this.stopY = 0f
    }

    constructor(x: Float, y: Float, size: Int, color: Int) : super(color) {
        this.startX = x
        this.startY = y
        this.stopX = x
        this.stopY = y
        this.size = size
    }

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = color
        paint.strokeWidth = size.toFloat()
        canvas.drawRect(startX, startY, stopX, stopY, paint)
    }

    override fun move(mx: Float, my: Float) {
        stopX = mx
        stopY = my
    }
}

/**
 * 圆饼
 */
internal class MyFillCircle(private val startX: Float, private val startY: Float, private val size: Int, color: Int) : BaseAction(color) {
    private var stopX: Float = 0.toFloat()
    private var stopY: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()


    init {
        this.stopX = startX
        this.stopY = startY
        this.radius = 0f
    }

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = color
        paint.strokeWidth = size.toFloat()
        canvas.drawCircle((startX + stopX) / 2, (startX + stopY) / 2, radius, paint)
    }

    override fun move(mx: Float, my: Float) {
        stopX = mx
        stopY = my
        radius = (Math.sqrt(((mx - startX) * (mx - startX) + (my - startY) * (my - startY)).toDouble()) / 2).toFloat()
    }
}



