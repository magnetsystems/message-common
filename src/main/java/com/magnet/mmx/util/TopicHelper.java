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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.magnet.mmx.protocol.Constants;
import com.magnet.mmx.protocol.MMXTopicId;
import com.magnet.mmx.protocol.OSType;
import com.magnet.mmx.protocol.TopicAction;

/**
 * @hide
 * A helper class for topics in PubSub.  There are two formats for user topic:
 * userID/topicName (internal node path) and useID#topicName (used in a path for
 * REST API.)  Use TopicResource.nameToId() or TopicResource.idToName() to
 * encode/decode userID#topicName; otherwise, use {@link #toTopicId(String)}
 * or {@link #toNodeId(String, String, String)} to encode/decode userID/topicName.
 */
public class TopicHelper {
  private static boolean TOPIC_RESTRICTED_NAME = true;  // true for MOB-1423
  public final static char TOPIC_DELIM = '/';
  public final static char TOPIC_FOR_APP = '*';
  public final static char TOPIC_SEPARATOR = '#';   // use in URL only; userID#topicName
  public final static String TOPIC_DELIM_STR = String.valueOf(TOPIC_DELIM);
  public final static String TOPIC_FOR_APP_STR = String.valueOf(TOPIC_FOR_APP);
  public final static String TOPIC_GEOLOC = "com.magnet.geoloc";  // a leaf node
  public final static String TOPIC_OS_ROOT = "com.magnet.os";
  public final static String TOPIC_OS = TOPIC_OS_ROOT+"/";        // a collection node
  public final static String TOPIC_LEAF_ALL = "_all_";            // a leaf node
  public final static String TOPIC_NAME_PATTERN_STRING = "^[a-zA-Z0-9_\\.\\-]*$";

  private static final Pattern TOPIC_NAME_PATTERN = Pattern.compile(TOPIC_NAME_PATTERN_STRING);


  // the node is formulated as follows:
  // /<appId>/*/com.magnet.os/<osType>

  /**
   * Generate the container topic name of devices based on OS type. This container topic can be used
   * as the parent node for creating leaf topics for publishing events to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceTopicName(OSType osType) {
    StringBuilder stringBuilder = new StringBuilder()
        .append(TOPIC_OS)
        .append(osType.name());
    return stringBuilder.toString();
  }

  /**
   * Generate the publishable topic name of devices based on OS type where all event related this OS type
   * can be published to
   * @param osType ANDROID, IOS
   * @return
   */
  public static String generateDeviceAllLeafTopicName(OSType osType) {
    StringBuilder stringBuilder = new StringBuilder()
        .append(TOPIC_OS)
        .append(osType.name())
        .append(TOPIC_DELIM)
        .append(TOPIC_LEAF_ALL);
    return stringBuilder.toString();
  }

  /**
   * @hide
   * Assuming that <code>topicId</code> is in the form of "/appId/*" or
   * "/appId/userId", check if it is a user topic.
   * @param nodeId
   * @return
   */
  public static boolean isUserTopic(String nodeId) {
    int index = nodeId.indexOf(TOPIC_DELIM, 1);
    return (index > 1) && (nodeId.charAt(index+1) != TOPIC_FOR_APP);
  }

  /**
   * Check if the topic with specified id is a topic for the passed in appId
   * @param nodeId A path should be started with "/appId/".
   * @param appId
   * @return
   */
  public static boolean isAppTopic(String nodeId, String appId) {
    if (nodeId == null || nodeId.isEmpty()) {
      return false;
    }
    try {
      return ((nodeId.charAt(0) == TOPIC_DELIM) &&
               nodeId.startsWith(appId, 1) &&
               (nodeId.charAt(1+appId.length()) == TOPIC_DELIM));
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Check if the topic represented by the nodeId is a user topic
   * @param nodeId A non-null full topic path
   * @param appId A non-null app ID
   * @return true if it represents a user topic false other wise.
   */
  public static boolean isUserTopic(String nodeId, String appId) {
    String prefix = new StringBuilder().append(TOPIC_DELIM).append(appId)
                      .append(TOPIC_DELIM).append(TOPIC_FOR_APP)
                      .append(TOPIC_DELIM).toString();
    try {
      int index = nodeId.indexOf(prefix);
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
   * Construct an application prefix for the topic node ID.
   * @param appId
   * @return The prefix as "/appId/"
   */
  public static String makePrefix(String appId) {
    return TOPIC_DELIM + appId + TOPIC_DELIM;
  }

  /**
   * Parse an XMPP nodeID into a AppTopic object.  There are two formats:
   * /appID/&asterisk;/ID for global topic and /appID/userID/ID for user topic.
   * @param nodeId A XMPP PubSub nodeID string.
   * @return An AppTopic object, or null if not an MMX topic.
   */
  public static AppTopic toAppTopic(String nodeId) {
    if (nodeId.charAt(0) != TOPIC_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(TOPIC_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(TOPIC_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String appId = nodeId.substring(1, index1);
    String userId = nodeId.substring(index1+1, index2);
    String id = nodeId.substring(index2+1);
    return new AppTopic(appId, (userId.charAt(0) == TOPIC_FOR_APP) ?
                      null : userId, id);
  }

  /**
   * Construct the OS topic with an optional version.
   * @param os An OS type, or null for all OS's.
   * @param version A version string, or null for all versions.
   * @return
   */
  public static String makeOSTopic(OSType os, String version) {
    if (os == null) {
      return TOPIC_OS_ROOT;
    }
    if (version == null) {
      return TOPIC_OS + os.toString();
    } else {
      return TOPIC_OS + os.toString() + TOPIC_DELIM + version;
    }
  }

  /**
   * @hide
   * Find the length of the prefix in the topic.  It is the third slash in the
   * real topic.  The prefix is "/appId/userId/".
   * @param path The topic full path.
   * @return The length of the prefix.
   */
  public static int getPrefixLength(String path) {
    int offset;
    offset = path.indexOf(TOPIC_DELIM, 1);
    offset = path.indexOf(TOPIC_DELIM, offset+1);
    return ++offset;
  }

  /**
   * Get the base name of the path.
   * @param nodeId The full topic path
   * @return
   */
  public static String getBaseName(String nodeId) {
    int offset = nodeId.lastIndexOf(TOPIC_DELIM);
    if (offset < 0) {
      return nodeId;
    }
    return nodeId.substring(offset+1);
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
    return getAppId(nodeId);
  }

  /**
   * Get the appId from the node ID.
   * @param nodeId The pubsub node ID.
   * @return The app ID.
   */
  public static String getAppId(String nodeId) {
    if (nodeId.charAt(0) != TOPIC_DELIM) {
      return nodeId;
    }
    int offset = nodeId.indexOf(TOPIC_DELIM, 1);
    if (offset < 0) {
      return nodeId.substring(1);
    } else {
      return nodeId.substring(1, offset);
    }
  }

  /**
   * Get the user ID from node ID.
   * @param nodeId The pubsub node ID.
   * @return The user ID for user topic, or null for global topic.
   */
  public static String getUserId(String nodeId) {
    if (nodeId.charAt(0) != TOPIC_DELIM) {
      // It is the app root node.
      return null;
    }
    int index1 = nodeId.indexOf(TOPIC_DELIM, 1);
    if (index1 < 0) {
      // Malformed node ID.
      return null;
    }
    int index2 = nodeId.indexOf(TOPIC_DELIM, ++index1);
    String userId = (index2 < 0) ?
        nodeId.substring(index1) : nodeId.substring(index1, index2);
    return (userId.charAt(0) == TOPIC_FOR_APP) ? null : userId;
  }

  /**
   * @hide
   * Normalize the path by collapsing all contiguous '/'.  It also makes
   * sure that the path cannot be null, empty, started or ended with '/'.
   * @param path Fully qualified topic name path.
   * @return A normalized path.
   * @throws IllegalArgumentException Topic cannot be null or empty.
   * @throws IllegalArgumentException Topic cannot start or end with '/'.
   */
  public static String normalizePath(String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Fully qualified name cannot be null or empty");
    }
    if (path.charAt(0) == TopicHelper.TOPIC_DELIM ||
        path.charAt(path.length()-1) == TopicHelper.TOPIC_DELIM) {
      throw new IllegalArgumentException("Fully qualified name cannot be started or ended with '/'");
    }
    StringBuilder sb = new StringBuilder(path.length());
    char prev = '\0';
    for (char c : path.toCharArray()) {
      if (c == TopicHelper.TOPIC_DELIM) {
        if (prev == TopicHelper.TOPIC_DELIM) {
          continue;
        }
      } else {
        if (prev == TopicHelper.TOPIC_DELIM) {
          sb.append(TopicHelper.TOPIC_DELIM);
        }
        //MOB-2406:No longer lower case topic names
        sb.append(c);
      }
      prev = c;
    }
    return sb.toString();
  }

  /**
   * @hide
   * Validate the topic name.
   * @param name A non-null topic with path syntax.
   * @throws IllegalArgumentException Topic cannot contain '/'.
   */
  public static void checkPathAllowed(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Fully qualified name cannot be null or empty");
    }
    if (name.length() > Constants.MMX_MAX_TOPIC_LEN) {
      throw new IllegalArgumentException("The length of names exceeds "+Constants.MMX_MAX_TOPIC_LEN);
    }
    if (TOPIC_RESTRICTED_NAME) {
      if (name.indexOf(TopicHelper.TOPIC_DELIM) >= 0) {
        throw new IllegalArgumentException(
            "The path syntax is disabled; name cannot contain '/'");
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
    TOPIC_RESTRICTED_NAME = restrict;
  }

  /**
   * Validate supplied application topic name
   * @param topicName
   * @return true if the topic name is valid. false if the topic name is invalid.
   */
  public static boolean validateApplicationTopicName (String topicName) {
    if (topicName == null || topicName.isEmpty()) {
      return false;
    }
    int length = topicName.length();
    if (length > Constants.MMX_MAX_TOPIC_LEN) {
      return false;
    }
    Matcher matcher = TOPIC_NAME_PATTERN.matcher(topicName);
    return matcher.matches();
  }

  /**
   * The hack to fix MOB-2516 that allows the console to display user topics as
   * userID#topicName.  This method parses the global topic or user topic properly.
   * TODO: once the name and ID are decoupled, this method can be deprecated.
   * @param topicName The form of "topicID" or "userID#topicID".
   * @return
   * @see #idToName(String, String)
   */
  public static MMXTopicId nameToId(String topicId) {
    return MMXTopicId.fromId(topicId);
  }

  /**
   * The hack to fix MOB-2516 to convert a user topic to userId#topicName.
   * TODO: once the name and ID are decoupled, this method can be deprecated.
   * @param userId
   * @param topicName
   * @return
   * @see #nameToId(String)
   */
  public static String idToName(String userId, String id) {
    if (userId == null) {
      return id;
    } else {
      return userId + TOPIC_SEPARATOR + id;
    }
  }

  /**
   * Check if the topic ID is for user topic.  The topic ID is in the form of
   * "userID#ID" or "ID".
   * @param topicId A topic ID.
   * @return true if a user topic; otherwise, false.
   */
  public static boolean isUserTopicId(String topicId) {
    return topicId.indexOf(TOPIC_SEPARATOR) > 0;
  }

  /**
   * Convert MMXTopicId into a pubsub node ID.  The internal ID must have
   * the form of "ID" or "userID#ID".
   * @param appId The app ID.
   * @param topciId The topic ID object which must have the internal ID.
   * @return A pubsub node ID.
   * @see MMXTopicId#getId()
   */
  public static String toNodeId(String appId, MMXTopicId topicId) {
    String tid = topicId.getId();
    if (tid == null) {
      throw new IllegalArgumentException(
          "Cannot convert to nodeID without unique ID; name="+topicId.getName());
    }
    return toNodeId(appId, tid);
  }

  /**
   * Convert a string format of topic ID into a pubsub node ID.
   * @param appId The app ID.
   * @param topicId The topic ID in the form of "ID" or "userID#ID".
   * @return A pubsub node ID.
   * @see MMXTopicId#getId()
   */
  public static String toNodeId(String appId, String topicId) {
    int hash = topicId.indexOf(TopicHelper.TOPIC_SEPARATOR);
    if (hash <= 0) {
      return toNodeId(appId, null, topicId);
    } else {
      return toNodeId(appId, topicId.substring(0, hash), topicId.substring(hash+1));
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
      userId = TOPIC_FOR_APP_STR;
    } else {
      userId = userId.toLowerCase();
    }
    StringBuilder sb = new StringBuilder(appId.length()+userId.length()+id.length()+3);
    sb.append(TOPIC_DELIM).append(appId)
      .append(TOPIC_DELIM).append(userId)
      .append(TOPIC_DELIM).append(id.toLowerCase());
    return sb.toString();
  }

  /**
   * Construct the pubsub node ID for the app root node.  The node ID will be
   * "appID".
   * @param appId The app ID.
   * @return A pubsub node ID of the app root node.
   */
  public static String toAppNodeId(String appId) {
    return appId;
  }

  /**
   * Construct the pubsub node ID for the root global topics.  The node ID will
   * be "/appID/&asterisk;".
   * @param appId The app ID.
   * @return A pubsub node ID of the root global topic.
   */
  public static String toGlobalNodeId(String appId) {
    return TOPIC_DELIM + appId + TOPIC_DELIM + TOPIC_FOR_APP;
  }

  /**
   * Construct the pubsub node ID for the root user topic.  The node ID will be
   * "/appID/userID".
   * @param appId The app ID.
   * @param userId A user ID for user topics.
   * @return A pubsub node ID of the root user topic.
   */
  public static String toUserNodeId(String appId, String userId) {
    return TOPIC_DELIM + appId + TOPIC_DELIM + userId;
  }

  /**
   * Convert an XMPP pubsub node ID into a string form of topic ID.
   * The pubsub node ID must have the form of /appID/&asterisk;/ID or
   * /appID/userID/ID, and the topic ID will be in the form of "ID" or
   * "userID#ID."
   * @param nodeId A XMPP PubSub nodeID string.
   * @return A string form of topic ID, or null if not an MMX topic.
   */
  public static String toTopicId(String nodeId) {
    if (nodeId.charAt(0) != TOPIC_DELIM) {
      return null;
    }
    int index1 = nodeId.indexOf(TOPIC_DELIM, 1);
    if (index1 < 0) {
      return null;
    }
    int index2 = nodeId.indexOf(TOPIC_DELIM, index1+1);
    if (index2 < 0) {
      return null;
    }
    String userId = nodeId.substring(index1+1, index2);
    String topicId = nodeId.substring(index2+1);
    if (userId.charAt(0) == TOPIC_FOR_APP) {
      return topicId;
    } else {
      return userId + TOPIC_SEPARATOR + topicId;
    }
  }

  /**
   * Convert an XMPP pubsub node ID into a topic ID object with a display
   * name.  The pubsub node ID must have the form of /appID/&asterisk;/ID or
   * /appID/userID/ID, and the topic ID will be "ID" or "userID#ID."
   * @param nodeId A XMPP pubsub nodeID string.
   * @param displayName An optional display name.
   * @return A MMXTopicId, or null if not an MMX topic.
   */
  public static MMXTopicId toTopicId(String nodeId, String displayName) {
    return MMXTopicId.fromNodeId(nodeId, displayName);
  }

  /**
   * Convert an XMPP pubsub node ID and a fully qualified name into a topic ID
   * object.  The topic display name is the last component of <code>name</code>.
   * @param nodeId An XMPP pubsub nodeID.
   * @param userId Owner ID for user topic, null for global topic.
   * @param name A path-like fully qualified name.
   * @return
   */
  public static MMXTopicId toTopicId(String nodeId, String userId, String name) {
    return MMXTopicId.fromIdName(toTopicId(nodeId), userId, name);
  }

  /**
   * Check if the <code>nodeId</code> is the app root node ID (appID),
   * global root node ID (/appID/*) or user root node ID (/appID/userID.)
   * @return true if it is one of the top node ID's; otherwise, false.
   */
  public static boolean isTopNodeId(String nodeId) {
    int count = 0, start = 0;
    while ((start = nodeId.indexOf(TOPIC_DELIM, start)) >= 0) {
      if (++count >= 3) {
        return false;
      }
      ++start;
    }
    return true;
  }
}
