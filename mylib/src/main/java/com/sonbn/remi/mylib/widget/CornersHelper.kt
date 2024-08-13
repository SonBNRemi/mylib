package com.sonbn.remi.mylib.widget

import android.graphics.Canvas
import android.graphics.Path
import com.sonbn.remi.mylib.BuildConfig

class CornersHelper(cornersModel: CornersModel) {
    private val path = Path()
    private var mCornersModel: CornersModel

    init {
        this.mCornersModel = cornersModel
    }

    fun setCornersModel(cornersModel: CornersModel) {
        this.mCornersModel = cornersModel
    }

    fun draw(pair: Pair<Float, Float>, canvas: Canvas) {
        try {
            val width = pair.first
            val height = pair.second

            val radius = mCornersModel.radius
            val topLeft = mCornersModel.topLeft
            val topRight = mCornersModel.topRight
            val bottomLeft = mCornersModel.bottomLeft
            val bottomRight = mCornersModel.bottomRight

            path.reset()
            if (radius != 0f) {
                path.addRoundRect(0f, 0f, width, height, radius, radius, Path.Direction.CW)
            } else {
                path.addRoundRect(
                    0f, 0f, width, height,
                    floatArrayOf(
                        topLeft, topLeft, // Top-left corner
                        topRight, topRight, // Top-right corner
                        bottomRight, bottomRight, // Bottom-right corner
                        bottomLeft, bottomLeft // Bottom-left corner
                    ),
                    Path.Direction.CW
                )
            }
            canvas.clipPath(path)
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
}