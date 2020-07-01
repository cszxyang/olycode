package com.github.cszxyang.olycode.common.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yzx
 */
public class ThreadLocalInputStream extends InputStream {

    private ThreadLocal<InputStream> holdInputStream = new ThreadLocal<>();

    @Override
    public int read() throws IOException {
        return 0;
    }

    public InputStream get() {
        return holdInputStream.get();
    }

    public void set(String systemIn) {
        holdInputStream.set(new ByteArrayInputStream(systemIn.getBytes()));
    }

    @Override
    public void close() {
        holdInputStream.remove();
    }
}
