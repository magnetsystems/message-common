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

import com.magnet.mmx.protocol.MMXChannelId;

/**
 * @hide
 * An internal representation of an MMX global or user channel under an
 * application.
 */
public class AppChannel extends MMXChannelId {
  private String mAppId;

  AppChannel(String appId, String userId, String channel) {
    super(userId, channel);
    mAppId = appId;
  }

  /**
   * Get the appID.
   * @return
   */
  public String getAppId() {
    return mAppId;
  }

  @Override
  public String toString() {
    return "[ app="+mAppId+", channel="+super.toString()+" ]";
  }
}
