package com.prateekj.snooper.presenter;

import android.support.annotation.NonNull;

import com.prateekj.snooper.formatter.ResponseFormatter;
import com.prateekj.snooper.formatter.ResponseFormatterFactory;
import com.prateekj.snooper.model.HttpCall;
import com.prateekj.snooper.model.HttpHeader;
import com.prateekj.snooper.model.HttpHeaderValue;
import com.prateekj.snooper.repo.SnooperRepo;
import com.prateekj.snooper.viewmodel.HttpBodyViewModel;

import static com.prateekj.snooper.activity.HttpCallActivity.REQUEST_MODE;
import static com.prateekj.snooper.model.HttpHeader.CONTENT_TYPE;

public class HttpCallFragmentPresenter {
  private SnooperRepo repo;
  private int httpCallId;
  private int mode;

  public HttpCallFragmentPresenter(SnooperRepo repo, int httpCallId) {
    this.repo = repo;
    this.httpCallId = httpCallId;
  }

  public void init(HttpBodyViewModel viewModel, int mode) {
    this.mode = mode;
    HttpCall httpCall = this.repo.findById(httpCallId);
    viewModel.init(httpCall, getFormatter(httpCall), mode);
  }

  @NonNull
  private ResponseFormatter getFormatter(HttpCall httpCall) {
    HttpHeader contentTypeHeader = getContentTypeHeader(httpCall);
    HttpHeaderValue headerValue = contentTypeHeader.getValues().get(0);
    return new ResponseFormatterFactory().getFor(headerValue.getValue());
  }

  private HttpHeader getContentTypeHeader(HttpCall httpCall) {
    if (this.mode == REQUEST_MODE) {
      return httpCall.getRequestHeader(CONTENT_TYPE);
    }
    return httpCall.getResponseHeader(CONTENT_TYPE);
  }
}
