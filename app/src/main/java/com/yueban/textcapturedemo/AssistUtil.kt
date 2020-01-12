package com.yueban.textcapturedemo

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.text.TextUtils.SimpleStringSplitter
import android.view.accessibility.AccessibilityNodeInfo
import java.util.ArrayList

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
object AssistUtil {
    fun parseNodes(nodeInfo: AccessibilityNodeInfo): MutableList<AccessibilityNodeInfo> {
        val result: MutableList<AccessibilityNodeInfo> = ArrayList()
        iterateNodes(nodeInfo, result)
        return result
    }

    private fun iterateNodes(
        nodeInfo: AccessibilityNodeInfo, result: MutableList<AccessibilityNodeInfo>
    ) {
        for (i in 0 until nodeInfo.childCount) {
            val childNode = nodeInfo.getChild(i)
            childNode?.let {
                if (!TextUtils.isEmpty(it.text)) {
                    result.add(childNode)
                }
                iterateNodes(it, result)
            }
        }
    }

    fun isAccessibilitySettingsOn(
        mContext: Context, clazz: Class<out AccessibilityService?>
    ): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.packageName + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
