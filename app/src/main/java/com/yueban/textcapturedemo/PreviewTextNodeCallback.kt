package com.yueban.textcapturedemo

import android.view.accessibility.AccessibilityNodeInfo


/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/10
 */
interface PreviewTextNodeCallback {
    fun onFound(nodeInfo: AccessibilityNodeInfo?)
}