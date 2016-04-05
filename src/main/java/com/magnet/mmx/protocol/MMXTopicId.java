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
 * This class represents an identifier for a topic under the global name-space
 * or under a user name-space.  Under a global name-space, the topic name is
 * unique within the application.  Under a user name-space, topic name is unique
 * under a user ID within the application.
 */
public class MMXTopicId implements MMXTopic {
  private static final long serialVersionUID = -6889781636342385244L;
  private transient int mHashCode;
  protected transient String mUserId; // null or human readable user ID.
  @SerializedName("topicName")
  protected String mTopic;
  @SerializedName("userId")
  protected String mEscUserId; // null or XEP-0106 conformed user ID.

  /**
   * @hide
   * Constructor to convert MMXTopic into MMXTopicId
   * @param topic
   */
  public MMXTopicId(MMXTopic topic) {
    if (topic == null) {
      throw new IllegalArgumentException("topic cannot be null");
    }
    mUserId = topic.getUserId();
    mEscUserId = Utils.escapeNode(mUserId);
    mTopic = topic.getName();
  }

  /**
   * @hide
   * Constructor for a global topic.  The topic name is case insensitive.
   * @param topic The topic name.
   */
  public MMXTopicId(String topic) {
    if (topic == null || topic.isEmpty()) {
      throw new IllegalArgumentException("topic name cannot be null or empty");
    }
    mTopic = topic;
  }

  /**
   * @hide
   * Constructor for a user topic.  The topic name is case insensitive.
   * @param userId The user ID in lower case of the user topic.
   * @param topic The topic name.
   */
  public MMXTopicId(String userId, String topic) {
    if (topic == null || topic.isEmpty()) {
      throw new IllegalArgumentException("topic name cannot be null or empty");
    }
    mTopic = topic;
    mHashCode = mTopic.toLowerCase().hashCode();
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
   * Get the friendly user ID of the personal topic.
   * @return A user ID of the personal topic or null for global topic.
   */
  @Override
  public String getUserId() {
    if (mEscUserId == null) {
      return null;
    }
    if (mUserId != null) {
      return mUserId;
    }
    return mUserId = Utils.unescapeNode(mEscUserId);
  }

  /**
   * Get the topic name.  The topic name is case insensitive.
   * @return The topic name.
   */
  @Override
  public String getName() {
    return mTopic;
  }

  /**
   * Get the escaped user ID.
   * @return
   */
  public String getEscUserId() {
    return mEscUserId;
  }

  /**
   * Check if this topic is under a user name-space.
   * @return true if it is a user topic, false if it is a global topic.
   */
  @Override
  public boolean isUserTopic() {
    return mEscUserId != null;
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
    if ((topic == null) || (getUserId() == null ^ topic.getUserId() == null)) {
      return false;
    }
    if ((mUserId != null) && !mUserId.equalsIgnoreCase(topic.getUserId())) {
      return false;
    }
    return mTopic.equalsIgnoreCase(topic.getName());
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
   * Get the hash code based on the lower case of the topic name.
   */
  @Override
  public int hashCode() {
    if (mHashCode == 0) {
      mHashCode = mTopic.toLowerCase().hashCode();
    }
    return mHashCode;
  }

  /**
   * Get a string representation of this topic identifier.  Caller must ignore
   * the case of the string representation.
   * @return A string in "&#42/topic" for global topic or "userID/topic" for user topic.
   * @see #parse(String)
   */
  @Override
  public String toString() {
    String userId = getUserId();
    return (userId == null) ? "*/"+mTopic : userId+'/'+mTopic;
  }

  /**
   * Construct to a URL path component for this topic.
   * @return The path in the form of "[userID#]topic".
   */
  public String toPath() {
    String userId = getUserId();
    return (userId == null) ? mTopic : userId+'#'+mTopic;
  }

  /**
   * Convert a string representation of topic identifier to the object.
   * @param topicId The value from {@link #toString()}
   * @return A MMXTopic object.
   * @see #toString()
   */
  public static MMXTopicId parse(String topicId) {
    int slash = topicId.indexOf('/');
    if ((slash == 1) && (topicId.charAt(0) == '*')) {
      return new MMXTopicId(topicId.substring(slash+1));
    } else if (slash >= 1) {
      return new MMXTopicId(topicId.substring(0, slash), topicId.substring(slash+1));
    }
    throw new IllegalArgumentException("Not a valid topic format: "+topicId);
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
    channelId.mChannel = this.mTopic;
    channelId.mEscUserId = this.mEscUserId;
    channelId.mUserId = this.mUserId;
    return channelId;
  }
}
