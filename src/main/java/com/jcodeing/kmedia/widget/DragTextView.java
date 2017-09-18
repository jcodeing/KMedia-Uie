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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Drag Text View
 */
public class DragTextView extends TextView {

  public DragTextView(Context context) {
    this(context, null);
  }

  public DragTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  private int parentWidth;
  private int parentHeight;

  private int left = -1;//unset
  private int top;
  private int right;
  private int bottom;

  @Override
  public void layout(int l, int t, int r, int b) {
    if (left != -1) {
      // =========@Valid Changed@=========
      if (left != l || top != t || right != r
          || bottom != b) {
        int width = Math.abs(r - l);
        int height = Math.abs(b - t);

        // =========@boundary
        if (left <= 0) {
          left = 0;
          right = left + width;
        } else if (right >= parentWidth) {
          right = parentWidth;
          left = right - width;
        } else {
          right = left + width;
          if (right > parentWidth) {
            right = parentWidth;
            left = right - width;
          }
        }
        if (top <= 0) {
          top = 0;
          bottom = top + height;
        } else if (bottom >= parentHeight) {
          bottom = parentHeight;
          top = bottom - height;
        } else {
          bottom = top + height;
          if (bottom > parentHeight) {
            bottom = parentHeight;
            top = bottom - height;
          }
        }
      }
      super.layout(left, top, right, bottom);
    } else {
      super.layout(l, t, r, b);
    }
  }

  private float startX;
  private float startY;

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (isLocked) {
      return false;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = event.getX();
        startY = event.getY();

        if ((parentWidth <= 0 || parentHeight <= 0) && getParent() instanceof View) {
          View parent = (View) getParent();
          parentWidth = parent.getWidth();
          parentHeight = parent.getHeight();
        }
        break;
      case MotionEvent.ACTION_MOVE:
        float endX = event.getX();
        float endY = event.getY();

        int hor = (int) (endX - startX);
        int ver = (int) (endY - startY);

        if (hor != 0 || ver != 0) {
          left = getLeft() + hor;
          top = getTop() + ver;
          right = getRight() + hor;
          bottom = getBottom() + ver;
          if (left < 0) {
            left = 0;
            right = left + getWidth();
          }
          if (top < 0) {
            top = 0;
            bottom = top + getHeight();
          }
          if (right > parentWidth) {
            right = parentWidth;
            left = right - getWidth();
          }
          if (bottom > parentHeight) {
            bottom = parentHeight;
            top = bottom - getHeight();
          }
          layout(left, top, right, bottom);
        }
        break;
    }
    return true;
  }

  // ============================@Lock
  protected boolean isLocked = false;

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }
}