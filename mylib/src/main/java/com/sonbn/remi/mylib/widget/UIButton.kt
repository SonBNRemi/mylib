package com.sonbn.remi.mylib.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.sonbn.remi.mylib.R

class UIButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatButton(context, attrs) {
    private var cornersHelper: CornersHelper

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIButton, 0, 0)
        val radius = typedArray.getDimension(R.styleable.UIButton_android_radius, 0f)
        val topLeft = typedArray.getDimension(R.styleable.UIButton_android_topLeftRadius, 0f)
        val topRight = typedArray.getDimension(R.styleable.UIButton_android_topRightRadius, 0f)
        val bottomLeft = typedArray.getDimension(R.styleable.UIButton_android_bottomLeftRadius, 0f)
        val bottomRight =
            typedArray.getDimension(R.styleable.UIButton_android_bottomRightRadius, 0f)
        typedArray.recycle()

        val cornersModel = CornersModel(radius, topLeft, topRight, bottomLeft, bottomRight)
        cornersHelper = CornersHelper(cornersModel)
    }

    override fun draw(canvas: Canvas) {
        cornersHelper.draw(Pair(width.toFloat(), height.toFloat()), canvas)
        super.draw(canvas)
    }

    fun setCorners(cornersModel: CornersModel) {
        cornersHelper.setCornersModel(cornersModel)
        invalidate()
    }
}