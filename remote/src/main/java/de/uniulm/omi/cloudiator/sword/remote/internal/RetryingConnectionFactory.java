package de.uniulm.omi.cloudiator.sword.remote.internal;

import com.github.rholder.retry.*;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.annotations.Base;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 19.08.15.
 */
class RetryingConnectionFactory implements RemoteConnectionFactory {

    @Inject(optional = true) @Named(Constants.SSH_MAX_RETRIES) private int connectionRetries = 10;
    @Inject(optional = true) @Named(Constants.SSH_EXPONENTIAL_MULTIPLIER) private long
        exponentialMultiplier = 1000;
    @Inject(optional = true) @Named(Constants.SSH_EXPONENTIAL_MAX_TIME) private long
        exponentialMaxTime = 30;

    private final RemoteConnectionFactory remoteConnectionFactory;

    @Inject RetryingConnectionFactory(@Base RemoteConnectionFactory remoteConnectionFactory) {
        this.remoteConnectionFactory = remoteConnectionFactory;
    }

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
        LoginCredential loginCredential, int port) {

        Callable<RemoteConnection> callable = () -> remoteConnectionFactory
            .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);

        Retryer<RemoteConnection> remoteConnectionRetryer =
            RetryerBuilder.<RemoteConnection>newBuilder().retryIfRuntimeException()
                .retryIfException(throwable -> throwable instanceof RemoteException)
                .withStopStrategy(StopStrategies.stopAfterAttempt(connectionRetries))
                .withWaitStrategy(WaitStrategies
                    .exponentialWait(exponentialMultiplier, exponentialMaxTime, TimeUnit.SECONDS))
                .build();

        try {
            return remoteConnectionRetryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            throw new RuntimeException(e);
        }
    }
}
