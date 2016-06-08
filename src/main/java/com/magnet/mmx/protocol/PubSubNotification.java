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
 * The payload for PubSub wakeup (silent notification) or push notification.
 * Any reserved properties (e.g. title, body, sound, badge) used by APNS can be
 * added to this JSON payload.
 */
public class PubSubNotification {
  @SerializedName("text")
  private final String mText;
  @SerializedName("channel")
  private final MMXChannelId mChannel;
  @SerializedName("publishDate")
  private final Date mPublishDate;
  @SerializedName("publisher")
  private final MMXid mPublisher;
  @SerializedName("title")
  private final String mTitle;
  @SerializedName("body")
  private final String mBody;
  @SerializedName("sound")
  private final String mSound;
  @SerializedName("badge")
  private final Integer mBadge;

  /**
   * @hide
   * Constructor with a Channel object for wake-up (silent notification.)
   * @param channel The channel name.
   * @param pubDate The oldest publish date among the new items.
   * @param publisher The oldest publisher among the new items.
   * @param text Optional text message, or null
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, MMXid publisher,
                            String text) {
    mChannel = channel;
    mPublishDate = pubDate;
    mPublisher = publisher;
    mText = text;
    mTitle = null;
    mBody = null;
    mSound = null;
    mBadge = null;
  }

  /**
   * @hide
   * Constructor with a Channel object for push notification without sound.  The
   * optional text will be set to the <code>title</code> for backward
   * compatibility.
   * @param channel The channel name.
   * @param pubDate The oldest publish date of the new items.
   * @param title A non-null title for push notification.
   * @param body An optional body for push notification.
   * @param badge An optional badge for push notification.
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, MMXid publisher,
                            String title, String body, Integer badge) {
    this(channel, pubDate, publisher, title, body, null, badge);
  }

  /**
   * @hide
   * Constructor with a Channel object for push notification.  The optional text
   * will be set to the <code>title</code> for backward compatibility.  If a
   * sound name is specified in <code>sound</code>, it will be platform
   * dependent.  It is recommended to use either "default" or null for
   * portability.
   * @param channel The channel name.
   * @param pubDate The oldest publish date among the new items.
   * @param publisher The oldest publisher among the new items.
   * @param title A non-null title for push notification.
   * @param body An optional body for push notification.
   * @param sound "default", name-of-sound-file, or null.
   * @param badge An option badge for push notification.
   */
  public PubSubNotification(MMXChannelId channel, Date pubDate, MMXid publisher,
                            String title, String body, String sound, Integer badge) {
    mChannel = channel;
    mPublishDate = pubDate;
    mPublisher = publisher;
    mText = title;
    mTitle = title;
    mBody = body;
    mSound = sound;
    mBadge = badge;
  }

  /**
   * @hide
   * Constructor with a Topic object for wake-up (silent notification.)
   * The Topic object will be converted to Channel object.
   * @param topic The topic name.
   * @param pubDate The oldest publish date of the new items.
   * @param publisher The publisher.
   * @param text Optional text message, or null.
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, MMXid publisher,
                            String text) {
    this(topic.toMMXChannelId(), pubDate, publisher, text);
  }

  /**
   * @hide
   * Constructor with a Topic object for push notification without sound.
   * @param topic The topic name
   * @param pubDate
   * @param publisher
   * @param title
   * @param body
   * @param badge
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, MMXid publisher,
                            String title, String body, Integer badge) {
    this(topic.toMMXChannelId(), pubDate, publisher, title, body, null, badge);
  }

  /**
   * @hide
   * Constructor with a Topic object for push notification.  If a sound name
   * is specified in <code>sound</code>, it will be platform dependent.  It is
   * recommended to use either "default" or null for portability.
   * @param topic The topic name
   * @param pubDate
   * @param publisher
   * @param title
   * @param body
   * @param sound "default", sound-file-name, or null.
   */
  public PubSubNotification(MMXTopicId topic, Date pubDate, MMXid publisher,
                            String title, String body, String sound, Integer badge) {
    this(topic.toMMXChannelId(), pubDate, publisher, title, body, sound, badge);
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
   * Get the published date of the new item.
   * @return The published date of the new item.
   */
  public Date getPublishDate() {
    return mPublishDate;
  }

  /**
   * Get the publisher of the new item.
   * @return The publisher of the new item.
   */
  public MMXid getPublisher() {
    return mPublisher;
  }

  /**
   * Get the sound to be used for notification.  The value "default" is reserved
   * for default notification sound.
   * @return null or a sound file name.
   */
  public String getSound() {
    return mSound;
  }

  /**
   * Get the type of this payload.
   * @return Always "pubsub".
   */
  public static String getType() {
    return Constants.PingPongCommand.pubsub.toString();
  }

  /**
   * Get the badge to be used for notification.
   * @return null if not used; otherwise, a number.
   */
  public Integer getBadge() {
    return mBadge;
  }
}
