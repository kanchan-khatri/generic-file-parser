import com.eb.renderview.OutputRenderer
import com.eb.renderview.models.CreditRecord
import com.eb.renderview.CreditRecordsManager
import org.scalatest.FunSuite


class CreditRecordsManagerTest extends FunSuite {

  /* Check Various Tests associated with file input
    1. Extra Column in the file
    2. Reduced Column in the file
    3. Null Values
    4. Integer and Float Values
    5. File Not Found Path
    6. "File Not Found Argument parameter" not terminating the Flow for other file
   */
  test("CreditRecordsManager.parseRecords") {
    //input test csv paths
    val csvFilePath1 = getClass.getClassLoader.getResource("storage/input/Workbook2-testcases.csv").getPath
    val csvFilePath2 = "storage/input/filenotfound.csv"
    assertResult(List(
      (csvFilePath1, List(
        CreditRecord("abc", "addr1", "p1", "9890", "10000", "01/01/1987"),
        CreditRecord("def", "addr2", "p2", "null", "10.5", "03/12/1965"))
      ),
      (csvFilePath2, null),
      (csvFilePath1, List(
        CreditRecord("abc", "addr1", "p1", "9890", "10000", "01/01/1987"),
        CreditRecord("def", "addr2", "p2", "null", "10.5", "03/12/1965"))
      )
    ).toString()) {
      new CreditRecordsManager().parseRecords(Array(csvFilePath1, csvFilePath2, csvFilePath1)).toString()
    }

    val csvFilePath3 = getClass.getClassLoader.getResource("storage/input/Workbook2-testcases.prn").getPath
    assertResult(List(
      (csvFilePath3, List(
        CreditRecord("abc", "addr1", "p1", "9890", "10000", "19870101"),
        CreditRecord("def", "addr 2", "p2", "", "10.5", "19651203"))
      )
    ).toString()) {
      new CreditRecordsManager().parseRecords(Array(csvFilePath3)).toString()
    }
  }

  test("CreditRecordsManager.parseFile")
  {
    val csvFilePath = getClass.getClassLoader.getResource("storage/input/Workbook2-testcases.csv").getPath
    //expected output
    val expectedOutput = List(
      CreditRecord("abc", "addr1", "p1", "9890", "10000", "01/01/1987"),
      CreditRecord("def", "addr2", "p2", "null", "10.5", "03/12/1965")
    )
    assertResult(Some(expectedOutput).toString) {
      new CreditRecordsManager().parseFile(csvFilePath).toString
    }
  }

}
