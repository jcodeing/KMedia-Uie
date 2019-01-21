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
package com.jcodeing.kmedia.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.jcodeing.kmedia.assist.C;
import com.jcodeing.kmedia.definition.IMediaItem;
import com.jcodeing.kmedia.definition.IMediaQueue;
import java.util.List;

/**
 * Base class for an Adapter <p /> Adapters provide a binding from an media-queue data set to views
 * that are displayed within a {@link RecyclerView}.
 *
 * @param <VH> ViewHolder
 * @see IMediaQueue
 */
public abstract class MediaQueueRecyclerAdapter<VH extends ViewHolder> extends
    RecyclerView.Adapter<VH> implements IMediaQueue.Listener {

  @Override
  public int getItemCount() {
    return mediaQueue != null ? mediaQueue.size() : 0;
  }

  // ============================@MediaQueue@============================
  protected IMediaQueue mediaQueue;

  public void setMediaQueue(IMediaQueue mediaQueue) {
    if (this.mediaQueue != mediaQueue) {
      this.mediaQueue = mediaQueue;
      lastQueueIndex = mediaQueue.getCurrentIndex();
      mediaQueue.addListener(this);
      notifyItemRangeChanged(0, mediaQueue.size());
    }
  }

  public IMediaQueue getMediaQueue() {
    return mediaQueue;
  }

  @Override
  public void onQueueUpdated(List<? extends IMediaItem> newQueue) {
    notifyItemRangeChanged(0, newQueue.size());
  }

  @Override
  public void onItemRemoved(int index) {
    notifyItemRemoved(index);
  }

  private int lastQueueIndex = C.INDEX_UNSET;

  @Override
  public void onCurrentQueueIndexUpdated(int queueIndex) {
    if (lastQueueIndex != C.INDEX_UNSET) {
      notifyItemChanged(lastQueueIndex);
    }
    notifyItemChanged(lastQueueIndex = queueIndex);
  }

  @Override
  public boolean onSkipQueueIndex(int queueIndex) {
    return false;
  }
}