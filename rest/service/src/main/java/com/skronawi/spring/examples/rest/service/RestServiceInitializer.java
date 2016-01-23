package com.skronawi.spring.examples.rest.service;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class RestServiceInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RestService.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}

//public class RestServiceInitializer implements WebApplicationInitializer {
//
//    public void onStartup(ServletContext container) throws ServletException {
//
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//        ctx.register(RestService.class);
//        ctx.setServletContext(container);
//
//        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
//
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");
//    }
//
//}
