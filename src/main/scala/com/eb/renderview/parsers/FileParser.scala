package com.eb.renderview.parsers

import com.eb.renderview.utils.FileTerms
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{AnalysisException, DataFrame, SparkSession}
import scala.reflect.runtime.universe.TypeTag
import org.slf4j.LoggerFactory


/** Class: FileParser
  * @param path : File Path
  * Uses Apache Spark Library to parse the files and get a DataFrame
  * DataFrame can be transformed to any Generic Case Class provided
  * spark Session -> is created once on FileParser Instantiation
  *  and the same is used everytime to avoid multiple spark sessions
  */
abstract class FileParser(path: String) extends FileTerms {

  val spark = SparkSession.builder().appName("File Parser Using Spark").config("spark.master", "local").getOrCreate()
  lazy private val LOG = LoggerFactory.getLogger(getClass)
  /** Suggests the sparkFormat to be used by specific file-type classes */
  val sparkFormat: String

  /** fn: getParserOptions
    * Abstract Function Implemented in specific file-type classes
    * @return Map[String,String] -> Options provided to spark for Loading the file
    */
  def getParserOptions():Map[String, String]

  protected def createDataFrame[T <: Product : TypeTag]() : Option[DataFrame] = {
    import spark.implicits._
    try {
      var df = spark.read.format(sparkFormat)
        .options(getParserOptions())
        .schema(ScalaReflection.schemaFor[T].dataType.asInstanceOf[StructType])
        .load(path)
      Some(df)
      //    val d = spark.read.textFile(path)
      //      d.toDF()
    } catch {
      case e: AnalysisException =>
        LOG.error("Error Loading File: "+path)
        None
      case e: Exception =>
        LOG.error("Exception occured. Skipping File: "+ path)
        None
    }
  }

  protected def formatDFRow[T <: Product : TypeTag](df: DataFrame):Option[List[T]] = {
    import spark.implicits._
    //df.as[T].collect().map(parseLine).toList
    // df.as[T](Encoders.kryo[T]).collect().toList
    try {
      Some(df.as[T].collect().toList)
    } catch {
      case e: AnalysisException=> {
        LOG.error("Error Loading DataFrame")
        None
      }
    }
  }

  def parse[T <: Product : TypeTag](): Option[List[T]] = {
    var df:Option[DataFrame] = createDataFrame[T]
    if(df.isDefined && df.nonEmpty ) {
      var records = formatDFRow[T](df.get)
      spark.stop()
      records
    } else {
      None
    }
  }

}

/* Companion Object: FileParser
 * File Parser Factory -> returns the file-type specific instances which are extended by FileParser
 * Input: File Path
 * Output: Instance of FileParser
 * Supports csv / prn files. Throws exception otherwise
 */
object FileParser {
  def apply(path: String): FileParser = {
    path match {
      case csv if path.trim.toLowerCase.endsWith(".csv") => new CSVParser(path)
      case prn if path.trim.toLowerCase.endsWith(".prn") => new PRNParser(path)
//      case _ => throw new Exception("File Not Supported")
      case _ => throw new NotImplementedError("File Not Supported")
    }
  }
}