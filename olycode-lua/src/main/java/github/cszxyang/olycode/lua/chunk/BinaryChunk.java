package github.cszxyang.olycode.lua.chunk;

import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author yzx
 */
public class BinaryChunk {

    private static final byte[] LUA_SIGNATURE    = {0x1b, 'L', 'u', 'a'};
    private static final int    LUA_SIG          = 0x1B4C7561;
    private static final int    LUAC_VERSION     = 0x53;
    private static final int    LUAC_FORMAT      = 0;
    private static final byte[] LUAC_DATA        = {0x19, (byte) 0x93, '\r', '\n', 0x1a, '\n'};
    private static final int    CINT_SIZE        = 4;
    private static final int    CSIZET_SIZE      = 8;
    private static final int    INSTRUCTION_SIZE = 4;
    private static final int    LUA_INTEGER_SIZE = 8;
    private static final int    LUA_NUMBER_SIZE  = 8;
    private static final int    LUAC_INT         = 0x5678;
    private static final double LUAC_NUM         = 370.5;

    public static void main(String[] args) throws IOException {
        Resource resource = new ClassPathResource("/lua/com.luac");
        byte[] bytes = toByteArray(resource.getURL().getPath());
        for (byte b : bytes) {
            System.out.print(Integer.toHexString(b) + " ");
        }
        System.out.println();
        undump(bytes);
    }


    public static byte[] toByteArray(String filename) throws IOException {
        System.out.println(filename);
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Header header;

    private int upValueSize;
    private Prototype mainFunction;

    /**
     * parse Prototype
     * @param data Prototype bytes
     * @return Prototype
     */
    public static Prototype undump(byte[] data) {
        ByteBuffer buf;
        // get sys byte order
        ByteOrder nativeOrder = ByteOrder.nativeOrder();
        if (nativeOrder == ByteOrder.LITTLE_ENDIAN) {
            buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        } else {
            buf = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);
        }
//        byte[] array = buf.array();
//        System.out.println("\nundump");
//        for (byte b : array) {
//            System.out.print(Integer.toHexString(b) + " ");
//        }
//        System.out.println("\nundump1");
//        while (buf.remaining() > 0) {
//            System.out.print(Integer.toHexString(buf.getInt()) + " ");
//        }
        checkHeader(buf);
        buf.get(); // size_upvalues
        Prototype mainFunc = new Prototype();
        mainFunc.read(buf, "");
        return mainFunc;
    }

    private static void checkHeader(ByteBuffer buf) {
        int sig = buf.getInt();
        System.out.println("\nsig" + Integer.toHexString(sig));

        ByteBuffer tempBuf;
        // get sys byte order
        ByteOrder nativeOrder = ByteOrder.nativeOrder();
        if (nativeOrder == ByteOrder.LITTLE_ENDIAN) {
            tempBuf = ByteBuffer.wrap(LUA_SIGNATURE).order(ByteOrder.LITTLE_ENDIAN);
        } else {
            tempBuf = ByteBuffer.wrap(LUA_SIGNATURE).order(ByteOrder.BIG_ENDIAN);
        }
        if (tempBuf.getInt() != sig) {
            throw new RuntimeException("not a precompiled chunk!");
        }
       /* byte[] bytes = getBytes(buf, 4);
        if (!Arrays.equals(LUA_SIGNATURE, bytes)) {
            for (byte b : bytes) {
                System.out.print(Integer.toHexString(b) + " ");
            }
            throw new RuntimeException("not a precompiled chunk!");
        }*/
        if (buf.get() != LUAC_VERSION) {
            throw new RuntimeException("version mismatch!");
        }
        if (buf.get() != LUAC_FORMAT) {
            throw new RuntimeException("format mismatch!");
        }
        if (!Arrays.equals(LUAC_DATA, getBytes(buf, 6))) {
            throw new RuntimeException("corrupted!");
        }
        if (buf.get() != CINT_SIZE) {
            throw new RuntimeException("int size mismatch!");
        }
        if (buf.get() != CSIZET_SIZE) {
            throw new RuntimeException("size_t size mismatch!");
        }
        if (buf.get() != INSTRUCTION_SIZE) {
            throw new RuntimeException("instruction size mismatch!");
        }
        if (buf.get() != LUA_INTEGER_SIZE) {
            throw new RuntimeException("lua_Integer size mismatch!");
        }
        if (buf.get() != LUA_NUMBER_SIZE) {
            throw new RuntimeException("lua_Number size mismatch!");
        }
        if (buf.getLong() != LUAC_INT) {
            throw new RuntimeException("endianness mismatch!");
        }
        if (buf.getDouble() != LUAC_NUM) {
            throw new RuntimeException("float format mismatch!");
        }
    }

    static String getLuaString(ByteBuffer buf) {
        int size = buf.get() & 0xFF;
        if (size == 0) {
            return "";
        }
        if (size == 0xFF) {
            size = (int) buf.getLong(); // size_t
        }

        byte[] a = getBytes(buf, size - 1);
        return new String(a); // todo
    }

    private static byte[] getBytes(ByteBuffer buf, int n) {
        byte[] arr = new byte[n];
        buf.get(n);
        return arr;
    }


    /**
     * 总共约占 30 字节
     */
    @Data
    private static class Header {
        /**
         * Magic Number, actually
         * using 4 bytes to store ASC11 code of 'ESC',‘L’,'u','a' (0x1B4C7561)
         */
        private static final byte[] LUA_SIGNATURE    = {0x1b, 'L', 'u', 'a'};

        // private static final int sig = 0x1B4C7561;
        /**
         * version (major.minor.release), 1 byte
         * 0x_version = major * 16 + minor
         * for lua-5.3.4, 5 * 16 + 3 = 0b83 ==> 0x53
         */
        private static final int    LUAC_VERSION     = 0x53;
        /**
         * vm format, default 0
         */
        private static final int    LUAC_FORMAT      = 0;
        /**
         * 6 bytes,
         * first 2 bytes: 0x1993 (lua-1.0 was released in 1993)
         * last 4 bytes: 0x0D('\r'), 0x0A(‘\n’), 0x1A, 0x0A(‘\n’)
         */
        private static final byte[] LUAC_DATA        = {0x19, (byte) 0x93, '\r', '\n', 0x1a, '\n'};
        /**
         * 5 bytes, each byte represents the size of
         * cint, size_t, lua_vm_instruction, lua_integer, lua_number
         */
        // todo may change to byte[5]
        private static final int    CINT_SIZE        = 4;
        private static final int    CSIZET_SIZE      = 8;
        private static final int    INSTRUCTION_SIZE = 4;
        private static final int    LUA_INTEGER_SIZE = 8;
        private static final int    LUA_NUMBER_SIZE  = 8;
        /**
         * constant 0x5678
         * for checking sys byte order, a lua_integer size long
         */
        private static final int    LUAC_INT         = 0x5678;
        /**
         * constant 370.5, a lua_number size long
         * check float format
         */
        private static final double LUAC_NUM         = 370.5;
    }
}