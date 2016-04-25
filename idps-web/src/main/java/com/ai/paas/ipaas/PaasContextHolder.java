package com.ai.paas.ipaas;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 用于初始化并保存paas的相应的bean
 */
public class PaasContextHolder {

    private static ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"context/applicationContext-dubbo-service-provider.xml"});

    public static ApplicationContext getContext() {
        return ctx;
    }

    public static void setCtx(ApplicationContext ctx) {
        PaasContextHolder.ctx = ctx;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

    public static void closeCtx() {
        ((ClassPathXmlApplicationContext) ctx).close();
    }

}
