package com.rest.yun.listener;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rest.yun.beans.User;

/**
 * @project:					yun 
 * @Title: 						AuthorityAnnotationInterceptor.java 		
 * @Package 					com.rest.yun.listener		
 * @Description: 				建立过滤器, 用于过滤需要控制权限的方法 
 * @author 						杨贵松   
 * @date 						2014年2月21日 上午12:17:29 
 * @version 					V1.0
 */
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

    final Logger logger = Logger.getLogger(AuthorityAnnotationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handler2=(HandlerMethod) handler;
        FireAuthority fireAuthority = handler2.getMethodAnnotation(FireAuthority.class);

        if(null == fireAuthority){
            //没有声明权限,放行
            return true;
        }

        logger.debug("fireAuthority: "+fireAuthority.toString());

        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        boolean aflag = false;

        //根据调用的模块权限索引和用户所拥有的权限值进行比较，判断是否有权限访问
        for(AuthorityType at:fireAuthority.authorityTypes()){
            if(AuthorityHelper.hasAuthority(at.getIndex(), user.getRightcontent())){
                aflag = true;
                break;
            }
        }

        if(false == aflag){
            if (fireAuthority.resultType() == ResultTypeEnum.page) {
                //传统的登录页面               
                StringBuilder sb = new StringBuilder();
                sb.append(request.getContextPath());
                sb.append("/oprst.jsp?oprst=false&opmsg=").append(URLEncoder.encode("noRight","utf-8"));
                response.sendRedirect(sb.toString());
            } else if (fireAuthority.resultType() == ResultTypeEnum.json) {
                //ajax类型的登录提示
//                AjaxResponseUtil.toJson(response, "noRight");
            }
            return false;
        }
        return true;
    }
}
