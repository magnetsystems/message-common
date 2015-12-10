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

import java.io.Serializable;
import java.util.Map;

/**
 * Push payload from client to client.  The mmx dictionary contains at least
 * {@link #KEY_CALLBACK_URL}, {@link #KEY_CUSTOM_CONTENT}, {@link #KEY_PUSH_ID},
 * {@link #KEY_TYPE} properties.
 */
public class GCMPayload implements Serializable {
  private static final long serialVersionUID = 1678052400350808468L;
  /**
   * MMX dictionary key for the unique push ID.
   */
  public final static String KEY_PUSH_ID = Constants.PAYLOAD_ID_KEY;
  /**
   * MMX dictionary key for the callback URL.
   */
  public final static String KEY_CALLBACK_URL = Constants.PAYLOAD_CALLBACK_URL_KEY;
  /**
   * MMX dictionary key for the custom content type.
   */
  public final static String KEY_TYPE = Constants.PAYLOAD_TYPE_KEY;
  /**
   * MMX dictionary key for the custom content.
   */
  public final static String KEY_CUSTOM_CONTENT = Constants.PAYLOAD_CUSTOM_KEY;

  @SerializedName(Constants.PAYLOAD_PUSH_TITLE)
  private String title;
  @SerializedName(Constants.PAYLOAD_PUSH_BODY)
  private String body;
  @SerializedName(Constants.PAYLOAD_PUSH_ICON)
  private String icon;
  @SerializedName(Constants.PAYLOAD_PUSH_SOUND)
  private String sound;
  @SerializedName(Constants.PAYLOAD_PUSH_BADGE)
  private Integer badge;
  @SerializedName(Constants.PAYLOAD_MMX_KEY)
  private Map<String, ? super Object> mmx;

  /**
   * Get the optional title.
   * @return A title or null.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set the title.
   * @param title Title text, or null.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get the optional content text.
   * @return Content text, or null.
   */
  public String getBody() {
    return body;
  }

  /**
   * Set the content text.
   * @param body Content text.
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * Get the optional icon name.  The icon name will be mapped to an icon
   * resource ID in the target Android application.
   * @return An icon name, or null.
   */
  public String getIcon() {
    return icon;
  }

  /**
   * Set the icon to be displayed in the notification.
   * @param icon The icon resource name, or null.
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /**
   * Get the optional sound name.  Currently the custom sound is not
   * implemented in Android, but used to enable/disable default notification
   * ring-tone.
   * @return A sound name or null.
   */
  public String getSound() {
    return sound;
  }

  /**
   * Set the sound name to be used for notification.  In Android, the value is
   * ignored but used to enable/disable the default notification ring-tone.
   * @param sound A sound name or null.
   */
  public void setSound(String sound) {
    this.sound = sound;
  }

  /**
   * Get the optional badge (iOS) or number at the right-hand side of the
   * notification (Android.)
   * @return A numeric value or null.
   */
  public Integer getBadge() {
    return badge;
  }

  /**
   * Set a badge in iOS or a number at the right-hand side of the notification
   * in Android.
   * @param badge A numeric value or null.
   */
  public void setBadge(Integer badge) {
    this.badge = badge;
  }

  /**
   * Get an optional dictionary encapsulating non-UI properties.  Some of the
   * properties are created by the MMX Push subsystem.
   * @return A dictionary or null.
   */
  public Map<String, ? super Object> getMmx() {
    return mmx;
  }

  /**
   * @hide
   * Set a dictionary encapsulating non-UI properties.  It is intended for
   * internal use.
   * @param mmx A dictionary.
   */
  public void setMmx(Map<String, ? super Object> mmx) {
    this.mmx = mmx;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (title != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_PUSH_TITLE).append("=\"").append(title).append('"');
    }
    if (body != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_PUSH_BODY).append("=\"").append(body).append('"');
    }
    if (icon != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_PUSH_ICON).append("=\"").append(icon).append('"');
    }
    if (sound != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_PUSH_SOUND).append("=\"").append(sound).append('"');
    }
    if (badge != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_PUSH_BADGE).append('=').append(badge);
    }
    if (mmx != null) {
      if (sb.length() > 0) sb.append(", ");
      sb.append(Constants.PAYLOAD_MMX_KEY).append('=').append(mmx);
    }
    return sb.toString();
  }
  
  public static String getType() {
    return "gcmpayload";
  }
}
