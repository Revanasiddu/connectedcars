package com.connectedcars.config;

import com.connectedcars.model.FileTypeEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.Arrays;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 1:37
 */
@Component
public class HeaderValidationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(Arrays.stream(FileTypeEnum.values()).noneMatch(x->
                String.valueOf(x).equalsIgnoreCase(String.valueOf(request.getHeader(Constant.FILE_TYPE)).toUpperCase()))){
            throw new ValidationException("file-type should be csv/xml");
        }

        return true;
    }

}
