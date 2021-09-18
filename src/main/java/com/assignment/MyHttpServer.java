package com.assignment;

import com.assignment.filter.MyHttpFilter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Deepak
 *
 */
public class MyHttpServer {

    private static final String CONTEXT = "/";
    private static final int PORT = 8081;

    public static void main(String[] args) throws Exception {

        // Create a new SimpleHttpServer
        HttpServer simpleHttpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext httpContext=simpleHttpServer.createContext(CONTEXT, new HttpRequestHandler());
        httpContext.getFilters().add(new MyHttpFilter());
        ExecutorService executorService = Executors.newCachedThreadPool();
        simpleHttpServer.setExecutor(executorService);
        // Start the server
        simpleHttpServer.start();
        System.out.println("Server is started and listening on port " + PORT);
    }

}
