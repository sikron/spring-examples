package org.glassfish.jersey.examples.helloworld.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

//instead of web.xml
public class WebInit implements WebApplicationInitializer {

    //    @Override
    public void onStartup(ServletContext container) throws ServletException {

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();

        // Registers the application configuration with the root context
        appContext.register(Config.class);

        // Creates the Spring Container shared by all Servlets and Filters
        container.addListener(new ContextLoaderListener(appContext));

        AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();

        servletContext.register(org.glassfish.jersey.servlet.ServletContainer.class);

        ServletRegistration.Dynamic jerseyServlet = container.addServlet("JerseySpringApplication",
                new DispatcherServlet(servletContext));
        jerseyServlet.setLoadOnStartup(1);
        jerseyServlet.addMapping("/");
        jerseyServlet.setInitParameter("javax.ws.rs.Application", "org.glassfish.jersey.examples.helloworld.spring.MyApplication");

        container.setInitParameter("contextConfigLocation", "");
    }
}

