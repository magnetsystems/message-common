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

/**
 * A payload for non-actionable Notification (UI).
 */
public class Notification {
  @SerializedName("title")
  private String mTitle;
  @SerializedName("body")
  private String mBody;
  @SerializedName("icon")
  private String mIconResName;
  @SerializedName("sound")
  private String mSound;
  @SerializedName("badge")
  private Integer mBadge;
  
  public Notification(String title, String body) {
    mTitle = title;
    mBody = body;
  }
  public String getTitle() {
    return mTitle;
  }
  public Notification setTitle(String title) {
    mTitle = title;
    return this;
  }
  public String getBody() {
    return mBody;
  }
  public Notification setBody(String body) {
    mBody = body;
    return this;
  }
  public String getIconResName() {
    return mIconResName;
  }
  public Notification setIconResName(String iconResName) {
    mIconResName = iconResName;
    return this;
  }
  public String getSound() {
    return mSound;
  }
  public Notification setSound(String sound) {
    mSound = sound;
    return this;
  }
  public Integer getBadge() {
    return mBadge;
  }
  public Notification setBadge(Integer badge) {
    mBadge = badge;
    return this;
  }
  
  public static String getType() {
    return Constants.PingPongCommand.notify.toString();
  }
}
