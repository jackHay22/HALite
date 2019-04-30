# HALite geochemical analysis interface

## NOTE: this is a deprecated version of HALite, not the active development branch.

## About
HALite is a geochemical analysis tool created by the Hamilton College Geoscience department, the Hamilton Analytical Laboratory, and the Hamilton College Computer Science department.

## Build
- In Eclipse go to File -> Export:
  - Select Java > Runnable Jar file
  - Select correct Launch Configuration (HALite)
  - Export Destination: ```rock_analysis_interface/out/HALite.jar```
- From the command line execute ```./build```
- Optionally, specify release version with ```./build --release 0.0.0``` (replace with desired release version)
- Optionally, rebuild the app icns file with a new image. Use ```./build --icon path/to/icon.png```

### Releases
- Note: The macOS native package build is large because it contains the jre. The cross-platform build (at present) is just the jar and should be considered the lightweight cross-platform distribution of the application.

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

- Note: This is currently used in ```DataBackend.notify_update()```. This will prompt the user to save a log file in case the system crashes in an unexpected location.  DataBackend acts as the source of all change propagation throughout the system.

## Help Menu Content
- To add content to the internal help menu, edit the html files in ```resources/docs/```
- CSS markup rules can be found in ```SystemThemes.HELP_MARKUP_RULES```
- To add an image to an HTML document, add the following to the file (it must be on its own line)
```
img:buttons/minus_button.png
```
- Optionally, include colon-separated resize values for the width and the height. ex:
```
img:buttons/minus_button.png:140:80
```
- Replace the url to the right of the colon with the appropriate image location
- When the doc is loaded in the help menu, the image tag is dynamically created as it needs to convert this jar resource path into a full image path.

## Dependencies
- Apache Commons Math3 3.6.1 (license included: ```docs/APACHE_COMMONS_LICENCE.txt```)
- PdfBox 2.0.12

####
#### © 2019 Ben Parfitt, Jack Hay, and Oliver Keh
