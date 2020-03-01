package com.eb.renderview.utils

import java.io.{BufferedWriter, File, FileWriter}

import com.eb.renderview.models.CreditRecord


/** FileTerms include the terms mostly used for file iterations
  * Row: is the type from inbuilt spark library
  * ARow: As Row is the defined type, ARow is the notion used for Row of a file
  * ARow of a file is the list of column values in the line of a file
  * Rows: List of ARows
  * FileRecords: FileName, List of parsed credit records
*/

trait FileTerms {
  type ARow = List[String]
  type Rows = List[ARow]
  type FileRecords = (String, List[CreditRecord])
}

object FileHelper {
  def writeFile(path: String, text: String): String = {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(text)
    bw.close()
    file.getAbsolutePath()
  }
}