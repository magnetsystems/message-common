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
  @SerializedName("channelId")
  private String mId;
  @SerializedName("displayName")
  private String mDisplayName;
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

  /**
   * @hide
   * @param userId
   * @param channel A channel name.
   * @param isCollection
   */
  public ChannelInfo(String userId, String channel, boolean isCollection) {
    super(userId, channel);
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
   * Get the channel ID.
   * @return The channel ID.
   */
  public String getId() {
    return mId;
  }

  /**
   * Set the channel ID.
   * @param id The channel ID.
   * @return This object.
   */
  public ChannelInfo setId(String id) {
    this.mId = id;
    return this;
  }

  /**
   * Get the channel display name.
   * @return The display name.
   */
  public String getDisplayName() {
    return mDisplayName;
  }

  /**
   * Set the channel display name.
   * @param displayName The display name, or null.
   * @return This object.
   */
  public ChannelInfo setDisplayName(String displayName) {
    mDisplayName = displayName;
    return this;
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
   * @return This object.
   */
  public ChannelInfo setDescription(String description) {
    mDescription = description;
    return this;
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
  public ChannelInfo setPersistent(boolean persistent) {
    mPersistent = persistent;
    return this;
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
   * @return
   */
  public ChannelInfo setMaxItems(int maxItems) {
    mMaxItems = maxItems;
    return this;
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
   * @return
   */
  public ChannelInfo setMaxPayloadSize(int maxPayloadSize) {
    mMaxPayloadSize = maxPayloadSize;
    return this;
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
   * @return
   */
  public ChannelInfo setCreationDate(Date creationDate) {
    mCreationDate = creationDate;
    return this;
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
  public ChannelInfo setModifiedDate(Date modifiedDate) {
    mModifiedDate = modifiedDate;
    return this;
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
  public ChannelInfo setPublishPermission(TopicAction.PublisherType publisherType) {
    mPublisherType = publisherType;
    return this;
  }

  /**
   * Get the creator JID.
   * @return
   */
  public String getCreator() {
    return mCreator;
  }

  public ChannelInfo setCreator(String creator) {
    mCreator = creator;
    return this;
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
  public ChannelInfo setSubscriptionEnabled(boolean subscriptionEnabled) {
    mSubscriptionEnabled = subscriptionEnabled;
    return this;
  }

  /**
   * Get the channel information in string format for debug purpose.
   * @return Informative data about the channel.
   */
  @Override
  public String toString() {
    return "[channel="+super.toString()+", id="+mId+", name="+mDisplayName+
        ", desc="+mDescription+", sub="+mSubscriptionEnabled+
        ", maxItems="+mMaxItems+", maxSize="+mMaxPayloadSize+
        ", pubtype="+mPublisherType+", create="+mCreationDate+
        ", mod="+mModifiedDate+"]";
  }
}
