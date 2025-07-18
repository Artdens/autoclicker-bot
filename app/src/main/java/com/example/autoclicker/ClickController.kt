package com.example.autoclicker

import android.graphics.Path
import android.view.accessibility.GestureDescription

object ClickController {
    fun tap(x: Int, y: Int) {
        val path = Path().apply { moveTo(x.toFloat(), y.toFloat()) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        AutoClickService.instance?.dispatchGesture(gesture, null, null)
    }
}
