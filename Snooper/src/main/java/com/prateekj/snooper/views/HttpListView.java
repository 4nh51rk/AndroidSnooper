package com.prateekj.snooper.views;

public interface HttpListView {
  void navigateToResponseBody(int httpCallId);

  void finishView();

  void showDeleteConfirmationDialog();

  void updateListView();
}
