package de.uniulm.omi.cloudiator.sword.remote.internal;

import com.github.rholder.retry.*;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 19.08.15.
 *
 * @todo: should be moved to a general package
 */
class RetryingConnectionFactory implements RemoteConnectionFactory {

    /**
     * How many retries should be done.
     *
     * @todo make configurable
     */
    private static final int CONNECTION_RETRIES = 10;

    /**
     * The factor of increasing the timeout value.
     *
     * @todo make configurable
     */
    private static final int INCREASE_TIMEOUT_FACTOR = 2;

    /**
     * The maximum timeout.
     *
     * @todo make configurable
     */
    private static final int MAXIMUM_TIMEOUT = 30;

    private final RemoteConnectionFactory remoteConnectionFactory;

    RetryingConnectionFactory(RemoteConnectionFactory remoteConnectionFactory) {
        this.remoteConnectionFactory = remoteConnectionFactory;
    }

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port) {

        Callable<RemoteConnection> callable = () -> remoteConnectionFactory
            .createRemoteConnection(remoteAddress, osFamily, loginCredential, port);

        Retryer<RemoteConnection> remoteConnectionRetryer =
            RetryerBuilder.<RemoteConnection>newBuilder().retryIfRuntimeException()
                .withStopStrategy(StopStrategies.stopAfterAttempt(CONNECTION_RETRIES))
                .withWaitStrategy(WaitStrategies
                    .exponentialWait(INCREASE_TIMEOUT_FACTOR, MAXIMUM_TIMEOUT, TimeUnit.SECONDS))
                .build();

        try {
            return remoteConnectionRetryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            throw new RuntimeException(e);
        }
    }
}
