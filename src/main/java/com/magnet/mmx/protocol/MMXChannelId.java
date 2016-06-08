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
import com.magnet.mmx.util.ChannelHelper;
import com.magnet.mmx.util.GsonData;
import com.magnet.mmx.util.TopicHelper;

/**
 * @hide
 * This class represents an identifier for a channel under the global name-space
 * or under a user name-space.  Under a global name-space, the channel name is
 * unique within the application.  Under a user name-space, channel name is unique
 * under a user ID within the application.
 */
public class MMXChannelId implements MMXChannel {
  private static final long serialVersionUID = -6101018225170967229L;
  private transient int mHashCode;
  @SerializedName("channelId")
  protected String mId;
  @SerializedName("userId")
  protected String mUserId;
  @SerializedName("channelName")
  protected String mName;
  @SerializedName("displayName")
  protected String mDisplayName;

  /**
   * Get a new instance from a pubsub node ID.
   * @param nodeId Node ID from pubsub.
   * @param displayName Optional display name.
   * @return
   */
  public static MMXChannelId fromNodeId(String nodeId, String displayName) {
    if (nodeId.charAt(0) != ChannelHelper.CHANNEL_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(ChannelHelper.CHANNEL_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(ChannelHelper.CHANNEL_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String userId = nodeId.substring(index1+1, index2);
    String id = nodeId.substring(index2+1);
    if (userId.charAt(0) == ChannelHelper.CHANNEL_FOR_APP) {
      return MMXChannelId.fromId(id, displayName);
    } else {
      return MMXChannelId.fromId(userId + ChannelHelper.CHANNEL_SEPARATOR + id, displayName);
    }
  }

  /**
   * Get a new instance from an internal ID without display name.  It is
   * equivalent to {@link #fromId(String, String)} with a null display name.
   * @param id An internal ID.
   * @return
   */
  public static MMXChannelId fromId(String id) {
    return new MMXChannelId(id, null, null, null);
  }

  /**
   * Get a new instance from an internal ID and display name.
   * @param id An internal ID.
   * @param displayName A display name.
   * @return
   */
  public static MMXChannelId fromId(String id, String displayName) {
    MMXChannelId channel = new MMXChannelId(id, displayName, null, null);
    return channel;
  }

  /**
   * Get a new instance from the channel fully qualified name.
   * @param userId Owner ID of a private channel, or null for public channel.
   * @param name A fully qualified channel name.
   * @return
   */
  public static MMXChannelId fromName(String userId, String name) {
    return new MMXChannelId(userId, name);
  }

  public MMXChannelId() {
    // Used by MMXTopicId as a transformation.
  }

  /**
   * @hide
   * Specify id/displayName, userId/name or both.  The last component in the
   * <code>name</code> is also the display name.  If id/displayName are used,
   * the fully qualified name won't be available.  If both pairs are used, the
   * id/displayName will have higher precedence.
   * @param id A unique topic ID.
   * @param displayName Optional display name.
   * @param userId Owner ID of a private channel, null for public channel.
   * @param name A path-like fully qualified name.
   */
  public MMXChannelId(String id, String displayName, String userId, String name) {
    mDisplayName = displayName;
    mUserId = userId;
    if (((mName = name) != null) && (mDisplayName == null)) {
      int slash = mName.lastIndexOf(ChannelHelper.CHANNEL_DELIM);
      if (slash >= 0) {
        mDisplayName = mName.substring(slash+1);
      } else {
        mDisplayName = mName;
      }
    }
    if (((mId = id) != null) && (mUserId == null)) {
      int hash = mId.indexOf(ChannelHelper.CHANNEL_SEPARATOR);
      if (hash > 0) {
        mUserId = mId.substring(0, hash);
      }
    }
  }

  /**
   * @hide
   * Constructor with a fully qualified channel name.  The channel name is case
   * insensitive.
   * @param userId The user ID in lower case of the user channel.
   * @param name The fully qualified channel name.
   */
  public MMXChannelId(String userId, String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Channel name cannot be null or empty");
    }
    mId = null;
    mUserId = userId;
    mName = name;
    int slash = mName.lastIndexOf(ChannelHelper.CHANNEL_DELIM);
    if (slash >= 0) {
      mDisplayName = mName.substring(slash+1);
    } else {
      mDisplayName = mName;
    }
  }

  /**
   * Set the display name when channel ID is used.
   * @param displayName
   */
  protected void setDisplayName(String displayName) {
    mDisplayName = displayName;
  }

  /**
   * Get the friendly user ID of the personal channel.
   * @return A user ID of the personal channel or null for global channel.
   */
  @Override
  public String getUserId() {
    return mUserId;
  }

  /**
   * Get the fully qualified channel name.  The fully qualified name is case
   * insensitive.
   * @return The channel fully qualified name.
   */
  @Override
  public String getName() {
    return mName;
  }

  /**
   * Get the display name of this channel.  The display name is case sensitive.
   * @return The channel display name.
   */
  @Override
  public String getDisplayName() {
    return mDisplayName;
  }

  /**
   * Get the escaped user ID.
   * @return
   */
  public String getEscUserId() {
    return mUserId;
  }

  /**
   * Check if this channel is under a user name-space.
   * @return true if it is a user channel, false if it is a global channel.
   * @deprecated Use {@link #isPrivateChannel()}
   */
  @Deprecated
  @Override
  public boolean isUserChannel() {
    return isPrivateChannel();
  }

  /**
   * Check if this channel is under a private one under an owner.
   * @return true if it is a private channel, false if it is a public channel.
   */
  public boolean isPrivateChannel() {
    return mUserId != null;
  }

  /**
   * Check if two channels are equals.  The channel fully qualified name is case
   * insensitive.
   * @param channel A channel to be matched
   * @return true if they are equals; otherwise, false.
   */
  @Override
  public boolean equals(MMXChannel channel) {
    if (channel == this) {
      return true;
    }
    if ((channel == null) || (mId == null ^ channel.getId() == null)) {
      return false;
    }
    if (mId != null && !mId.equalsIgnoreCase(channel.getId())) {
      return false;
    }
    if ((mUserId == null ^ channel.getUserId() == null) ||
        (mUserId != null && !mUserId.equalsIgnoreCase(channel.getUserId()))) {
      return false;
    }
    if ((mName == null ^ channel.getName() == null) ||
        (mName != null && mName.equalsIgnoreCase(channel.getName()))) {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MMXChannel)) {
      return false;
    }
    MMXChannel channel = (MMXChannel) obj;
    return equals(channel);
  }

  /**
   * Get the hash code based on the lower case of the channel name.
   */
  @Override
  public int hashCode() {
    if (mHashCode == 0) {
      mHashCode = (mId != null) ? mId.hashCode() :
        (mName.toLowerCase().hashCode() + ((mUserId != null) ? mUserId.hashCode() : 0));
    }
    return mHashCode;
  }

  /**
   * Get a string representation of this channel identifier.
   * @return A string in "channel" for global channel or "userID#channel" for user channel.
   * @see #parse(String)
   */
  @Override
  public String toString() {
    // TODO: for backward compatibility return the channel ID.
    return getId();
//    return "{ id="+mId+", userId="+mUserId+", name="+mName+" }";
  }

  /**
   * Get the channel ID.  The channel ID can be used in the URL path.  Currently
   * the ID is derived from the name.  TODO: decouple the ID from the channel name.
   * @return The ID in the form of "userID#ID" or "ID".
   */
  @Override
  public String getId() {
    return mId;
  }

  /**
   * Convert a string representation of channel identifier to the object.
   * @param channelId The value from {@link #toString()}
   * @return A MMXChannel object.
   * @see #toString()
   */
  public static MMXChannelId parse(String channelId) {
    return new MMXChannelId(channelId, null, null, null);
  }

  /**
   * @hide
   */
  public static MMXChannelId fromJson(String json) {
    return GsonData.getGson().fromJson(json, MMXChannelId.class);
  }
}
