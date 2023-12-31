/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class HelloServiceImpl implements IHelloService{
    @Override
    public String hello(String name) {
        return "Hello,"+name;
    }
}
