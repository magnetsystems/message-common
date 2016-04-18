/*   Copyright (c) 2016 Magnet Systems, Inc.
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

import java.util.Date;
import java.util.Map;

/**
 * Requests and responses for the MMX template component.
 *
 */
public class TemplateDataModel {

  /**
   * Request for the template CRUD operations.
   */
  public static class TemplateRequest {

      String appId;
      String templateName;
      String template;

      public String getAppId() {
          return appId;
      }
      public void setAppId(String appId) {
          this.appId = appId;
      }
      public String getTemplateName() {
          return templateName;
      }
      public void setTemplateName(String templateName) {
          this.templateName = templateName;
      }
      public String getTemplate() {
          return template;
      }
      public void setTemplate(String template) {
          this.template = template;
      }
  }

  /**
   * Response for the template CRUD operations.
   */
  public static class TemplateResponse {

      int templateId;
      String appId;
      String templateType;
      String templateName;
      String template;

      public int getTemplateId() {
          return templateId;
      }
      public void setTemplateId(int templateId) {
          this.templateId = templateId;
      }
      public String getAppId() {
          return appId;
      }
      public void setAppId(String appId) {
          this.appId = appId;
      }
      public String getTemplateType() {
          return templateType;
      }
      public void setTemplateType(String templateType) {
          this.templateType = templateType;
      }
      public String getTemplateName() {
          return templateName;
      }
      public void setTemplateName(String templateName) {
          this.templateName = templateName;
      }
      public String getTemplate() {
          return template;
      }
      public void setTemplate(String template) {
          this.template = template;
      }
  }

  /**
   * Template validation request
   */
  public static class ValidationRequest {

    private MockPushConfig config;
    private NameDesc application;
    private NameDesc channel;
    private MsgData msg;

    public ValidationRequest() {
    }

    public ValidationRequest(TemplateDataModel.MockPushConfig config,
                NameDesc application, NameDesc channel, MsgData msg) {
      this.config = config;
      this.application = application;
      this.channel = channel;
      this.msg = msg;
    }

    public MockPushConfig getConfig() {
      return config;
    }

    public void setConfig(MockPushConfig config) {
      this.config = config;
    }

    public NameDesc getApplication() {
      return application;
    }

    public void setApplication(NameDesc application) {
      this.application = application;
    }

    public NameDesc getChannel() {
      return channel;
    }

    public void setChannel(NameDesc channel) {
      this.channel = channel;
    }

    public MsgData getMsg() {
      return msg;
    }

    public void setMsg(MsgData msg) {
      this.msg = msg;
    }
  }

  /**
   * Template validation response.
   */
  public static class ValidationResponse {
    private boolean success;
    private String result;

    public void setSuccessResult(String result) {
      this.success = true;
      this.result = result;
    }

    public void setErrorResult(String result) {
      this.success = false;
      this.result = result;
    }

    public boolean isSuccess() {
      return this.success;
    }

    public String getResult() {
      return this.result;
    }
  }

  /**
   * Content of a message as a template context.
   */
  public static class MsgData {
    private String from;
    private Date date;
    private Map<String, String> content;

    public MsgData() {
    }

    public MsgData(String from, Date pubDate, Map<String, String> content) {
      this.from = from;
      this.date = pubDate;
      this.content = content;
    }

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public Map<String, String> getContent() {
      return content;
    }

    public void setContent(Map<String, String> content) {
      this.content = content;
    }
  }

  /**
   * Meta data for application or channel as a template context.
   */
  public static class NameDesc {
    private String name;
    private String desc;
    private int count;

    public NameDesc() {
    }

    public NameDesc(String name, String desc, int count) {
      this.name = name;
      this.desc = desc;
      this.count = count;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }
  }

  /**
   * Mock push configuration as a template context.
   */
  public static class MockPushConfig {
    private boolean mSilentPush;
    private Map<String, String> mMeta;

    public MockPushConfig() {
    }

    public MockPushConfig(boolean silentPush, Map<String, String> meta) {
      mSilentPush = silentPush;
      mMeta = meta;
    }

    public boolean isSilentPush() {
      return mSilentPush;
    }

    public void setSilentPush(boolean silentPush) {
      mSilentPush = silentPush;
    }

    public Map<String, String> getMeta() {
      return mMeta;
    }

    public void setMeta(Map<String, String> meta) {
      mMeta = meta;
    }
  }
}
