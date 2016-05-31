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
import com.magnet.mmx.protocol.SearchAction.Match;
import com.magnet.mmx.protocol.SearchAction.MultiValues;
import com.magnet.mmx.protocol.SearchAction.Operator;
import com.magnet.mmx.protocol.SearchAction.SingleValue;
import com.magnet.mmx.util.GsonData;
import com.magnet.mmx.util.JSONifiable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the PubSub protocols and operations.
 *
 */
public class ChannelAction {

  /**
   * @hide
   * Filter for channel listings.
   */
  public static enum ListType {
    /**
     * List global channels only
     */
    global,
    /**
     * List personal channels only.
     */
    personal,
    /**
     * List both global and personal channels.
     */
    both,
  }

  /**
   * The channel tags.
   */
  public static class ChannelTags extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("tags")
    private final List<String> mTags;
    @SerializedName("lastModTime")
    private Date mLastModTime;
    private transient MMXChannel mMMXChannel;

    /**
     * @hide
     * Constructor for the request of setting the tags.  Setting an empty list
     * will remove all tags.
     * @param userId The user ID for a personal channel, null for global channel.
     * @param channel The channel name.
     * @param tags A list of tags or an empty list.
     */
    public ChannelTags(String userId, String channel, List<String> tags) {
      mUserId = userId;
      mChannel = channel;
      mTags = tags;
    }

    /**
     * @hide
     * Constructor for the response of getting the tags.
     * @param userId The user ID for a personal channel, null for global channel.
     * @param channel The channel name.
     * @param tags A list of tags or an empty list.
     * @param lastModTime The last modified time.
     */
    public ChannelTags(String userId, String channel, List<String> tags, Date lastModTime) {
      mUserId = userId;
      mChannel = channel;
      mTags = tags;
      mLastModTime = lastModTime;
    }

    /**
     * @hide
     * Get the user ID of the personal channel.
     * @return The user ID of personal channel, or null for global channel.
     */
    public String getUserId() {
      return mUserId;
    }

    /**
     * @hide
     * Get the channel name.
     * @return A channel name.
     */
    public String getChannelName() {
      return mChannel;
    }

    /**
     * Get the channel associated with these tags.
     * @return The channel.
     */
    public MMXChannel getChannel() {
      if (mMMXChannel == null) {
        mMMXChannel = new MMXChannelId(mUserId, mChannel);
      }
      return mMMXChannel;
    }

    /**
     * Get the tags.
     * @return A list of tags or an empty list.
     */
    public List<String> getTags() {
      return mTags;
    }

    /**
     * Get the last modified time.
     * @return Last modified time.
     */
    public Date getLastModTime() {
      return mLastModTime;
    }

    @Override
    public String toString() {
      return "[userId="+mUserId+", channel="+mChannel+", modTime="+mLastModTime+
              ", tags="+mTags+"]";
    }

    /**
     * @hide
     */
    public static ChannelTags fromJson(String json) {
      return GsonData.getGson().fromJson(json, ChannelTags.class);
    }
  }

  /**
   * @hide
   * Request payload for creating a channel.  The channel to be created can always
   * be published and subscribed.
   */
  public static class CreateRequest extends JSONifiable {
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("isPersonal")
    private final boolean mPersonal;
    @SerializedName("isCollection")
    private final boolean mCollection;
    @SerializedName("options")
    private final MMXTopicOptions mOptions;
    @SerializedName("roles")
    private final List<String> roles;

    /**
     * Default constructor.  A path-like channel name provides a simplified syntax
     * in hierarchical form.  The channelName must not be started or ended with '/',
     * and each node in the path can only be alphanumeric or '_'.  All the
     * parent nodes will be created automatically and they can be subscribed.
     * @param channelName A path like channel name.
     * @param isPersonal True for personal channel; false for global channel.
     * @param options A creation options or null.
     * @param roles a list of roles.
     */
    public CreateRequest(String channelName, boolean isPersonal,
                         MMXTopicOptions options,  List<String> roles) {
      mChannel = channelName;
      mPersonal = isPersonal;
      mCollection = false;
      mOptions = options;
      this.roles = roles;
    }

    /**
     * Default constructor.  A path-like channel name provides a simplified syntax
     * in hierarchical form.  The channelName must not be started or ended with '/',
     * and each node in the path can only be alphanumeric or '_'.  All the
     * parent nodes will be created automatically and they can be subscribed.
     * @param channelName A path like channel name.
     * @param isPersonal True for personal channel; false for global channel.
     * @param options A creation options or null.
     */
    public CreateRequest(String channelName, boolean isPersonal,
                         MMXTopicOptions options) {
      this(channelName, isPersonal, options, null);
    }

    /**
     * Get the channel name.
     * @return The channel name.
     */
    public String getChannelName() {
      return mChannel;
    }

    /**
     * Check if the creating channel is personal.
     * @return true for personal channel, false for global channel.
     */
    public boolean isPersonal() {
      return mPersonal;
    }

    /**
     * @hide
     * @return true to create this channel as a collection; false to create it as leaf node.
     */
    public boolean isCollection() {
      return mCollection;
    }

    /**
     * Get the channel creation options.
     * @return The creation options, or null.
     */
    public MMXTopicOptions getOptions() {
      return mOptions;
    }

    /**
     * Get the list of roles that we need to associate with the channel.
     * If empty the created channel will be accessible to all users (public).
     *
     * @return
     */
    public List<String> getRoles() {
      return roles;
    }

    public static CreateRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, CreateRequest.class);
    }
  }

  /**
   * @hide
   * Response payload for creating a channel.  The created channel has a unique
   * ID.
   */
  public static class CreateResponse extends MMXStatus {
    @SerializedName("channel")
    private final MMXChannelId mChannelId;

    public CreateResponse(MMXChannelId channelId) {
      mChannelId = channelId;
    }

    public MMXChannelId getId() {
      return mChannelId;
    }

    public static CreateResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, CreateResponse.class);
    }
  }

  /**
   * @hide
   * Delete a channel and its children if it is a collection.
   */
  public static class DeleteRequest extends JSONifiable {
    @SerializedName("channelId")
    private final String mChannelId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("isPersonal")
    private final boolean mPersonal;

    /**
     * Default constructor for the channel deletion request.
     * @param channel A path like channel name.
     * @param isPersonal True for personal channel; false for global channel.
     */
    public DeleteRequest(String channel, boolean isPersonal) {
      mChannelId = null;
      mChannel = channel;
      mPersonal = isPersonal;
    }

    /**
     * Get the channel name.
     * @return The channel name.
     */
    public String getChannel() {
      return mChannel;
    }

    /**
     * Check if the deleting channel is personal.
     * @return true for personal channel, false for global channel.
     */
    public boolean isPersonal() {
      return mPersonal;
    }

    public static DeleteRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, DeleteRequest.class);
    }
  }

  /**
   * @hide
   * Request payload for retracting all published items from the channel owner.
   */
  public static class RetractAllRequest extends JSONifiable {
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("isPersonal")
    private final boolean mPersonal;

    /**
     * Constructor to retract all published items from a personal channel or
     * global channel owned by the requester.
     * @param channel A path like channel name.
     * @param isPersonal true for a personal channel; false for global channel.
     */
    public RetractAllRequest(String channel, boolean isPersonal) {
      mChannel = channel;
      mPersonal = isPersonal;
    }

    public String getChannel() {
      return mChannel;
    }

    public boolean isPersonal() {
      return mPersonal;
    }

    public static RetractAllRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, RetractAllRequest.class);
    }
  }

  /**
   * @hide
   * Request payload for retracting published items from a channel.  The requester
   * must have proper permission to remove the items.
   */
  public static class RetractRequest extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("itemIds")
    private final List<String> mItemIds;

    /**
     * Constructor to retract published items from a channel.  The requester must
     * have permission to retract the published items.
     * @param userId User ID of a personal channel or null for global channel.
     * @param channel A path like channel name.
     * @param itemIds Published item ID's to be retracted.
     */
    public RetractRequest(String userId, String channel, List<String> itemIds) {
      mUserId = userId;
      mChannel = channel;
      mItemIds = itemIds;
    }

    public String getUserId() {
      return mUserId;
    }

    public String getChannel() {
      return mChannel;
    }

    public List<String> getItemIds() {
      return mItemIds;
    }

    public static RetractRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, RetractRequest.class);
    }
  }

  /**
   * @hide
   * Response payload for retracting published items from a channel.
   */
  public static class RetractResponse extends HashMap<String, Integer> {
    public RetractResponse() {
      super();
    }

    public RetractResponse(int initSize) {
      super(initSize);
    }
  }

  public static class GetChannelsRequest extends ArrayList<MMXChannelId> {
    public GetChannelsRequest() {
      super();
    }

    public static GetChannelsRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, GetChannelsRequest.class);
    }
  }

  public static class GetChannelsResponse extends ArrayList<ChannelInfo> {
    public GetChannelsResponse() {
      super();
    }

    public static GetChannelsResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, GetChannelsResponse.class);
    }
  }

  /**
   * @hide
   * A request to access published items by ID's.
   */
  public static class ItemsByIdsRequest extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("itemIds")
    private final List<String> mItemIds;

    /**
     * Constructor to get published items from a channel by item ID's.
     * @param userId User ID of a user channel or null for global channel.
     * @param channel A path like channel name.
     * @param itemIds Published item ID's.
     */
    public ItemsByIdsRequest(String userId, String channel, List<String> itemIds) {
      mUserId = userId;
      mChannel = channel;
      mItemIds = itemIds;
    }

    public String getUserId() {
      return mUserId;
    }

    public String getChannel() {
      return mChannel;
    }

    public List<String> getItemIds() {
      return mItemIds;
    }

    public static ItemsByIdsRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, ItemsByIdsRequest.class);
    }
  }

  /**
   * @hide
   * Request payload for listing all channels with a specified limit.  If no
   * limit is specified, no maximum number of channels will be imposed.
   */
  public static class ListRequest extends JSONifiable {
    @SerializedName("limit")
    private Integer mLimit;
    @SerializedName("offset")
    private int mOffset;
    @SerializedName("recursive")
    private boolean mRecursive = true;
    @SerializedName("channelName")
    private String mStart;
    @SerializedName("type")
    private ListType mType;

    /**
     * Optionally set a limit on the returning channels.
     * @param limit A positive number.
     * @return This object.
     */
    public ListRequest setLimit(int limit) {
      if ((mLimit = limit) <= 0) {
        throw new IllegalArgumentException("Limit must be > 0");
      }
      return this;
    }

    /**
     * Get the specified limit.
     * @return A specified limit, or null.
     */
    public Integer getLimit() {
      return mLimit;
    }

    public int getOffset() {
      return mOffset;
    }

    public ListRequest setOffset(int offset) {
      this.mOffset = offset;
      return this;
    }

    /**
     * Specify if the list is recursive down to its descendants.
     * @param recursive true for all descendants, false for the immediate children
     * @return This object.
     */
    public ListRequest setRecursive(boolean recursive) {
      mRecursive = recursive;
      return this;
    }

    public boolean isRecursive() {
      return mRecursive;
    }

    /**
     * Set the starting point.  Default is from the root.
     * @param start
     * @return This object.
     */
    public ListRequest setStart(String start) {
      mStart = start;
      return this;
    }

    public String getStart() {
      return mStart;
    }

    public ListType getType() {
      return mType;
    }

    public ListRequest setType(ListType type) {
      mType = type;
      return this;
    }

    public static ListRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, ListRequest.class);
    }
  }

  /**
   * @hide
   * Response payload for listing all channels.
   */
  public static class ListResponse extends ArrayList<ChannelInfo> {

    /**
     * @hide
     */
    public ListResponse() {
      super();
    }

    /**
     * @hide
     * @param capacity
     */
    public ListResponse(int capacity) {
      super(capacity);
    }

    public static ListResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, ListResponse.class);
    }
  }

  /**
   * @hide
   */
  public static class SubscribeRequest extends JSONifiable {
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("devId")
    private final String mDevId;
    @SerializedName("errorOnDup")
    private boolean mErrorOnDup;

    /**
     * Subscription request for a global channel.
     * @param channel The channel name.
     * @param devId A device ID for this subscription or null for all devices.
     */
    public SubscribeRequest(String channel, String devId) {
      mChannel = channel;
      mDevId = devId;
    }

    /**
     * Subscription request for a personal channel.
     * @param userId The user ID of a personal channel.
     * @param channel The channel name.
     * @param devId A device ID for this subscription or null for all devices.
     */
    public SubscribeRequest(String userId, String channel, String devId) {
      mUserId = userId;
      mChannel = channel;
      mDevId = devId;
    }

    /**
     * Get the user ID of a personal channel.
     * @return A user ID of a personal channel, or null for global channel.
     */
    public String getUserId() {
      return mUserId;
    }

    /**
     * Get the channel name.
     * @return The channel name.
     */
    public String getChannel() {
      return mChannel;
    }

    /**
     * Get the device ID for this subscription.
     * @return A device ID for the subscription or null.
     */
    public String getDevId() {
      return mDevId;
    }

    /**
     * Check if MMX server should report an error for duplicated subscription.
     * @return false for not reporting, true for reporting an error.
     */
    public boolean isErrorOnDup() {
      return mErrorOnDup;
    }

    /**
     * Report an error if the subscription is a duplicate.  Default is false.
     * @param errorOnDup trie to report error; false otherwise.
     * @return This object.
     */
    public SubscribeRequest setErrorOnDup(boolean errorOnDup) {
      mErrorOnDup = errorOnDup;
      return this;
    }

    public static SubscribeRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, SubscribeRequest.class);
    }
  }

  /**
   * @hide
   */
  public static class SubscribeResponse extends JSONifiable {
    @SerializedName("subscriptionId")
    private final String mSubId;
    @SerializedName("code")
    private final int mCode;
    @SerializedName("msg")
    private final String mMsg;

    /**
     * @hide
     * Default constructor.
     * @param subId The subscription ID.
     * @param code The status code.
     * @param msg A diagnostic message.
     */
    public SubscribeResponse(String subId, int code, String msg) {
      mSubId = subId;
      mCode = code;
      mMsg = msg;
    }

    /**
     * Get the subscription ID.
     * @return The subscription ID.
     */
    public String getSubId() {
      return mSubId;
    }

    /**
     * Get the status code.
     * @return The status code.
     */
    public int getCode() {
      return mCode;
    }

    /**
     * Get the diagnostic message.
     * @return The diagnostic message.
     */
    public String getMsg() {
      return mMsg;
    }

    public static SubscribeResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, SubscribeResponse.class);
    }
  }

  /**
   * @hide
   */
  public static class UnsubscribeRequest extends JSONifiable {
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("subscriptionId")
    private final String mSubId;

    /**
     * Constructor for unsubscribing a global channel.
     * @param channel The channel name.
     * @param subId A subscription ID or null for all subscriptions to the channel.
     */
    public UnsubscribeRequest(String channel, String subId) {
      mChannel = channel;
      mSubId = subId;
    }

    /**
     * Constructor for unsubscribing a personal channel.
     * @param userId The user ID of a personal channel.
     * @param channel The channel name.
     * @param subId A subscription ID or null for all subscriptions to the channel.
     */
    public UnsubscribeRequest(String userId, String channel, String subId) {
      mUserId = userId;
      mChannel = channel;
      mSubId = subId;
    }

    /**
     * Get the user ID of the personal channel.
     * @return The user ID of the personal channel, or null.
     */
    public String getUserId() {
      return mUserId;
    }

    /**
     * Get the channel name.
     * @return The channel name.
     */
    public String getChannel() {
      return mChannel;
    }

    /**
     * Get the subscription ID.
     * @return The subscription ID or null.
     */
    public String getSubId() {
      return mSubId;
    }

    public static UnsubscribeRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, UnsubscribeRequest.class);
    }
  }

  /**
   * @hide
   * Request payload to unsubscribe from all subscriptions for a device.
   */
  public static class UnsubscribeForDevRequest extends JSONifiable {
    @SerializedName("devId")
    private final String mDevId;

    /**
     * Default constructor.
     * @param devId A non-null device ID.
     */
    public UnsubscribeForDevRequest(String devId) {
      mDevId = devId;
    }

    /**
     * The device ID for the unsubscription reuest.
     * @return The device ID.
     */
    public String getDevId() {
      return mDevId;
    }

    public static UnsubscribeForDevRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, UnsubscribeForDevRequest.class);
    }
  }

  /**
   * @hide
   * Request for the summary of a list of channels.
   */
  public static class SummaryRequest extends JSONifiable {
    @SerializedName("channelNodes")
    private final List<MMXChannelId> mChannels;
    @SerializedName("since")
    private Date mSince;
    @SerializedName("until")
    private Date mUntil;

    public SummaryRequest(List<MMXChannelId> channels) {
      mChannels = channels;
    }

    public List<MMXChannelId> getChannelNodes() {
      return mChannels;
    }

    public Date getSince() {
      return mSince;
    }

    public SummaryRequest setSince(Date since) {
      mSince = since;
      return this;
    }

    public Date getUntil() {
      return mUntil;
    }

    public SummaryRequest setUntil(Date until) {
      mUntil = until;
      return this;
    }

    public static SummaryRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, SummaryRequest.class);
    }
  }

  /**
   * @hide
   * Response of the channel summary.
   */
  public static class SummaryResponse extends ArrayList<ChannelSummary> {
    public SummaryResponse() {
      super();
    }

    public SummaryResponse(int capacity) {
      super(capacity);
    }
  }

  /**
   * Attributes for channel search.
   */
  public static class ChannelSearch extends JSONifiable {
    @SerializedName("channelName")
    private SingleValue mDisplayName;
    @SerializedName("description")
    private SingleValue mDescription;
    @SerializedName("tags")
    private MultiValues mTags;

    /**
     * Get the searching channel name.
     * @return The channel name search value.
     */
    public String getChannelName() {
      return mDisplayName != null ? mDisplayName.getValue() : null;
    }

    /**
     * Get the match type of channel name.
     * @return
     */
    public Match getChannelNameMatch() {
      return mDisplayName != null ? mDisplayName.getMatch() : null;
    }

    /**
     * Set the search value for channel name using the default matching type which
     * is the prefix match.
     * @param channelName The channel name search value.
     * @return This object.
     */
    public ChannelSearch setChannelName(String channelName) {
      return setChannelName(channelName, null);
    }

    /**
     * Specify the channel name to be searched.
     * @param channelName The channel name search value.
     * @param matchType The match type.
     * @return This object.
     */
    public ChannelSearch setChannelName(String channelName, Match matchType) {
      mDisplayName = new SingleValue(channelName, matchType);
      return this;
    }

    /**
     * Get the searching channel description.
     * @return The description search value.
     */
    public String getDescription() {
      return mDescription != null ? mDescription.getValue() : null;
    }

    /**
     * Get the match type of description.
     * @return The description match type.
     */
    public Match getDescriptionMatch() {
      return mDescription != null ? mDescription.getMatch() : null;
    }

    /**
     * Set the search of description using the server default match which is
     * the prefix match.
     * @param description The description search value.
     * @return This object.
     */
    public ChannelSearch setDescription(String description) {
      return setDescription(description, null);
    }

    /**
     * Specify the channel description to be searched.
     * @param description The description search value.
     * @param matchType The match type.
     * @return This object.
     */
    public ChannelSearch setDescription(String description, Match matchType) {
      mDescription = new SingleValue(description, matchType);
      return this;
    }

    /**
     * Specify a list of tags to be searched.  Any matching values will be
     * considered as a match.
     * @return The tag search values.
     */
    public List<String> getTags() {
      return mTags != null ? mTags.getValues() : null;
    }

    /**
     * Get the match type of tags.
     * @return Always null.
     */
    public Match getTagsMatch() {
      return mTags != null ? mTags.getMatch() : null;
    }

    /**
     * Set the search values of channel tags using exact match in each value.
     * @param tags
     * @return This object.
     */
    public ChannelSearch setTags(List<String> tags) {
      mTags = new MultiValues(tags, null);
      return this;
    }
  }

  /**
   * @hide
   * Channel search attributes.
   */
  public static enum ChannelAttr {
    channelName,
    description,
  }

  /**
   * @hide
   * Request of the channel query.
   */
  public static class ChannelQueryRequest extends JSONifiable {
    @SerializedName("criteria")
    private List<MMXAttribute<ChannelAttr>> mCriteria = new ArrayList<MMXAttribute<ChannelAttr>>();
    @SerializedName("offset")
    private final int mOffset;
    @SerializedName("limit")
    private final int mLimit;

    public ChannelQueryRequest(List<MMXAttribute<ChannelAttr>> criteria,
                              int offset, int limit) {
      if (criteria == null || criteria.size() != 1) {
        throw new IllegalArgumentException("Criteria must have one search type.");
      }
      mCriteria = criteria;
      mOffset = offset;
      mLimit = limit;
    }

    public List<MMXAttribute<ChannelAttr>> getCriteria() {
      return mCriteria;
    }

    public int getLimit() {
      return mLimit;
    }

    public int getOffset() {
      return mOffset;
    }

    public static ChannelQueryRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, ChannelQueryRequest.class);
    }
  }

  /**
   * @hide
   * ChannelInfo with subscription count.
   */
  public static class ChannelInfoWithSubscriptionCount extends ChannelInfo {
    private int subscriptionCount;
    /**
     * @hide
     * @param userId
     * @param channel
     * @param isCollection
     */
    public ChannelInfoWithSubscriptionCount(String userId, String channel, boolean isCollection) {
      super(userId, channel, isCollection);
    }

    /**
     * Get the subscription count.
     * @return
     */
    public int getSubscriptionCount() {
      return subscriptionCount;
    }

    public ChannelInfoWithSubscriptionCount setSubscriptionCount(int subscriptionCount) {
      this.subscriptionCount = subscriptionCount;
      return this;
    }
  }

  /**
   * Response of the channel search.
   */
  public static class ChannelQueryResponse extends JSONifiable {
    @SerializedName("total")
    private final int mTotal;
    @SerializedName("results")
    private final List<ChannelInfo> mResults;

    /**
     * @hide
     * @param total
     * @param results
     */
    public ChannelQueryResponse(int total, List<ChannelInfo> results) {
      mTotal = total;
      mResults = results;
    }

    /**
     * Return the total counts.  -1 if unknown.
     * @return
     */
    public int getTotal() {
      return mTotal;
    }

    /**
     * Get the result set.
     * @return
     */
    public List<ChannelInfo> getResults() {
      return mResults;
    }

    /**
     * @hide
     * @param json
     * @return
     */
    public static ChannelQueryResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, ChannelQueryResponse.class);
    }
  }

  /**
   * Options on fetching the published items.
   */
  public static class FetchOptions implements Serializable {
    private static final long serialVersionUID = -1986734451547877221L;
    @SerializedName("subscriptionId")
    private String mSubId;
    @SerializedName("since")
    private Date mSince;
    @SerializedName("until")
    private Date mUntil;
    @SerializedName("ascending")
    private boolean mAscending;
    @SerializedName("maxItems")
    private int mMaxItems = -1;
    @SerializedName("offset")
    private int mOffset;

    /**
     * Get an optional subscription ID.
     * @return Subscription ID, or null.
     */
    public String getSubId() {
      return mSubId;
    }

    /**
     * Set the subscription ID.
     * @param subId The subscription ID.
     * @return This object.
     */
    public FetchOptions setSubId(String subId) {
      mSubId = subId;
      return this;
    }

    /**
     * Get the start date/time of publishing date range.
     * @return The start date/time.
     */
    public Date getSince() {
      return mSince;
    }

    /**
     * Set the start date/time for the search of publishing date/time.
     * @param since The start date/time.
     * @return This object.
     */
    public FetchOptions setSince(Date since) {
      mSince = since;
      return this;
    }

    /**
     * Get the end date/time of publishing date range.
     * @return The end date/time.
     */
    public Date getUntil() {
      return mUntil;
    }

    /**
     * Set the end date/time for the search of publishing date/time.
     * @param until The end date/time.
     * @return This object.
     */
    public FetchOptions setUntil(Date until) {
      mUntil = until;
      return this;
    }

    /**
     * The sorting order of the published items.
     * @return true for ascending order; false for descending order.
     */
    public boolean isAscending() {
      return mAscending;
    }

    /**
     * Set the sort order of the published items.
     * @param ascending true for ascending (chronological order); false for descending order.
     * @return This object.
     */
    public FetchOptions setAscending(boolean ascending) {
      mAscending = ascending;
      return this;
    }

    /**
     * The max number of records to be returned.
     * @return Max number of records to be returned, -1 for using the server default.
     */
    public int getMaxItems() {
      return mMaxItems;
    }

    /**
     * Set the max number of records to be returned.
     * @param maxItems -1 for max records set by the server, or > 0.
     * @return This object.
     */
    public FetchOptions setMaxItems(int maxItems) {
      mMaxItems = maxItems;
      return this;
    }

    /**
     * The offset of records to be returned.
     * @return Offset of records to be returned, 0 for using the server default.
     */
    public int getOffset() {
      return mOffset;
    }

    /**
     * Set the offset of records to be returned.
     * @param offset
     * @return This object.
     */
    public FetchOptions setOffset(int offset) {
      mOffset = offset;
      return this;
    }
  }

  /**
   * @hide
   * Request payload for fetching published items from a channel.
   */
  public static class FetchRequest extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("options")
    private final FetchOptions mOptions;

    public FetchRequest(String userId, String channel, FetchOptions options) {
      mUserId = userId;
      mChannel = channel;
      mOptions = options;
    }

    public String getUserId() {
      return mUserId;
    }

    public String getChannel() {
      return mChannel;
    }

    public FetchOptions getOptions() {
      return mOptions;
    }

    public static FetchRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, FetchRequest.class);
    }
  }

  /**
   * @hide
   */
  public static class MMXPublishedItem {
    @SerializedName("itemId")
    private final String mItemId;
    @SerializedName("publisher")
    private final String mPublisher;
    @SerializedName("creationDate")
    private final Date mCreationDate;
    @SerializedName("payloadXML")
    private final String mPayloadXml;

    public MMXPublishedItem(String itemId, String publisher, Date creationDate,
                             String payloadXml) {
      mItemId = itemId;
      mPublisher = publisher;
      mCreationDate = creationDate;
      mPayloadXml = payloadXml;
    }

    public String getItemId() {
      return mItemId;
    }

    public String getPublisher() {
      return mPublisher;
    }

    public Date getCreationDate() {
      return mCreationDate;
    }

    public String getPayloadXml() {
      return mPayloadXml;
    }
  }

  /**
   * @hide
   * Response payload for fetching published items.
   */
  public static class FetchResponse extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("totalCount")
    private final int mTotal;
    @SerializedName("items")
    private final List<MMXPublishedItem> mItems;

    /**
     * @hide
     * @param userId
     * @param channel
     * @param items
     */
    public FetchResponse(String userId, String channel, int total,
        List<MMXPublishedItem> items) {
      mUserId = userId;
      mChannel = channel;
      mTotal = total;
      mItems = items;
    }

    public String getUserId() {
      return mUserId;
    }

    public String getChannel() {
      return mChannel;
    }

    public List<MMXPublishedItem> getItems() {
      return mItems;
    }

    public int getTotal() {
      return mTotal;
    }

    public static FetchResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, FetchResponse.class);
    }
  }

  /**
   * @hide
   * Request to get all subscribers from a channel.
   */
  public static class SubscribersRequest extends JSONifiable {
    @SerializedName("userId")
    private final String mUserId;
    @SerializedName("channelName")
    private final String mChannel;
    @SerializedName("limit")
    private final int mLimit;
    @SerializedName("offset")
    private final int mOffset;

    /**
     *
     * @param userId Null for global channel, user ID for the user channel.
     * @param channel The channel name.
     * @param limit -1 for unlimited, or > 0.
     */
    public SubscribersRequest(String userId, String channel, int limit) {
      this(userId, channel, 0, limit);
    }

    /**
     *
     * @param userId Null for global channel, user ID for the user channel.
     * @param channel The channel name.
     * @param offset
     * @param limit -1 for unlimited, or > 0.
     */
    public SubscribersRequest(String userId, String channel, int offset, int limit) {
      mUserId = userId;
      mChannel = channel;
      this.mOffset = offset;
      mLimit = limit;
    }

    public String getUserId() {
      return mUserId;
    }

    public String getChannel() {
      return mChannel;
    }

    public int getLimit() {
      return mLimit;
    }

    /**
     * The offset of records to be returned.
     * @return Offset of records to be returned, 0 for using the server default.
     */
    public int getOffset() {
      return mOffset;
    }

    public static SubscribersRequest fromJson(String json) {
      return GsonData.getGson().fromJson(json, SubscribersRequest.class);
    }
  }

  /**
   * @hide
   * Response of getting all subscribers to a channel.
   */
  public static class SubscribersResponse extends MMXStatus {
    @SerializedName("subscribers")
    private List<UserInfo> mSubscribers;
    @SerializedName("totalCount")
    private int mTotal;

    public List<UserInfo> getSubscribers() {
      return mSubscribers;
    }

    public SubscribersResponse setSubscribers(List<UserInfo> subscribers) {
      mSubscribers = subscribers;
      return this;
    }

    public int getTotal() {
      return mTotal;
    }

    public SubscribersResponse setTotal(int total) {
      mTotal = total;
      return this;
    }

    public static SubscribersResponse fromJson(String json) {
      return GsonData.getGson().fromJson(json, SubscribersResponse.class);
    }
  }
}
