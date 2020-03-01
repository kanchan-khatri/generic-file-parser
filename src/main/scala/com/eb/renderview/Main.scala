/** Developer: Kanchan Khatri
  * Main -> processes the input supported(csv, prn) files and combines the file records to html table view
  * unsupported files throw exception: "File not supported"
  * Corrupted / Unparsed csv/prn file get skipped and other files get processed.
  * Output File Dir can be defined in the application.properties file
  * Default Output File Path : resources/storage/output/
  * Default Input File : resources/storage/input/Workbook2.csv
  */
package com.eb.renderview

import org.slf4j.LoggerFactory

object Main {

  def main(args: Array[String]): Unit = {
    lazy val LOG = LoggerFactory.getLogger(getClass)
    val appConfig = ConfigManager
    val outputDir = appConfig("file.output.default_path")

    val inputFiles: Array[String] = if (args.length > 1) {
      args
    } else {
      Array(getClass.getResource(appConfig("file.input.default_path")).getPath)
    }

    LOG.debug("Input Files:")
    inputFiles.map(LOG.debug)

    new CreditRecordsManager().parseRecordsToHtml(inputFiles, outputDir)

    LOG.info(s"All Files are processed")
  }

}