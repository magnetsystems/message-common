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

/**
 * @hide
 * Common MMX element, namespace and attributes.
 */
public class Constants {
  /**
   * The default domain or service name for MMX server.
   */
  public final static String MMX_DOMAIN = "mmx";
  /**
   * The max length for topic name.
   */
  public final static int MMX_MAX_TOPIC_LEN = 50;
  public final static int MMX_MAX_CHANNEL_LEN = 50;
  /**
   * The max length for tag name.
   */
  public final static int MMX_MAX_TAG_LEN = 25;
  /**
   * The minimum user ID length for regular user account.
   */
  public final static int MMX_MIN_USERID_LEN = 5;
  /**
   * The maximum user ID length for regular user account.
   */
  public final static int MMX_MAX_USERID_LEN = 42;
  /**
   * The max payload size in bytes that MMX server supports.  It is not
   * necessary same as what the client allows.
   */
  public final static int MAX_PAYLOAD_SIZE = 2 * 1024 * 1024;
  /**
   * The payload threshold in bytes to switch from RAM to memory-mapped I/O.
   */
  public final static int PAYLOAD_THRESHOLD = 102400;
  /**
   * A delimiter used in multi-tenant environment.
   */
  public final static char APP_ID_DELIMITER = '%';
  /**
   * A flag to control if '@' is allowed in the user ID.
   */
  public final static boolean AT_SIGN_DISALLOWED = false;
  /**
   * The current protocol major version number.
   */
  public final static int MMX_VERSION_MAJOR = 2;
  /**
   * The current protocol minor version number.
   */
  public final static int MMX_VERSION_MINOR = 1;
  /**
   * The elements for MMX.
   */
  public final static String MMX_ELEMENT = "mmx";
  public final static String MMX = MMX_ELEMENT;
  public final static String MMX_APP_REG = MMX_ELEMENT;
  public final static String MMX_DEV_REG = MMX_ELEMENT;

  public final static String MMX_MMXMETA = "mmxmeta";
  public final static String MMX_META = "meta";
  public final static String MMX_PAYLOAD = "payload";

  /**
   * The default encoding type to be used for binary payload.
   */
  public final static String BASE64 = "base64";

  /**
   * The default message type if it is not specified or it is empty.
   */
  public final static String MMX_MTYPE_UNKNOWN = "unknown";
  /**
   * The message type for GeoLocaion.
   */
  public final static String MMX_MTYPE_GEOLOC = "geoloc";
  /**
   * The message type for MMXError.
   */
  public final static String MMX_MTYPE_ERROR = "mmxerror";

  // XEP-0184 message delivery receipts
  public final static String XMPP_REQUEST = "request";
  public final static String XMPP_RECEIVED = "received";
  public final static String XMPP_ATTR_ID = "id";
  public final static String XMPP_NS_RECEIPTS = "urn:xmpp:receipts";

  /**
   * The namespaces used in the MMX extension.
   */
  public final static String MMX_NS_APP = "com.magnet:appreg";
  public final static String MMX_NS_DEV = "com.magnet:dev";
  public final static String MMX_NS_USER = "com.magnet:user";
  public final static String MMX_NS_AUTH = "com.magnet:auth";
  public final static String MMX_NS_MSG_ACTION = "com.magnet:msg:action";
  public final static String MMX_NS_MSG_PAYLOAD = "com.magnet:msg:payload";
  public final static String MMX_NS_MSG_STATE = "com.magnet:msg:state";
  public final static String MMX_NS_MSG_ACK = "com.magnet:msg:ack";
  public final static String MMX_NS_MSG_PUSH = "com.magnet:msg:push";
  public final static String MMX_NS_MSG_WAKEUP = "com.magnet:msg:wakeup";
  public final static String MMX_NS_PUBSUB = "com.magnet:pubsub";
  public final static String MMX_NS_CONTEXT = "com.magnet:ctx";
  public static final String MMX_NS_MSG_SIGNAL = "com.magnet:msg:signal";

  public final static String MMX_ACTION_CODE_WAKEUP = "w";
  public final static String MMX_ACTION_CODE_PUSH = "p";

  /**
   * The attributes used in the MMX extension.
   */
  public final static String MMX_ATTR_COMMAND = "command";
  public final static String MMX_ATTR_CTYPE = "ctype";
  public final static String MMX_ATTR_MTYPE = "mtype";
  public final static String MMX_ATTR_STAMP = "stamp";
  public final static String MMX_ATTR_CHUNK = "chunk";
  public final static String MMX_ATTR_CID = "cid";
  public final static String MMX_ATTR_DST = "dst";

  /**
   * User extended properties as metadata of UserCreate; this goes into the User tables
   */
  public final static String MMX_PROP_NAME_USER_GUEST_MODE = "guest";
  public final static String MMX_PROP_VALUE_USER_GUEST_TRUE = "true";
  public final static String MMX_PROP_VALUE_USER_GUEST_FALSE = "false";
  public final static String MMX_PROP_VALUE_USER_GUEST_REMOVE = "remove";

  public static enum UserCreateMode {
    /**
     * Anonymous user as guest.
     */
    GUEST,        // create the user as a guest user
    /**
     * Regular authenticated user.
     */
    UPGRADE_USER  // upgrade to real user; mark current logged in user as deactive if in guest mode
  }

  public final static String UTF8_CHARSET = "utf-8";

  public final static int STATUS_CODE_200 = 200;
  public final static int STATUS_CODE_400 = 400;
  public final static int STATUS_CODE_500 = 500;

  /**
   * Commands for device management.
   */
  public static enum DeviceCommand {
    REGISTER,
    UNREGISTER,
    QUERY,
    GETTAGS,
    SETTAGS,
    ADDTAGS,
    REMOVETAGS,
  }

  /**
   * Commands for application management.
   */
  public static enum AppMgmtCommand {
    create,
    read,
    readMine,
    update,
    delete,
  }

  /**
   * Commands for account (user) management.
   */
  public static enum UserCommand {
    create,
    delete,
    query,
    get,
    list,
    search,
    update,
    reset,
    getTags,
    setTags,
    addTags,
    removeTags,
    searchByTags,
  }

  /**
   * Commands for wake-up messages.
   */
  public static enum PingPongCommand {
    /**
     * One way request without any response.
     */
    ping,
    /**
     * One way response from the two-way request.
     */
    pong,
    /**
     * Two-way request: one-way request and one-way response.
     */
    pingpong,
    /**
     * Send a notification using the Notification payload.
     */
    notify,
    /**
     * Wakeup the device and ask it to phone home
     */
    retrieve,
    /**
     * Pubsub wakeup with the PubSubNotification payload.
     */
    pubsub,
  }

  /**
   * Possible message states returned by the MessageManager.
   */
  public static enum MessageState {
    /**
     * The message is in an unknown state.
     */
    UNKNOWN,
    /**
     * client-only: the message has not been communicated MMX and can be cancelled.
     */
    CLIENT_PENDING,
    /**
     * Every message starts in this state
     */
    PENDING,
    /**
     * Multicast message has been submitted to the server.
     */
    SUBMITTED,
    /**
     * Message has been accepted and validated by the server.
     */
    ACCEPTED,
    /**
     * Recipient is offline and hence we need to send a wake-up notification
     */
    WAKEUP_REQUIRED,
    /**
     * Message wake up has been timed out
     */
    WAKEUP_TIMEDOUT,
    /**
     * We are waiting for recipient to wake up
     */
    WAKEUP_SENT,
    /**
     * Recipient is online and hence we transitioned to this
     * state
     */
    DELIVERY_ATTEMPTED,
    /**
     * XMPP packet has been delivered to the endpoint
     */
    DELIVERED,
    /**
     * Message has been processed by the endpoint
     */
    RECEIVED,
    /**
     * Timeout experienced by server when attempting delivery
     */
    TIMEDOUT,
  }

  /**
   * Commands for message management.  The setEvents/getEvents/addEvents/removeEvents
   * are applicable to push messages in PushManager.
   */
  public static enum MessageCommand {
    query,
    ack,
    setTags,
    getTags,
    addTags,
    removeTags,
    setEvents,
    getEvents,
    addEvents,
    removeEvents,
  }

  /**
   * Commands for PubSub.
   */
  public static enum PubSubCommand {
    /**
     * Get the latest published items.
     */
    getlatest,
    /**
     * List all nodes under an app ID.
     */
    listtopics,
    /**
     * Create a topic.
     */
    createtopic,
    /**
     * Delete a topic.
     */
    deletetopic,
    /**
     * Get topic information by a topic ID.
     * @deprecated #getTopics
     */
    getTopic,
    /**
     * Get topic information by topic ID's.
     */
    getTopics,
    /**
     * Retract a published item or all items.
     */
    retract,
    /**
     * Retract all published items from a topic owned by a user.
     */
    retractall,
    /**
     * Subscribe to a topic.
     */
    subscribe,
    /**
     * Unsubscribe a subscription.
     */
    unsubscribe,
    /**
     * Unsubscribe all topics for a device.
     */
    unsubscribeForDev,
    /**
     * Get the summary of topics.
     */
    getSummary,
    /**
     * Get the tags
     */
    getTags,
    /**
     * Set the tags
     */
    setTags,
    /**
     * Add the tags
     */
    addTags,
    /**
     * Remove the tags
     */
    removeTags,
    /**
     * Query for topics
     * @deprecated Use {@link #searchTopic}
     */
    queryTopic,
    /**
     * Search for topics.
     */
    searchTopic,
    /*
     * Fetch published items
     */
    fetch,
    /**
     * Search topics by tags
     */
    searchByTags,
    /**
     * Get published items by item ID's
     */
    getItems,
    /**
     * Get all subscribers to a topic.
     */
    getSubscribers,
  }

  // constants used in top level push payloads
  // define it here for Android sharing and gson serialized names

  /**
   * Name of the push title
   */
  public static final String PAYLOAD_PUSH_TITLE = "title";

  /**
   * Name of the push body text
   */
  public static final String PAYLOAD_PUSH_BODY = "body";

  /**
   * Name of the icon
   */
  public static final String PAYLOAD_PUSH_ICON = "icon";

  /**
   * Name of the sound
   */
  public static final String PAYLOAD_PUSH_SOUND = "sound";

  /**
   * Name of the badge
   */
  public static final String PAYLOAD_PUSH_BADGE = "badge";
  //constants related to mmx dictionary in push/ping payloads
  /**
   * Name of the mmx dictionary element
   */
  public static final String PAYLOAD_MMX_KEY = "_mmx";
  /**
   * Key for the callback url value
   */
  public static final String PAYLOAD_CALLBACK_URL_KEY = "cu";
  /**
   * Key for the type value
   */
  public static final String PAYLOAD_TYPE_KEY = "ty";
  /**
   * Key for the id value
   */
  public static final String PAYLOAD_ID_KEY = "id";

  /**
   * Key for the custom dictionary
   */
  public static final String PAYLOAD_CUSTOM_KEY = "custom";

  /**
   * The display name for all-versions of Android topic.
   */
  public static final String MMX_TOPIC_ANDROID_ALL = "Android-All";
  /**
   * The display name for all versions of iOS topic.
   */
  public static final String MMX_TOPIC_IOS_ALL = "iOS-All";
  /**
   * A partial display name for user's geo-location topic.
   */
  public static final String MMX_TOPIC_GEOLOCATION = "GeoLocation";
  /**
   * A special address prefix for MMX ad-hoc message to a node.  The JID is
   * <code>node$nodeID%appID@domain/resource</code> where nodeID contains an
   * opaque ID.  Currently the format of <code>nodeID</code> is "[userID#]ID",
   * but it is subject to change.
   */
  public static final String MMX_NODE_PREFIX = "node$";
  /**
   * A special address for MMX multicast.  When a message has multiple
   * recipients, the message should be sent to this address.
   */
  public static final String MMX_MULTICAST = "mmx$multicast";
  /**
   * Keys for the unicast message server ack, multicast message acks.
   */
  public static final String SERVER_ACK_KEY = "serverack";
  public static final String BEGIN_ACK_KEY = "beginack";
  public static final String END_ACK_KEY = "endack";
  /**
   * Flag indicated if MMX is integrated with MMS.
   */
  public static final boolean MMS_INTEGRATION_ENABLED = true;
}
