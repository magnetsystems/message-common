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

import java.util.Map;

/**
 * GCM push payload from client to client.  The mmx dictionary contains at least
 * {@link #KEY_CALLBACK_URL}, {@link #KEY_CUSTOM_CONTENT}, {@link #KEY_PUSH_ID},
 * {@link #KEY_TYPE} properties.  
 * 
 * TODO: this class should be renamed to PushPayload.
 */
public class GCMPayload {
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getSound() {
    return sound;
  }

  public void setSound(String sound) {
    this.sound = sound;
  }

  public Integer getBadge() {
    return badge;
  }

  public void setBadge(Integer badge) {
    this.badge = badge;
  }

  public Map<String, ? super Object> getMmx() {
    return mmx;
  }

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
