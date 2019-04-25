package com.de

import scala.io.Source
import java.io._
import scala.io.BufferedSource
import com.typesafe.config.ConfigFactory

object DEConfigGenerator {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val inputFile = config.getString("inputPath")
    val outputFile = config.getString("outputPath")
    val startTime = System.nanoTime
    val input = Source.fromFile(inputFile)
    val file = new File(outputFile)
    val pw = new BufferedWriter(new FileWriter(file, true))

    val addString = Constant.predefinedConfiguration

    pw.write(addString + "\n")

    for (line <- input.getLines()) {
      
      // Line checked for POST_EVENT_LIST and add generated line to config file
      if (line.contains(Constant.POST_EVENT_LIST)) {
        val eventValue = eventNumGenerator(line.split(Constant.TAB_DELIMITER)(7).substring(5).toInt)
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        pw.write("F.when(array_contains(split(col('post_event_list'),','),'" + eventValue + "'), '" + eventName + "').otherwise(0)," + "\n")
      }

      // Line checked for POST_EVAR26 and generate and add respective line in config file
      if (line.contains(Constant.POST_EVAR26)) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        val eventCondition = line.split(Constant.TAB_DELIMITER)(7)
        if (eventCondition.contains(" or ")) {
          val conditions = eventCondition.split("or")
          pw.write("F.when(")
          conditions.map(x => pw.write("(col('post_evar26').contains('" + x.trim() + "')) | "))
          pw.write(",'" + eventName + "').otherwise(0)," + "\n")
        } else if (eventCondition.contains('&')) {
          val conditions = eventCondition.split("&")
          pw.write("F.when(")
          conditions.map(x => pw.write("(col('post_evar26').contains('" + x.trim() + "')) & "))
          pw.write(",'" + eventName + "').otherwise(0)," + "\n")
        } else {
          pw.write("F.when((col('post_evar26') == '" + eventCondition + "'),'" + eventName + "').otherwise(0)," + "\n")
        }
      }
      
      // Line checked for POST_PROP1 and generate and add respective line in config file
      if (line.contains(Constant.POST_PROP1)) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        val eventCondition = line.split(Constant.TAB_DELIMITER)(7)
        if (eventCondition.contains("contains")) {
          val newCondition = eventCondition.substring(9)
          pw.write("F.when((col('post_prop1').contains('" + newCondition + "')),'" + eventName + "').otherwise(0)," + "\n")
        } else
          pw.write("F.when((col('post_prop1') == '" + eventCondition + "'),'" + eventName + "').otherwise(0)," + "\n")
      }

      // Line checked for POST_PROP28 and generate and add respective line in config file
      if (line.contains(Constant.POST_PROP28)) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        val eventCondition = line.split(Constant.TAB_DELIMITER)(7)
        if (eventCondition.contains("contains")) {
          val newCondition = eventCondition.substring(9)
          pw.write(("F.when((col('post_prop28').contains('" + newCondition + "')),'" + eventName + "').otherwise(0),").replaceAllLiterally("''", "'") + "\n")
        } else
          pw.write("F.when((col('post_prop28') == '" + eventCondition + "'),'" + eventName + "').otherwise(0)," + "\n")
      }
      
           // Line checked for POST_EVAR28 and generate and add respective line in config file
      if (line.contains(Constant.POST_EVAR28)) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        val eventCondition = line.split(Constant.TAB_DELIMITER)(7)
        if (eventCondition.contains("contains")) {
          val newCondition = eventCondition.substring(9)
          pw.write(("F.when((col('post_eVar28').contains('" + newCondition + "')),'" + eventName + "').otherwise(0),").replaceAllLiterally("''", "'") + "\n")
        } else
          pw.write("F.when((col('post_eVar28') == '" + eventCondition + "'),'" + eventName + "').otherwise(0)," + "\n")
      }

    }

    pw.close()

    val input1 = Source.fromFile(inputFile)
    
    val file1 = new File(outputFile)
    
    val bw = new BufferedWriter(new FileWriter(file1, true))

    val input2 = Source.fromFile(inputFile)
    
    val file2 = new File(outputFile)
    
    val bw2 = new BufferedWriter(new FileWriter(file2, true))

    bw2.write("       ]+ generic_events," + "\n")

    bw2.write(Constant.LEAD_FLAG)
    
    // method to add LeadFlag configuration in file
    addLeadFlag(input2, file2, bw2)
    
    bw2.write(") , 1).otherwise(0)," + "\n")
    
    bw2.close()
    
    bw.write(Constant.ENGAGED_FLAG)
    
    // Method to add Engaged flag configuration in file
    addEngageFlag(input1, file1, bw)
    
    bw.write("),1).otherwise(0)")
    
    bw.close
    
    println("File generated")
    
    val stopTime = System.nanoTime
    
    val delta = stopTime - startTime
    
    println("Time taken =:" + delta / 1000000d)
  }
  
  /**
   * Method to generate event number
   */

  def eventNumGenerator(eventNum: Int): String = {
    if (eventNum < 100)
      ("2" + (eventNum - 1).toString)
    else
      ("20" + (eventNum - 1).toString)
  }

  /**
   * Method to add Engage flag entries in config file
   */
  def addEngageFlag(input: BufferedSource, file: File, pw: BufferedWriter) = {
    for (line <- input.getLines()) {
      if (line.split(Constant.TAB_DELIMITER)(4) == Constant.ONE) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        pw.write("array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),'" + eventName + "') |" + "\n")
      }
    }
  }

  
  /**
   * Method to add Lead Flag entries in config file
   */
  def addLeadFlag(input: BufferedSource, file: File, pw: BufferedWriter) = {
    for (line <- input.getLines()) {
      if (line.split(Constant.TAB_DELIMITER)(3) == Constant.ONE) {
        val eventName = line.split(Constant.TAB_DELIMITER)(1)
        pw.write("array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),'" + eventName + "') |" + "\n")
      }
    }
  }
  
  /**
   * Method to calculate time taken in config file generation
   */
  def timer[A](blockOfCode: => A) = {
    val startTime = System.nanoTime
    val result = blockOfCode // the "block of code" you pass in is run here
    val stopTime = System.nanoTime
    val delta = stopTime - startTime
    (result, delta / 1000000d) // return the result and time as a Tuple

  }
}