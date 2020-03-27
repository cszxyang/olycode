package com.github.cszxyang.olycode.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Miscellaneous method utility methods.
 * Mainly for internal use within the framework.
 *
 * @author LiZhenNet
 * @since 2.7.2
 */
public class MethodUtils {

    /**
     * Return {@code true} if the provided method is a set method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is setter method
     */
    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && !"set".equals(method.getName())
                && Modifier.isPublic(method.getModifiers())
                && method.getParameterCount() == 1
                && ClassUtils.isPrimitive(method.getParameterTypes()[0]);
    }

    /**
     * Return {@code true} if the provided method is a get method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is getter method
     */
    public static boolean isGetter(Method method) {
        String name = method.getName();
        return (name.startsWith("get") || name.startsWith("is"))
                && !"get".equals(name) && !"is".equals(name)
                && !"getClass".equals(name) && !"getObject".equals(name)
                && Modifier.isPublic(method.getModifiers())
                && method.getParameterTypes().length == 0
                && ClassUtils.isPrimitive(method.getReturnType());
    }

    /**
     * Return {@code true} If this method is a meta method.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is meta method
     */
    public static boolean isMetaMethod(Method method) {
        String name = method.getName();
        if (!(name.startsWith("get") || name.startsWith("is"))) {
            return false;
        }
        if ("get".equals(name)) {
            return false;
        }
        if ("getClass".equals(name)) {
            return false;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        return ClassUtils.isPrimitive(method.getReturnType());
    }

    /**
     * Check if the method is a deprecated method. The standard is whether the {@link java.lang.Deprecated} annotation is declared on the class.
     * Return {@code true} if this annotation is present.
     * Otherwise, return {@code false}.
     *
     * @param method the method to check
     * @return whether the given method is deprecated method
     */
    public static boolean isDeprecated(Method method) {
        return method.getAnnotation(Deprecated.class) != null;
    }
}