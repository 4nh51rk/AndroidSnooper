package com.prateekj.snooper;

import com.prateekj.snooper.model.HttpCall;
import com.prateekj.snooper.model.HttpCall.HttpCallBuilder;
import com.prateekj.snooper.rules.RealmCleanRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.realm.Realm;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class AndroidSnooperTest {

  private AndroidSnooper androidSnooper;

  @Rule
  public RealmCleanRule rule = new RealmCleanRule();

  @Before
  public void setUp() throws Exception {
    androidSnooper = AndroidSnooper.getInstance();
  }

  @Test
  public void shouldReturnSameInstanceOnEveryInit() throws Exception {
    AndroidSnooper newSnooper = AndroidSnooper.init(getTargetContext());
    assertThat(newSnooper, sameInstance(androidSnooper));
  }

  @Test
  public void shouldSaveHttpCallViaSpringHttpRequestInterceptor() throws Exception {
    final String url = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0";
    final String responseBody = "responseBody";
    final String requestBody = "requestBody";

    HttpCall call = new HttpCallBuilder()
      .withUrl(url)
      .withMethod("POST")
      .withPayload(requestBody)
      .withResponseBody(responseBody)
      .withStatusCode(200)
      .withStatusText("OK").build();

    androidSnooper.record(call);
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        Realm realm = TestApplication.getInstance().getRealm();
        HttpCall httpCall = realm.where(HttpCall.class).findAll().first();
        assertThat(httpCall.getUrl(), is(url));
        assertThat(httpCall.getPayload(), is(requestBody));
        assertThat(httpCall.getMethod(), is("POST"));
        assertThat(httpCall.getResponseBody(), is(responseBody));
        assertThat(httpCall.getStatusCode(), is(200));
        assertThat(httpCall.getStatusText(), is("OK"));
      }
    });
  }
}
