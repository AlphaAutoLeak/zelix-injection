package zelix.utils;

import java.lang.reflect.*;

public class ReflectionHelper
{
    public static Field findField(final Class<?> clazz, final String... fieldNames) {
        Exception failed = null;
        final int length = fieldNames.length;
        int i = 0;
        while (i < length) {
            final String fieldName = fieldNames[i];
            try {
                final Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e) {
                failed = e;
                ++i;
                continue;
            }
        }
        throw new UnableToFindFieldException(fieldNames, failed);
    }
    
    public static <T, E> T getPrivateValue(final Class<? super E> classToAccess, final E instance, final int fieldIndex) {
        try {
            final Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T)f.get(instance);
        }
        catch (Exception e) {
            throw new UnableToAccessFieldException(new String[0], e);
        }
    }
    
    public static <T, E> T getPrivateValue(final Class<? super E> classToAccess, final E instance, final String... fieldNames) {
        try {
            return (T)findField(classToAccess, fieldNames).get(instance);
        }
        catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }
    
    public static <T, E> void setPrivateValue(final Class<? super T> classToAccess, final T instance, final E value, final int fieldIndex) {
        try {
            final Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        }
        catch (Exception e) {
            throw new UnableToAccessFieldException(new String[0], e);
        }
    }
    
    public static <T, E> void setPrivateValue(final Class<? super T> classToAccess, final T instance, final E value, final String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        }
        catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }
    
    public static Class<? super Object> getClass(final ClassLoader loader, final String... classNames) {
        Exception err = null;
        final int length = classNames.length;
        int i = 0;
        while (i < length) {
            final String className = classNames[i];
            try {
                return (Class<? super Object>)Class.forName(className, false, loader);
            }
            catch (Exception e) {
                err = e;
                ++i;
                continue;
            }

        }
        throw new UnableToFindClassException(classNames, err);
    }
    
    public static <E> Method findMethod(final Class<? super E> clazz, final E instance, final String[] methodNames, final Class<?>... methodTypes) {
        Exception failed = null;
        final int length = methodNames.length;
        int i = 0;
        while (i < length) {
            final String methodName = methodNames[i];
            try {
                final Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e) {
                failed = e;
                ++i;
                continue;
            }

        }
        throw new UnableToFindMethodException(methodNames, failed);
    }
    
    public static class UnableToFindMethodException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        
        public UnableToFindMethodException(final String[] methodNames, final Exception failed) {
            super(failed);
        }
    }
    
    public static class UnableToFindClassException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        
        public UnableToFindClassException(final String[] classNames, final Exception err) {
            super(err);
        }
    }
    
    public static class UnableToAccessFieldException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        
        public UnableToAccessFieldException(final String[] fieldNames, final Exception e) {
            super(e);
        }
    }
    
    public static class UnableToFindFieldException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        
        public UnableToFindFieldException(final String[] fieldNameList, final Exception e) {
            super(e);
        }
    }
}
