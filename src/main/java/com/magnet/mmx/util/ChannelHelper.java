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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @hide
 * A helper class for channels in PubSub.  There are two formats for user channel:
 * userID/channelName (internal node path) and useID#channelName (used in a path for
 * REST API.)  Use ChannelResource.nameToId() or ChannelResource.idToName() to
 * encode/decode userID#channelName; otherwise, use {@link #parseNode(String)}
 * or {@link #makeChannel(String, String, String)} to encode/decode userID/channelName.
 */
public class ChannelHelper {
  private static boolean CHANNEL_RESTRICTED_NAME = true;  // true for MOB-1423
  public final static char CHANNEL_DELIM = '/';
  public final static char CHANNEL_FOR_APP = '*';
  public final static char CHANNEL_SEPARATOR = '#';   // use in URL only; userID#channelName
  public final static String CHANNEL_FOR_APP_STR = String.valueOf(CHANNEL_FOR_APP);
  public final static String CHANNEL_GEOLOC = "com.magnet.geoloc";  // a leaf node
  public final static String CHANNEL_OS_ROOT = "com.magnet.os";
  public final static String CHANNEL_OS = CHANNEL_OS_ROOT+"/";        // a collection node
  public final static String CHANNEL_LEAF_ALL = "_all_";            // a leaf node
  public final static String CHANNEL_NAME_PATTERN_STRING = "^[a-zA-Z0-9_\\.\\-]*$";

  private static final Pattern CHANNEL_NAME_PATTERN = Pattern.compile(CHANNEL_NAME_PATTERN_STRING);


  // the node is formulated as follows:
  // /<appId>/*/com.magnet.os/<osType>

  /**
   * Generate the container channel name of devices based on OS type. This container channel can be used
   * as the parent node for creating leaf channels for publishing events to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceChannelName(OSType osType) {
    StringBuilder stringBuilder = new StringBuilder()
        .append(CHANNEL_OS)
        .append(osType.name());
    return stringBuilder.toString();
  }

  /**
   * Generate the publishable channel name of devices based on OS type where all event related this OS type
   * can be published to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceAllLeafChannelName(OSType osType) {
    StringBuilder stringBuilder = new StringBuilder()
        .append(CHANNEL_OS)
        .append(osType.name())
        .append(CHANNEL_DELIM)
        .append(CHANNEL_LEAF_ALL);
    return stringBuilder.toString();
  }

  /**
   * @hide
   * Assuming that <code>channelId</code> is in the form of "/appId/*" or
   * "/appId/userId", check if it is a user channel.
   * @param channelId
   * @return
   */
  public static boolean isUserChannel(String channelId) {
    int index = channelId.indexOf(CHANNEL_DELIM, 1);
    return (index > 1) && (channelId.charAt(index+1) != CHANNEL_FOR_APP);
  }

  /**
   * Check if the channel with specified id is a channel for the passed in appId
   * @param channelId A path should be started with "/appId/".
   * @param appId
   * @return
   */
  public static boolean isAppChannel(String channelId, String appId) {
    if (channelId == null || channelId.isEmpty()) {
      return false;
    }
    try {
      return ((channelId.charAt(0) == CHANNEL_DELIM) &&
               channelId.startsWith(appId, 1) &&
               (channelId.charAt(1+appId.length()) == CHANNEL_DELIM));
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Check if the channel represented by the channelId is a user channel
   * @param channelId A non-null full channel path
   * @param appId A non-null app ID
   * @return true if it represents a user channel false other wise.
   */
  public static boolean isUserChannel(String channelId, String appId) {
    String prefix = new StringBuilder().append(CHANNEL_DELIM).append(appId).append(CHANNEL_DELIM).append(CHANNEL_FOR_APP).append(CHANNEL_DELIM).toString();
    try {
      int index = channelId.indexOf(prefix);
      if (index == -1) {
        return true;
      } else {
        return false;
      }
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Construct an application prefix for the channel.
   * @param appId
   * @return The prefix as "/appId/"
   */
  public static String makePrefix(String appId) {
    return CHANNEL_DELIM + appId + CHANNEL_DELIM;
  }

  /**
   * Parse an XMPP nodeID into a AppChannel object.  There are two formats:
   * /appID/*&#x002Fchannel for global channel and /appID/userID/channel for personal
   * channel.
   * @param channel A XMPP PubSub nodeID string.
   * @return An AppChannel object, or null if not an MMX channel.
   */
  public static AppChannel parseChannel(String channel) {
    if (channel.charAt(0) != CHANNEL_DELIM) {
      return null;
    }
    int index1 = channel.indexOf(CHANNEL_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = channel.indexOf(CHANNEL_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String appId = channel.substring(1, index1);
    String userId = channel.substring(index1+1, index2);
    String channelName = channel.substring(index2+1);
    return new AppChannel(appId, (userId.charAt(0) == CHANNEL_FOR_APP) ?
                      null : userId, channelName);
  }

  /**
   * Parse an XMPP nodeID into a Channel ID object.  There are two formats:
   * /appID/*&#x002Fchannel for global channel and /appID/userID/channel for personal
   * channel.
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
   * Convert an XMPP nodeID /appID/&asterisk;/channelID or /appID/userID/channelID
   * into "channelID" or "userID#channelID."  This separates the dependency between
   * channel name and channel ID.
   * @param nodeId A XMPP PubSub nodeID string.
   * @return A unique channel ID within an application, or null if not an MMX channel.
   * @deprecated #toChannelId(String)
   */
  public static String convertToId(String nodeId) {
    return toChannelId(nodeId);
  }

  /**
   * @hide
   * Make a complete channel path.  There is a special root path if both userId
   * and channel are null.  The path may be "appID", "/appID/*", "/appID/userID",
   * "/appID/*\u002achannel", or "/appID/userID/channel".
   * @param appId The app ID.
   * @param userId A user ID for user channel or null for global channel.
   * @param channel A channel name or null.
   * @return
   */
  public static String makeChannel(String appId, String userId, String channel) {
    if (userId == null && channel == null) {
      return appId;
    }

    int len = appId.length() + 2;
    if (userId == null || userId.isEmpty()) {
      ++len;
      userId = null;
    } else {
      if (userId.indexOf('@') >= 0) {
        userId = Utils.escapeNode(userId);
      }
      len += userId.length();
      userId = userId.toLowerCase();
    }
    if (channel != null) {
      if (channel.charAt(0) != CHANNEL_DELIM) {
        ++len;
      }
      len += channel.length();
    }

    StringBuilder sb = new StringBuilder(len);
    sb.append(CHANNEL_DELIM).append(appId).append(CHANNEL_DELIM);
    if (userId == null) {
      sb.append(CHANNEL_FOR_APP);
    } else {
      sb.append(userId);
    }
    if (channel != null) {
      if (channel.charAt(0) != CHANNEL_DELIM) {
        sb.append(CHANNEL_DELIM);
      }
      sb.append(channel.toLowerCase());
    }
    return sb.toString();
  }

  /**
   * Construct the OS channel with an optional version.
   * @param os An OS type, or null for all OS's.
   * @param version A version string, or null for all versions.
   * @return
   */
  public static String makeOSChannel(OSType os, String version) {
    if (os == null) {
      return CHANNEL_OS_ROOT;
    }
    if (version == null) {
      return CHANNEL_OS + os.toString();
    } else {
      return CHANNEL_OS + os.toString() + CHANNEL_DELIM + version;
    }
  }

  /**
   * @hide
   * Find the length of the prefix in the channel.  It is the third slash in the
   * real channel.  The prefix is "/appId/userId/".
   * @param path The channel full path.
   * @return The length of the prefix.
   */
  public static int getPrefixLength(String path) {
    int offset;
    offset = path.indexOf(CHANNEL_DELIM, 1);
    offset = path.indexOf(CHANNEL_DELIM, offset+1);
    return ++offset;
  }

  /**
   * @hide
   * Get the parent full path of the channel.  The parent full path would be
   * "/appId/userId/parent..." where the prefix is "/appId/userId/".
   * @param prefix The prefix length.
   * @param path The channel full path.
   * @return null if no parent is found (, or full path of the parent.
   */
  public static String getParent(int prefix, String path) {
    int offset = path.lastIndexOf(CHANNEL_DELIM);
    if (offset < prefix) {
      return null;
    }
    return path.substring(0, offset);
  }

  /**
   * Get the base name of the path.
   * @param path The full channel path
   * @return
   */
  public static String getBaseName(String path) {
    int offset = path.lastIndexOf(CHANNEL_DELIM);
    if (offset < 0) {
      return path;
    }
    return path.substring(offset+1);
  }

  /**
   * Get the root node ID from the path.  In the current implementation, it is
   * the app ID.
   * @param path The real path of the channel.
   * @return The root node ID.
   */
  public static String getRootNodeId(String path) {
    int start = (path.charAt(0) == CHANNEL_DELIM) ? 1 : 0;
    int offset = path.indexOf(CHANNEL_DELIM, start);
    if (offset < 0) {
      return path.substring(start);
    } else {
      return path.substring(start, offset);
    }
  }

  /**
   * @hide
   * Normalize the channel path by collapsing all contiguous '/' and make it lower
   * case.  It also makes sure that channel cannot be null, empty, start or end
   * with '/'.
   * @param path A channel path.
   * @return A normalized channel path.
   * @throws IllegalArgumentException Channel cannot be null or empty.
   * @throws IllegalArgumentException Channel cannot start or end with '/'.
   */
  public static String normalizePath(String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Channel cannot be null or empty");
    }
    if (path.charAt(0) == ChannelHelper.CHANNEL_DELIM ||
        path.charAt(path.length()-1) == ChannelHelper.CHANNEL_DELIM) {
      throw new IllegalArgumentException("Channel cannot start or end with '/'");
    }
    StringBuilder sb = new StringBuilder(path.length());
    char prev = '\0';
    for (char c : path.toCharArray()) {
      if (c == ChannelHelper.CHANNEL_DELIM) {
        if (prev == ChannelHelper.CHANNEL_DELIM) {
          continue;
        }
      } else {
        if (prev == ChannelHelper.CHANNEL_DELIM) {
          sb.append(ChannelHelper.CHANNEL_DELIM);
        }
        //MOB-2406:No longer lower case channel names
        sb.append(c);
      }
      prev = c;
    }
    return sb.toString();
  }

  /**
   * @hide
   * Validate the channel name.
   * @param channel A non-null channel with path syntax.
   * @throws IllegalArgumentException Channel cannot contain '/'.
   */
  public static void checkPathAllowed(String channel) {
    if (channel == null || channel.isEmpty()) {
      throw new IllegalArgumentException("The channel name cannot be null or empty");
    }
    if (channel.length() > Constants.MMX_MAX_CHANNEL_LEN) {
      throw new IllegalArgumentException("The length of channel name exceeds "+Constants.MMX_MAX_CHANNEL_LEN);
    }
    if (CHANNEL_RESTRICTED_NAME) {
      if (channel.indexOf(ChannelHelper.CHANNEL_DELIM) >= 0) {
        throw new IllegalArgumentException(
            "The path syntax is disabled; channel cannot contain '/'");
      }
    }
  }

  public static boolean validatePublisherType(String publisherType) {
    for (TopicAction.PublisherType type : TopicAction.PublisherType.values()) {
      if (type.name().equals(publisherType)) {
        return true;
      }
    }
    return false;
  }

  public static void restrictPathSyntax(boolean restrict) {
    CHANNEL_RESTRICTED_NAME = restrict;
  }

  /**
   * Validate supplied application channel name
   * @param channelName
   * @return true if the channel name is valid. false if the channel name is invalid.
   */
  public static boolean validateApplicationChannelName (String channelName) {
    if (channelName == null || channelName.isEmpty()) {
      return false;
    }
    int length = channelName.length();
    if (length > Constants.MMX_MAX_CHANNEL_LEN) {
      return false;
    }
    Matcher matcher = CHANNEL_NAME_PATTERN.matcher(channelName);
    return matcher.matches();
  }

  /**
   * The hack to fix MOB-2516 that allows the console to display user channels
   * as userID#channelName. This method parses the global channel or user
   * channel properly.
   * TODO: once the name and ID are decoupled, this method can be deprecated.
   * @param channelName The form of "channelID" or "userID#channelID".
   * @return
   */
  public static MMXChannelId nameToId(String channelName) {
      int index = channelName.indexOf(CHANNEL_SEPARATOR);
      if (index < 0) {
          return new MMXChannelId(channelName);
      } else {
          return new MMXChannelId(channelName.substring(0, index), channelName
                  .substring(index + 1));
      }
  }

  /**
   * The hack to fix MOB-2516 to convert a user channel to userID#channelName.
   * TODO: once the name and ID are decoupled, this method can be deprecated.
   * @param userId
   * @param channelName
   * @return
   */
  public static String idToName(String userId, String channelName) {
      if (userId == null) {
          return channelName;
      } else {
          return userId + CHANNEL_SEPARATOR + channelName;
      }
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
    int hash = channelId.indexOf(ChannelHelper.CHANNEL_SEPARATOR);
    if (hash <= 0) {
      return toNodeId(appId, null, channelId);
    } else {
      return toNodeId(appId, channelId.substring(0, hash), channelId.substring(hash+1));
    }
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
    if (userId == null && id == null) {
      return appId;
    }
    if (userId == null || userId.isEmpty()) {
      userId = CHANNEL_FOR_APP_STR;
    } else {
      userId = userId.toLowerCase();
    }
    StringBuilder sb = new StringBuilder(appId.length()+userId.length()+id.length()+3);
    sb.append(CHANNEL_DELIM).append(appId)
      .append(CHANNEL_DELIM).append(userId)
      .append(CHANNEL_DELIM).append(id.toLowerCase());
    return sb.toString();
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
    String channelId = nodeId.substring(index2+1);
    if (userId.charAt(0) == CHANNEL_FOR_APP) {
      return channelId;
    } else {
      return userId + CHANNEL_SEPARATOR + channelId;
    }
  }
}
