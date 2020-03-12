package github.cszxyang.olycode.java.exec;

/**
 * @author cszxyang
 */
public class CustomClassLoader extends ClassLoader {

    public CustomClassLoader() {
        super(CustomClassLoader.class.getClassLoader());
    }

    /**
     * defineClass 方法为 protected
     * @param classBytes 字节码文件
     * @return Class 对象
     */
    public Class loadBytes(byte[] classBytes) {
        return defineClass(null, classBytes, 0, classBytes.length);
    }
}
