package org.eintr.springframework.beans;

import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

final class GenericTypeAwarePropertyDescriptor extends FeatureDescriptor {
    /**
     * 类型
     */
    private final Class<?> beanClass;

    /**
     * 可读方法
     */

    private final Method readMethod;

    /**
     * 可写方法
     */

    private final Method writeMethod;

    /**
     * 属性编辑器类型
     */
    private final Class<?> propertyEditorClass;

    /**
     * 可能的可写方法
     */

    private volatile Set<Method> ambiguousWriteMethods;

    /**
     * 可写方法的参数
     */

    private MethodParameter writeMethodParameter;

    /**
     * 属性类型
     */

    private Class<?> propertyType;


    /**
     * 构造函数
     * @param beanClass 对象
     * @param propertyName 属性名称
     * @param readMethod 可读函数
     * @param writeMethod 可写函数
     * @param propertyEditorClass 属性编辑器类型
     * @throws IntrospectionException
     */
    public GenericTypeAwarePropertyDescriptor(Class<?> beanClass, String propertyName,
                                               Method readMethod,  Method writeMethod, Class<?> propertyEditorClass)
            throws IntrospectionException {

        this.beanClass = beanClass;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;

        //if (this.writeMethod != null) {
        //    if (this.readMethod == null) {
        //        // Write method not matched against read method: potentially ambiguous through
        //        // several overloaded variants, in which case an arbitrary winner has been chosen
        //        // by the JDK's JavaBeans Introspector...
        //        Set<Method> ambiguousCandidates = new HashSet<>();
        //        // 方法推测 , 满足下面要求的就可能是 可写方法
        //        for (Method method : beanClass.getMethods()) {
        //            if (method.getName().equals(writeMethodToUse.getName()) &&
        //                    !method.equals(writeMethodToUse) && !method.isBridge() &&
        //                    method.getParameterCount() == writeMethodToUse.getParameterCount()) {
        //                ambiguousCandidates.add(method);
        //            }
        //        }
        //        if (!ambiguousCandidates.isEmpty()) {
        //            // 赋值
        //            this.ambiguousWriteMethods = ambiguousCandidates;
        //        }
        //    }
        //    // 构造可写函数的参数对象
        //    this.writeMethodParameter = new MethodParameter(this.writeMethod, 0).withContainingClass(this.beanClass);
        //}

        //if (this.readMethod != null) {
        //    // 属性类型的计算
        //    // 计算方式: 通过 class 中寻找 method , 将 找到的 method 的返回值作为结果
        //    this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
        //}
        //else if (this.writeMethodParameter != null) {
        //    // 获取参数类型
        //    this.propertyType = this.writeMethodParameter.getParameterType();
        //}

        // 属性编辑器类型赋值
        this.propertyEditorClass = propertyEditorClass;
    }


    public Class<?> getBeanClass() {
        return this.beanClass;
    }


    public Method getReadMethod() {
        return this.readMethod;
    }


    public Method getWriteMethod() {
        return this.writeMethod;
    }



}

