package com.prateekj.snooper.networksnooper.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.prateekj.snooper.R;
import com.prateekj.snooper.networksnooper.model.HttpCall;
import com.prateekj.snooper.realm.RealmFactory;
import com.prateekj.snooper.networksnooper.repo.SnooperRepo;
import com.prateekj.snooper.rules.RealmCleanRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.prateekj.snooper.utils.EspressoViewMatchers.withListSize;
import static com.prateekj.snooper.utils.EspressoViewMatchers.withRecyclerView;
import static com.prateekj.snooper.utils.TestUtilities.getDate;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HttpCallListActivityTest {

  @Rule
  public RealmCleanRule rule = new RealmCleanRule();

  @Rule
  public IntentsTestRule<HttpCallListActivity> activityRule =
    new IntentsTestRule<>(HttpCallListActivity.class, true, false);
  private SnooperRepo snooperRepo;

  @Before
  public void setUp() throws Exception {
    snooperRepo = new SnooperRepo(RealmFactory.create(getTargetContext()));
  }

  @Test
  public void shouldRenderHttpCalls() throws Exception {
    Date beforeDate = getDate(2017, 5, 2, 11, 22, 33);
    Date afterDate = getDate(2017, 5, 3, 11, 22, 33);
    saveHttpCall("https://www.google.com", "GET", 200, "OK", beforeDate);
    saveHttpCall("https://www.facebook.com", "GET", 200, "OK", afterDate);

    activityRule.launchActivity(null);

    onView(withText(R.string.title_activity_http_call_list)).check(matches(isDisplayed()));
    onView(withText(R.string.done)).check(matches(isDisplayed()));
    onView(withRecyclerView(R.id.list, 0)).check(matches(allOf(
      hasDescendant(withText("https://www.facebook.com")),
      hasDescendant(withText("GET")),
      hasDescendant(withText("200")),
      hasDescendant(withText("OK")),
      hasDescendant(withText("06/03/2017 11:22:33"))
    )));

    onView(withRecyclerView(R.id.list, 1)).check(matches(allOf(
      hasDescendant(withText("https://www.google.com")),
      hasDescendant(withText("GET")),
      hasDescendant(withText("200")),
      hasDescendant(withText("OK")),
      hasDescendant(withText("06/02/2017 11:22:33"))
    )));

    verifyClickActionOnListItem(0, 2);
    verifyClickActionOnListItem(1, 1);

    onView(withText(R.string.done)).perform(click());
    assertTrue(activityRule.getActivity().isFinishing());
  }

  @Test
  public void shouldNotRenderAnyRecordWhenDeleteTapped() throws Exception {
    Date beforeDate = getDate(2017, 5, 2, 11, 22, 33);
    Date afterDate = getDate(2017, 5, 3, 11, 22, 33);
    saveHttpCall("https://www.google.com", "GET", 200, "OK", beforeDate);
    saveHttpCall("https://www.facebook.com", "GET", 200, "OK", afterDate);

    activityRule.launchActivity(null);
    onView(withRecyclerView(R.id.list, 0)).check(matches(allOf(
      hasDescendant(withText("https://www.facebook.com")),
      hasDescendant(withText("GET")),
      hasDescendant(withText("200")),
      hasDescendant(withText("OK")),
      hasDescendant(withText("06/03/2017 11:22:33"))
    )));

    onView(withRecyclerView(R.id.list, 1)).check(matches(allOf(
      hasDescendant(withText("https://www.google.com")),
      hasDescendant(withText("GET")),
      hasDescendant(withText("200")),
      hasDescendant(withText("OK")),
      hasDescendant(withText("06/02/2017 11:22:33"))
    )));

    onView(withId(R.id.delete_records_menu)).perform(click());

    onView(withText(R.string.delete_records_dialog_confirmation)).perform(click());
    onView(withText(R.string.title_activity_http_call_list)).check(matches(isDisplayed()));
    onView(withId(R.id.list)).check(matches(withListSize(0)));

  }

  private void verifyClickActionOnListItem(int itemIndex, int httpCallId) {
    intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(RESULT_OK, new Intent()));
    onView(withRecyclerView(R.id.list, itemIndex)).perform(click());
    intended(allOf(
      hasComponent(HttpCallActivity.class.getName()),
      hasExtra(HttpCallActivity.HTTP_CALL_ID, httpCallId)));
  }

  private void saveHttpCall(String url, String method, int statusCode, String statusText, Date date) {
    HttpCall httpCall = new HttpCall.Builder()
      .withUrl(url)
      .withMethod(method)
      .withStatusCode(statusCode)
      .withStatusText(statusText)
      .build();
    httpCall.setDate(date);
    snooperRepo.save(httpCall);
  }
}
