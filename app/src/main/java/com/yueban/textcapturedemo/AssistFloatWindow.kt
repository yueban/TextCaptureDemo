package com.yueban.textcapturedemo

import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnFloatCallbacks

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class AssistFloatWindow private constructor() {
    fun open(floatWindowCallback: FloatWindowCallback) {
        EasyFloat.with(MyApp.context).setLayout(R.layout.layout_assist_float_window).setShowPattern(ShowPattern.ALL_TIME)
            .setSidePattern(SidePattern.RESULT_RIGHT)
            .setGravity(Gravity.END, 0, ScreenUtil.getScreenHeightPixels(MyApp.context) * 2 / 5).setAppFloatAnimator(null)
            .registerCallbacks(object : OnFloatCallbacks {
                override fun createdResult(
                    isCreated: Boolean, msg: String?, view: View?
                ) {
                    val logoView = view!!.findViewById<View>(R.id.ic_logo)
                    floatWindowCallback.onLogoWindowCreated(logoView)
                }

                override fun show(view: View) {}
                override fun hide(view: View) {}
                override fun dismiss() {}
                override fun touchEvent(view: View, event: MotionEvent) {}
                override fun drag(view: View, event: MotionEvent) {
                    floatWindowCallback.onDrag(view, event)
                }

                override fun dragEnd(view: View) {
                    floatWindowCallback.onDragEnd()
                }
            }).show()
    }

    fun openSelection(callback: SelectionFloatWindowCallback) {
        EasyFloat.with(MyApp.context).setTag(FLOAT_WINDOW_SELECTION).setLayout(R.layout.view_float_window_hierarchy)
            .setShowPattern(ShowPattern.ALL_TIME).setMatchParent(true, true).setAppFloatAnimator(null)
            .registerCallbacks(object : OnFloatCallbacks {
                override fun createdResult(
                    isCreated: Boolean, msg: String?, view: View?
                ) {
                    view?.apply {
                        val hierarchyView: HierarchyView = findViewById(R.id.hierarchy_view)
                        callback.onSelectionWindowCreated(hierarchyView)
                    }
                }

                override fun show(view: View) {}
                override fun hide(view: View) {}
                override fun dismiss() {}
                override fun touchEvent(view: View, event: MotionEvent) {}
                override fun drag(view: View, event: MotionEvent) {}
                override fun dragEnd(view: View) {}
            }).show()
    }

    fun show(tag: String?) {
        EasyFloat.showAppFloat(tag)
    }

    fun dismiss() {
        EasyFloat.dismissAppFloat()
    }

    fun dismiss(tag: String?) {
        EasyFloat.dismissAppFloat(tag)
    }

    fun isShowing(): Boolean {
        return EasyFloat.appFloatIsShow(null)
    }

    fun isShowing(tag: String?): Boolean {
        return EasyFloat.appFloatIsShow(tag)
    }

    companion object {
        const val FLOAT_WINDOW_SELECTION = "AssistSelectionWindow"
        val instance: AssistFloatWindow by lazy {
            AssistFloatWindow()
        }
    }
}
