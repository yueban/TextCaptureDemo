package com.yueban.textcapturedemo

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import com.lzf.easyfloat.permission.PermissionUtils

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class AssistService : AccessibilityService(), FloatWindowCallback {
    private var mAssistDragDelegate: AssistDragDelegate? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        mAssistDragDelegate = AssistDragDelegate.newInstance(this)
        if (PermissionUtils.checkPermission(MyApp.context)) {
            if (!AssistFloatWindow.instance.isShowing()) {
                AssistFloatWindow.instance.open(this)
            }
        }
    }

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mAssistDragDelegate = null
        if (PermissionUtils.checkPermission(MyApp.context)) {
            AssistFloatWindow.instance.dismiss()
            AssistFloatWindow.instance.dismiss(AssistFloatWindow.FLOAT_WINDOW_SELECTION)
        }
        return super.onUnbind(intent)
    }

    override fun onDrag(view: View, event: MotionEvent) {
        mAssistDragDelegate?.onDrag(view)
    }

    override fun onDragEnd() {
        mAssistDragDelegate?.onDragEnd()
    }

    override fun onLogoWindowCreated(logoView: View) {
        mAssistDragDelegate?.onLogoWindowCreated(logoView)
    }
}
