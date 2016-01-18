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

import java.util.List;

/**
 * This class represents the push/wake-up result.
 */
public class PushResult {
  /**
   * The device that a push message has been sent to.
   */
  public static class PushIdTuple {
    private String deviceId;
    private String pushId;

    public PushIdTuple(String deviceId, String pushId) {
      this.deviceId = deviceId;
      this.pushId = pushId;
    }

    public PushIdTuple() {
    }

    public String getDeviceId() {
      return deviceId;
    }

    public void setDeviceId(String deviceId) {
      this.deviceId = deviceId;
    }

    public String getPushId() {
      return pushId;
    }

    public void setPushId(String pushId) {
      this.pushId = pushId;
    }
  }
  
  /**
   * Unsent device and its status code of a push message.
   */
  public static class Unsent {
    private String deviceId;
    private int code;
    private String message;

    /**
     * Constructor
     * @param deviceId
     * @param code
     * @param message
     */
    public Unsent(String deviceId, int code, String message) {
      this.deviceId = deviceId;
      this.code = code;
      this.message = message;
    }

    public Unsent() {
    }

    public String getDeviceId() {
      return deviceId;
    }

    public void setDeviceId(String deviceId) {
      this.deviceId = deviceId;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

  private Count count;
  private List<Unsent> unsentList;
  private List<PushIdTuple> sentList;

  public Count getCount() {
    return count;
  }

  public void setCount(Count count) {
    this.count = count;
  }

  public List<Unsent> getUnsentList() {
    return unsentList;
  }

  public void setUnsentList(List<Unsent> unsentList) {
    this.unsentList = unsentList;
  }

  public List<PushIdTuple> getSentList() {
    return sentList;
  }

  public void setSentList(List<PushIdTuple> sentList) {
    this.sentList = sentList;
  }
}
