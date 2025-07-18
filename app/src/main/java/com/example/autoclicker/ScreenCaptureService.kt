package com.example.autoclicker

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.DisplayManager
import android.view.Surface

class ScreenCaptureService : Service() {
    companion object {
        private var lastBitmap: Bitmap? = null
        fun getLastBitmap(): Bitmap? = lastBitmap
    }

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var imageReader: ImageReader? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultCode = intent?.getIntExtra("RESULT_CODE", -1) ?: return START_NOT_STICKY
        val data = intent.getParcelableExtra<Intent>("DATA") ?: return START_NOT_STICKY
        mediaProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)

        val metrics = resources.displayMetrics
        imageReader = ImageReader.newInstance(2400, 1080, PixelFormat.RGBA_8888, 2)
        mediaProjection?.createVirtualDisplay(
            "ScreenCap", 2400, 1080, metrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader!!.surface, null, null
        )

        Thread {
            while (true) {
                val img: Image = imageReader!!.acquireLatestImage() ?: continue
                val plane = img.planes[0]
                val buffer = plane.buffer
                val rowStride = plane.rowStride
                val pixelStride = plane.pixelStride
                val bitmap = Bitmap.createBitmap(
                    2400 + (rowStride - pixelStride * 2400) / pixelStride,
                    1080, Bitmap.Config.ARGB_8888
                )
                bitmap.copyPixelsFromBuffer(buffer)
                img.close()

                val crop = Bitmap.createBitmap(bitmap, 2100, 0, 300, 300)
                lastBitmap = crop
                ImageAnalyzer.initIfNeeded()
                Thread.sleep(1000)
            }
        }.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
