
# Coincidix logic game solver 📝  
This java program solve the Coincidix game in less than a second.

## Using the game solver 🚀  
To get started, you only have to click on the green button. You can then download the .zip file and extract it. The only thing you need in that .zip is Coincidix.jar

If you have a valid installation of java, you only have to run cmd. Then run
java -ea -jar /path/to/Coincidix.jar

Then you can enter the number of the level you are looking to solve. 

You can then start solving levels. If it has not been registred, go to "data/grille" to do so. It is extremely simple: you only have to specify the coordinates of the smileys. Be carefull, the top left corner is (0,0).

## Getting started in the project
All the needed files are in java folder. Feel free to fork the repository
It is important to enable assertion as they are used by the program

To compile the archive and run it:
```java
javac .\src\main\* -d .\bin\; cd ./bin; jar cfmv ../../Coincidix.jar ../MANIFEST.MF main/*.class ../data; cd ..
java -ea -jar ../Coincidix.jar
```

Else:
```java
javac .\src\main\* -d .\bin\; java -cp ./bin main.Grid
```