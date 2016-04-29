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
 * The payload for Message wakeup (silent notification) or push notification.
 * Any reserved properties (e.g. title, body, sound, badge) used by APNS can be
 * added to this JSON payload.
 */
public class MessageNotification {
  @SerializedName("text")
  private final String mText;
  @SerializedName("sentDate")
  private final Date mSentDate;
  @SerializedName("sender")
  private final MMXid mSender;
  @SerializedName("title")
  private final String mTitle;
  @SerializedName("body")
  private final String mBody;
  @SerializedName("sound")
  private final String mSound;

  /**
   * @hide
   * Constructor for wake-up (silent notification.)
   * @param sentDate The sent date of the new message.
   * @param sender The sender of the new message.
   * @param text Optional text message, or null
   */
  public MessageNotification(Date sentDate, MMXid sender, String text) {
    mSentDate = sentDate;
    mSender = sender;
    mText = text;
    mTitle = null;
    mBody = null;
    mSound = null;
  }

  /**
   * @hide
   * Constructor for push notification.  The optional text
   * will be set to the <code>title</code> for backward compatibility.
   * If a sound file name is specified in <code>sound</code>, it will
   * be platform dependent.  It is recommended to use either "default"
   * or null for portability.
   * @param sentDate The sent date of the new message.
   * @param sender The sender of the new message.
   * @param title A non-null title for push notification.
   * @param body An optional body for push notification.
   * @param sound "default", name-of-sound-file, or null.
   */
  public MessageNotification(Date sentDate, MMXid sender, String title,
                            String body, String sound) {
    mSentDate = sentDate;
    mSender = sender;
    mText = title;
    mTitle = title;
    mBody = body;
    mSound = sound;
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
   * Get the sent date of the new message.
   * @return The sent date of the new message.
   */
  public Date getSentDate() {
    return mSentDate;
  }

  /**
   * Get the sender of the new message.
   * @return The sender of the new message.
   */
  public MMXid getSender() {
    return mSender;
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
   * @return Always "retrieve".
   */
  public static String getType() {
    return Constants.PingPongCommand.retrieve.toString();
  }
}
