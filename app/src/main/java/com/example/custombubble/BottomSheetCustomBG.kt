package com.example.custombubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View

class BottomSheetCustomBG: View {

    ///////////////////////////////////////////////////////////////////////////////////////////////

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BottomSheetCustomBG, 0, 0
        ).apply {
            leftPadding =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_bottomSheetCustomBGLeftPadding, 0).toFloat()
            rightPadding =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_bottomSheetCustomBGRightPadding, 0).toFloat()
            bottomPadding =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_bottomSheetCustomBGBottomPadding, 0).toFloat()
            topPadding =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_bottomSheetCustomBGTopPadding, 0).toFloat()
            pointerWidth =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_pointerWidth, 0).toFloat()
            pointerHeight =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_pointerHeight, 0).toFloat()
            borderStrokeWidth =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_borderStrokeWidth, 0).toFloat()
            borderStrokeColor =
                getString(R.styleable.BottomSheetCustomBG_borderStrokeColor).toString()
            sideCornerRadius =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_sideCornerRadius, 4).toFloat()
            boxFillColor =
                getString(R.styleable.BottomSheetCustomBG_boxFillColor).toString()
            pointerMarginFromLeftSide =
                getDimensionPixelSize(R.styleable.BottomSheetCustomBG_pointerMarginFromLeft, 0).toFloat()
        }
        pointerInsideCurvatureRadius = pointerWidth/2F - pointerOutsideCurvatureRadius
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private var leftPadding: Float = 0F
    private var rightPadding: Float = 0F
    private var topPadding: Float = 0F
    private var bottomPadding: Float = 0F
    private var canvasWidth: Float = 0F
    private var canvasHeight: Float = 0F
    private var pointerWidth: Float = 0F
    private var pointerHeight: Float = 0F
    private var borderStrokeWidth: Float = 0F
    private var borderStrokeColor: String = ""
    private var boxFillColor: String = ""
    private var sideCornerRadius: Float = 0F
    private var backgroundXOrigin: Float = 0F
    private var backgroundYOrigin: Float = 0F
    private var backgroundHeight: Float = 0F
    private var backgroundWidth: Float = 0F
    private var pointerOutsideCurvatureRadius: Float = 0F
    private var pointerInsideCurvatureRadius: Float = 0F
    private var pointerMarginFromLeftSide: Float = 0F

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var borderStrokePaint: Paint
    private lateinit var boxFillPaint: Paint

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun getExactXCoordinate(value: Float): Float {
        Log.i("Point", "${backgroundXOrigin + value}")
        return backgroundXOrigin + value
    }
    private fun getExactYCoordinate(value: Float): Float {
        Log.i("Point", "${backgroundYOrigin - value}")
        return backgroundYOrigin - value
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private fun initializeMembers() {
        backgroundHeight = canvasHeight - topPadding - bottomPadding
        backgroundWidth = canvasWidth - leftPadding - rightPadding
        backgroundXOrigin = leftPadding
        backgroundYOrigin = canvasHeight - bottomPadding
        initializePaints()
    }

    private fun initializePaints() {
        borderStrokePaint = Paint()
        borderStrokePaint.isAntiAlias = true
        borderStrokePaint.strokeWidth = borderStrokeWidth
        borderStrokePaint.style = Paint.Style.STROKE
        borderStrokePaint.pathEffect = CornerPathEffect(sideCornerRadius)
        borderStrokePaint.color = Color.parseColor(borderStrokeColor)

//        boxFillPaint = Paint()
//        boxFillPaint.isAntiAlias = true
//        boxFillPaint.strokeWidth = borderStrokeWidth
//        boxFillPaint.style = Paint.Style.FILL_AND_STROKE
//        boxFillPaint.color = Color.parseColor(boxFillColor)
    }

    private fun drawUI(canvas: Canvas, paint: Paint) {
        val borderPath: Path = Path()
        
        borderPath.apply {
            reset()
            moveTo(getExactXCoordinate(0F), getExactYCoordinate(0F))
            lineTo(getExactXCoordinate(0F), getExactYCoordinate(backgroundHeight))
            lineTo(getExactXCoordinate(pointerMarginFromLeftSide), getExactYCoordinate(backgroundHeight))
            lineTo(getExactXCoordinate(pointerMarginFromLeftSide + pointerWidth/2F), getExactYCoordinate(backgroundHeight + pointerHeight))
            lineTo(getExactXCoordinate(pointerMarginFromLeftSide + pointerWidth), getExactYCoordinate(backgroundHeight))
            lineTo(getExactXCoordinate(backgroundWidth), getExactYCoordinate(backgroundHeight))
            lineTo(getExactXCoordinate(backgroundWidth), getExactYCoordinate(0F))
            lineTo(getExactXCoordinate(0F), getExactYCoordinate(0F))
            close()
        }
        canvas.drawPath(borderPath, paint)

        borderPath.reset()
        borderPath.apply {
            moveTo(
                getExactXCoordinate(backgroundHeight/2F),
                getExactYCoordinate(backgroundWidth/2F)
            )
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    fun reArrangePointer(value: Float) {
        pointerMarginFromLeftSide = value
        invalidate()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        initializeMembers()
        drawUI(canvas, borderStrokePaint)
    }
}