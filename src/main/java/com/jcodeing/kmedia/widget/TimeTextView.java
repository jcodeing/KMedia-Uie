/*
 * Copyright (c) 2017 K Sun <jcodeing@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcodeing.kmedia.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Time Text View <p /> Simple show time change.
 */
public class TimeTextView extends AppCompatTextView {

  public TimeTextView(Context context) {
    this(context, null);
  }

  public TimeTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    post(updateTime);
  }

  @Override
  protected void onDetachedFromWindow() {
    removeCallbacks(updateTime);
    if (calendar != null) {
      calendar.clear();
    }
    super.onDetachedFromWindow();
  }

  // ============================@Time@============================
  private GregorianCalendar calendar = new GregorianCalendar();
  private final Runnable updateTime = new Runnable() {
    @Override
    public void run() {
      calendar.setTimeInMillis(System.currentTimeMillis());
      setText(String.format(Locale.US, "%02d:%02d",
          calendar.get(Calendar.HOUR_OF_DAY),
          calendar.get(Calendar.MINUTE)));
      postDelayed(updateTime, (60 - calendar.get(Calendar.SECOND)) * 1000);//31000
    }
  };
}