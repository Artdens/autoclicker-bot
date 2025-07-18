package com.example.autoclicker

import android.util.Log

object AutomationLoop {
    private var isRunning = false
    private const val WATCH_X = 1300
    private const val WATCH_Y = 1000

    fun start() {
        if (isRunning) return
        isRunning = true
        Thread {
            while (isRunning) {
                perform("Start Game", 1100, 900, 1000)
                Thread.sleep(15000)
                perform("Pause", 2200, 100, 1000)
                Thread.sleep(1000)
                perform("Exit Menu", 1400, 650, 1000)
                Thread.sleep(1000)
                perform("Confirm Exit", 1200, 800, 1000)
                Thread.sleep(6000)

                Thread.sleep(2000)
                val bmp = ScreenCaptureService.getLastBitmap()
                val hasAd = ImageAnalyzer.analyzeForAd(bmp)
                if (!hasAd) continue

                perform("Watch Ad", WATCH_X, WATCH_Y, 1000)
                Thread.sleep(30000)

                repeat(10) {
                    val img = ScreenCaptureService.getLastBitmap()
                    if (!ImageAnalyzer.analyzeForAd(img)) return@repeat
                    Thread.sleep(1000)
                }
                Thread.sleep(2000)
            }
        }.start()
    }

    fun stop() {
        isRunning = false
    }

    private fun perform(name: String, x: Int, y: Int, delay: Long) {
        Log.d("AutomationLoop", name)
        ClickController.tap(x, y)
        Thread.sleep(delay)
    }
}
