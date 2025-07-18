package com.example.autoclicker

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AutoClickService : AccessibilityService() {
    companion object {
        var instance: AutoClickService? = null
    }
    override fun onServiceConnected() { instance = this }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}
