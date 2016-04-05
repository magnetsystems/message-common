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

import java.util.Hashtable;

/**
 * @hide
 * A convenient class for the optional MMXMessage mmx headers (mmxmeta data.)
 * All headers are optional.
 */
public class MmxHeaders extends Hashtable<String, Object> {
  private static final long serialVersionUID = 9047305151889919321L;
  /**
   * A special header used by MMX SDK to specify the sender with display name.
   * The value must be MMXid.
   */
  public final static String FROM = "From";
  /**
   * A special header used by MMX SDK to specify the recipient(s) with display
   * name(s).  The value must be MMXid[].
   */
  public final static String TO = "To";
  /**
   * Do not send the ack signal messages when server receives the message.
   */
  public final static String NO_ACK = "NoAck";
  /**
   * Notify all devices of the publisher of the new items.
   */
  public final static String SELF_NOTIFICATION = "SelfNotification";
  /**
   * Optional push configuration name.
   */
  public final static String PUSH_CONFIG = "Push-Config-Name";
  /**
   * A default constructor.
   */
  public MmxHeaders() {
    super();
  }

  /**
   * A constructor with a default capacity.
   * @param capacity The initial capacity.
   */
  public MmxHeaders(int capacity) {
    super(capacity);
  }

  /**
   * A convenient method to set or remove a header with any JSONifiable value.
   * This is mainly used for mmxmeta stanza.  If <code>value</code> is null,
   * the header will be removed.
   * @param key A key name.
   * @param value Any JSONifiable object, or null.
   * @return
   */
  public MmxHeaders setHeader(String key, Object value) {
    if (key == null) {
      throw new IllegalArgumentException("The key cannot be null");
    }
    if (value == null) {
      remove(key);
    } else {
      put(key, value);
    }
    return this;
  }

  /**
   * Get a header JSONifiable value with default.  This method is mainly used
   * with mmxmeta stanza.
   * @param key A key name.
   * @param defVal A default value to be returned if the key does not exist.
   * @return A header value or the default value.
   */
  public Object getHeader(String key, Object defVal) {
    if (key == null) {
      throw new IllegalArgumentException("The key cannot be null");
    }
    Object value;
    if ((value = this.get(key)) == null) {
      return defVal;
    } else {
      return value;
    }
  }

  /**
   * Set the TO header with the MMX ID's of recipients.  It is equivalent to
   * {@link #setHeader(String, Object)} with {@link #TO}.
   * @param xids The recipients' MMX ID's, or null
   * @return
   */
  public MmxHeaders setTo(MMXid[] xids) {
    return setHeader(TO, xids);
  }

  /**
   * Set the FROM header with the MMX ID of the sender.  It is equivalent to
   * {@link #setHeader(String, Object)} with {@link #FROM}.
   * @param xid The sender MMX ID.
   * @return
   */
  public MmxHeaders setFrom(MMXid xid) {
    return setHeader(FROM, xid);
  }

  /**
   * Not to send the ack signal messages.  REST API should disable the ack
   * signaling messages for unicast and multicast messages.
   * @param enable
   * @return
   */
  public MmxHeaders setNoAck(boolean noAck) {
    return setHeader(NO_ACK, noAck ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Enable or disable to send push notification to publisher's devices.
   * Default is false.
   * @param selfNotify true to enable; otherwise, false.
   * @return
   */
  public MmxHeaders setSelfNotification(boolean selfNotify) {
    return setHeader(SELF_NOTIFICATION, selfNotify ? Boolean.TRUE : Boolean.FALSE);
  }
}
