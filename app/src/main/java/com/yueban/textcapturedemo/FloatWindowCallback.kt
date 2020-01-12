package com.yueban.textcapturedemo

import android.view.MotionEvent
import android.view.View

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
interface FloatWindowCallback {
    fun onDrag(view: View, event: MotionEvent)
    fun onDragEnd()
    fun onLogoWindowCreated(logoView: View)
}