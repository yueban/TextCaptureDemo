package com.yueban.textcapturedemo

import android.accessibilityservice.AccessibilityService
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.lzf.easyfloat.EasyFloat
import java.util.ArrayList

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-12
 */
class AssistDragDelegate private constructor(service: AccessibilityService) : SelectionFloatWindowCallback {
    /**
     * 存储当前屏幕内所有文本 node
     */
    private val mNodeInfos: MutableList<AccessibilityNodeInfo> = ArrayList()
    private val mService: AccessibilityService = service
    /**
     * 悬浮球
     */
    private var mLogoView: View? = null
    /**
     * 文本框高亮绘制
     */
    private var mHierarchyView: HierarchyView? = null
    /**
     * 正在拖拽
     */
    private var mIsDragging = false

    /**
     * 悬浮球创建完成回调
     *
     * @param logoView 悬浮球 view
     */
    fun onLogoWindowCreated(logoView: View?) {
        mLogoView = logoView
    }

    override fun onSelectionWindowCreated(hierarchyView: HierarchyView) {
        mHierarchyView = hierarchyView
    }

    /**
     * 拖拽进行中
     *
     * @param view 拖拽 view
     */
    fun onDrag(view: View) {
        if (!mIsDragging) {
            mNodeInfos.clear()
            parseRootNode()
            AssistFloatWindow.instance.openSelection(this)
            mIsDragging = true
        }
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val point = Point(location[0] + view.width / 2, location[1] + view.height / 2)
        val nodeInfo = captureNode(point)
        mHierarchyView?.setSelectedNode(nodeInfo, point)
    }

    /**
     * 拖拽停止
     */
    fun onDragEnd() {
        if (mIsDragging) {
            mHierarchyView?.getSelectedNode()?.let { executeSelectNode(it) }
            EasyFloat.dismissAppFloat(AssistFloatWindow.FLOAT_WINDOW_SELECTION)
            mIsDragging = false
            mHierarchyView = null
        }
    }

    /**
     * 捕获坐标点对应的 node
     *
     * @param point 坐标
     * @return 坐标对应的 node
     */
    private fun captureNode(point: Point): AccessibilityNodeInfo? {
        var targetNode: AccessibilityNodeInfo? = null
        for (item in mNodeInfos) {
            val itemRect = Rect()
            item.getBoundsInScreen(itemRect)
            if (!itemRect.contains(point.x, point.y)) {
                continue
            }
            if (targetNode == null) {
                targetNode = item
            } else {
                val targetRect = Rect()
                targetNode.getBoundsInScreen(targetRect)
                if (itemRect.width() < targetRect.width() && itemRect.height() < targetRect.height()) {
                    targetNode = item
                }
            }
        }
        return targetNode
    }

    /**
     * 解析屏幕 node 树，找到文本节点
     */
    private fun parseRootNode() {
        Thread(Runnable {
            AssistUtil.parseNodes(mService.rootInActiveWindow).apply {
                reverse()
                mNodeInfos.addAll(this)
            }
        }).start()
    }

    /**
     * 节点选中处理逻辑
     *
     * @param selectedNode 选中的节点
     */
    private fun executeSelectNode(selectedNode: AccessibilityNodeInfo) {
        val text = selectedNode.text
        Toast.makeText(MyApp.context, "text: $text", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(service: AccessibilityService): AssistDragDelegate {
            return AssistDragDelegate(service)
        }
    }
}