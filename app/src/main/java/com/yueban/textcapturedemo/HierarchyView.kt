package com.yueban.textcapturedemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class HierarchyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val strokePaint = Paint()
    private val contentPaint = Paint()
    private var selectedNode: AccessibilityNodeInfo? = null
    private var dragPoint: Point? = null

    constructor(context: Context) : this(context, null)

    init {
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2F
        contentPaint.color = Color.argb(15, 0, 0, 0)
    }

    fun getSelectedNode(): AccessibilityNodeInfo? = selectedNode

    fun setSelectedNode(
            selectedNode: AccessibilityNodeInfo?, dragPoint: Point
    ) {
        this.selectedNode = selectedNode
        this.dragPoint = dragPoint
        visibility = VISIBLE
        selectedNode?.let {
            if (AssistUtil.isWechatMsgNode(it)) {
                strokePaint.color = Color.GREEN
            } else {
                strokePaint.color = Color.RED
            }
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        drawWidget(canvas, selectedNode)
    }

    private fun drawWidget(canvas: Canvas, node: AccessibilityNodeInfo?) {
        node?.let {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            val statusBarHeight = getStatusBarHeight()
            bounds.top -= statusBarHeight
            bounds.bottom -= statusBarHeight
            bounds.let {
                canvas.drawRect(it, contentPaint)
                canvas.drawRect(it, strokePaint)
            }
        }
    }

    private fun getStatusBarHeight(): Int {
        val arr = IntArray(2)
        getLocationOnScreen(arr)
        return arr[1]
    }
}