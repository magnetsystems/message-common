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
 * The payload for PubSub wakeup (silent notifification) or push notification.
 */
public class PubSubNotification {
  @SerializedName("text")
  private final String mText;
  @SerializedName("channel")
  private final MMXChannelId mChannel;
  @SerializedName("publishDate")
  private final Date mPublishDate;
  @SerializedName("title")
  private final String mTitle;
  @SerializedName("body")
  private final String mBody;

  /**
   * @hide
   * Constructor with a Channel object for wake-up (silent notification.)
   * @param channel The channel name.
   * @param pubDate The oldest publish date of the new items.
   * @param text Optional text message, or null
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, String text) {
    mChannel = channel;
    mPublishDate = pubDate;
    mText = text;
    mTitle = null;
    mBody = null;
  }

  /**
   * @hide
   * Constructor with a Channel object for push notification.  The optional text
   * will be set to the <code>title</code> for backward compatibility.
   * @param channel The channel name.
   * @param pubDate The oldest publish date of the new items.
   * @param title A non-null title for push notification.
   * @param body An optional body for push notification.
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, String title, String body) {
    mChannel = channel;
    mPublishDate = pubDate;
    mText = title;
    mTitle = title;
    mBody = body;
  }

  /**
   * @hide
   * Constructor with a Topic object for wake-up (silent notification.)
   * The Topic object will be converted to Channel object.
   * @param topic The topic name.
   * @param pubDate The oldest publish date of the new items.
   * @param text Optional text message, or null.
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, String text) {
    this(topic.toMMXChannelId(), pubDate, text);
  }

  /**
   * @hide
   * Constructor with a lower Topic object for push notification.
   * @param topic The topic name
   * @param pubDate
   * @param title
   * @param body
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, String title, String body) {
    this(topic.toMMXChannelId(), pubDate, title, body);
  }

  /**
   * Get a text message for the wake-up (silent) notification.
   * @return Text message, or null.
   */
  public String getText() {
    return mText;
  }

  /**
   * Get the title for the push notification.  The title is only available for
   * push notification; otherwise, it will be null.
   * @return Title text, or null.
   */
  public String getTitle() {
    return mTitle;
  }

  /**
   * Get the body for the push notification.  The optional body is only
   * available for push notification; otherwise, it will be null.
   * @return Body text, or null.
   */
  public String getBody() {
    return mBody;
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
   * Get the type of this payload.
   * @return Always "pubsub".
   */
  public static String getType() {
    return Constants.PingPongCommand.pubsub.toString();
  }
}
