package com.wang17.lib

import java.io.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.DateTimeException
import java.util.*
import kotlin.collections.ArrayList

class MyClass {
    class Person{
        var name=""
        var age=18
        var sex =1
        var birthday:DateTime

        constructor(name: String, age: Int) {
            this.name = name
            this.age = age
            this.birthday = DateTime()
        }
        constructor(name: String, age: Int,birthday:DateTime) {
            this.name = name
            this.age = age
            this.birthday = birthday
        }
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {


            val buddhaList:MutableList<BuddhaRecord> = ArrayList()
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,11,23,0),600000,1,11,""))
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,11,33,0),600000,1,11,""))
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,11,43,0),600000,1,11,""))
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,12,23,0),600000,1,11,""))
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,12,33,0),600000,1,11,""))
            buddhaList.add(BuddhaRecord(DateTime(2020,1,19,12,43,0),600000,1,11,""))
            val removeList: MutableList<BuddhaRecord> = ArrayList()
            var tmp: BuddhaRecord? = null
            buddhaList.forEach { buddha ->
                if (tmp != null) {
                    if (buddha.startTime.get(Calendar.DAY_OF_YEAR) != tmp!!.startTime.get(Calendar.DAY_OF_YEAR)) {
                        tmp = buddha
                    } else {
                        if (buddha.startTime.hour == tmp!!.startTime.hour) {
                            tmp!!.duration += buddha.duration
                            tmp!!.count += buddha.count
                            removeList.add(buddha)
                        } else {
                            tmp = buddha
                        }
                    }
                } else {
                    tmp = buddha
                }
            }
            buddhaList.removeAll(removeList)

            buddhaList.forEach {
                println("${it.startTime.toShortTimeString()}  ${it.duration}")
            }

            println()
            removeList.forEach {
                println("${it.startTime.toShortTimeString()}  ${it.duration}")
            }


//            var date1 = DateTime()
//            var date2 = date1.clone() as DateTime
//
//            date1.add(Calendar.DAY_OF_YEAR,3)
//
//            println(date1.toLongDateTimeString())
//            println(date2.toLongDateTimeString())

//            val date = DateTime()
//            var list:MutableList<Person> = ArrayList()
//            list.add(Person("zhang3",29,date))
//            list.add(Person("zhang5",25,date))
//            list.add(Person("zhang6",27,date))
//            list.add(Person("zhang8",21,date))
//            list.forEach {
//                println("name : ${it.name} age : ${it.age}")
//            }


//            for(year in 1900..2049){
//                var dt = DateTime(year,11,19)
//                println(dt.toLongDateTimeString())
//                var lunnar = Lunar(dt)
//                println("${lunnar.year}年 ${lunnar.monthStr} ${lunnar.dayStr}")
//            }



//            var dt = DateTime(2049,11,19)
//            println(dt.toLongDateTimeString())
//            var lunnar = Lunar(dt)
//            println("${lunnar.monthStr} ${lunnar.dayStr}")

//            dt = DateTime()
//            dt.timeZone= TimeZone.getTimeZone("America/Tijuana")
//            println(dt.toLongDateTimeString())
//            lunnar = Lunar(dt)
//            println("${lunnar.monthStr} ${lunnar.dayStr}")
        }

        private fun religiousTest() {
            val today = DateTime()
            val cc = loadJavaSolarTerms()
            println(cc.size)
            var re = Religious(today.year, today.month, today.day, cc, null)
            var a = re.religiousDays
            var b = re.remarks

            a?.forEach {
                println("${it.key.toLongDateTimeString()}  ${it.value}")
            }

            println("==============================================")
            b?.forEach {
                println("${it.key.toLongDateTimeString()}  ${it.value}")
            }
        }

        private fun testApplys(){
           var pp =  Person("赵五",33).apply {
                name="张三"
            }.apply {
                sex=0
            }

            println("${pp.name}  ${pp.age}  ${pp.sex}")


            pp =  Person("赵五",33).also {
                it.name="张三"
            }.also {
                it.sex=0
            }

            println("${pp.name}  ${pp.age}  ${pp.sex}")

        }
        /**
         * 读取JAVA结构的二进制节气数据文件。
         *
         * @param resId 待读取的JAVA二进制文件。
         * @return
         */
        @Throws(Exception::class)
        private fun loadJavaSolarTerms(): TreeMap<DateTime, SolarTerm> {
            val result = TreeMap<DateTime, SolarTerm>()
            try {
                val dis = DataInputStream(FileInputStream("d:\\solar_java_50.dat"))
                var date = dis.readLong()
                var solar = dis.readInt()
                try {
                    while (true) {
                        val cal = DateTime()
                        cal.timeInMillis = date
                        val solarTerm = SolarTerm.Int2SolarTerm(solar)
                        result[cal] = solarTerm
                        date = dis.readLong()
                        solar = dis.readInt()
                    }
                } catch (ex: EOFException) {
                    dis.close()
                }
            } catch (e: Exception) {
                println(e.message)
            }

            // 按照KEY排序TreeMap
//        TreeMap<DateTime, SolarTerm> result = new TreeMap<DateTime, SolarTerm>(new Comparator<DateTime>() {
//            @Override
//            public int compare(DateTime lhs, DateTime rhs) {
//                return lhs.compareTo(rhs);
//            }
//        });
            return result
        }

        private fun testListFind(){
            var person:MutableList<Person> = ArrayList()
            person.add(Person("赵五",33))
            person.add(Person("郑七",33))
            person.add(Person("张三",35))
            person.add(Person("李四",33))
            person.add(Person("王二",30))
            person.add(Person("吴六",23))
            person.add(Person("孙八",33))

            println("------------------ filter test --------------------")
            val aa = person.filter {
                it.age==33
            }
            aa.forEach {
                println("name : ${it.name} , age : ${it.age}")
            }

            println("------------------ takeWhile test --------------------")
            val bb = person.takeWhile {
                it.age==33
            }
            bb.forEach {
                println("name : ${it.name} , age : ${it.age}")
            }
        }

        private fun testBigDecimal() {
            println("----------------------------------------------------------------------------------------------------------------------------------------------------")
            println("在BigDecimal数值类型进行除法计算时，为了避免精度丢失，要预估除法结果小数位数，并将被除数设置decimal小数位数。")
            println("否则的话，被除数和除数都是整数，进行decimal数据转换时，会自动转换为精度为0的decimal类型，计算结果也会自动按照整数相除处理，处理的结果也是没有小数的四舍五入值。")
            println()
            var aaa = 1.000000001.toBigDecimal() / 3.toBigDecimal()
            println(aaa)
            aaa = 0.00001.toBigDecimal() + 1.01.toBigDecimal() / 3.toBigDecimal()
            println(aaa)
            println()

            println("----------------------------------------------------------------------------------------------------------------------------------------------------")
            println("用double.toBigDecimal()得到的BigDecimal数值，不存在精度丢失，但是小数位数只会精确到最后一个非0数字。")
            println()
            aaa = 1.000010.toBigDecimal()
            println(aaa - 1.toBigDecimal())
            println()

            /**
             *
             *
             */
            println("----------------------------------------------------------------------------------------------------------------------------------------------------")
            println("用BigDecimal(str:String)构造方法创建的BigDecimal数值，不会有精度的丢失")
            println("用BigDecimal(str:Double)构造方法创建的BigDecimal数值，有精度的丢失")
            println()
            aaa = BigDecimal("1.324003563")
            println(aaa - 1.toBigDecimal())
            aaa = BigDecimal(1.324003563)
            println(aaa - 1.toBigDecimal())
            println()

            println("----------------------------------------------------------------------------------------------------------------------------------------------------")
            println("自定义decimal带默认精度的转换方法")
            println()
            aaa = 1.toDecimal()
            println(aaa)
        }

        fun abc(callBack: CallBack) {
            callBack.execute()
        }

        fun getDltHttp(){
            _OkHttpUtil.getRequest("https://www.xinti.com/prizedetail/dlt.html") { html ->
                try {
                    var cc = html.substring(html.indexOf("<span class=\"iballs\">"))
                    cc = cc.substring(0, cc.indexOf("</span>"))
                    println(cc)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }

        /**
         * 大乐透
         */
        fun dlt() {
            val bonus = arrayOf(
                    arrayOf(5, 2, "一", 5000000),
                    arrayOf(5, 1, "二", 100000),
                    arrayOf(5, 0, "三", 10000),
                    arrayOf(4, 2, "四", 3000),
                    arrayOf(4, 1, "五", 300),
                    arrayOf(3, 2, "六", 200),
                    arrayOf(4, 0, "七", 100),
                    arrayOf(3, 1, "八", 15), arrayOf(2, 2, "八", 15),
                    arrayOf(3, 0, "九", 5), arrayOf(1, 2, "九", 5), arrayOf(2, 1, "九", 5), arrayOf(0, 2, "九", 5))
            val winLottery = arrayOf(intArrayOf(25, 28, 4, 35, 22), intArrayOf(4, 5))
            val winRedArr = winLottery[0]
            val winBlueArr = winLottery[1]

            val lotteryArr = arrayOf(
                    arrayOf(intArrayOf(25, 28, 4, 35, 22), intArrayOf(4, 10), 1),
                    arrayOf(intArrayOf(3, 12, 15, 23, 30), intArrayOf(11, 12), 1),
                    arrayOf(intArrayOf(4, 9, 10, 31, 33), intArrayOf(5, 12), 1),
                    arrayOf(intArrayOf(4, 6, 11, 18, 35), intArrayOf(6, 12), 1),
                    arrayOf(intArrayOf(4, 8, 10, 12, 35), intArrayOf(9, 12), 1))
            lotteryArr.forEach { lottery ->
                val redArr = lottery[0] as IntArray
                val blueArr = lottery[1] as IntArray
                val multiple = lottery[2] as Int

                var redWinCount = 0
                var blueWinCount = 0
                var redWinStr = StringBuffer()
                var blueWinStr = StringBuffer()
                redArr.forEach { num ->
                    if (winRedArr.contains(num)) {
                        redWinStr.append("$num ")
                        redWinCount++
                    }
                }
                redWinStr.append("+ ")
                blueArr.forEach { num ->
                    if (winBlueArr.contains(num)) {
                        blueWinStr.append("$num ")
                        blueWinCount++
                    }
                }
                bonus.forEach {
                    val rc = it[0] as Int
                    val bc = it[1] as Int
                    if (rc == redWinCount && bc == blueWinCount) {
                        println("${redWinStr}${blueWinStr}")
                        println("${it[2]}等奖 奖金${DecimalFormat("#,###").format((it[3] as Int) * multiple)}元")
                    }
                }
            }
        }

        /**
         * 双色球
         */
        fun ssq() {
            val bonus = arrayOf(
                    arrayOf(6, 1, "一", 5000000),
                    arrayOf(6, 0, "二", 100000),
                    arrayOf(5, 1, "三", 3000),
                    arrayOf(5, 0, "四", 200), arrayOf(4, 1, "四", 200),
                    arrayOf(4, 0, "五", 10), arrayOf(3, 1, "五", 10),
                    arrayOf(2, 1, "六", 5), arrayOf(1, 1, "六", 5), arrayOf(0, 1, "六", 5))
            val winLottery = arrayOf(intArrayOf(7, 9, 14, 26, 30, 31), 4)
            val winRedArr = winLottery[0] as IntArray
            val winBlue = winLottery[1] as Int
            var totalWin = 0
            var totalAward = 0

            val lotteryArr = arrayOf(
                    arrayOf(intArrayOf(3, 8, 12, 15, 23, 30), 11, 1),
                    arrayOf(intArrayOf(4, 8, 9, 10, 21, 31), 6, 1),
                    arrayOf(intArrayOf(4, 8, 6, 11, 18, 31), 10, 1),
                    arrayOf(intArrayOf(4, 8, 10, 12, 22, 30), 7, 1))
            lotteryArr.forEach { lottery ->
                val redArr = lottery[0] as IntArray
                val blue = lottery[1] as Int
                val multiple = lottery[2] as Int

                var redWinCount = 0
                var blueWinCount = 0
                var redWinStr = StringBuffer()
                var blueWinStr = StringBuffer()
                redArr.forEach { num ->
                    if (winRedArr.contains(num)) {
                        redWinStr.append("$num ")
                        redWinCount++
                    }
                }
                redWinStr.append("+ ")
                if (winBlue == blue) {
                    blueWinStr.append("$blue ")
                    blueWinCount++
                }


                bonus.forEach {
                    val rc = it[0] as Int
                    val bc = it[1] as Int
                    if (rc == redWinCount && bc == blueWinCount) {
                        val award = (it[3] as Int) * multiple
                        totalWin++
                        totalAward += award
                        println("${redWinStr}${blueWinStr}")
                        println("${it[2]}等奖 奖金${DecimalFormat("#,###").format(award)}元")
                    }
                }
            }
            println("中奖${totalWin}注，奖金${totalAward}元")
        }
    }
}
