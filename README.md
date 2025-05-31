# Joe-Fraser-DocMe360

Reference Materials for Project<br/>
My Existing Code from other Projects and Assessments<br/>
Google, lol

#Maven Compile in Eclipse(Do this first)
after the Project is loaded in Eclipse<br/>
right click on Project VA-assessment<br>
Hover on Run As<br/>
choose Maven build, not Maven build...<br>
The Maven build Run Configuration needs to have the goals set to: clean package


#Directions for VA Assessment Boot and Testing
#Example Java Location
C:\Program Files\Java\jdk1.8.0_241\bin


#Example Project Boot(Do this second)
open your fav Windows Shell Instance(Command Prompt Instance)<br/>
cd c:\work\java\eclipse-workspace\VA-assessment<br/>
"C:\Program Files\Java\jdk1.8.0_241\bin\java" -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -jar target/VA-assessment-0.0.1-SNAPSHOT.jar


#Swagger Testing after Boot
fav Browser url, I use google chrome<br/>
http://localhost:8080/rest/api/swagger-ui.html

#Swagger check
fav Browser url, I use google chrome<br/>
http://localhost:8080/rest/api/v2/api-docs

#Docker Information
I put the files docker-compose.yml and Dockerfile into the project<br/>
It is now working, and tested<br/>
"docker compose up" in the windows project folder<br/>
you cannot use the command until DockerDesktop is running

#SQLite 
Download the sqlite-tools-win-x64-3490200.zip from site: https://sqlite.org/index.html and install it<br/>
Then run sqlite3.exe and create the va.db database as per the requirements.md document<br/>
The requirements say to make two tables Notifications and Templates joined by a foreign key<br/>
You should be able to use the file, in the project folder databaseScripts to help<br/>
After the tables are made and va.db is persisted to the disk, copy the va.db to the folder C:/DATA/SQLite on the disk drive<br/>
Create the folder if it does not exist<br/>
The application will now work, outside of Docker<br/>

the Application testing showed SQLite has an issue.<br/>
the value of foreign_keys is set to OFF by default<br/>
the only way I was able to see it ON, was in the command line shell<br/>
so thru the command line, the foreign_keys are created properly<br/>
However the Spring Boot Application cannot use a database created in the command line shell<br/>
I tried many times, but could not get foreign_keys to be ON in the Spring Boot application.<br/>
In the Application the foreign Keys are not created properly, the tables are<br/>

An example run from the windows shell would be:  sqlite3 va.db<br/>

Example SQLite command line to list all tables<br/>
sqlite> .tables <br/>



