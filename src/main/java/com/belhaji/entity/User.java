package com.belhaji.entity;

import java.util.Set;
import java.util.TreeSet;

public class User implements Comparable<User>
{
  private Integer id;
  private Set<Session> sessions = new TreeSet<>();

  public User()
  {
  }

  public User(Integer id, Set<Session> sessions)
  {
    this.id = id;
    this.sessions = sessions;
  }

  public Integer getId()
  {
    return this.id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public Set<Session> getSessions()
  {
    return this.sessions;
  }

  public void setSessions(Set<Session> sessions)
  {
    this.sessions = sessions;
  }

  @Override
  public String toString()
  {
    return String.format("id=%d\n\tsessions=%s}", this.id, this.sessions);
  }

  @Override
  public int compareTo(User o)
  {
    return this.id.compareTo(o.id);
  }
}
