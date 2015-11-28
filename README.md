# KG-DBLP
Java version: 1.8 (You cannot use 1.7 or below)

Install maven 

How to run: mvn spring-boot:run -Drun.jvmArguments="-Dusername=neo4j -Dpassword=root"  
You need to store data into your Neo4j database first


mvn spring-boot:run -Drun.jvmArguments="-Dusername=neo4j -Dpassword=root"

MATCH (n) DETACH DELETE (n)

MATCH (p:Paper)<-[:PUBLISH]-(a:Author) WHERE a.name = 'Maciej Koutny' RETURN p

MATCH (a:Author) WHERE a.name = 'Juha Honkala' RETURN a