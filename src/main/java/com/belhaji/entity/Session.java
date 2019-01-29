package com.belhaji.entity;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Session implements Comparable<Session>
{
  private Long id;
  private Date startDate;
  private Date endDate;
  private Set<Action> actions = new TreeSet<>();

  public Session()
  {
  }

  public Session(Long id, Date startDate, Date endDate, Set<Action> actions)
  {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.actions = actions;
  }

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Date getStartDate()
  {
    return this.startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public Set<Action> getActions()
  {
    return this.actions;
  }

  public void setActions(Set<Action> actions)
  {
    this.actions = actions;
  }

  @Override
  public int compareTo(Session o)
  {
    return this.id.compareTo(o.id);
  }

  @Override
  public String toString()
  {
    return String.format("Session id=%d\n\t\tstartDate=%s, endDate=%s\n\t\t\t actions=%s", id, startDate, endDate, actions);
  }
}
