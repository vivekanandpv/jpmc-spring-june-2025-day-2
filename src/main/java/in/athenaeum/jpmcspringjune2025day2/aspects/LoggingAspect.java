package in.athenaeum.jpmcspringjune2025day2.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@EnableAspectJAutoProxy
@Component
public class LoggingAspect {
    private final Logger logger;

    public LoggingAspect() {
        this.logger = Logger.getLogger(LoggingAspect.class.getName());
    }
    
    //  Advise
    @Around("@annotation(in.athenaeum.jpmcspringjune2025day2.aspects.AppLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        
    }
}
