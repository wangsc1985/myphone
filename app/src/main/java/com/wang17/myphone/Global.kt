package com.wang17.myphone

import android.text.TextUtils
import android.util.Log
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun e(data: Any) {
    Log.e("wangsc", data.toString())
}
fun Double.format(precision:Int):String{
    e(this)
    when(precision){
        0->{
            val format = DecimalFormat("0")
            return format.format(this)
        }
        1->{
            val format = DecimalFormat("0.0")
            return format.format(this)
        }
        2->{
            val format = DecimalFormat("0.00")
            return format.format(this)
        }
        else->{
            val format = DecimalFormat("0")
            return format.format(this)
        }
    }
}

fun Double.formatCNY(precision:Int):String{
    when(precision){
        0->{
            val format = DecimalFormat("#,##0")
            return format.format(this)
        }
        1->{
            val format = DecimalFormat("#,##0.0")
            return format.format(this)
        }
        2->{
            val format = DecimalFormat("#,##0.00")
            return format.format(this)
        }
        else->{
            val format = DecimalFormat("#,##0")
            return format.format(this)
        }
    }
}
fun Float.formatCNY(precision:Int):String{
    return this.toDouble().formatCNY(precision)
}
fun Float.format(precision:Int):String{
    return this.toDouble().format(precision)
}
fun Int.formatCNY():String{
    return this.toDouble().formatCNY(0)
}

val scale = 8
val roundMode = BigDecimal.ROUND_DOWN
fun Int.toMyDecimal():BigDecimal{
    return this.toBigDecimal().setScale(scale,roundMode)
}

fun Double.toMyDecimal():BigDecimal{
    return this.toBigDecimal().setScale(scale,roundMode)
}
fun Float.toMyDecimal():BigDecimal{
    return this.toBigDecimal().setScale(scale,roundMode)
}
fun Long.toMyDecimal():BigDecimal{
    return this.toBigDecimal().setScale(scale,roundMode)
}
fun String.toMyDecimal():BigDecimal{
    return this.toBigDecimal().setScale(scale,roundMode)
}
fun BigDecimal.setMyScale():BigDecimal{
    return this.setScale(scale,roundMode)
}

fun String.Companion.concat(vararg args:Any):String{
    var sb=StringBuffer()
    for(str in args){
        sb.append(str.toString())
    }
    return sb.toString()
}

/**
 * 根据timeInMillis格式化时间，默认格式"yyyy-MM-dd HH:mm:ss"。
 * @param timeInMillis
 * @param strPattern
 * @return
 */
fun String.Companion.formatUTC(timeInMillis: Long, strPattern: String?): String {
    var strPattern: String? = strPattern
    if (TextUtils.isEmpty(strPattern)) {
        strPattern = "yyyy-MM-dd HH:mm:ss"
    }
    var sdf = SimpleDateFormat(strPattern, Locale.CHINA)
    sdf.applyPattern(strPattern)
    return if (sdf == null) "NULL" else sdf.format(timeInMillis)
}

fun String.formatToCardNumber(): String {
    var num: String = ""
    var index: Int = this.length - 1
    for (i in 0 until this.length) {
        index = this.length - 1 - i
        if (i % 4 == 0 && i != 0) {
            num = " " + num
        }
        num = this.get(index).toString() + num
    }
    return num
}
