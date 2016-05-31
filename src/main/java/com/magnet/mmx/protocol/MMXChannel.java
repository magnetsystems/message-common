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

import java.io.Serializable;

/**
 * A generic channel identifier interface for the channel.  Typically, a channel
 * resides in the global name-space that the channel name must be unique within
 * the application.
 */
public interface MMXChannel extends Serializable {
  /**
   * Get the unique channel ID (within an app).
   * @return
   */
  public String getId();
  /**
   * @hide
   * Return the user ID under which the channel resides.  A channel in the user
   * name-space must have a unique name under the user.
   * @return A user ID for user channel, or null for global channel.
   */
  public String getUserId();
  /**
   * Get the fully qualified channel name.
   * @return A channel name.
   */
  public String getName();
  /**
   * Get the display name of this channel node.
   * @return
   */
  public String getDisplayName();
  /**
   * @hide
   * Check if this channel is under the user name-space.
   * @return true if a user channel; otherwise, false.
   */
  public boolean isUserChannel();

  /**
   * Check if two channels are equal.  The channel name is case insensitive.
   * @param channel A channel to be compared.
   * @return true if they are equals; otherwise, false.
   */
  public boolean equals(MMXChannel channel);
}
