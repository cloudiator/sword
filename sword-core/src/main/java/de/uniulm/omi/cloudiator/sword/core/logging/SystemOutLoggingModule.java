package de.uniulm.omi.cloudiator.sword.core.logging;

import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;

/**
 * Created by Daniel Seybold on 03.08.2015.
 */
public class SystemOutLoggingModule extends AbstractLoggingModule {

    @Override
    protected LoggerFactory getLoggerFactory() {
        return category -> new SystemOutLogger();
    }
}
