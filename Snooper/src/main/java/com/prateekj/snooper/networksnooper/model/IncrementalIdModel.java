package com.prateekj.snooper.networksnooper.model;

import io.realm.RealmModel;

public interface IncrementalIdModel<T extends RealmModel> {
  Class<T> getClazz();

  void setId(int id);
}
