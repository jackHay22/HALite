# Geochemical Analysis Interface

## System Outline
### Windows
- R^2 selection settings pane
- Graph Pane

## Build
- In Eclipse go to File -> Export:
  - Select Java > Runnable Jar file
  - Select correct Launch Configuration
  - Export Destination: ```rock_analysis_interface/out/Interface.jar```
- From the command line execute ```./build```
- Optionally, specify release version with ```./build --release 0.0.0``` (replace with desired release version)

## About
- This project uses the Apache Commons Math Library.  The licence for this can be found in:
    - ```docs/APACHE_COMMONS_LICENCE.txt```

## Dependencies
- Apache Commons Math3 3.6.1
