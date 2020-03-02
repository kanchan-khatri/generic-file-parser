package com.eb.renderview.utils

import com.eb.renderview.models.CreditRecord

/** Helper Functions to render the input variables to html strings for tables, headers, etc.*/

object TableString extends FileTerms {

  def getTables(fileRecords: List[FileRecords]): String = {
    fileRecords.map(recs=>getCreditRows(recs._2)
      .mkString("<div align=\"center\">" +
        "<span>Records from File:"+recs._1+"</span>" +
        "<table border='1'>","","</table></div>"))
      .mkString("<br/>")
  }

  def getCreditRows(creditRecords: List[CreditRecord]): String = {
    if (creditRecords != null) {
      var headers: List[String] = List("Name","Address","Postcode", "Phone", "Credit Limit", "Birthday")
      var headersHtml:String = getHeader(headers)
      var rowString: String = creditRecords.map(c=>
        getRow(List(c.name, c.address, c.postcode, c.phone, c.credit_limit, c.birthday ))
      ).mkString("\n")
      (headersHtml+rowString)
    } else {
      "Unable to parse creditRecords".mkString("<tr><td>","","</td></tr>\n")
    }
  }

  def getRows(rows: Rows): String = {
    rows.map(row=>getRow(row)).mkString("\n")
  }

  def getRow(row: ARow): String = {
    row.mkString("<tr><td>","</td>\n<td>","</td></tr>")
  }

  def getHeader(row: ARow): String = {
    row.mkString("\n<tr><th>","</th>\n<th>","</th></tr>\n")
  }
}

object HtmlString {

  def getTitleHeader(title: String): String = {
    ("<head> <title>" + title + "</title>" +
      "<link rel=\"stylesheet\" href=\"record-table.css\">" +
      "</head>")
  }

}