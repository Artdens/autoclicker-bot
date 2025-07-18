package com.example.autoclicker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

object ImageAnalyzer {
    private lateinit var templateX: Bitmap
    private lateinit var templateArrow: Bitmap

    fun initIfNeeded() {
        if (::templateX.isInitialized) return
        val res = MyApp.instance.resources
        templateX = BitmapFactory.decodeResource(res, R.drawable.template_x)
        templateArrow = BitmapFactory.decodeResource(res, R.drawable.template_arrow)
    }

    fun analyzeForAd(bmp: Bitmap?): Boolean {
        if (bmp == null) return false
        val mat = Mat()
        Utils.bitmapToMat(bmp, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2GRAY)
        val tX = Mat(); Utils.bitmapToMat(templateX, tX); Imgproc.cvtColor(tX, tX, Imgproc.COLOR_RGBA2GRAY)
        val tA = Mat(); Utils.bitmapToMat(templateArrow, tA); Imgproc.cvtColor(tA, tA, Imgproc.COLOR_RGBA2GRAY)

        fun match(t: Mat): Boolean {
            val res = Mat()
            Imgproc.matchTemplate(mat, t, res, Imgproc.TM_CCOEFF_NORMED)
            val mmr = Core.minMaxLoc(res)
            Log.d("ImageAnalyzer", "match val: ${mmr.maxVal}")
            return mmr.maxVal >= 0.8
        }

        return match(tX) || match(tA)
    }
}
