package com.github.cszxyang.olycode.lua.chunk;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * @author cszxyang
 */
@Getter
@Setter
public class LocVar {

    /**
     * name of a variable
     */
    private String varName;
    /**
     * starting index in a program counter
     */
    private int startPC;
    /**
     * ending index in a program counter
     */
    private int endPC;

    void read(ByteBuffer buf) {
        varName = BinaryChunk.getLuaString(buf);
        startPC = buf.getInt();
        endPC = buf.getInt();
    }

}