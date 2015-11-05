/*   Copyright (c) 2015 Magnet Systems, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.magnet.mmx.protocol;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A channel summary.  The summary only provides a very condensed information.
 * There is an intention to expand this class in the future.
 */
public class ChannelSummary {
  @SerializedName("channelNode")
  private MMXChannelId mChannel;
  @SerializedName("count")
  private int mCount;
  @SerializedName("lastPubTime")
  private Date mLastPubTime;

  /**
   * @hide
   * @param channel
   */
  public ChannelSummary(MMXChannelId channel) {
    mChannel = channel;
  }

  /**
   * @hide
   * @param count
   * @return
   */
  public ChannelSummary setCount(int count) {
    mCount = count;
    return this;
  }

  /**
   * @hide
   * @param lastPubTime
   * @return
   */
  public ChannelSummary setLastPubTime(Date lastPubTime) {
    mLastPubTime = lastPubTime;
    return this;
  }

  /**
   * Get the channel name.
   * @return
   */
  public MMXChannel getChannelNode() {
    return mChannel;
  }

  /**
   * Get the number of published items in this channel.
   * @return
   */
  public int getCount() {
    return mCount;
  }

  /**
   * Get the last published date/time.
   * @return
   */
  public Date getLastPubTime() {
    return mLastPubTime;
  }

  @Override
  public String toString() {
    return "[node="+mChannel.toString()+", count="+mCount+
            ", lastpub="+mLastPubTime+"]";
  }
}
