package com.github.cszxyang.olycode.java.compiler;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.annotation.processing.Messager;

public abstract class ExprCodeChecker<R, P> extends TreePathScanner<R, P> {

    // 当前被扫描代码对应的节点转换工具类, 运行时由Processor负责置入
    protected Trees trees;
    // 错误信息打印、处理流程控制工具, 运行时由Processor负责置入
    protected Messager messager;

    /**
     * 取得代码检查的错误信息, 返回结果为null或空字符串串则表示无错误, 否则认为有错误发生
     * 
     * @return
     */
    public abstract String getErrorMsg();

    /**
     * 取得初始参数
     * 
     * @return 用于遍历代码树的初始参数
     */
    protected abstract P getInitParam();

    /**
     * 代码检查是否成功
     * 
     * @return true - 成功，无问题； false - 失败，调用getErrorMsg可获取错误信息
     */
    final boolean isSuccess() {
        String err = this.getErrorMsg();
        return err == null || err.length() == 0;
    }

    /**
     * package访问权限，专门用于由Processor置入Trees工具实例
     * 
     * @param trees
     */
    final void setTrees(Trees trees) {
        this.trees = trees;
    }

    /**
     * package访问权限，专门用于由Processor置入Messager工具实例
     * 
     * @param messager
     */
    final void setMessager(Messager messager) {
        this.messager = messager;
    }

    /**
     * 开始遍历处理传入的代码树节点
     * 
     * @param path
     */
    final void check(TreePath path) {
        this.scan(path, getInitParam());
    }

    /**
     * 获取指定语法节点缩在源文件中的行号和列号信息, 用于错误信息输出
     * 
     * @param node
     * @return
     */
    protected String resolveRowAndCol(Tree node) {
        CompilationUnitTree unit = this.getCurrentPath().getCompilationUnit();
        long pos = this.trees.getSourcePositions().getStartPosition(unit, node);
        LineMap m = unit.getLineMap();
        return "row: " + m.getLineNumber(pos) + ", col: " + m.getColumnNumber(pos);
    }

}