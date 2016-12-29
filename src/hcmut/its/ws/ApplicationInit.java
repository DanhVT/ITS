package hcmut.its.ws;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hcmut.its.cache.RealTimeScheduler;
import hcmut.its.cache.RealTimeSpeedCache;
import hcmut.its.graph.Graph;
import hcmut.its.roadmap.RoadMapImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



@SuppressWarnings("unused")
public class ApplicationInit implements ServletContextListener {
	
    private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent e) {
		System.out.println("ServletContextListener started");
		ServletContext context = e.getServletContext();
		scheduler = Executors.newSingleThreadScheduledExecutor();
		/**
		 * polling time is 1 minute
		 */
        scheduler.scheduleAtFixedRate(new RealTimeScheduler(), 0, 1, TimeUnit.MINUTES);
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		System.out.println("ServletContextListener destroyed");
        scheduler.shutdownNow();
	}
}
