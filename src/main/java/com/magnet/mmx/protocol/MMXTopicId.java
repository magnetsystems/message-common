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
import com.magnet.mmx.util.TopicHelper;

/**
 * @hide
 * This class represents an identifier for a topic under the global name-space
 * or under a user name-space.  Under a global name-space, the topic name is
 * unique within the application.  Under a user name-space, topic name is unique
 * under a user ID within the application.
 */
public class MMXTopicId implements MMXTopic {
  private static final long serialVersionUID = -6889781636342385244L;
  private transient int mHashCode;
  @SerializedName("topicId")
  protected String mId;
  @SerializedName("userId")
  protected String mUserId;
  @SerializedName("topicName")
  protected String mName;
  @SerializedName("displayName")
  protected String mDisplayName;

  /**
   * Get a new instance from a pubsub node ID.
   * @param nodeId Node ID from pubsub.
   * @param displayName Optional display name.
   * @return
   */
  public static MMXTopicId fromNodeId(String nodeId, String displayName) {
    if (nodeId.charAt(0) != TopicHelper.TOPIC_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(TopicHelper.TOPIC_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(TopicHelper.TOPIC_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String userId = nodeId.substring(index1+1, index2);
    String id = nodeId.substring(index2+1);
    if (userId.charAt(0) == TopicHelper.TOPIC_FOR_APP) {
      return MMXTopicId.fromId(id, displayName);
    } else {
      return MMXTopicId.fromId(userId + TopicHelper.TOPIC_SEPARATOR + id, displayName);
    }
  }

  /**
   * Get a new instance from a unique id without a display name.
   * @param id An internal ID format.
   * @return
   */
  public static MMXTopicId fromId(String id) {
    return new MMXTopicId(id, null, null, null);
  }

  /**
   * Get a new instance from a unique id and display name.
   * @param id An internal ID format.
   * @param displayName
   * @return
   */
  public static MMXTopicId fromId(String id, String displayName) {
    return new MMXTopicId(id, displayName, null, null);
  }

  /**
   * Get a new instance from a fully qualified topic name.
   * @param userId Owner ID of a user topic, or null for global topic.
   * @param name Path-like fully qualified names.
   * @return
   */
  public static MMXTopicId fromName(String userId, String name) {
    return new MMXTopicId(null, null, userId, name);
  }

  /**
   * Get a new instance from a unique id, and a fully qualified topic name.
   * @param id A unique topic ID.
   * @param userId Owner ID of a user topic, or null for global topic.
   * @param name Path-like fully qualified name.
   * @return
   */
  public static MMXTopicId fromIdName(String id, String userId, String name) {
    return new MMXTopicId(id, null, userId, name);
  }
  /**
   * @hide
   * Constructor to convert MMXTopic into MMXTopicId
   * @param topic
   */
  public MMXTopicId(MMXTopic topic) {
    if (topic == null) {
      throw new IllegalArgumentException("topic cannot be null");
    }
    mId = topic.getId();
    mUserId = topic.getUserId();
    mName = topic.getName();
    mDisplayName = topic.getDisplayName();
  }

  /**
   * @hide
   * Specify id/displayName, userId/name or both.  The last component in the
   * <code>name</code> is also the display name.
   * @param id A unique topic ID.
   * @param displayName Optional display name.
   * @param userId Owner ID of a user topic, null for global topic.
   * @param name A path-like fully qualified name.
   */
  protected MMXTopicId(String id, String displayName, String userId, String name) {
    mId = id;
    mDisplayName = displayName;
    mUserId = userId;
    if (((mName = name) != null) && (mDisplayName == null)) {
      int slash = mName.lastIndexOf(TopicHelper.TOPIC_DELIM);
      if (slash >= 0) {
        mDisplayName = mName.substring(slash+1);
      } else {
        mDisplayName = mName;
      }
    }
  }

//  /**
//   * @hide
//   * Constructor with the topic ID.  No qualified name is available.  This
//   * constructor is only used by the client if the ID is known.
//   * @param topic The topic id.
//   */
//  protected MMXTopicId(String id) {
//    if (id == null || id.isEmpty()) {
//      throw new IllegalArgumentException("ID cannot be null or empty");
//    }
//    mId = id;
//    int hash = mId.indexOf(TopicHelper.TOPIC_SEPARATOR);
//    if (hash > 0) {
//      mUserId = mId.substring(0, hash);
//    }
//    mName = null;
//    mDisplayName = null;
//  }

  /**
   * @hide
   * Constructor with the topic fully qualified name.  The fully qualified name
   * is case insensitive and may have a path-like syntax.  This constructor is
   * only used by the client only if the ID is not known.
   * @param userId The user ID in lower case of the user topic.
   * @param name The topic fully qualified name.
   */
  protected MMXTopicId(String userId, String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Topic name cannot be null or empty");
    }
    mId = null;
    mUserId = userId;
    mName = name;
    int slash = mName.lastIndexOf(TopicHelper.TOPIC_DELIM);
    if (slash >= 0) {
      mDisplayName = mName.substring(slash+1);
    } else {
      mDisplayName = mName;
    }
  }

  protected void setId(String id) {
    this.mId = id;
  }

  protected void setDisplayName(String displayName) {
    this.mDisplayName = displayName;
  }

  /**
   * Get the friendly user ID of the personal topic.
   * @return A user ID of the personal topic or null for global topic.
   */
  @Override
  public String getUserId() {
    return mUserId;
  }

  /**
   * Get the topic fully qualified name.  The fully qualified name is case
   * insensitive and may have a path-like syntax.
   * @return The topic fully qualified name.
   */
  @Override
  public String getName() {
    return mName;
  }

  /**
   * Get the display name of this topic.  The display name is case sensitive.
   * @return The display name if available.
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
   * Check if this topic is under a user name-space.
   * @return true if it is a user topic, false if it is a global topic.
   */
  @Override
  public boolean isUserTopic() {
    return mUserId != null;
  }

  /**
   * Check if two topics are equals.  The topic name is case insensitive.
   * @param topic A topic to be matched
   * @return true if they are equals; otherwise, false.
   */
  @Override
  public boolean equals(MMXTopic topic) {
    if (topic == this) {
      return true;
    }
    if ((topic == null) || (mId == null ^ topic.getId() == null)) {
      return false;
    }
    if (mId != null && !mId.equalsIgnoreCase(topic.getId())) {
      return false;
    }
    if ((mUserId == null ^ topic.getUserId() == null) ||
        ((mUserId != null) && !mUserId.equalsIgnoreCase(topic.getUserId()))) {
      return false;
    }
    if ((mName == null ^ topic.getName() == null) ||
        (mName != null && mName.equalsIgnoreCase(topic.getName()))) {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MMXTopic)) {
      return false;
    }
    MMXTopic topic = (MMXTopic) obj;
    return equals(topic);
  }

  /**
   * Get the hash code based on topic ID or the hash of userID and topic name.
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
   * Get a string representation of this topic identifier for debug purpose.
   * Currently it returns {@link #getId()} for backward compatibility.
   * @return A topic in string.
   */
  @Override
  public String toString() {
//  return "{ id="+mId+", userId="+mUserId+", name="+mName+" }";
    // TODO: for backward compatibility?
    return getId();
  }

  /**
   * Get the topic ID.  The topic ID can be used in the URL path.
   * @return The topic ID.
   * @see #parse(String)
   */
  @Override
  public String getId() {
    return mId;
  }

  /**
   * Convert a topic ID to an object.
   * @param topicId The value from {@link #getId()}
   * @return A MMXTopic object.
   * @see #getId()
   */
  public static MMXTopicId parse(String topicId) {
    return MMXTopicId.fromId(topicId, null);
  }

  /**
   * @hide
   */
  public static MMXTopicId fromJson(String json) {
    return GsonData.getGson().fromJson(json, MMXTopicId.class);
  }

  /**
   * Transform this object to MMXChannelId.
   * @return
   */
  public MMXChannelId toMMXChannelId() {
    MMXChannelId channelId = new MMXChannelId();
    channelId.mId = this.mId;
    channelId.mName = this.mName;
    channelId.mUserId = this.mUserId;
    return channelId;
  }
}
