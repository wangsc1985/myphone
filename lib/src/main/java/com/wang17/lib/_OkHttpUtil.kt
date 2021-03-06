package com.wang17.lib

import com.alibaba.fastjson.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import sun.rmi.runtime.Log
import java.io.IOException

/**
 * @Description
 * @ClassName OkHttpClientUtil
 * @Author
 * @Copyright
 */
object _OkHttpUtil {

    @JvmField
    var client: OkHttpClient
    init {
        client = OkHttpClient()
    }

    @JvmStatic
    fun postRequestByJson(url: String?, args: List<PostArgument>, callback: HttpCallback) {
        try {
            //创建OkHttpClient对象。
            val client = client
            //创建表单请求体
            val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
            val json = JSONObject()
            for (arg in args) {
                json.put(arg.name, arg.value)
            }
            val requestBody: RequestBody = RequestBody.create(JSON, json.toString())
            val request = Request.Builder().url(url!!)
                    .post(requestBody) //传递请求体
                    .build()

            //new call
            val call = client!!.newCall(request)
            //请求加入调度
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        //回调的方法执行在子线程。
                        val htmlStr = response.body!!.string()
                        callback.excute(htmlStr)
                    }else{
                        callback.excute("xxxxxxxxxxx  response is not Successful......")
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun postRequest(url: String?, args: List<PostArgument>, callback: HttpCallback) {
        //创建OkHttpClient对象。
        val client = client
        //创建表单请求体
        val formBody = FormBody.Builder()
        //创建Request 对象。
        for (arg in args) {
            println("name : " + arg.name + " , value : " + arg.value)
            formBody.add(arg.name, arg.value)
        }
        val request = Request.Builder().url(url!!)
                .post(formBody.build()) //传递请求体
                .build()

        //new call
        val call = client!!.newCall(request)
        //请求加入调度
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    //回调的方法执行在子线程。
                    val htmlStr = response.body!!.string()
                    callback.excute(htmlStr)
                }else{
                    callback.excute("xxxxxxxxxxx  response is not Successful......")
                }
            }
        })
    }

    @JvmStatic
    fun getRequest(url: String?, callback: HttpCallback) {
        //创建okHttpClient对象
        val mOkHttpClient = client

        //创建一个Request
        val request = Request.Builder()
                .url(url!!)
                .build()
        //new call
        val call = mOkHttpClient!!.newCall(request)
        //请求加入调度
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                    callback.excute("xxxxxxxxxxx  ${e.message}")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    //回调的方法执行在子线程。
                    val htmlStr = response.body!!.string()
                    callback.excute(htmlStr)
                }else{
                    callback.excute("xxxxxxxxxxx  response is not Successful......")
                }
            }
        })
    }
}