package com.prateekj.snooper.rules;

import com.prateekj.snooper.utils.TestUtilities;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;
import static com.prateekj.snooper.utils.TestUtilities.takeSpoonScreenshot;

public class ScreenShotRule extends TestWatcher {
  @Override
  protected void starting(Description description) {
    File file = new File(getExternalStorageDirectory().getAbsolutePath()
      + "/app_spoon-screenshots/" + description.getClassName() + "/" + description.getMethodName());
    if (file.exists()) {
      TestUtilities.deleteDir(file);
    }
    String filename = "starting-" + description.getClassName() + "-" + description.getMethodName() + ".png";
    takeSpoonScreenshot(description, filename);
  }

  @Override
  protected void finished(Description description) {
    String filename = "finished-" + description.getClassName() + "-" + description.getMethodName() + ".png";
    takeSpoonScreenshot(description, filename);
  }

  @Override
  protected void failed(Throwable e, Description description) {
    String filename = "failed-" + description.getClassName() + "-" + description.getMethodName() + ".png";
    takeSpoonScreenshot(description, filename);
  }
}
