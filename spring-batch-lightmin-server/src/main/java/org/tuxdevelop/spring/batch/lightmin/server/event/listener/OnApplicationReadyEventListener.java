package org.tuxdevelop.spring.batch.lightmin.server.event.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerProperties;
import org.tuxdevelop.spring.batch.lightmin.server.support.ClientApplicationStatusUpdater;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public class OnApplicationReadyEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ScheduledTaskRegistrar serverScheduledTaskRegistrar;
    private final ClientApplicationStatusUpdater clientApplicationStatusUpdater;
    private final LightminServerProperties lightminServerProperties;

    public OnApplicationReadyEventListener(final ScheduledTaskRegistrar serverScheduledTaskRegistrar,
                                           final ClientApplicationStatusUpdater clientApplicationStatusUpdater,
                                           final LightminServerProperties lightminServerProperties) {
        this.serverScheduledTaskRegistrar = serverScheduledTaskRegistrar;
        this.clientApplicationStatusUpdater = clientApplicationStatusUpdater;
        this.lightminServerProperties = lightminServerProperties;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        serverScheduledTaskRegistrar.addFixedRateTask(new Runnable() {
            @Override
            public void run() {
                clientApplicationStatusUpdater.updateStatusForAllApplications();
            }
        }, lightminServerProperties.getHeartbeatPeriod());
        serverScheduledTaskRegistrar.afterPropertiesSet();
    }
}
