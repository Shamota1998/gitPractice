package proxy;


import domain.User;
import domain.UserImpl;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@AllArgsConstructor
public class CustomInvocationHandler implements InvocationHandler {

    private User userImpl;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getName")) {
            return ((String) method.invoke(userImpl.getName(), args)).toUpperCase();
        }
        return ((MethodProxy) proxy).invoke(userImpl, args);
    }
}
