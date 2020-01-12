package com.github.cszxyang.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

public class StringObject extends SimpleJavaFileObject {
    private String content;
 
    StringObject(URI uri, String contents) {
        super(uri, Kind.SOURCE);
        this.content = contents;
    }
 
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}