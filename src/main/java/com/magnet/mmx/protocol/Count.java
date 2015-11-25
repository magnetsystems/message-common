package com.magnet.mmx.protocol;

/**
 * Count object to represent the results of the operation
 */
public class Count {
  private int requested;
  private int sent;
  private int unsent;

  public Count(int requested, int sent, int unsent) {
    this.requested = requested;
    this.sent = sent;
    this.unsent = unsent;
  }

  public Count() {
  }

  public int getRequested() {
    return requested;
  }

  public void setRequested(int requested) {
    this.requested = requested;
  }

  public int getSent() {
    return sent;
  }

  public void setSent(int sent) {
    this.sent = sent;
  }

  public int getUnsent() {
    return unsent;
  }

  public void setUnsent(int unsent) {
    this.unsent = unsent;
  }
}