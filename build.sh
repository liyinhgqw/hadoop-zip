#!/bin/bash

if [[ -z $HADOOP_HOME ]]; then
  echo HADOOP_HOME must be set
  exit 1
fi

CP=$(ls $HADOOP_HOME/build/*.jar $HADOOP_HOME/*.jar | xargs | sed -e 's/ /:/g')

javac -cp "$CP" org/apache/hadoop/mapred/ZipFileOutputFormat.java
jar cvf hadoop-zip.jar $(find org/)
