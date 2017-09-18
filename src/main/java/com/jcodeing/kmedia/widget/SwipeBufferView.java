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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.jcodeing.kmedia.widget.android.SwipeProgressBar;

/**
 * Swipe Buffer View <p /> Base on {@link SwipeProgressBar}
 */
public class SwipeBufferView extends View {

  private final SwipeProgressBar mProgressBar;

  public SwipeBufferView(Context context) {
    this(context, null);
  }

  public SwipeBufferView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeBufferView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mProgressBar = new SwipeProgressBar(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mProgressBar.draw(canvas);
  }

  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    mProgressBar.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
  }

  /**
   * Set the four colors used in the progress animation. The first color will also be the color of
   * the bar that grows in response to a user swipe gesture.
   *
   * @param color1 Integer representation of a color.
   * @param color2 Integer representation of a color.
   * @param color3 Integer representation of a color.
   * @param color4 Integer representation of a color.
   */
  public void setColorScheme(int color1, int color2, int color3, int color4) {
    mProgressBar.setColorScheme(color1, color2, color3, color4);
  }

  public void showBuffering(boolean isBuffering) {
    if (isBuffering) {
      mProgressBar.start();
    } else {
      mProgressBar.stop();
    }
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    showBuffering(visibility == VISIBLE);
  }
}