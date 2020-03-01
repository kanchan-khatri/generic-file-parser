package com.eb.renderview

import com.eb.renderview.utils._


object OutputRenderer extends FileTerms {

  /** fn: generateHtmlTable
    * @param fileRecords Map[String,List(T)] -> file-name=>parsed-records
    * For Every File, Parsed Records are rendered to the Html String
    * Parsed Records when null for a file, are handled
    * @param title -> Page Title For the html document
    * @return String -> records rendered to html string
    */

  def generateHtmlTable(fileRecords: List[FileRecords], title: String) : String = {
    var html: String = "<html>\n"+HtmlString.getTitleHeader(title)
    html += "<body>\n"+TableString.getTables(fileRecords)+"\n</body></html>"
    html
  }

}
