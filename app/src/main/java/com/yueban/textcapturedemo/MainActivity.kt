package com.yueban.textcapturedemo

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkFloatWindowPermission()
    }

    private fun checkFloatWindowPermission() {
        if (PermissionUtils.checkPermission(this)) {
            checkAssistStatus()
            return
        }

        PermissionUtils.requestPermission(this, object : OnPermissionResult {
            override fun permissionResult(isOpen: Boolean) {
                if (isOpen) {
                    checkAssistStatus()
                } else {
                    Toast.makeText(this@MainActivity, "请给予悬浮窗权限", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun checkAssistStatus() {
        if (!AssistUtil.isAccessibilitySettingsOn(this, AssistService::class.java)) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }
}
