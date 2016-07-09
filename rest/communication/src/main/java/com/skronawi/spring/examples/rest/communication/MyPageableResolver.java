package com.skronawi.spring.examples.rest.communication;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MyPageableResolver implements HandlerMethodArgumentResolver {

    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 20;

    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(MyPageable.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        String offsetString = nativeWebRequest.getParameter("offset");
        String limitString = nativeWebRequest.getParameter("limit");

        int offset = DEFAULT_OFFSET;
        int limit = DEFAULT_LIMIT;
        if (offsetString != null && offsetString.length() > 0) {
            offset = Integer.parseInt(offsetString);
        }
        if (limitString != null && limitString.length() > 0) {
            limit = Integer.parseInt(limitString);
        }
        return new MyPageable(offset, limit);
    }
}
