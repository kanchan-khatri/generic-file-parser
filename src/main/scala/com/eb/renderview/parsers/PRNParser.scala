package com.eb.renderview.parsers

import scala.reflect.runtime.universe.TypeTag

class PRNParser(path: String) extends FileParser(path: String){
  val sparkFormat: String = "com.databricks.spark.csv"
  def getParserOptions():Map[String,String] = {
    val Options = Map(
      "header" -> "true",
      "inferSchema" -> "true",
      "format" -> "com.databricks.spark.csv",
      "encoding"->"UTF-8"
    )
    Options
  }
//  CreateDF
//  Get File Def - > map -> colum - start and end position
//  PArseFile - > line by lin and extract tosting
  override def parse[T <: Product : TypeTag](): Option[List[T]] = {
    //Logic to parse prn text file
    formatDFRow[T](createDataFrame[T].get)
  }

}
