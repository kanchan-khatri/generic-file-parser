package com.eb.renderview.parsers

class CSVParser(path: String) extends FileParser(path: String){

  val sparkFormat: String = "com.databricks.spark.csv"

  def getParserOptions():Map[String,String] = {
    // Options
    Map (
      "header" -> "true",
      "inferSchema" -> "true",
      "format" -> "com.databricks.spark.csv",
      "delimiter"->",",
      "quote"->"\"",
      "escape"-> "\"",
      "ignoreLeadingWhiteSpace"->"true",
      "ignoreTrailingWhiteSpace"->"true",
      "encoding"->"UTF-8"
    )
  }
}
