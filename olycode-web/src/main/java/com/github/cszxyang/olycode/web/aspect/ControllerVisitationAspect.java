package com.github.cszxyang.olycode.web.aspect;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.github.cszxyang.olycode.web.stat.entity.RequestRecord;
import com.github.cszxyang.olycode.web.stat.service.RequestRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ControllerVisitationAspect {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerVisitationAspect.class);

    @Autowired
    private RequestRecordService requestRecordService;

    @Pointcut("execution(public * com.github.cszxyang.olycode.web..controller.*.*(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void statisticsPointCut() {
    }

    @Before("statisticsPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        RequestRecord requestRecord = new RequestRecord();
        requestRecord.setRequestUrl(request.getRequestURL().toString());
        requestRecord.setIp(request.getRemoteAddr());
        requestRecord.setVisitTime(new Date());
        boolean inserted = requestRecordService.insert(requestRecord);
        if (inserted) {
            LOG.info("Insert record successfully");
        }
        // 记录下请求内容
        LOG.info("请求地址 : " + request.getRequestURL().toString());
        LOG.info("HTTP METHOD : " + request.getMethod());
        LOG.info("IP : " + request.getRemoteAddr());
        LOG.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        LOG.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "statisticsPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        LOG.info("返回值 : " + ret);
    }

    @Around("statisticsPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        LOG.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }
}
