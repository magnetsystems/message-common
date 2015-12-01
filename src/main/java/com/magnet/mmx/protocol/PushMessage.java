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

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.StringReader;

import com.magnet.mmx.util.GsonData;
import com.magnet.mmx.util.InvalidMessageException;
import com.magnet.mmx.util.TypeMapper;
import com.magnet.mmx.util.UnknownTypeException;

/**
 * @hide
 * A class to encode a payload (plain old Java object) to a push message, and 
 * decode a push message to a payload.  A built-in ping-pong message can test
 * if a device is ready to receive a push message for debug purpose.
 */
public class PushMessage implements Serializable {
  private static final long serialVersionUID = 877180761025820867L;
  /**
   * Maximum push message size.  It is the minimum of APNS and GCM max sizes.
   */
  public final static int MAX_SIZE = 2048;
  private Action mAction;
  private String mType;
  private Object mPayload;

  /**
   * The action what the recipient should do when a push message is received.
   */
  public static enum Action {
    /**
     * Just wake up the application.
     */
    WAKEUP("w"),
    /**
     * One client pushes data to another client.
     */
    PUSH("p");

    private String mCode;

    private Action(String code) {
      mCode = code;
    }

    public String getCode() {
      return mCode;
    }
  }

  PushMessage(Action action, String type, Object payload) {
    mAction = action;
    mType = type;
    mPayload = payload;
  }
  
  /**
   * Get the action code for a received push message.
   * @return A wakeup (silent notification) or push (non-silent notification).
   */
  public Action getAction() {
    return mAction;
  }
  
  /**
   * Get the push payload type.
   * @return
   */
  public String getType() {
    return mType;
  }
  
  /**
   * Get the payload object.  It is always a type of GCMPayload.
   * @return
   */
  public Object getPayload() {
    return mPayload;
  }

  @Override
  public String toString() {
    return "{action="+mAction+", type="+mType+", data={"+mPayload+"}}";
  }

  /**
   * Encode a payload type and payload object into MMX push message.  A wake-up
   * action is just to wake up the device.  A push action is to send a payload
   * to a device.  A built-in ping-pong push message can check if a device is
   * ready to receive push messages using the native push channel (GCM or APNS.)
   * To do a two-way ping between current device and a remote device, the 
   * <code>type</code> is {@link Constants.PingPongCommand#pingpong}.  To ping
   * the current device, the <code>type</code> is {@link Constants.PingPongCommand#ping}.
   * @param action WAKEUP or PUSH
   * @param type A unique payload type or null.
   * @param payload A POJO, or null.
   * @return An encoded string to be sent as push message.
   * @throws InvalidMessageException The payload is too large.
   */
  public static String encode(Action action, String type, Object payload) 
                                throws InvalidMessageException {
    StringBuilder sb = new StringBuilder();
    sb.append("mmx:")
      .append(action.getCode());
    if (type != null) {
      sb.append(':')
        .append(type);
    }
    sb.append("\r\n");
    if (type != null && payload != null) {
      GsonData.getGson().toJson(payload, sb);
    }
    if (sb.length() > MAX_SIZE) {
      throw new InvalidMessageException("The payload is too large for push message");
    }
    return sb.toString();
  }
  
  /**
   * Decode an MMX push message into a payload type and payload object.  Current
   * implementation is to have payload in GCMPayload class.  In the future, the
   * payload can have different types using the MMXTypeMapper for mapping.
   * @param pushMsg The encoded push message string.
   * @param mapper Not used, but it maps a type to a class.
   * @return A PushMessage.
   * @throws InvalidMessageException A malformed push message.
   * @throws UnknownTypeException Cannot find a class for the type.
   * @see MMXTypeMapper
   */
  public static PushMessage decode(String pushMsg, TypeMapper mapper)
      throws InvalidMessageException, UnknownTypeException {
    BufferedReader reader = new BufferedReader(new StringReader(pushMsg));
    try {
      String[] tokens = reader.readLine().split(":");
      if (tokens == null || tokens.length < 2 || !tokens[0].equals("mmx")) {
        throw new InvalidMessageException("Malformed MMX Push Message");
      }
      Object payload = null;
      String type = null;
      Action action = tokens[1].equalsIgnoreCase(Action.WAKEUP.getCode()) ?
          Action.WAKEUP : Action.PUSH;
      if (tokens.length >= 3 && !tokens[2].isEmpty()) {
        type = tokens[2];
      }
      // Always to use GCMPayload class.
      payload = GsonData.getGson().fromJson(reader, GCMPayload.class);
      return new PushMessage(action, type, payload);
    } catch (InvalidMessageException e) {
      throw e;
    } catch (UnknownTypeException e) {
      throw e;
    } catch (Throwable e) {
      throw new InvalidMessageException("Cannot decode push message", e);
    }
  }
}

