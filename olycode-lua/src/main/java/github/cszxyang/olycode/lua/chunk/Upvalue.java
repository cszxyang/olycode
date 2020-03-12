package github.cszxyang.olycode.lua.chunk;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class Upvalue {

    private byte instack;
    private byte idx;

    void read(ByteBuffer buf) {
        instack = buf.get();
        idx = buf.get();
    }

}