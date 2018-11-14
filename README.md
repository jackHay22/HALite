# Geochemical Analysis Interface

## Build
- In Eclipse go to File -> Export:
  - Select Java > Runnable Jar file
  - Select correct Launch Configuration
  - Export Destination: ```rock_analysis_interface/out/Interface.jar```
- From the command line execute ```./build```
- Optionally, specify release version with ```./build --release 0.0.0``` (replace with desired release version)

## About
- This project uses the Apache Commons Math Library.  The license for this can be found in:
    - ```docs/APACHE_COMMONS_LICENCE.txt```

## Setup
To setup the project, configure the following:
- Add ```commons-math3-3.6.1.jar``` to a new folder called ```lib/```. This may also need to be added as an external library in eclipse.
- Add ```pdfbox-app-2.0.12.jar``` to lib as well.

## Crash reporting
If a failed try/catch is severe enough to record as a log, add the following to the catch block:
```CrashReporter.report_crash(window, exception);```
This will save a timestamped log file to Application Support with the exception.

## Dependencies
- Apache Commons Math3 3.6.1
- PdfBox 2.0.12
