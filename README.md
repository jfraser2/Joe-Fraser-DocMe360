# Joe-Fraser-DocMe360

Reference Materials for Project<br/>
My Existing Code from other Projects and Assessments<br/>
Google, lol

#SQLite(Do this first) 
Download the sqlite-tools-win-x64-3490200.zip from site: https://sqlite.org/index.html and install it<br/>
Then run sqlite3.exe va.db and create the va.db database as per the requirements.md document<br/>
The requirements say to make two tables Notifications and Templates joined by a foreign key<br/>
You should be able to use the file, in the project folder databaseScripts to help<br/>
After the tables are made and va.db is persisted to the disk, copy the va.db to the folder C:/data/SQLite on the disk drive<br/>
Create the folder if it does not exist<br/>
The application will now work, with and without Docker<br/>

An example run from the windows shell would be:  sqlite3 va.db<br/>
Example SQLite command line to list all tables<br/>
sqlite> .tables <br/>

#Maven Compile in Eclipse(Do this second)
after the Project is loaded in Eclipse(File Import from Git)<br/>
right click on Project VA-assessment<br>
Hover on Run As<br/>
choose Maven build, not Maven build...<br>
The Maven build Run Configuration needs to have the goals set to: clean package<br/>


#Directions for VA Assessment Boot and Testing

The Design constraints are the App will build in Docker and can run in Docker<br/>
or can run without Docker. These constraints have been meet.<br/>
 
#Example Java Location
C:\Program Files\Java\jdk1.8.0_241\bin<br/>


#Example Command Line Project Boot(Do this third)
open your fav Windows Shell Instance(Command Prompt Instance)<br/>
cd c:\work\java\eclipse-workspace\VA-assessment<br/>
"C:\Program Files\Java\jdk1.8.0_241\bin\java" -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -jar target/VA-assessment-0.0.1-SNAPSHOT.jar

#Docker Information for Project Boot
I put the files docker-compose.yml and Dockerfile into the project<br/>
It is now working, and tested. Enter <br/>
"docker compose up" from the command line, in the windows project folder.<br/>
You cannot use the command until DockerDesktop is running

To make the application use the database on the disk,<br/>
a mount was created in docker-compose.yml from C:\data\SQLite\va.db<br/>
to /data/SQLite/va.db in the Container. The C: is required or the app will<br/>
not run correctly in Docker. The Linux people will not like this. So the only<br>/
solution would be to modify the docker-compose.yml and the PersistencJpaConfig.java<br/>
file. The PersistenceJpaConfig.java contains the database connection url /data/SQLite/va.db. <br/>
Whatever the implemented solution is, a recompile and redeploy would be required.   


#Swagger Testing after Boot
fav Browser url, I use google chrome<br/>
http://localhost:8080/swagger-ui.html
With docker use windows command to get the container Ip<br/>

ping host.docker.internal<br/>

http://<ping result>:8080/swagger-ui.html<br/>

#Swagger check
fav Browser url, I use google chrome<br/>
http://localhost:8080/v2/api-docs



