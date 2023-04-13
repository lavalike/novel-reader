package com.wangzhen.reader.utils

import java.io.File

/**
 * FileStack
 * Created by wangzhen on 2023/4/13
 */
class FileStack {
    private var node: Node? = null

    fun push(snapshot: FileSnapshot?) {
        if (snapshot == null) return
        val fileNode = Node()
        fileNode.fileSnapshot = snapshot
        fileNode.next = this.node
        this.node = fileNode
    }

    fun pop(): FileSnapshot? {
        val fileNode = node ?: return null
        node = fileNode.next
        return fileNode.fileSnapshot
    }

    inner class Node {
        var fileSnapshot: FileSnapshot? = null
        var next: Node? = null
    }

    class FileSnapshot {
        @JvmField
        var filePath: String? = null

        @JvmField
        var files: ArrayList<File>? = null

        @JvmField
        var scrollOffset = 0
    }
}