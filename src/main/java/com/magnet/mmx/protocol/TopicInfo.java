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
import com.magnet.mmx.protocol.TopicAction.PublisherType;

import java.util.Date;

/**
 * @hide
 * Protocol topic information from a topic search or list.
 */
public class TopicInfo extends MMXTopicId implements MMXTopic {
  private static final long serialVersionUID = -5212539242296015590L;
  @SerializedName("isCollection")
  private final boolean mCollection;
  @SerializedName("description")
  private String mDescription;
  @SerializedName("isPersistent")
  private boolean mPersistent;
  @SerializedName("maxItems")
  private int mMaxItems;
  @SerializedName("maxPayloadSize")
  private int mMaxPayloadSize;
  @SerializedName("creationDate")
  private Date mCreationDate;
  @SerializedName("modificationDate")
  private Date mModifiedDate;
  @SerializedName("publisherType")
  private PublisherType mPublisherType;
  @SerializedName("creator")
  private String mCreator;
  @SerializedName("subscriptionEnabled")
  private boolean mSubscriptionEnabled;

  @SerializedName("isPushMutedByUser")
  private boolean mPushMutedByUser;

  @SerializedName("pushMutedUntil")
  private Date mPushMutedUntil;


  /**
   * @hide
   * Constructor for global topic or user topic.  The <code>topicId</code> is
   * in the form of [userID#]ID as a transformed component in node ID.
   * @param topicId Topic ID in the form of [userID#]ID.
   * @param displayName The topic display name.
   * @param isCollection
   */
  public TopicInfo(String topicId, String displayName, boolean isCollection) {
    super(topicId, displayName, null, null);
    mCollection = isCollection;
  }

  /**
   * Check if this topic is a collection (i.e. for subscription only.)
   * @return true for subscribe only; false for publishing and subscription.
   */
  public boolean isCollection() {
    return mCollection;
  }

  /**
   * Get the topic description.
   * @return The description, or null.
   */
  public String getDescription() {
    return mDescription;
  }

  /**
   * Set the topic description.
   * @param description
   */
  public void setDescription(String description) {
    mDescription = description;
  }

  /**
   * Are the published items in this topic persistent?
   * @return true if this topic holds persistent items; otherwise, false.
   */
  public boolean isPersistent() {
    return mPersistent;
  }

  /**
   * @param persistent
   * @return
   */
  public void setPersistent(boolean persistent) {
    mPersistent = persistent;
  }

  /**
   * Max number of persisted published items to be held in this topic.
   * @return Maximum number of persisted published items.
   */
  public int getMaxItems() {
    return mMaxItems;
  }

  /**
   * @hide
   * @param maxItems
   */
  public void setMaxItems(int maxItems) {
    mMaxItems = maxItems;
  }

  /**
   * Get the max payload size of published items.
   * @return The configured maximum payload size.
   */
  public int getMaxPayloadSize() {
    return mMaxPayloadSize;
  }

  /**
   * @hide
   * @param maxPayloadSize
   */
  public void setMaxPayloadSize(int maxPayloadSize) {
    mMaxPayloadSize = maxPayloadSize;
  }

  /**
   * Get the topic creation date/time.
   * @return The topic creation date/time.
   */
  public Date getCreationDate() {
    return mCreationDate;
  }

  /**
   * @param creationDate
   */
  public void setCreationDate(Date creationDate) {
    mCreationDate = creationDate;
  }

  /**
   * Get the last modified date/time of this topic.
   * @return The last modified date/time.
   */
  public Date getModifiedDate() {
    return mModifiedDate;
  }

  /**
   * @param modifiedDate
   */
  public void setModifiedDate(Date modifiedDate) {
    mModifiedDate = modifiedDate;
  }

  /**
   * Get the publishing role to this topic.
   * @return The publisher type.
   */
  public PublisherType getPublisherType() {
    return mPublisherType;
  }

  /**
   * @param publisherType
   */
  public void setPublisherType(PublisherType publisherType) {
    mPublisherType = publisherType;
  }

  /**
   * Get the creator JID.
   * @return
   */
  public String getCreator() {
    return mCreator;
  }

  public void setCreator(String creator) {
    mCreator = creator;
  }

  /**
   * Is subscription enabled for this topic?
   * @return
   */
  public boolean isSubscriptionEnabled() {
    return mSubscriptionEnabled;
  }

  /**
   * @param subscriptionEnabled
   * @return
   */
  public void setSubscriptionEnabled(boolean subscriptionEnabled) {
    mSubscriptionEnabled = subscriptionEnabled;
  }


  /**
   * Is push muted by user for this topic?
   * @return
   */
  public boolean isPushMutedByUser() {
    return mPushMutedByUser;
  }

  /**
   * @param pushMutedByUser
   */
  public void setPushMutedByUser(boolean pushMutedByUser) {
    mPushMutedByUser = pushMutedByUser;
  }


  /**
   * Get the topic information in string format for debug purpose.
   * @return Informative data about the topic.
   */
  @Override
  public String toString() {
    return "[topic="+super.toString()+", id="+mId+", name="+mDisplayName+
        ", desc="+mDescription+", sub="+mSubscriptionEnabled+
        ", maxItems="+mMaxItems+", maxSize="+mMaxPayloadSize+
        ", pubtype="+mPublisherType+", create="+mCreationDate+
        ", mod="+mModifiedDate+"]";
  }


  /**
   * Get the push muted until date if the channel is muted
   * @return
   */
  public Date getPushMutedUntil() {
    return mPushMutedUntil;
  }


  public void setPushMutedUntil(Date mPushMutedUntil) {
    this.mPushMutedUntil = mPushMutedUntil;
  }
}
