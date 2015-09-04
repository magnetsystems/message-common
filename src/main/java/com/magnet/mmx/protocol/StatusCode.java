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
 * Status code from MMX server.
 */
public interface StatusCode {
  /**
   * Unknown status code.
   */
  public final static int UNKNOWN = 0;
  /**
   * The request is success.
   */
  public final static int SUCCESS = 200;
  /**
   * The entity is created.
   */
  public final static int CREATED = 201;
  /**
   * The request has malformed syntax.
   */
  public final static int BAD_REQUEST = 400;
  /**
   * The request requires user authentication.
   */
  public final static int UNAUTHORIZED = 401;
  /**
   * Insufficient rights to perform the operation.
   */
  public final static int FORBIDDEN = 403;
  /**
   * Resource is not found.
   */
  public final static int NOT_FOUND = 404;
  /**
   * The request contains an unacceptable data.
   */
  public final static int NOT_ACCEPTABLE = 406;
  /**
   * The entity already exists.
   */
  public final static int CONFLICT = 409;
  /**
   * The entity is no longer available at the server.
   */
  public final static int GONE = 410;
  /**
   * The request is too large.
   */
  public final static int REQUEST_TOO_LARGE = 413;
  /**
   * Internal server error.
   */
  public final static int INTERNAL_ERROR = 500;
  /**
   * The feature is not implemented.
   */
  public final static int NOT_IMPLEMENTED = 501;
}
