package org.terasology.context;

import java.beans.beancontext.BeanContext;

public abstract class AbstractBeanDefinition<T> implements BeanDefinition<T> {

    public AnnotationMetadata getAnnotationMetadata() {
        return new DefaultAnnotationMetadata(new AnnotationValue[]{});
    }

    public T build(BeanResolution resolution) {
        return null;
    }

    public T inject(T instance, BeanResolution resolution) {
        return instance;
    }

    public abstract  Argument[] getArguments();

    @Override
    public Class[] getTypeArgument() {
        Class[] results = new Class[getArguments().length];
        Argument[] args = getArguments();
        for (int i = 0; i < args.length; i++) {
            results[i] = args[i].getType();
        }
        return results;
    }

    public abstract Class<T> targetClass();
}