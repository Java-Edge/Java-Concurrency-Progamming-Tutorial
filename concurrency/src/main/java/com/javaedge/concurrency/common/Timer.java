// onjava/Timer.java
// (c)2017 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.
package com.javaedge.concurrency.common;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Timer {
  private static long start = System.nanoTime();
  public static long duration() {
    return NANOSECONDS.toMillis(
      System.nanoTime() - start);
  }
  public static long duration(Runnable test) {
    Timer timer = new Timer();
    test.run();
    return timer.duration();
  }

  public static void main(String[] args) {
    duration();
  }
}
