package com.eb.renderview.parsers

import com.eb.renderview.models.CreditRecord
import org.apache.spark.sql.{AnalysisException, Dataset}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.slf4j.LoggerFactory
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe.TypeTag

class PRNParser(path: String) extends FileParser(path: String){

  val sparkFormat: String = "com.databricks.spark.text"

  lazy private val LOG = LoggerFactory.getLogger(getClass)

  def getParserOptions():Map[String,String] = {
    val Options = Map(
      "header" -> "true"
    )
    Options
  }

  /** fn: createParsingStructureForPRN
    * Gets Column Structure,
    * Parses each line with defined structure and forms Array
    * Formation is mapped to defined generic record i.e., CreditRecord
    * @param inputDF Dataset[String] from loading textFile with spark session
    * @param schemaStructToUse Defined StructType to transform the dataset
    * @return List of defined generic record. in this case, CreditRecord
    */
  def createParsingStructureForPRN[T <: Product : TypeTag](inputDF: Dataset[String], schemaStructToUse: StructType):
      Option[List[T]] = {
    try {
      var x = 2

      var y = if(x>1) 3 else 4

      var columnNamesAndTheirLengths = collection.mutable.LinkedHashMap[String, Int]()
      val outputDF: mutable.ArraySeq[T] = inputDF.collect().zipWithIndex.map {
        case (line, index) =>
          //Evaluates Columns from 1st line
          if (index == 0) {
            columnNamesAndTheirLengths = parseFirstLine(line)
          }
          var arrOfColumns: ArrayBuffer[String] = ArrayBuffer()
          var cIndex = 0
          for (field <- schemaStructToUse.fields) {
            arrOfColumns += line.substring(cIndex,
              cIndex + columnNamesAndTheirLengths(field.name.toLowerCase)
            ).trim
            cIndex = cIndex + columnNamesAndTheirLengths(field.name.toLowerCase())
          }
          CreditRecord(arrOfColumns(0), arrOfColumns(1), arrOfColumns(2), arrOfColumns(3),
            arrOfColumns(4), arrOfColumns(5)).asInstanceOf[T]
          // arrOfColumns.asInstanceOf[T]
      }
      return Some(outputDF.toList.drop(1))
    } catch {
      case e: Exception =>
        LOG.error("Exception occured in parsing prn file. Skipping File: "+ path)
        None
    }
  }

  def split[T](list: List[T]) : List[List[T]] = list match {
    case Nil => Nil
    case h::t => val segment = list takeWhile {h ==}
      segment :: split(list drop segment.length)
  }

  /** fn: parseFirstLine
    * Evaluates columns from 1st line of prn file and returns column name with string
    * Each Column in prn is having fixed characters. parseFirstLine evaluates the same.
    * @param line String with spaces
    * @return mutable.LinkedHashMap[String,Int] -> name, 16 mean name column has 16 chars defined
    */
  def parseFirstLine(line:String): mutable.LinkedHashMap[String, Int] =
  {
    var lineForParsing = line.replace("Credit Limit", "Credit_Limit")
    val allColumnsNames:List[String] = lineForParsing.split(" ").
      filterNot(_.forall(_.isWhitespace)).toList
    var columnsNamesAndTheirLength = collection.mutable.LinkedHashMap[String, Int]()
    for ((columnName,i) <- allColumnsNames.view.zipWithIndex)
    {
      if (i == allColumnsNames.length-1)
      {
        columnsNamesAndTheirLength += (lineForParsing.trim.toLowerCase() -> lineForParsing.length)
      }
      else {
        val lengthOfSpacesAfterFirstColumn = split(lineForParsing.toCharArray.toList)
          .filter(x => x.head.equals(' ') && x.size > 0)
          .map(y => y.length).distinct.head

        val firstColumn = lineForParsing.substring(0, lengthOfSpacesAfterFirstColumn + columnName.length())
        columnsNamesAndTheirLength += (firstColumn.trim.toLowerCase() -> firstColumn.length)
        lineForParsing = lineForParsing.replace(firstColumn, "")
      }
    }
    //    addTableHeader(line,columnsNamesAndTheirLength)
    LOG.info(s"Evaluated Column Names and Length:")
    LOG.info(s"${columnsNamesAndTheirLength}")

    return columnsNamesAndTheirLength
  }

  /*
   * Commented Code to discuss the approach during assesment interview
   */
  /*  def parseLine[T](line: String):T= {
        var l:String = line.toString()
        var x:Array[String] = Array[String](l.substring(0, 16),l.substring(16, 32))
        Row.fromSeq(x).asInstanceOf[T]
        ///    x.toArray.asInstanceOf[T]
      }
      override protected def formatDFRow[T <: Product : TypeTag](df: DataFrame):Option[List[T]] = {
        import spark.implicits._
        try {
          Some(df.as[T].collect().map(f=>parseLine[T](f.toString)).toList)
        } catch {
          case e: AnalysisException=> {
            LOG.error("Error Loading DataFrame")
            None
          }
        }
      }*/

  protected def createDataset[T <: Product : TypeTag]: Option[Dataset[String]] = {
    try {
      Some(spark.read.textFile(path))
    } catch {
      case e: AnalysisException =>
        LOG.error("Error Loading File: "+path)
        None
      case e: Exception =>
        LOG.error("Exception occured. Skipping File: "+ path)
        None
    }
  }

  override def parse[T <: Product : TypeTag](): Option[List[T]] = {
    //Logic to parse prn text file
    var ds: Option[Dataset[String]] = createDataset[T]
    if(ds.isDefined && ds.nonEmpty) {
      createParsingStructureForPRN[T](ds.get, ScalaReflection.schemaFor[T].dataType.asInstanceOf[StructType])
    }
    else
      None
  }

}
