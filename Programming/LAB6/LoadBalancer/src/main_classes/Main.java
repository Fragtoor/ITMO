package main_classes;

import app.LoadBalancerApp;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
            String host1 = props.getProperty("server1.host");
            int port1 = Integer.parseInt(props.getProperty("server1.port"));
            String host2 = props.getProperty("server2.host");
            int port2 = Integer.parseInt(props.getProperty("server2.port"));
            String host3 = props.getProperty("server3.host");
            int port3 = Integer.parseInt(props.getProperty("server3.port"));

            int portBalancer = Integer.parseInt(props.getProperty("balancer.port"));

            ArrayList<InetSocketAddress> servers = new ArrayList<>();
            servers.add(new InetSocketAddress(host1, port1));
            servers.add(new InetSocketAddress(host2, port2));
            servers.add(new InetSocketAddress(host3, port3));

            LoadBalancerApp loadBalancer = new LoadBalancerApp(servers, portBalancer);
            loadBalancer.run();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
