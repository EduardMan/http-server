package tech.itpark.http.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import tech.itpark.http.FrontController;
import tech.itpark.http.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestMappingBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, Class<?>> map = new HashMap<>();
    private final FrontController frontController;

    public RequestMappingBeanPostProcessor(FrontController frontController) {
        this.frontController = frontController;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(Controller.class)) {
            map.put(beanName, bean.getClass());
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (map.containsKey(beanName)) {
            final Class<?> aClass = map.get(beanName);
            final Method[] declaredMethods = aClass.getDeclaredMethods();

            for (Method method :
                    declaredMethods) {

                if (AnnotationUtils.findAnnotation(method, RequestMapping.class) != null) {
                    frontController.registerRoute(bean, method, Objects.requireNonNull(AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class)));
                }
            }
        }

        return bean;
    }
}
