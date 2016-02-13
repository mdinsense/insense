package com.ensense.insense.core.analytics.utils;

public class Pair {
	  public Object o1;
	  public Object o2;
	  public Object o3;
	  public Pair() {}
	  public Pair(Object o1, Object o2, Object o3) { this.o1 = o1; this.o2 = o2; this.o3 = o3;}
	 
	  public static boolean same(Object o1, Object o2) {
	    return o1 == null ? o2 == null : o1.equals(o2);
	  }
	 
	  public Object getFirst() { return o1; }
	  public Object getSecond() { return o2; }
	  public Object getThird() { return o3; }
	 
	  public void setFirst(Object o) { o1 = o; }
	  public void setSecond(Object o) { o2 = o; }
	  public void setThird(Object o) { o3 = o; }
	 
	  public boolean equals(Object obj) {
	    if( ! (obj instanceof Pair))
	      return false;
	    Pair p = (Pair)obj;
	    return same(p.o1, this.o1) && same(p.o2, this.o2);
	  }
	 
	  public String toString() {
	    return "Pair{"+o1+", "+o2+", "+o3+"}";
	  }
}