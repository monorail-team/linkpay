package monorail.linkpay.common.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;

import static monorail.linkpay.exception.ExceptionCode.SERVER_ERROR;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ssh")
public class SshTunnelingInitializer {
    private String sshHost;
    private String sshUser;
    private int sshPort;
    private String sshKey;
    private String databaseHost;
    private int databasePort;
    private String redisHost;
    private int redisPort;

    private Session rdsSession;
    private int rdsForwardedPort;
    private Session redisSession;
    private int redisForwardedPort;

    @PreDestroy
    public void closeSSH() {
        if (rdsSession != null && rdsSession.isConnected()) {
            rdsSession.disconnect();
        }
        if (redisSession != null && redisSession.isConnected()) {
            redisSession.disconnect();
        }
    }

    public int buildRdsSSHConnection() {
        if (rdsSession == null || !rdsSession.isConnected()) {
            rdsForwardedPort = buildSSHConnection(databaseHost, databasePort, session -> rdsSession = session);
        }
        return rdsForwardedPort;
    }

    public int buildRedisSSHConnection() {
        if (redisSession == null || !redisSession.isConnected()) {
            redisForwardedPort = buildSSHConnection(redisHost, redisPort, session -> redisSession = session);
        }
        return redisForwardedPort;
    }

    private int buildSSHConnection(final String targetHost, final int targetPort,
                                   final Consumer<Session> sessionConsumer) {
        try {
            JSch jsch = new JSch();
            byte[] privateKeyBytes = sshKey.getBytes(StandardCharsets.UTF_8);
            jsch.addIdentity("key-from-yaml", privateKeyBytes, null, null);
            Session session = jsch.getSession(sshUser, sshHost, sshPort);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            int forwardPort = session.setPortForwardingL(0, targetHost, targetPort);
            sessionConsumer.accept(session);
            return forwardPort;
        } catch (JSchException e) {
            this.closeSSH();
            throw new LinkPayException(SERVER_ERROR, e.getMessage());
        }
    }
}
