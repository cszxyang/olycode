package com.github.cszxyang.olycode.java.bytecode;

import com.github.cszxyang.olycode.java.util.ByteUtil;
import javafx.util.Pair;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字节码操纵工具
 * @author cszyang
 */
public class ConstantPoolManipulator {

    /**
     * 字节码文件中常量池的起始偏移
     */
    private static final int CONSTANT_POOL_SIZE_INDEX = 8;

    /**
     * CONSTANT_UTF8_INFO 常量的 tag
     */
    private static final int CONSTANT_UTF8_INFO = 1;

    /**
     * 常量池中 11 种常量的长度，CONSTANT_ITEM_LENGTH[tag] 表示它的长度
     */
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};

    /**
     * 操作字节数
     */
    private static final int U1 = 1;
    private static final int U2 = 2;

    /**
     * 获取常量池项数
     * @return 常量池中常量的个数
     */
    private static int getConstantPoolSize(byte[] bytecode) {
        return ByteUtil.bytesToInt(bytecode, CONSTANT_POOL_SIZE_INDEX, U2);
    }

    /**
     * 常量池项目结构体
     */
    @Data
    private static class ConstantPoolItem {
        private int offsetOfInfo;
        private int len;
        private String info;

        ConstantPoolItem(int offsetOfInfo, int len, String info) {
            this.offsetOfInfo = offsetOfInfo;
            this.len = len;
            this.info = info;
        }
    }

    /**
     * 获取常量池中的 CONSTANT_UTF8_INFO 项目
     * @param bytecode 字节码
     * @return CONSTANT_UTF8_INFO 与其 info 的映射表
     */
    private static Map<String, ConstantPoolItem> getConstantPool(byte[] bytecode) {
        Map<String, ConstantPoolItem> map = new HashMap<>();
        if (bytecode == null || bytecode.length == 0) {
            return map;
        }
        int poolSize = getConstantPoolSize(bytecode);
        int cpItemOffset = CONSTANT_POOL_SIZE_INDEX + U2;

        for (int i = 0; i < poolSize; i++) {
            // 在字节码中从 cpItemOffset 开始读 u1 个字节的数据
            int tag = ByteUtil.bytesToInt(bytecode, cpItemOffset, U1);
            if (tag == CONSTANT_UTF8_INFO) {
                int len = ByteUtil.bytesToInt(bytecode, cpItemOffset + U1, U2);
                cpItemOffset += U1 + U2;
                String utf8CpInfo = ByteUtil.byteToString(bytecode, cpItemOffset, len);

                ConstantPoolItem item = new ConstantPoolItem(cpItemOffset, len, utf8CpInfo);
                map.put(utf8CpInfo, item);

                cpItemOffset += len;
            } else {
                cpItemOffset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return map;
    }

    public static byte[] doManipulate(byte[] bytecode, List<Pair<String, String>> pairs) {
        Map<String, ConstantPoolItem> constantPool = getConstantPool(bytecode);
        /*for (String info : constantPool.keySet()) {
            System.out.println(constantPool.get(info));
        }
        System.out.println("---------------------------------");*/

        for (Pair<String, String> pair : pairs) {
            ConstantPoolItem item = constantPool.get(pair.getKey());
            if (item != null) {
                System.out.println(item);

                byte[] strReplaceBytes = ByteUtil.stringToBytes(pair.getValue());
                byte[] intReplaceBytes = ByteUtil.intToBytes(strReplaceBytes.length, U2);
                // 替换新的字符串的长度
                bytecode = ByteUtil.byteReplace(bytecode, item.getOffsetOfInfo() - U2, U2, intReplaceBytes);
                // 替换字符串本身
                bytecode = ByteUtil.byteReplace(bytecode, item.getOffsetOfInfo(), item.getLen(), strReplaceBytes);

                /*System.out.println("---------------------------------");
                Map<String, ConstantPoolItem> constantPool1 = getConstantPool(bytecode);
                for (String info : constantPool1.keySet()) {
                    System.out.println(constantPool1.get(info));
                }
                System.out.println("---------------------------------");*/
            }
        }
        return bytecode;
    }
}
