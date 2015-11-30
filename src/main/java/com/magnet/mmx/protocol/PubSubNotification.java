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

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * The custom payload for PubSub wakeup.
 */
public class PubSubNotification {
  @SerializedName("text")
  private String mText;
  @SerializedName("channel")
  private MMXChannelId mChannel;
  @SerializedName("publishDate")
  private Date mPublishDate;

  /**
   * @hide
   * Constructor with a high level Channel object.
   * @param channel The channel name.
   * @param pubDate The oldest publish date of the new items.
   * @param text Optional text message, or null
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, String text) {
    mChannel = channel;
    mPublishDate = pubDate;
    mText = text;
  }

  /**
   * @hide
   * Constructor with a lower Topic object.  The Topic object will be converted
   * to Channel object.
   * @param topic The topic name
   * @param pubDate The oldest publish date of the new items
   * @param title Optional text message, or null
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, String text) {
    mChannel = topic.toMMXChannelId();
    mPublishDate = pubDate;
    mText = text;
  }

  /**
   * Get an optional text message for the notification.
   * @return Text message, or null.
   */
  public String getText() {
    return mText;
  }
  
  /**
   * Get the channel for this notification.
   * @return The channel which new items are published to.
   */
  public MMXChannel getChannel() {
    return mChannel;
  }

  /**
   * Get the published date of the new items.
   * @return The published date of the new items.
   */
  public Date getPublishDate() {
    return mPublishDate;
  }

  /**
   * Get the type of this notification.
   * @return Always "pubsub".
   */
  public static String getType() {
    return Constants.PingPongCommand.pubsub.toString();
  }
}
