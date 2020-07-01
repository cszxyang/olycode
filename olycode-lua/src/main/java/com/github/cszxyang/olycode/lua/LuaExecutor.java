package com.github.cszxyang.olycode.lua;

import com.github.cszxyang.olycode.lua.api.LuaState;
import com.github.cszxyang.olycode.lua.state.LuaStateImpl;
import com.github.cszxyang.olycode.common.proxy.ProxySystem;
public class LuaExecutor {

    private LuaState luaState = new LuaStateImpl();

    public String execute(String code) {
        String res = null;
        try {
            luaState.openLibs();
            luaState.load(code, "olycode.lua");
            luaState.call(0, -1);
            res = ProxySystem.getBufferString();
            System.out.println("res: " + res);
            ProxySystem.closeBuffer();
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    public static void main(String[] args) {
        LuaExecutor executor = new LuaExecutor();
        String code = "-- test upvalues\n" +
                "function newCounter ()\n" +
                "    local count = 0\n" +
                "    return function () \n" +
                "        count = count + 1\n" +
                "        return count\n" +
                "    end\n" +
                "end\n" +
                "^%^#%^&%*&\n" +
                "c1 = newCounter()\n" +
                "print(c1())\n" +
                "print(c1())\n" +
                "\n" +
                "c2 = newCounter()\n" +
                "print(c2())\n" +
                "print(c1())\n" +
                "print(c2())";
        executor.execute(code);
    }
}
