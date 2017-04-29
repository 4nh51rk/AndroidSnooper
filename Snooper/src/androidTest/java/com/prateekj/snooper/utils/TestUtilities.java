package com.prateekj.snooper.utils;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import org.junit.runner.Description;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;
import static android.support.test.InstrumentationRegistry.getContext;

public class TestUtilities {

  public static InputStream readFileAsStream(String assetFileName) throws IOException {
    return getContext().getAssets().open(assetFileName);
  }

  public static String readFrom(String assetFileName) throws IOException {
    InputStream inputStream = readFileAsStream(assetFileName);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    String input = "";
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      input += line + "\n";
    }
    return input.substring(0, input.length() - 1);
  }

  public static Date getDate(int year, int month, int day) {
    Calendar instance = Calendar.getInstance();
    instance.set(year, month, day);
    return instance.getTime();
  }

  public static Date getDate(int year, int month, int day, int hour, int minute, int seconds) {
    Calendar instance = Calendar.getInstance();
    instance.set(year, month, day, hour, minute, seconds);
    return instance.getTime();
  }

  public static Calendar getCalendar(Date date) {
    Calendar instance = Calendar.getInstance();
    instance.setTime(date);
    return instance;
  }

  public static void deleteDir(File file) {
    File[] contents = file.listFiles();
    if (contents != null) {
      for (File f : contents) {
        deleteDir(f);
      }
    }
    file.delete();
  }


  public static void takeSpoonScreenshot(Description description, String filename) {
    File path = new File(getExternalStorageDirectory().getAbsolutePath()
      + "/app_spoon-screenshots/" + description.getClassName() + "/" + description.getMethodName());
    if (!path.exists()) {
      path.mkdirs();
    }

    UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    device.takeScreenshot(new File(path, System.currentTimeMillis() + "_" + filename));
  }
}
