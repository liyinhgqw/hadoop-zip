package org.apache.hadoop.mapred;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Progressable;

public class ZipFileOutputFormat<K, V> extends FileOutputFormat<K, V> {
  public static class ZipRecordWriter<K, V> implements
      org.apache.hadoop.mapred.RecordWriter<K, V> {
    private ZipOutputStream zipOut;

    public ZipRecordWriter(FSDataOutputStream fileOut) {
      zipOut = new ZipOutputStream(fileOut);
    }

    @Override
    public void write(K key, V value) throws IOException {
      String fname = null;
      if (key instanceof BytesWritable) {
        BytesWritable bk = (BytesWritable)key;
        fname = new String(bk.getBytes(), 0, bk.getLength());
      } else {
        fname = key.toString();
      }

      ZipEntry ze = new ZipEntry(fname);
      zipOut.closeEntry();
      zipOut.putNextEntry(ze);

      if (value instanceof BytesWritable) {
        zipOut.write(((BytesWritable) value).getBytes(), 0, ((BytesWritable)value).getLength());
      } else {
        zipOut.write(value.toString().getBytes());
      }

    }

    @Override
    public void close(Reporter reporter) throws IOException {
      zipOut.finish();
      zipOut.close();
    }

  }

  @Override
  public RecordWriter<K, V> getRecordWriter(FileSystem ignored, JobConf job,
      String name, Progressable progress) throws IOException {
    Path file = FileOutputFormat.getTaskOutputPath(job, name);
    FileSystem fs = file.getFileSystem(job);
    FSDataOutputStream fileOut = fs.create(file, progress);
    return new ZipRecordWriter<K, V>(fileOut);
  }
}
