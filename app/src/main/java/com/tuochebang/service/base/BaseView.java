package com.tuochebang.service.base;

public interface BaseView<T> {
    void dismissProgress();

    boolean isActive();

    void setPresenter(T t);

    void showProgress();

    void showTip(String str);
}
