package domain;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.Test;
import proxy.CustomInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProxyTest {

    @Test
    public void getMethodTest() {
        UserImpl userImpl = new UserImpl("andrei", Role.USER);

        MethodInterceptor handler = (Object obj, Method method, Object[] arg, MethodProxy proxy) -> {
            if (method.getName().equals("getName")) {
                return ((String) proxy.invoke(userImpl, arg)).toUpperCase();
            }
            return proxy.invoke(userImpl, arg);
        };
        UserImpl userImplProxy = (UserImpl) Enhancer.create(UserImpl.class, handler);
        assertEquals("ANDREI", userImplProxy.getName());
    }


    @Test
    public void shouldReturnUpperCase() {
        User userImpl = new UserImpl("andrei", Role.USER);
        CustomInvocationHandler invocationHandler = new CustomInvocationHandler(userImpl);
        ClassLoader classLoader = userImpl.getClass().getClassLoader();
        Class<?>[] interfaces = userImpl.getClass().getInterfaces();
        User proxyUserImpl = (UserImpl) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        assertEquals("ANDREI", proxyUserImpl.getName());


    }
}
