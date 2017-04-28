package com.prateekj.snooper.rules;

import android.app.Activity;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static org.hamcrest.CoreMatchers.is;

public class SnooperActivityRule<T extends Activity> extends IntentsTestRule<T> {
  public SnooperActivityRule(Class<T> activityClass) {
    super(activityClass);
  }

  public SnooperActivityRule(Class<T> activityClass, boolean initialTouchMode) {
    super(activityClass, initialTouchMode);
  }

  public SnooperActivityRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
    super(activityClass, initialTouchMode, launchActivity);
  }

  @Override
  protected void afterActivityLaunched() {
    super.afterActivityLaunched();
    onView(isRoot()).inRoot(withDecorView(is(getActivity().getWindow().getDecorView()))).perform(click());
  }
}
