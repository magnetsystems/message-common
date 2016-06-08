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

import java.util.Date;

/**
 * @hide
 * Protocol channel information from a channel search or list.
 */
public class ChannelInfo extends MMXChannelId implements MMXChannel {

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
  @SerializedName("publishPermission")
  private TopicAction.PublisherType mPublisherType;
  @SerializedName("creatorUserId")
  private String mCreator;
  @SerializedName("subscriptionEnabled")
  private boolean mSubscriptionEnabled;
  @SerializedName("isPushMutedByUser")
  private boolean mPushMutedByUser;
  @SerializedName("pushMutedUntil")
  private Date mPushMutedUntil;

  /**
   * Constructor using id/displayName and/or userId/channel.
   * @param id A unique channel ID.
   * @param displayName Channel display name.
   * @param userId Owner ID for private channel, or null for public channel.
   * @param channel Fully qualified channel name.
   * @param isCollection
   */
  public ChannelInfo(String id, String displayName, String userId, String channel,
                     boolean isCollection) {
    super(id, displayName, userId, channel);
    mCollection = isCollection;
  }

  /**
   * This is for backward compatibility.
   * @param userId Owner ID for private channel, null for public channel.
   * @param channel A channel fully qualified name.
   * @param isCollection
   * @deprecated Use {@link #ChannelInfo(String, String, String, String, boolean)}
   */
  @Deprecated
  public ChannelInfo(String userId, String channel, boolean isCollection) {
    super(null, null, userId, channel);
    mCollection = isCollection;
  }


  /**
   * Check if this channel is a collection (i.e. for subscription only.)
   * @return true for subscribe only; false for publishing and subscription.
   */
  public boolean isCollection() {
    return mCollection;
  }

  /**
   * Get the channel display name.
   * @return The display name.
   */
  @Override
  public String getDisplayName() {
    return mDisplayName;
  }

  /**
   * Get the channel description.
   * @return The description, or null.
   */
  public String getDescription() {
    return mDescription;
  }

  /**
   * Set the channel description.
   * @param description
   */
  public void setDescription(String description) {
    mDescription = description;
  }

  /**
   * Are the published items in this channel persistent?
   * @return true if this channel holds persistent items; otherwise, false.
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
   * Max number of persisted published items to be held in this channel.
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
   * Get the channel creation date/time.
   * @return The channel creation date/time.
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
   * Get the last modified date/time of this channel.
   * @return The last modified date/time.
   */
  public Date getModifiedDate() {
    return mModifiedDate;
  }

  /**
   * @param modifiedDate
   * @return
   */
  public void setModifiedDate(Date modifiedDate) {
    mModifiedDate = modifiedDate;
  }

  /**
   * Get the publishing role to this channel.
   * @return The publisher type.
   */
  public TopicAction.PublisherType getPublishPermission() {
    return mPublisherType;
  }

  /**
   * @param publisherType
   * @return
   */
  public void setPublishPermission(TopicAction.PublisherType publisherType) {
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
   * Is subscription enabled for this channel?
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
   * Get the channel information in string format for debug purpose.
   * @return Informative data about the channel.
   */
  @Override
  public String toString() {
    return "[channel="+super.toString()+", desc="+mDescription+
        ", sub="+mSubscriptionEnabled+", maxItems="+mMaxItems+
        ", maxSize="+mMaxPayloadSize+", pubtype="+mPublisherType+
        ", create="+mCreationDate+", mod="+mModifiedDate+"]";
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
   * @return
   */
  public ChannelInfo setPushMutedByUser(boolean pushMutedByUser) {
    mPushMutedByUser = pushMutedByUser;
    return this;
  }

  /**
   * Get the push muted until date if the channel is muted
   * @return
   */
  public Date getPushMutedUntil() {
    return mPushMutedUntil;
  }


  public ChannelInfo setPushMutedUntil(Date mPushMutedUntil) {
    this.mPushMutedUntil = mPushMutedUntil;
    return this;
  }
}
