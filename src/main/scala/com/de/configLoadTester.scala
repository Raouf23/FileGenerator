package com.de

import com.typesafe.config.ConfigFactory
import scala.io.Source

object configLoadTester {

  def main(args: Array[String]): Unit = {
    println("HI")

    val config = ConfigFactory.load()

    val inputFile = config.getString("inputPath")
    println(inputFile)
    
    val fix= config.getConfig("config").getString("pedefinedConfiguration")
    println(fix)
    val input = Source.fromFile(inputFile)

  }
}