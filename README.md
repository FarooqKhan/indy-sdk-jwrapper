# Indy-Sdk for Java

How to Build:

1. Ensure that you have synced the latest code from this repository.

2. Ensure that you have JDK installed on your system and PATH and CLASSPATH.

3. On Mac or Linux open a terminal, Execute the following command
   <code>./gradlew clean build</code>
   This will ensure that firstly correct version of gradle is downloaded and installed, next the other dependencies
   are downloaded installed and the code is compiled and built.

4. For those who use eclipse, execute the following
   <code>./gradlew eclipse</code>
   This will generate the eclipse project related artifacts with all sub projects properly created with even the source jar files of all the dependencies downloaded and linked correctly.
   Next import these projects into your Eclipse workspace.

5. Download and install Indy-Sdk c-callable library. This means depending on your OS you might have a libindy.so or libindy.dylib or libindy.dll present somewhere on your OS. Alternatively you could also clone the Indy-SDK rust project and build it to obtain the library directly.

6. Check the code in the subproject: indy-sdk-jwrapper/example to figure out how this SDK Java Wrapper can be consumed.

7. To run the example code without having to install Eclipse or carry out the complex other Java specific settings, edit file    <code>example/src/main/java/org/hyperledger/indy/sdk/example/Main.java</code>
make any necessary changes/edits to it save the file, then execute the following command
<code>./gradlew clean build run</code>
