# Hadoop-zip

## Zip file output for Hadoop

Hadoop's sequence files are nice for working inside of Hadoop, but they
aren't very accessible from other languages.  Zipfiles are certainly not
the most efficient file format, but they're well supported outside of
Hadoop-land (notably, via command line tools).

## Usage

1. Plop the source file into your Hadoop source distribution under `src/mapred` and run `ant jar`.
   This will incorporate it into the main hadoop-mapred jar file, so it will be available to any
   job.

   _or_

   Add hadoop-zip.jar to your job classpath.

2. Set your output format to org.apache.hadoop.mapred.ZipFileOutputFormat
