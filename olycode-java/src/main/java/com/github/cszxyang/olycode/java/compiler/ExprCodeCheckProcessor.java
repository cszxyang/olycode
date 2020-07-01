package com.github.cszxyang.olycode.java.compiler;

import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class ExprCodeCheckProcessor extends AbstractProcessor {

    // 工具实例类，用于将CompilerAPI, CompilerTreeAPI和AnnotationProcessing框架粘合起来
    private Trees trees;
    // 分析过程中可用的日志、信息打印工具
    private Messager messager;
    private TreeMaker treeMaker;
    // 所有的CodeChecker
    private LoopLimitEnhancer enhancer;

    // 搜集错误信息
    private StringBuilder               errMsg       = new StringBuilder();

    // 代码检查是否成功, 若false, 则'errMsg'里应该有具体错误信息
    private boolean                     success      = true;

    private JavacElements elements;

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = Trees.instance(processingEnv);
        this.messager = processingEnv.getMessager();
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        elements = (JavacElements) processingEnv.getElementUtils();
        enhancer = new LoopLimitEnhancer(trees, treeMaker, elements,10000, messager);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (!env.processingOver()) {
            for (Element e : env.getRootElements()) {
                enhancer.scan(this.trees.getPath(e), null);
            }
        }
        /*
         * 这里若return true将阻止任何后续可能存在的Processor的运行，因此这里可以固定返回false
         */
        return false;
    }

    /**
     * 获取代码检查的错误信息
     * 
     * @return
     */
    public String getErrMsg() {
        return errMsg.toString();
    }

    /**
     * 指示代码检查过程是否成功，若为false，则可调用getErrMsg取得具体错误信息
     * 
     * @return
     */
    public boolean isSuccess() {
        return success;
    }
}