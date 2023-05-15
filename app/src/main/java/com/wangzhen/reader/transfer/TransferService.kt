package com.wangzhen.reader.transfer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.ByteBufferList
import com.koushikdutta.async.DataEmitter
import com.koushikdutta.async.callback.CompletedCallback
import com.koushikdutta.async.callback.DataCallback
import com.koushikdutta.async.http.body.AsyncHttpRequestBody
import com.koushikdutta.async.http.body.MultipartFormDataBody
import com.koushikdutta.async.http.body.MultipartFormDataBody.MultipartCallback
import com.koushikdutta.async.http.body.Part
import com.koushikdutta.async.http.body.UrlEncodedFormBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.wangzhen.reader.utils.AppConfig
import com.wangzhen.reader.utils.closeIO
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.DecimalFormat

/**
 * TransferService
 * Created by wangzhen on 2023/5/15
 */
class TransferService : Service() {
    private val fileUploadHolder = FileUploadHolder()
    private val server = AsyncHttpServer()
    private val asyncServer = AsyncServer()
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (AppConfig.Transfer.ACTION_START_WEB_SERVICE == action) {
            startServer()
        } else if (AppConfig.Transfer.ACTION_STOP_WEB_SERVICE == action) {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
        asyncServer.stop()
    }

    private fun startServer() {
        server.get("/images/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            sendResources(
                request, response
            )
        }
        server.get("/scripts/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            sendResources(
                request, response
            )
        }
        server.get("/css/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            sendResources(
                request, response
            )
        }
        //index page
        server.get("/") { _: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            try {
                response.send(indexContent())
            } catch (e: IOException) {
                e.printStackTrace()
                response.code(500).end()
            }
        }
        //query upload list
        server.get("/files") { _: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            val array = JSONArray()
            val dir = AppConfig.Transfer.DIR
            if (dir.exists() && dir.isDirectory) {
                val fileNames = dir.list()
                if (fileNames != null) {
                    for (fileName in fileNames) {
                        val file = File(dir, fileName)
                        if (file.exists() && file.isFile) {
                            try {
                                val jsonObject = JSONObject()
                                jsonObject.put("name", fileName)
                                val fileLen = file.length()
                                val df = DecimalFormat("0.00")
                                if (fileLen > 1024 * 1024) {
                                    jsonObject.put(
                                        "size",
                                        df.format((fileLen * 1f / 1024 / 1024).toDouble()) + "MB"
                                    )
                                } else if (fileLen > 1024) {
                                    jsonObject.put(
                                        "size", df.format((fileLen * 1f / 1024).toDouble()) + "KB"
                                    )
                                } else {
                                    jsonObject.put("size", fileLen.toString() + "B")
                                }
                                array.put(jsonObject)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
            response.send(array.toString())
        }
        //delete
        server.post("/files/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            val body = request.getBody<AsyncHttpRequestBody<*>>() as UrlEncodedFormBody
            if ("delete".equals(body.get().getString("_method"), ignoreCase = true)) {
                var path: String? = request.path.replace("/files/", "")
                try {
                    path = URLDecoder.decode(path, "utf-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                val file = File(AppConfig.Transfer.DIR, path)
                if (file.exists() && file.isFile) {
                    val ignored = file.delete()
                }
            }
            response.end()
        }
        //download
        server.get("/files/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            var path: String? = request.path.replace("/files/", "")
            try {
                path = URLDecoder.decode(path, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val file = File(AppConfig.Transfer.DIR_NAME, path)
            if (file.exists() && file.isFile) {
                try {
                    response.headers.add(
                        "Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(file.name, "utf-8")
                    )
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                response.sendFile(file)
            } else {
                response.code(404).send("Not found!")
            }
        }
        //upload
        server.post("/files") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            val body = request.getBody<AsyncHttpRequestBody<*>>() as MultipartFormDataBody
            body.multipartCallback = MultipartCallback { part: Part ->
                if (part.isFile) {
                    body.dataCallback = DataCallback { emitter: DataEmitter?, bb: ByteBufferList ->
                        fileUploadHolder.write(bb.allByteArray)
                        bb.recycle()
                    }
                } else {
                    if (body.dataCallback == null) {
                        body.dataCallback = DataCallback { _: DataEmitter, bb: ByteBufferList ->
                            try {
                                val fileName = URLDecoder.decode(String(bb.allByteArray), "UTF-8")
                                fileUploadHolder.setFileName(fileName)
                            } catch (e: UnsupportedEncodingException) {
                                e.printStackTrace()
                            }
                            bb.recycle()
                        }
                    }
                }
            }
            request.endCallback = CompletedCallback { e: Exception? ->
                fileUploadHolder.reset()
                response.end()
            }
        }
        server.get("/progress/.*") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            val res = JSONObject()
            val path = request.path.replace("/progress/", "")
            if (path == fileUploadHolder.getFileName()) {
                try {
                    res.put("fileName", fileUploadHolder.getFileName())
                    res.put("size", fileUploadHolder.getTotalSize())
                    res.put("progress", if (fileUploadHolder.fileOutPutStream == null) 1 else 0.1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            response.send(res)
        }
        server.listen(asyncServer, AppConfig.Transfer.HTTP_PORT)
    }

    private fun indexContent(): String {
        var stream: BufferedInputStream? = null
        return try {
            stream = BufferedInputStream(assets.open("wifi/index.html"))
            val baos = ByteArrayOutputStream()
            var len: Int
            val tmp = ByteArray(10240)
            while (stream.read(tmp).also { len = it } > 0) {
                baos.write(tmp, 0, len)
            }
            baos.toString("utf-8")
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } finally {
            stream?.closeIO()
        }
    }

    private fun sendResources(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        try {
            var fullPath = request.path
            fullPath = fullPath.replace("%20", " ")
            var resourceName = fullPath
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1)
            }
            if (resourceName.indexOf("?") > 0) {
                resourceName = resourceName.substring(0, resourceName.indexOf("?"))
            }
            if (!TextUtils.isEmpty(getContentTypeByResourceName(resourceName))) {
                response.setContentType(getContentTypeByResourceName(resourceName))
            }
            val bInputStream = BufferedInputStream(assets.open("wifi/$resourceName"))
            response.sendStream(bInputStream, bInputStream.available().toLong())
        } catch (e: IOException) {
            e.printStackTrace()
            response.code(404).end()
        }
    }

    private fun getContentTypeByResourceName(resourceName: String): String {
        if (resourceName.endsWith(".css")) {
            return AppConfig.ContentType.CSS_CONTENT_TYPE
        } else if (resourceName.endsWith(".js")) {
            return AppConfig.ContentType.JS_CONTENT_TYPE
        } else if (resourceName.endsWith(".swf")) {
            return AppConfig.ContentType.SWF_CONTENT_TYPE
        } else if (resourceName.endsWith(".png")) {
            return AppConfig.ContentType.PNG_CONTENT_TYPE
        } else if (resourceName.endsWith(".jpg") || resourceName.endsWith(".jpeg")) {
            return AppConfig.ContentType.JPG_CONTENT_TYPE
        } else if (resourceName.endsWith(".woff")) {
            return AppConfig.ContentType.WOFF_CONTENT_TYPE
        } else if (resourceName.endsWith(".ttf")) {
            return AppConfig.ContentType.TTF_CONTENT_TYPE
        } else if (resourceName.endsWith(".svg")) {
            return AppConfig.ContentType.SVG_CONTENT_TYPE
        } else if (resourceName.endsWith(".eot")) {
            return AppConfig.ContentType.EOT_CONTENT_TYPE
        } else if (resourceName.endsWith(".mp3")) {
            return AppConfig.ContentType.MP3_CONTENT_TYPE
        } else if (resourceName.endsWith(".mp4")) {
            return AppConfig.ContentType.MP4_CONTENT_TYPE
        }
        return ""
    }

    class FileUploadHolder {
        private lateinit var fileName: String

        var fileOutPutStream: BufferedOutputStream? = null
            private set
        private var totalSize: Long = 0

        fun getTotalSize() = this.totalSize

        fun getFileName() = this.fileName

        fun setFileName(fileName: String) {
            this.fileName = fileName
            totalSize = 0
            val dir = AppConfig.Transfer.DIR
            if (!dir.exists()) {
                val ignored = dir.mkdirs()
            }
            val receivedFile = File(dir, this.fileName)
            try {
                fileOutPutStream = BufferedOutputStream(FileOutputStream(receivedFile))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        fun reset() {
            if (fileOutPutStream != null) {
                try {
                    fileOutPutStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            fileOutPutStream = null
        }

        fun write(data: ByteArray) {
            if (fileOutPutStream != null) {
                try {
                    fileOutPutStream!!.write(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            totalSize += data.size.toLong()
        }
    }

    companion object {
        fun start(context: Context) {
            context.startService(Intent(context, TransferService::class.java).apply {
                action = AppConfig.Transfer.ACTION_START_WEB_SERVICE
            })
        }

        fun stop(context: Context) {
            context.startService(Intent(context, TransferService::class.java).apply {
                action = AppConfig.Transfer.ACTION_STOP_WEB_SERVICE
            })
        }
    }
}