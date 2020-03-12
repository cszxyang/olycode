import org.junit.Test;

public class BinaryChunkTest {

    @Test
    public void undump() throws Exception {
        /*Resource resource = new ClassPathResource("/lua/com.luac");
        byte[] bytes = BinaryChunk.toByteArray(resource.getURL().getPath());
        Prototype proto = BinaryChunk.undump(bytes);
        System.out.println(proto);*/

        /*assertEquals("@hello_world.lua", proto.getSource());
        assertEquals(0, proto.getLineDefined());
        assertEquals(0, proto.getLastLineDefined());
        assertEquals(0, proto.getNumParams());
        assertEquals(1, proto.getIsVararg());
        assertEquals(2, proto.getMaxStackSize());
        assertEquals(4, proto.getCode().length);
        assertEquals(2, proto.getConstants().length);
        assertEquals(1, proto.getUpvalues().length);
        assertEquals(0, proto.getProtos().length);
        assertEquals(4, proto.getLineInfo().length);
        assertEquals(0, proto.getLocVars().length);
        assertEquals(1, proto.getUpvalueNames().length);

        assertEquals("print", proto.getConstants()[0]);
        assertEquals("Hello, World!", proto.getConstants()[1]);
        assertEquals("_ENV", proto.getUpvalueNames()[0]);*/
    }

}