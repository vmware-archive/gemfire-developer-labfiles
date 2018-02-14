package io.pivotal.training;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ServerConfiguration {

    @Value("${db.port}")
    private String h2TcpPort;

   /**
    * TCP connection to connect with SQL clients to the embedded h2 database.
    *
    * @see Server
    * @throws SQLException if something went wrong during startup the server.
    * @return h2 db Server
    */
    @Bean
    public Server server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2TcpPort).start();
    }

}
