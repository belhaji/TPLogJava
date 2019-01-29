package com.belhaji.entity;

import java.util.Date;

public class Action implements Comparable<Action>
{
  private Date actionDate;

  public Action()
  {
  }

  public Action(Date actionDate)
  {
    this.actionDate = actionDate;
  }

  public Date getActionDate()
  {
    return this.actionDate;
  }

  public void setActionDate(Date actionDate)
  {
    this.actionDate = actionDate;
  }

  @Override
  public int compareTo(Action o)
  {
    return this.actionDate.compareTo(o.actionDate);
  }

  @Override
  public String toString()
  {
    return String.format("\n\t\t\t\tactionDate=%s", actionDate);
  }
}
