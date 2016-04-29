/*   Copyright (c) 2015-2016 Magnet Systems, Inc.
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

import java.io.Serializable;
import java.util.List;

/**
 * The topic options.  If no options are specified, global topics are defaulted
 * with anyone access, published by anyone, unlimited persistent published
 * items, and subscription is enabled.  User topics are defaulted with whitelist
 * access, published by subscribers, unlimited persistent published items, and
 * subscription is enabled.
 * TODO: Review the definition of PublisherType after roles are introduced.
 */
public class MMXTopicOptions implements Serializable {
  private static final long serialVersionUID = 5092103177218064923L;
  private static final Integer DEFAULT_MAX_ITEMS = -1;
  private static final PublisherType DEFAULT_PUB_TYPE = PublisherType.anyone;

  @SerializedName("maxItems")
  private Integer mMaxItems;
  @SerializedName("publisherType")
  private PublisherType mPublisherType;
  @SerializedName("enableSubscription")
  private Boolean mSubscriptionEnabled;
  @SerializedName("displayName")
  private String mDisplayName;
  @SerializedName("description")
  private String mDescription;
  @SerializedName("subscribeOnCreate")
  private boolean mSubscribeOnCreate;
  @SerializedName("whitelist")
  private List<String> mWhiteList;

  /**
   * Get the publisher type.  Default will be {@link PublisherType#anyone}.
   * @return The publisher type.
   */
  public PublisherType getPublisherType() {
    return mPublisherType;
  }

  /**
   * Set the publisher type.  The publisher type states who can publish an
   * item to a topic.
   * @param type Publisher type.
   * @return This object.
   */
  public MMXTopicOptions setPublisherType(PublisherType type) {
    mPublisherType = type;
    return this;
  }

  /**
   * Check if the topic can be subscribed.
   * @return Subscription enabled flag.
   */
  public Boolean isSubscriptionEnabled() {
    return mSubscriptionEnabled;
  }

  /**
   * Allow this topic to be subscribed or not.  Default is true.
   * @param enable True to enable; false to disable.
   * @return This object.
   */
  public MMXTopicOptions setSubscriptionEnabled(boolean enable) {
    mSubscriptionEnabled = enable ? Boolean.TRUE : Boolean.FALSE;
    return this;
  }

  /**
   * Get the maximum number of persistent items.
   * @return Max number of persistent items.
   */
  public Integer getMaxItems() {
    return mMaxItems;
  }

  /**
   * Maximum number of items can be persisted.  If it is not set, the default
   * will be 1.  If the max number of items is 0, no published items will
   * be persisted.  The -1 means unlimited.
   * @param maxItems Max number of published items to be persisted.
   * @return This object.
   */
  public MMXTopicOptions setMaxItems(int maxItems) {
    mMaxItems = maxItems;
    return this;
  }

  /**
   * Get the display name.
   * @return The display name.
   */
  public String getDisplayName() {
    return mDisplayName;
  }

  /**
   * Set the display name of the topic.
   * @param displayName The display name of the topic.
   * @return This object.
   */
  public MMXTopicOptions setDisplayName(String displayName) {
    mDisplayName = displayName;
    return this;
  }

  /**
   * Get an optional description of this topic.
   * @return The description of the topic, or null.
   */
  public String getDescription() {
    return mDescription;
  }

  /**
   * Set an optional description to this topic.
   * @param description The description of the topic.
   * @return This object.
   */
  public MMXTopicOptions setDescription(String description) {
    mDescription = description;
    return this;
  }

  /**
   * Subscribe the topic for the creator when the topic is created.  It is a
   * user subscription, not an end-point subscription.
   * @return
   */
  public boolean isSubscribeOnCreate() {
    return mSubscribeOnCreate;
  }

  /**
   * For topic creation only that topic will be subscribed automatically.  This
   * option is not persisted.
   * @param subscribe true to subscribe on topic creation; otherwise, false.
   * @return This object.
   */
  public MMXTopicOptions setSubscribeOnCreate(boolean subscribe) {
    mSubscribeOnCreate = subscribe;
    return this;
  }

  /**
   * Get the white-list of subscribers if specified.  The list contains just
   * the user ID's (not JID.)
   * @return Null, empty list or a white-listed subscribers.
   */
  public List<String> getWhiteList() {
    return mWhiteList;
  }

  /**
   * Set the white-list of subscribers.  The list can be null or containing
   * user ID's (not JID.)  Currently all user topics are created with whitelist
   * access model.
   * @param whiteList A list of white-listed subscribers, or null.
   * @return This object.
   */
  public MMXTopicOptions setWhiteList(List<String> whiteList) {
    mWhiteList = whiteList;
    return this;
  }

  /**
   * @hide
   * Fill the values with default values if they are not set.
   */
  public void fillWithDefaults() {
    if (mPublisherType == null) {
      mPublisherType = DEFAULT_PUB_TYPE;
    }
    if (mMaxItems == null) {
      mMaxItems = DEFAULT_MAX_ITEMS;
    }
    if (mSubscriptionEnabled == null) {
      mSubscriptionEnabled = Boolean.TRUE;
    }
  }
}
