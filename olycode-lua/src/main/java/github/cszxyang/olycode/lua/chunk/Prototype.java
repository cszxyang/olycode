package github.cszxyang.olycode.lua.chunk;

import lombok.Data;

import java.nio.ByteBuffer;

// function prototype
@Data
public class Prototype {

    private static final int TAG_NIL       = 0x00;
    private static final int TAG_BOOLEAN   = 0x01;
    private static final int TAG_NUMBER    = 0x03;
    private static final int TAG_INTEGER   = 0x13;
    private static final int TAG_SHORT_STR = 0x04;
    private static final int TAG_LONG_STR  = 0x14;

    /**
     * src file name
     */
    private String source;
    /**
     * a cint, begin line number
     */
    private int lineDefined;
    /**
     * a cint, end line number
     */
    private int lastLineDefined;

    /**
     * 1 byte, number of fix args
     */
    private byte numParams;
    /**
     * 1 byte, if the function has variable args
     */
    private byte isVararg;
    /**
     * 1 byte, number of registers
     */
    private byte maxStackSize;
    /**
     * instructions of a function,
     * each of the costs 4 bytes to store
     */
    // todo may change its name to `instructions`
    private int[] code;
    /**
     * table of constants in a function
     * each constant starts with a 1-byte-long tag
     * to identify the type of itself
     */
    private Object[] constants;
    /**
     * Upvalue table, each element of it requires two bytes of storage
     */
    private Upvalue[] upvalues;
    /**
     * recursively, a prototype could have sub-prototypes
     */
    private Prototype[] protos;
    /**
     * each of it store with a cint
     * the line number it represents is corresponding to the
     * instructions table
     */
    private int[] lineInfo;
    /**
     * table of local variables
     */
    private LocVar[] locVars;
    /**
     * strings representing the an upvalue in the src
     */
    private String[] upvalueNames;

    void read(ByteBuffer buf, String parentSource) {
        source = BinaryChunk.getLuaString(buf);
        if (source.isEmpty()) {
            source = parentSource;
        }
        lineDefined = buf.getInt();
        lastLineDefined = buf.getInt();
        numParams = buf.get();
        isVararg = buf.get();
        maxStackSize = buf.get();
        readCode(buf);
        readConstants(buf);
        readUpvalues(buf);
        readProtos(buf, source);
        readLineInfo(buf);
        readLocVars(buf);
        readUpvalueNames(buf);
    }

    private void readCode(ByteBuffer buf) {
        code = new int[buf.getInt()];
        for (int i = 0; i < code.length; i++) {
            code[i] = buf.getInt();
        }
    }

    private void readConstants(ByteBuffer buf) {
        constants = new Object[buf.getInt()];
        for (int i = 0; i < constants.length; i++) {
            constants[i] = readConstant(buf);
        }
    }

    private Object readConstant(ByteBuffer buf) {
        switch (buf.get()) {
            case TAG_NIL: return null;
            case TAG_BOOLEAN: return buf.get() != 0;
            case TAG_INTEGER: return buf.getLong();
            case TAG_NUMBER: return buf.getDouble();
            case TAG_SHORT_STR: return BinaryChunk.getLuaString(buf);
            case TAG_LONG_STR: return BinaryChunk.getLuaString(buf);
            default: throw new RuntimeException("corrupted!"); // todo
        }
    }

    private void readUpvalues(ByteBuffer buf) {
        upvalues = new Upvalue[buf.getInt()];
        for (int i = 0; i < upvalues.length; i++) {
            upvalues[i] = new Upvalue();
            upvalues[i].read(buf);
        }
    }

    private void readProtos(ByteBuffer buf, String parentSource) {
        protos = new Prototype[buf.getInt()];
        for (int i = 0; i < protos.length; i++) {
            protos[i] = new Prototype();
            protos[i].read(buf, parentSource);
        }
    }

    private void readLineInfo(ByteBuffer buf) {
        lineInfo = new int[buf.getInt()];
        for (int i = 0; i < lineInfo.length; i++) {
            lineInfo[i] = buf.getInt();
        }
    }

    private void readLocVars(ByteBuffer buf) {
        locVars = new LocVar[buf.getInt()];
        for (int i = 0; i < locVars.length; i++) {
            locVars[i] = new LocVar();
            locVars[i].read(buf);
        }
    }

    private void readUpvalueNames(ByteBuffer buf) {
        upvalueNames = new String[buf.getInt()];
        for (int i = 0; i < upvalueNames.length; i++) {
            upvalueNames[i] = BinaryChunk.getLuaString(buf);
        }
    }

}