# difficutly-level-stackoverflow
Estimating Difficulty level of stackoverflow questions by text analysis
The objective of this lab is to estimate the difficulty level of questions asked in Stackoverflow by using text analysis.

### Build the jar file
```shell
C:\Users\Asit\workspace\Kom2Lab>ant build
Buildfile: C:\Users\Asit\workspace\Kom2Lab\build.xml

clean:
   [delete] Deleting directory C:\Users\Asit\workspace\Kom2Lab\bin
   [delete] Deleting: C:\Users\Asit\workspace\Kom2Lab\load-dump.jar

compile:
    [mkdir] Created dir: C:\Users\Asit\workspace\Kom2Lab\bin
    [javac] C:\Users\Asit\workspace\Kom2Lab\build.xml:49: warning: 'includeantruntime' was not set, defaulting to build.sysclasspath=last; set to false for repeatable builds
    [javac] Compiling 8 source files to C:\Users\Asit\workspace\Kom2Lab\bin
    [javac] Note: C:\Users\Asit\workspace\Kom2Lab\src\de\tu_darmstadt\kom\stackoverflow\StaxParser.java uses unchecked or unsafe operations.
    [javac] Note: Recompile with -Xlint:unchecked for details.

build:
      [jar] Building jar: C:\Users\Asit\workspace\Kom2Lab\load-dump.jar

BUILD SUCCESSFUL
Total time: 3 seconds
```

### Create db setup
```shell
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom setup
Buildfile: C:\Users\Asit\workspace\Kom2Lab\build.xml

setup:
      [sql] Executing resource: C:\Users\Asit\workspace\Kom2Lab\sql\db.sql
      [sql] 4 of 4 SQL statements executed successfully

BUILD SUCCESSFUL
Total time: 1 second
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom post
Buildfile: C:\Users\Asit\workspace\Kom2Lab\build.xml
```

### Load stackoverflow dump to database
```shell
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom -Dxmldump="posts.xml,users.xml" load
```

### Do post processing
```shell
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom -Dtags="python,java,javascript,php" post
Output:-
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom -Dtags="python,java,javascript,php" post
Buildfile: C:\Users\Asit\workspace\Kom2Lab\build.xml

post:
     [echo] Doing Post processing...
      [sql] Executing resource: C:\Users\Asit\workspace\Kom2Lab\sql\posts_python.sql
      [sql] 17 of 17 SQL statements executed successfully
     [java] 0 [main] INFO stackoverflow-load  - Doing post processing of data
     [java] 186732 [main] INFO stackoverflow-load  - Post procssing finished
     [java] 186732 [main] INFO stackoverflow-load  - Time Taken : 186 seconds

BUILD SUCCESSFUL
Total time: 37 minutes 44 seconds
```

### Generate arff file
```shell
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom generate
Output :-
C:\Users\Asit\workspace\Kom2Lab> ant -Dusername=root -Dpassword=tintin -Ddb=kom generate
Buildfile: C:\Users\Asit\workspace\Kom2Lab\build.xml

generate:
     [java] 0 [main] INFO stackoverflow-load  - Generating ARFF file
     [java] 6 [main] INFO stackoverflow-load  - Generating arff file : data/20141204_014445.arff
     [java] 27488 [main] INFO stackoverflow-load  - Generating ARFF file finished
     [java] 27488 [main] INFO stackoverflow-load  - Time Taken : 27.481629459 seconds

BUILD SUCCESSFUL
Total time: 28 seconds
```
### Analyze
In order to do classification, it's better to work directly from build.xml file
Please modify the following fields according to your need.
```xml
	<property name="model" value="naivebayes" /> <!-- allowed models naivebayes, smo, j48 -->
	<property name="model-options" value="-K " />  
	<property name="weight" value="0.7" />
	<property name="cost-matrix" value="[ 0.0 9.0; 30.0 0.0 ]"/>
	<property name="filter-options" value="-L -C -T -I -tokenizer weka.core.tokenizers.AlphabeticTokenizer" />
	<property name="folds" value="10" />
```
then run the following command
```shell
C:\Users\Asit\workspace\Kom2Lab>
ant -Danalyze="data\20150210_092809_python.arff" analyze
data\20150210_092809_python.arff - file generated in step 5
```
