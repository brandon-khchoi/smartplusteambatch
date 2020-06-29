package kr.co.lunasoft.batchadmin.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }
    
    public static Object getBean(Class<?> classType) {
        return applicationContext.getBean(classType);
    }

    public static Object getBean(String name, Class<?> classType) {
    	return applicationContext.getBean(name, classType);
    }

    public static String getProperty(String key) {
    	return applicationContext.getEnvironment().getProperty(key);
    }
    
}

