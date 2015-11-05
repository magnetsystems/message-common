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
import com.magnet.mmx.util.GsonData;
import com.magnet.mmx.util.Utils;

/**
 * @hide
 * This class represents an identifier for a channel under the global name-space
 * or under a user name-space.  Under a global name-space, the channel name is
 * unique within the application.  Under a user name-space, channel name is unique 
 * under a user ID within the application.
 */
public class MMXChannelId implements MMXChannel {
  private transient int mHashCode;
  protected transient String mUserId; // null or human readable user ID.
  @SerializedName("channelName")
  protected String mChannel;
  @SerializedName("userId")
  protected String mEscUserId; // null or XEP-0106 conformed user ID.

  /**
   * @hide
   * Constructor for a global channel.  The channel name is case insensitive.
   * @param channel The channel name.
   */
  public MMXChannelId(String channel) {
    if (channel == null || channel.isEmpty())
      throw new IllegalArgumentException("channel name cannot be null or empty");
    mChannel = channel;
  }

  /**
   * @hide
   * Constructor for a user channel.  The channel name is case insensitive.
   * @param userId The user ID in lower case of the user channel.
   * @param channel The channel name.
   */
  public MMXChannelId(String userId, String channel) {
    if (channel == null || channel.isEmpty())
      throw new IllegalArgumentException("channel name cannot be null or empty");
    mChannel = channel;
    mHashCode = mChannel.toLowerCase().hashCode();
    if (userId != null) {
      if (userId.indexOf('@') >= 0) {
        mUserId = userId;
        mEscUserId = Utils.escapeNode(userId);
      } else if (userId.indexOf('\\') >= 0) {
        mEscUserId = userId;
        mUserId = Utils.unescapeNode(userId);
      } else {
        mEscUserId = mUserId = userId;
      }
    }
  }

  /**
   * Get the friendly user ID of the personal channel.
   * @return A user ID of the personal channel or null for global channel.
   */
  public String getUserId() {
    if (mEscUserId == null)
      return null;
    if (mUserId != null)
      return mUserId;
    return mUserId = Utils.unescapeNode(mEscUserId);
  }

  /**
   * Get the channel name.  The channel name is case insensitive.
   * @return The channel name.
   */
  public String getName() {
    return mChannel;
  }
  
  /**
   * Get the escaped user ID.
   * @return
   */
  public String getEscUserId() {
    return mEscUserId;
  }
  
  /**
   * Check if this channel is under a user name-space.
   * @return true if it is a user channel, false if it is a global channel.
   */
  public boolean isUserChannel() {
    return mEscUserId != null;
  }

  /**
   * Check if two channels are equals.  The channel name is case insensitive.
   * @param channel A channel to be matched
   * @return true if they are equals; otherwise, false.
   */
  public boolean equals(MMXChannel channel) {
    if (channel == this)
      return true;
    if ((channel == null) || (getUserId() == null ^ channel.getUserId() == null))
      return false;
    if ((mUserId != null) && !mUserId.equalsIgnoreCase(channel.getUserId()))
      return false;
    return mChannel.equalsIgnoreCase(channel.getName());
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MMXChannel))
      return false;
    MMXChannel channel = (MMXChannel) obj;
    return equals(channel);
  }
  
  /**
   * Get the hash code based on the lower case of the channel name.
   */
  @Override
  public int hashCode() {
    if (mHashCode == 0) {
      mHashCode = mChannel.toLowerCase().hashCode();
    }
    return mHashCode;
  }
  
  /**
   * Get a string representation of this channel identifier.  Caller must ignore
   * the case of the string representation.
   * @return A string in "&#42/channel" for global channel or "userID/channel" for user channel.
   * @see #parse(String)
   */
  @Override
  public String toString() {
    String userId = getUserId();
    return (userId == null) ? "*/"+mChannel : userId+'/'+mChannel;
  }
  
  /**
   * Convert a string representation of channel identifier to the object.
   * @param channelId The value from {@link #toString()}
   * @return A MMXChannel object.
   * @see #toString()
   */
  public static MMXChannelId parse(String channelId) {
    int slash = channelId.indexOf('/');
    if ((slash == 1) && (channelId.charAt(0) == '*')) {
      return new MMXChannelId(channelId.substring(slash+1));
    } else if (slash >= 1) {
      return new MMXChannelId(channelId.substring(0, slash), channelId.substring(slash+1));
    }
    throw new IllegalArgumentException("Not a valid channel format: "+channelId);
  }
  
  /**
   * @hide
   */
  public static MMXChannelId fromJson(String json) {
    return GsonData.getGson().fromJson(json, MMXChannelId.class);
  }
}
