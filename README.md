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
You should be able to use the files from the databaseScripts folder to help<br/>

the Application testing should only be done if SQLite is running.<br/>
An example run from the windows shell would be:  sqlite3 va.db<br/>

Example SQLite command line to list all tables<br/>
sqlite> .tables <br/>



