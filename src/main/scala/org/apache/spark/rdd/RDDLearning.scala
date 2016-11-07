package org.apache.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by xubo on 2016/11/7.
  */
object RDDLearning {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("hello RDD")
    val sc = new SparkContext(conf)
    sc.makeRDD(Array(1, 2, 3)).foreach(println)
    sc.stop()

  }
}
