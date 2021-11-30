# GenericFileParser

## Summary
GenericFileParser is designed to stream huge files and parse the records from csv/prn file and transform the results to the generic record.
Transforming to generic record can help keep track of any generic record from several sources as mentioned in the problem statement.

## Library
This repo is using apache.spark library to parse the records and to some extent transform them to the generic record.
apache.spark gives many advantages other than just streaming huge files and parsing which can also help in building fast poc products
apache.spark helps in achieving high performance streaming operations which can help dealing with large files

## Execution

1. Define the default input and output config in resources/application.properties which is already defined.
The inputs can also be overriden using the command line arguments.

Default Input Files are saved in resources/storage/input/ directory.
Default Input Files are: resources/storage/input/Workbook2.csv,resources/storage/input/Workbook2.prn
Output File is defined in application.properties and can be changed if needed.
Default Output File: resources/storage/output/
Output File Produced post execution: resources/storage/output/CreditRecords.html

**Run with default inputs**
```bash
run
```

**Run with arguments for input files**
```bash
run <path to input file 1> <path to input file 2> ...
```

2. Invalid File Paths (file not found) will be ignored and won't affect other files execution.
3. All the Input files will be processed and output table for all will be generated in Output File.

## Code Flow
Please refer Code Comments for detailed information.

Supported File Types are: csv/prn. An NotImplementedError Exception is thrown otherwise

**models**
CreditRecord -> credit record to track
Multiple Models can be supported

**FileParser**
A Factory that returns the specific parser instance and abstracts the parser methods using spark library

**CreditRecordsManager**
Calls FileParser for parsing the records to List[CreditRecord] for every input file and gives the results to outputrenderer
For future use suppose CustomerRecord is to be parsed, just adding CustomerRecord model and CustomerRecordManager will solve the purpose

**OutputRenderer**
Renders the variables to the html string. other methods can also be implemented.

## Future Improvements
1. Can read the input files in parallel and handle the output resource for multiple writes at once
2. Real Time Stream files using spark
