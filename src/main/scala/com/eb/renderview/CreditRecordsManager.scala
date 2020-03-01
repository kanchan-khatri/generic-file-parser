package com.eb.renderview

import com.eb.renderview.models.CreditRecord
import com.eb.renderview.parsers.FileParser
import com.eb.renderview.utils.{FileHelper, FileTerms}
import org.slf4j.LoggerFactory

class CreditRecordsManager extends FileTerms {

  val title:String = "Credit Records"
  val outputFileName:String = "CreditRecords.html"
  lazy private val LOG = LoggerFactory.getLogger(getClass)

  def parseRecords(inputFiles: Array[String]): List[FileRecords] = {
    inputFiles.map((f) => (f, parseFile(f).getOrElse(null))).toList
  }

  def parseRecordsToHtml(inputFiles: Array[String], outputDir: String): Unit = {
    var outputfile = FileHelper.writeFile(outputDir+outputFileName,
      OutputRenderer.generateHtmlTable(parseRecords(inputFiles), title))
    LOG.info(s"Html File is generated at: file:///${outputfile}")
  }

  def parseFile(path: String): Option[List[CreditRecord]] = {
    FileParser(path).parse[CreditRecord]
  }

}