import com.eb.renderview.models.CreditRecord
import com.eb.renderview.parsers.{CSVParser, FileParser, PRNParser}
import org.scalatest.FunSuite

class FileParserTest extends FunSuite {

  test("FileParser.parse")
  {
    assert(FileParser("storage/input/filenotfoud.csv").parse[CreditRecord] === None)
  }

  test("FileParser.init.class")
  {
    assert( FileParser("storage/input/Workbook2.csv").isInstanceOf[CSVParser] === true)

    assert( FileParser("storage/input/Workbook2.csv").isInstanceOf[PRNParser] === false)

    assert( FileParser("storage/input/Workbook2.PRN").isInstanceOf[PRNParser] === true)

    assert( FileParser("storage/input/Workbook2.PRN       ").isInstanceOf[PRNParser] === true)

    assertThrows[NotImplementedError] {
      FileParser("storage/input/Workbook2.txt")
    }

    assertThrows[NotImplementedError] {
      FileParser("storage/input/Workbook2.csv.doc")
    }
  }
}
