# KG-DBLP
Java version: 1.8 (You cannot use 1.7 or below)

Install maven 

Running instructions:
1.  Open the terminal and start Neo4j using “neo4j start”. Then open System Preference and start MySQL.
2. Change to the project directory and build the whole project using “mvn spring-boot:run -Drun.jvmArguments="-Dusername=neo4j -Dpassword=root"
3. Open the browser, and type “http://localhost:8080”
4. Before signing up or logging in, users can not use this service. So sign up or log in first.
5. After signing up or logging in, users can use the service. Move the mouse to the user email on top-right of the screen, the user can see his/her query history, check the list of all the users, follow a specific user or check the list of all the followers and all the users he/she follows. Also, users can choose either “GET QUERIES OF AUTHOR” or  “GET QUERIES OF PAPER”. And then follow the instructions to use a specific query.
