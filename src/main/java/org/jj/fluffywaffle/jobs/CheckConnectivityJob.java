package org.jj.fluffywaffle.jobs;


import org.jj.fluffywaffle.selenium.Rebooter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.commons.lang3.time.DateUtils.addMinutes;
import static org.jj.fluffywaffle.jobs.JobRescheduler.delayCurrentJob;
import static org.jj.fluffywaffle.selenium.Rebooter.rebootRouter;
import static org.slf4j.LoggerFactory.getLogger;

public class CheckConnectivityJob implements Job {

  private static final String address = "http://www.google.pl";
  private static final int delayInMinutes = 10;

  private static final Logger LOGGER = getLogger("CheckConnectivityJob");

  private Date getDelayedDate(){

    return addMinutes(new Date(), delayInMinutes);
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {

    if (!pingUrl(address)) {

      LOGGER.info("rebooting router...");

      rebootRouter();
      delayCurrentJob(context, getDelayedDate());
    }
  }

  public static boolean pingUrl(final String address) {
    try {
      final URL url = new URL(address);
      final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds

      final long startTime = currentTimeMillis();

      urlConn.connect();

      final long endTime = currentTimeMillis();

      if (urlConn.getResponseCode() == HTTP_OK) {

        LOGGER.info("Ping time (ms) : " + (endTime - startTime));
        LOGGER.info("Ping to " + address + " was success");

        return true;
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
