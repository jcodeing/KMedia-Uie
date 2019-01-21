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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.jcodeing.kmedia.uie.R;

/**
 * Battery View <p /> Simple draw battery change.
 */
public class BatteryView extends AppCompatImageView {

  private final int WARNING_LEVEL = 17;
  // =========@View@=========
  private Paint paint;
  private Rect rect;
  // =========@BroadcastReceiver@=========
  private boolean charging;
  private int level;
  private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
      refreshBattery(intent);
    }
  };

  public BatteryView(Context context) {
    this(context, null);
  }

  public BatteryView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BatteryView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    paint = new Paint();
    rect = new Rect();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
    intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
    intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
    refreshBattery(context.registerReceiver(batteryReceiver, intentFilter));
  }

  @Override
  protected void onDetachedFromWindow() {
    Context context = getContext();
    if (batteryReceiver != null && context != null) {
      context.unregisterReceiver(batteryReceiver);
    }
    super.onDetachedFromWindow();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (level == 0 || (charging)) {
      super.onDraw(canvas);
    } else {
      Rect rect = getDrawable().getBounds();
      int i = (int) (dp2px(getContext(), 1.5f) + 0.5f);
      int v3 = level * (rect.width() - i * 2) / 100;
      if (v3 < i + 2) {
        v3 = i + 2;
      }

      this.rect.set(rect.left + i, rect.top + i, rect.left + v3, rect.bottom - i);

      Paint paint = this.paint;
      paint.setColor(level < WARNING_LEVEL ? 0xFFF9746D : 0xFFB0B0B0);
      int saveCount = canvas.getSaveCount();
      canvas.save();
      canvas.concat(getImageMatrix());
      canvas.drawRect(this.rect, this.paint);
      canvas.restoreToCount(saveCount);
      super.onDraw(canvas);
    }
  }

  private void refreshBattery(Intent intent) {
    if (intent != null) {

      int level = intent.getIntExtra("level", 100);
      charging = intent.getIntExtra("plugged", 0) != 0;

      if (charging) {
        setImageResource(R.drawable.ic_battery_charging);
      } else if (level != 0) {
        if (level < WARNING_LEVEL) {
          setImageResource(R.drawable.ic_battery_warning);
        } else {
          setImageResource(R.drawable.ic_battery);
        }
      }
      if (this.level != level) {
        this.level = level;
        invalidate();
      }
    }
  }

  // ============================@Assist@============================
  private static int dp2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}