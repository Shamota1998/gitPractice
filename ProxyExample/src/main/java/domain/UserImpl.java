package domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.cglib.proxy.MethodInterceptor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserImpl implements User {
    private String name;
    private Role role;


}
