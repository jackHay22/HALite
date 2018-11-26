# HALite geochemical analysis interface

## About
HALite is a geochemical analysis tool created by the Hamilton College Geoscience department, the Hamilton Analytical Laboratory, and the Hamilton College Computer Science department.

## Build
- In Eclipse go to File -> Export:
  - Select Java > Runnable Jar file
  - Select correct Launch Configuration (HALite)
  - Export Destination: ```rock_analysis_interface/out/HALite.jar```
- From the command line execute ```./build```
- Optionally, specify release version with ```./build --release 0.0.0``` (replace with desired release version)

## Setup
To setup the project, configure the following:
- Add ```commons-math3-3.6.1.jar``` to a new folder called ```lib/```. This may also need to be added as an external library in eclipse.
- Add ```pdfbox-app-2.0.12.jar``` to lib as well.

## Crash reporting
If a failed try/catch is severe enough to record as a log, add the following to the catch block:
```java
CrashReporter.report_crash(window, exception);
```
This will prompt the user to save a timestamped log file to Application Support with the exception stack trace.

## Dependencies
- Apache Commons Math3 3.6.1 (license included: ```docs/APACHE_COMMONS_LICENCE.txt```)
- PdfBox 2.0.12

#### © 2018 Ben Parfitt, Jack Hay, and Oliver Keh
