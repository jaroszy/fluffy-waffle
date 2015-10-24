package org.jj.fluffywaffle.jobs;


import org.jj.fluffywaffle.selenium.Rebooter;
import org.quartz.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static org.apache.commons.lang3.time.DateUtils.addMinutes;
import static org.jj.fluffywaffle.jobs.JobRescheduler.delayCurrentJob;
import static org.jj.fluffywaffle.selenium.Rebooter.rebootRouter;

public class CheckConnectivityJob implements Job {

  private static final String address = "http://www.google.pl";
  private static final int delayInMinutes = 2;

  private Date getDelayedDate(){

    return addMinutes(new Date(), delayInMinutes);
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {

    if (!pingUrl(address)) {

      rebootRouter();
      delayCurrentJob(context, getDelayedDate());
    }
  }

  public static boolean pingUrl(final String address) {
    try {
      final URL url = new URL(address);
      final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds

      final long startTime = System.currentTimeMillis();

      urlConn.connect();

      final long endTime = System.currentTimeMillis();

      if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

        System.out.println("Time (ms) : " + (endTime - startTime));
        System.out.println("Ping to " + address + " was success");

        return true;
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
