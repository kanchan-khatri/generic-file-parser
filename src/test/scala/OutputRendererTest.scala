import com.eb.renderview.OutputRenderer
import com.eb.renderview.models.CreditRecord
import com.eb.renderview.utils.FileTerms
import org.scalatest.FunSuite
import com.eb.renderview.utils.TableString

class OutputRendererTest extends FunSuite with FileTerms{

  test("OutputRenderer.generateHtmlTable") {
    // input value
    val csvFilePath1 = getClass.getClassLoader.getResource("storage/input/Workbook2-testcases.csv").getPath
    val inputFileRecord: FileRecords = (csvFilePath1, List(
      CreditRecord("abc", "addr1", "p1", "9890", "10000", "01/01/1987"),
      CreditRecord("def", "addr2", "p2", "null", "10.5", "03/12/1965"))
    )
    val title = "Credit Records"

    val expectedOutput:String = "<html>\n<head> <title>Credit Records</title>" +
      "<link rel=\"stylesheet\" href=\"record-table.css\"></head>" +
      "<body>\n<div align=\"center\"><span>Records from File:"+csvFilePath1+"</span>" +
      "<table border='1'>\n<tr><th>Name</th>\n<th>Address</th>\n<th>Postcode</th>\n<th>Phone</th>\n" +
      "<th>Credit Limit</th>\n<th>Birthday</th>" +
      "</tr>\n<tr><td>abc</td>\n<td>addr1</td>\n<td>p1</td>\n<td>9890</td>\n<td>10000</td>\n<td>01/01/1987</td></tr>\n" +
      "<tr><td>def</td>\n<td>addr2</td>\n<td>p2</td>\n<td>null</td>\n<td>10.5</td>\n<td>03/12/1965</td></tr>" +
      "</table></div>\n</body></html>"
    assert(OutputRenderer.generateHtmlTable(List(inputFileRecord), title) === expectedOutput)
  }

  test("HtmlHelpers") {
    assertResult("<tr><td>abc</td>\n<td>def</td></tr>") {TableString.getRow(List("abc","def"))}

    assertResult("<tr><td>abc</td>\n<td>def</td></tr>") {TableString.getRows(List(List("abc","def")))}

    assertResult(s"<tr><td>Unable to parse creditRecords</td></tr>\n") {
      TableString.getCreditRows(null)
    }
  }

}
