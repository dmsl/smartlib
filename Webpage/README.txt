How to setup your own SmartLib Directions:

1. Download and Extract Webpage Source code

2. Upload extracted source to your FTP Server, and give appropriate permissions:
    a. 0755 to all files
    b. 0777 to CONFIG.PHP (in the root folder), so automated installation can proceed

3. Go to your domain/INSTALL , and follow instructions

4. Restore permissions of CONFIG.PHP to 0755.

5. Change your logos in images/ directory

What this procedure does:
.checks your connection to your database
.creates tables to your database
.creates a configuration file with parameters chosen to build your library
.enrolls your library to SmartLib Central database
..your library will be available as a login option from Smartphones and Tablets