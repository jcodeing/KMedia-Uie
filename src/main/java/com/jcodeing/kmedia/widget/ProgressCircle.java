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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.jcodeing.kmedia.uie.R;
import com.jcodeing.kmedia.view.ProgressAny;

/**
 * Progress Circle <p /> Simple implements progressAny view
 */
public class ProgressCircle extends FrameLayout implements ProgressAny {

  // =========@Attrs@=========
  private int progressColor;
  private int progressBackgroundColor;
  private int progressCoreBackgroundColor;

  private float progressWidth;
  private float progressOffset;

  private int progressStyle;
  public static final int STROKE = 1;
  public static final int FILL = 2;

  public ProgressCircle(Context context) {
    this(context, null);
  }

  public ProgressCircle(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    // =========@Attrs@=========
    progressCoreBackgroundColor = -1;
    progressColor = Color.RED;
    progressBackgroundColor = Color.DKGRAY;
    progressWidth = 5;
    progressOffset = 0;
    max = 100;
    progressStyle = STROKE;
    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(
          attrs, R.styleable.ProgressCircle, defStyleAttr, 0);
      try {
        progressColor = a.getColor(
            R.styleable.ProgressCircle_progressColor, progressColor);
        progressBackgroundColor = a.getColor(
            R.styleable.ProgressCircle_progressBackgroundColor, progressBackgroundColor);
        progressCoreBackgroundColor = a.getColor(
            R.styleable.ProgressCircle_progressCoreBackgroundColor, progressCoreBackgroundColor);

        progressWidth = a.getDimension(
            R.styleable.ProgressCircle_progressWidth, progressWidth);
        progressOffset = a.getDimension(
            R.styleable.ProgressCircle_progressOffset, progressOffset);

        max = a.getInteger(R.styleable.ProgressCircle_max, max);

        progressStyle = a.getInt(
            R.styleable.ProgressCircle_progressStyle, progressStyle);
      } finally {
        a.recycle();
      }
    }
    // =========@Other@=========
    paint = new Paint();
    setPadding(getPaddingLeft() + (int) progressWidth, getPaddingTop() + (int) progressWidth,
        getPaddingRight() + (int) progressWidth, getPaddingBottom() + (int) progressWidth);
  }


  // ============================@Progress@============================
  private int progress;
  private int max;

  @Override
  public synchronized int getProgress() {
    return progress;
  }

  @Override
  public synchronized void setProgress(int progress) {
    if (progress < 0) {
      progress = 0;
    }
    if (progress > max) {
      progress = max;
    }
    if (this.progress != progress) {
      this.progress = progress;
      postInvalidate();
    }
  }

  @Override
  public int getSecondaryProgress() {
    return 0;
  }

  @Override
  public void setSecondaryProgress(int secondaryProgress) {
    //Do nothing
  }

  @Override
  public synchronized int getMax() {
    return max;
  }

  @Override
  public synchronized void setMax(int max) {
    if (max < 0) {
      max = 0;
    }
    if (this.max != max) {
      this.max = max;
      postInvalidate();
    }
  }

  @Override
  public void setOnChangeListener(OnChangeListener onChangeListener) {
    //Do nothing
  }

  // ============================@Draw@============================
  private Paint paint;

  @Override
  protected void dispatchDraw(Canvas canvas) {

    // =========@Progress Background@=========
    int centre;
    if (getWidth() <= getHeight()) {
      centre = getWidth() / 2;
    } else {
      centre = getHeight() / 2;
    }
    int radius = (int) (centre - progressWidth / 2);
    paint.setColor(progressBackgroundColor);
    paint.setStyle(progressStyle == STROKE ? Paint.Style.STROKE : Style.FILL_AND_STROKE);
    paint.setStrokeWidth(progressWidth);
    paint.setAntiAlias(true);
    canvas.drawCircle(centre, centre, radius, paint);

    // =========@Child Background Color@=========
    if (progressCoreBackgroundColor != -1) {
      paint.setColor(progressCoreBackgroundColor);
      paint.setStyle(Style.FILL_AND_STROKE);
      paint.setStrokeWidth(0);
      paint.setAntiAlias(true);
      canvas.drawCircle(centre, centre, radius, paint);
    }

    // =========@Progress@=========
    paint.setStrokeWidth(progressWidth);
    paint.setColor(progressColor);
    RectF oval = new RectF(
        centre - radius + progressOffset,
        centre - radius + progressOffset,
        centre + radius - progressOffset,
        centre + radius - progressOffset);
    switch (progressStyle) {
      case STROKE: {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, -90, 360 * progress / max, false, paint);
        break;
      }
      case FILL: {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (progress != 0) {
          canvas.drawArc(oval, 0, 360 * progress / max, true, paint);
        }
        break;
      }
    }

    super.dispatchDraw(canvas);
  }
}