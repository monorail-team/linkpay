package monorail.linkpay.banking.common.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Consumer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@ConfigurationProperties(prefix = "ssh")
@Validated
@Setter
public class SshTunnelingInitializer {

    @Value("${ssh.ssh_host}")
    private String sshHost;
    @Value("${ssh.ssh_user}")
    private String sshUser;
    @Value("${ssh.ssh_port}")
    private int sshPort;
    @Value("${ssh.ssh_key}")
    private String sshKey;
    @Value("${ssh.database_host}")
    private String databaseHost;
    @Value("${ssh.database_port}")
    private int databasePort;
    @Value("${ssh.redis_host}")
    private String redisHost;
    @Value("${ssh.redis_port}")
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
            int port = buildSSHConnection(databaseHost, databasePort, "RDS", session -> rdsSession = session);
            rdsForwardedPort = port;
        }
        return rdsForwardedPort;
    }

    public int buildRedisSSHConnection() {
        if (redisSession == null || !redisSession.isConnected()) {
            int port = buildSSHConnection(redisHost, redisPort, "Redis", session -> redisSession = session);
            redisForwardedPort = port;
        }
        return redisForwardedPort;
    }

    private Integer buildSSHConnection(String targetHost, int targetPort, String label,
                                       Consumer<Session> sessionConsumer) {
        try {
            log.info("🔹 Connecting to SSH for {} with {}@{}:{} using key {}", label, sshUser, sshHost, sshPort, sshKey);
            JSch jsch = new JSch();

            byte[] privateKeyBytes = sshKey.getBytes(StandardCharsets.UTF_8);
            jsch.addIdentity("key-from-yaml", privateKeyBytes, null, null);
            Session session = jsch.getSession(sshUser, sshHost, sshPort);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            log.info("🔹 Starting SSH session for {}...", label);
            session.connect();

            int forwardPort = session.setPortForwardingL(0, targetHost, targetPort);
            log.info("✅ Port forwarding for {} created on local port {} to remote port {}", label, forwardPort,
                    targetPort);

            sessionConsumer.accept(session);
            return forwardPort;

        } catch (JSchException e) {
            log.error("❗ SSH Tunnel Error for {}: {}", label, e.getMessage());
            this.closeSSH();
            throw new RuntimeException(e);
        }
    }
}
