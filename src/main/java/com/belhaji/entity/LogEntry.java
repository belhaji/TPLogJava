package com.belhaji.entity;

import java.util.Date;

public class LogEntry
{
  private Integer id;
  private Date timestamp;

  public LogEntry()
  {
  }

  public LogEntry(Integer id, Date timestamp)
  {
    this.id = id;
    this.timestamp = timestamp;
  }

  public Integer getId()
  {
    return this.id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public Date getTimestamp()
  {
    return this.timestamp;
  }

  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }

  @Override
  public String toString()
  {
    return "LogEntry{" +
        "id=" + id +
        ", timestamp=" + timestamp +
        '}';
  }
}
