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

package com.magnet.mmx.util;

import com.magnet.mmx.protocol.*;

/**
 * @hide
 * A helper class for channels in PubSub.  Each user channel has a unique
 * channel ID in the form of userID#ID.  There are various conversion methods
 * between pubsub node ID and channel ID.
 */
public class ChannelHelper {
  public final static char CHANNEL_DELIM = TopicHelper.TOPIC_DELIM;
  public final static char CHANNEL_FOR_APP = TopicHelper.TOPIC_FOR_APP;
  public final static char CHANNEL_SEPARATOR = TopicHelper.TOPIC_SEPARATOR; // use in URL only; userID#channelName
  public final static String CHANNEL_DELIM_STR = String.valueOf(CHANNEL_DELIM);
  public final static String CHANNEL_FOR_APP_STR = String.valueOf(CHANNEL_FOR_APP);
  public final static String CHANNEL_GEOLOC = TopicHelper.TOPIC_GEOLOC;     // a leaf node
  public final static String CHANNEL_OS_ROOT = TopicHelper.TOPIC_OS_ROOT;
  public final static String CHANNEL_OS = CHANNEL_OS_ROOT+CHANNEL_DELIM;    // a collection node
  public final static String CHANNEL_LEAF_ALL = TopicHelper.TOPIC_LEAF_ALL; // a leaf node
  public final static String CHANNEL_NAME_PATTERN_STRING = TopicHelper.TOPIC_NAME_PATTERN_STRING;

  // the node is formulated as follows:
  // /<appId>/*/com.magnet.os/<osType>

  /**
   * Generate the container channel name of devices based on OS type. This container channel can be used
   * as the parent node for creating leaf channels for publishing events to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceChannelName(OSType osType) {
    return TopicHelper.generateDeviceTopicName(osType);
  }

  /**
   * Generate the publishable channel name of devices based on OS type where all event related this OS type
   * can be published to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceAllLeafChannelName(OSType osType) {
    return TopicHelper.generateDeviceAllLeafTopicName(osType);
  }

  /**
   * @hide
   * Check if a <code>nodeId</code> is for a private channel.
   * @param nodeId
   * @return
   */
  public static boolean isUserChannel(String nodeId) {
    return TopicHelper.isUserTopic(nodeId);
  }

  /**
   * Check if the channel with specified id is a channel for the passed in appId
   * @param nodeId A path should be started with "/appId/".
   * @param appId
   * @return
   */
  public static boolean isAppChannel(String nodeId, String appId) {
    return TopicHelper.isAppTopic(nodeId, appId);
  }

  /**
   * Construct an application prefix for the channel.
   * @param appId
   * @return The prefix as "/appId/"
   */
  public static String makePrefix(String appId) {
    return TopicHelper.makePrefix(appId);
  }

  /**
   * Parse an XMPP nodeID into a AppChannel object.  There are two formats:
   * /appID/&asterisk;/ID for public channel and /appID/userID/ID for private
   * channel.
   * @param nodeId A XMPP PubSub nodeID string.
   * @return An AppChannel object, or null if not an MMX channel.
   */
  public static AppChannel toAppChannel(String nodeId) {
    if (nodeId.charAt(0) != CHANNEL_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(CHANNEL_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(CHANNEL_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String appId = nodeId.substring(1, index1);
    String userId = nodeId.substring(index1+1, index2);
    String channelName = nodeId.substring(index2+1);
    return new AppChannel(appId, (userId.charAt(0) == CHANNEL_FOR_APP) ?
                      null : userId, channelName);
  }

  /**
   * @deprecated Use #toAppChannel(String)
   */
  @Deprecated
  public static AppChannel parseChannel(String nodeId) {
    return toAppChannel(nodeId);
  }

  /**
   * Parse an XMPP nodeID into a Channel ID object.  There are two formats:
   * /appID/&asterisk;/channel for global channel and /appID/userID/channel for
   * personal channel.
   * @param nodeId A XMPP PubSub nodeID string.
   * @return A MMXChannelId, or null if not an MMX channel.
   */
  public static MMXChannelId parseNode(String nodeId) {
    if (nodeId.charAt(0) != CHANNEL_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(CHANNEL_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(CHANNEL_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String userId = nodeId.substring(index1+1, index2);
    String channelName = nodeId.substring(index2+1);
    return new MMXChannelId((userId.charAt(0) == CHANNEL_FOR_APP) ?
        null : userId, channelName);
  }

  /**
   * Construct the OS channel with an optional version.
   * @param os An OS type, or null for all OS's.
   * @param version A version string, or null for all versions.
   * @return
   */
  public static String makeOSChannel(OSType os, String version) {
    return TopicHelper.makeOSTopic(os, version);
  }

  /**
   * @hide
   * Find the length of the prefix in the channel.  It is the third slash in the
   * real channel.  The prefix is "/appId/userId/".
   * @param path The channel full path.
   * @return The length of the prefix.
   */
  public static int getPrefixLength(String path) {
    return TopicHelper.getPrefixLength(path);
  }

  /**
   * Get the base name of the path.
   * @param path The full channel path
   * @return
   */
  public static String getBaseName(String path) {
    return TopicHelper.getBaseName(path);
  }

  /**
   * Get the root component from the node ID.  In the current implementation,
   * it is the app ID.
   * @param nodeId The pubsub node ID.
   * @return The root component in node ID.
   * @deprecated Use {@link #getAppId(String)}
   */
  @Deprecated
  public static String getRootNodeId(String nodeId) {
    return TopicHelper.getRootNodeId(nodeId);
  }

  /**
   * Get the root component from the node ID.  In the current implementation,
   * it is the app ID.
   * @param nodeId
   * @return
   */
  public static String getAppId(String nodeId) {
    return TopicHelper.getAppId(nodeId);
  }

  /**
   * @hide
   * Normalize the channel path by collapsing all contiguous '/' and make it lower
   * case.  It also makes sure that channel cannot be null, empty, start or end
   * with '/'.
   * @param name Channel names.
   * @return A normalized channel path.
   * @throws IllegalArgumentException Channel cannot be null or empty.
   * @throws IllegalArgumentException Channel cannot start or end with '/'.
   */
  public static String normalizePath(String name) {
    return TopicHelper.normalizePath(name);
  }

  /**
   * @hide
   * Validate the channel name.
   * @param channel A non-null channel with path syntax.
   * @throws IllegalArgumentException Channel cannot contain '/'.
   */
  public static void checkPathAllowed(String name) {
    TopicHelper.checkPathAllowed(name);
  }

  public static boolean validatePublisherType(String publisherType) {
    return TopicHelper.validatePublisherType(publisherType);
  }

  // For MOB-1423
  public static void restrictPathSyntax(boolean restrict) {
    TopicHelper.restrictPathSyntax(restrict);
  }

  /**
   * Validate supplied application channel name
   * @param channelName
   * @return true if the channel name is valid. false if the channel name is invalid.
   */
  public static boolean validateApplicationChannelName (String name) {
    return TopicHelper.validateApplicationTopicName(name);
  }

  /**
   * Check if the channel ID is for private channel.  The channel ID is in the
   * form of "userID#ID" or "ID".
   * @param channelId A channel ID.
   * @return true if a private channel; otherwise, false.
   */
  public static boolean isUserChannelId(String channelId) {
    return channelId.indexOf(CHANNEL_SEPARATOR) > 0;
  }

  /**
   * Convert MMXChannelId into a pubsub node ID.  The internal ID must have
   * the form of "ID" or "userID#ID".
   * @param appId The app ID.
   * @param channelId The channel ID object which must have the internal ID.
   * @return A pubsub node ID.
   * @see MMXChannelId#getId()
   */
  public static String toNodeId(String appId, MMXChannelId channelId) {
    return toNodeId(appId, channelId.getId());
  }

  /**
   * Convert a string format of channel ID into a pubsub node ID.
   * @param appId The app ID.
   * @param channelId The channel ID in the form of "ID" or "userID#ID".
   * @return A pubsub node ID.
   * @see MMXChannelId#getId()
   */
  public static String toNodeId(String appId, String channelId) {
    return TopicHelper.toNodeId(appId, channelId);
  }

  /**
   * Construct a pubsub node ID.  There is a special root node ID if both
   * userId and id are null.  The node ID will be "appID",
   * "/appID/&asterisk;/id", or "/appID/userID/id".
   * @param appId The app ID.
   * @param userId A user ID for user topic or null for global topic.
   * @param id A unique id.
   * @return A pubsub node ID.
   */
  public static String toNodeId(String appId, String userId, String id) {
    return TopicHelper.toNodeId(appId, userId, id);
  }

  /**
   * Construct the pubsub node ID for the app root node.  The node ID will be
   * "appID".
   * @param appId The app ID.
   * @return A pubsub node ID of the app root node.
   */
  public static String toAppNodeId(String appId) {
    return TopicHelper.toAppNodeId(appId);
  }

  /**
   * Construct the pubsub node ID for the root public channel.  The node ID will
   * be "/appID/&asterisk;".
   * @param appId The app ID.
   * @return A pubsub node ID of the root public channel.
   */
  public static String toPublicNodeId(String appId) {
    return TopicHelper.toGlobalNodeId(appId);
  }

  /**
   * Construct the pubsub node ID for the root private channel.  The node ID
   * will be "/appID/userID".
   * @param appId The app ID.
   * @param userId A user ID for the root private channel.
   * @return A pubsub node ID of the root private channel.
   */
  public static String toPrivateNodeId(String appId, String userId) {
    return TopicHelper.toUserNodeId(appId, userId);
  }

  /**
   * Convert an XMPP pubsub node ID into a string form of channel ID.
   * The pubsub node ID must have the form of /appID/&asterisk;/ID or
   * /appID/userID/ID, and the channel ID will be in the form of "ID"
   * or "userID#ID."
   * @param nodeId An XMPP PubSub nodeID string.
   * @return A string form of channel ID, or null if not a MMX channel.
   */
  public static String toChannelId(String nodeId) {
    return TopicHelper.toTopicId(nodeId);
  }

  /**
   * Convert an XMPP pubsub node ID into a channel ID object with a display
   * name.  The pubsub node ID must have the form of /appID/&asterisk;/ID or
   * /appID/userID/ID, and the channel ID will be "ID" or "userID#ID."
   * @param nodeId A XMPP pubsub nodeID string.
   * @param displayName An optional display name.
   * @return A MMXChannelId, or null if not an MMX channel.
   */
  public static MMXChannelId toChannelId(String nodeId, String displayName) {
    return MMXChannelId.fromNodeId(nodeId, displayName);
  }

  /**
   * Check if the <code>nodeId</code> is the app root node ID (appID),
   * global root node ID (appID/*), or user root node ID (appID/userID.)
   * @return true if it is the top node ID; otherwise, false.
   */
  public static boolean isTopNodeId(String nodeId) {
    return TopicHelper.isTopNodeId(nodeId);
  }
}
