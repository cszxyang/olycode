<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" href="css/ide.css">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" >

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.js"></script>

    <%--<link rel="stylesheet" href="bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="bootstrap3/css/bootstrap-theme.min.css">
    <script src="bootstrap3/js/bootstrap.min.js"></script>
    <script src="bootstrap3/js/jquery-1.11.2.min.js"></script>--%>

    <title>olycode</title>
</head>
<body>

<div class="fuck-row">
    <div class="row" >
        <div class="outer">
            <form action="/run">
                <h1 style="text-align: center; margin-top: 2px">Olycode - An Awesome<sup>lame</sup> Online IDE</h1>
                <div class="custom">
                    <label class="control-label">Language</label>
                    <select class="language-picker" id="lang-picker">
                        <option selected value="java">java</option>
                        <option value="python">python</option>
                        <option value="lua">lua</option>
                    </select>
                    <label class="control-label">Theme</label>
                    <select class="theme-picker">
                        <option value="vs-dark">Visual Studio Dark</option>
                        <option value ="vs">Visual Studio</option>
                        <option value ="hc-black">High Contrast Dark</option>
                    </select>

                    <input class="run" type="button" onclick="run()" value="run" />
                </div>

                <div class="new-container">
                    <div class="col-md-7" ><div id="editor"></div></div>

                    <div class="col-md-5" >
                        <textarea id="resArea" class="result" disabled placeholder="ready to run"></textarea>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="/min/vs/loader.js"></script>
<script>
    var lp = document.querySelector(".language-picker");
    var index1 = lp.selectedIndex;

    var tp = document.querySelector(".theme-picker");
    var index2 = tp.selectedIndex;

    var javaCode = "import java.util.Arrays;\n" +
        "import java.util.List;\n" +
        "import java.util.stream.IntStream;\n" +
        "\n" +
        "public class Example {\n" +
        "\n" +
        "    public static void main(String[] args) {\n" +
        "        // printStream proxy\n" +
        "        System.out.println(\"Hello World!\");\n" +
        "\n" +
        "        // basic computing\n" +
        "        int num = 1;\n" +
        "        System.out.println(\"num << 4 :\" + (num << 4));\n" +
        "\n" +
        "        // Java 8 operations\n" +
        "        IntStream intStream = IntStream.of(1, 2, 3, 4);\n" +
        "        System.out.println(\"sum:\" + intStream.sum());\n" +
        "        List<Integer> integers = Arrays.asList(1, 2, 3, 3, 4);\n" +
        "        System.out.println(\"count of integers: \" + integers.stream().distinct().count());\n" +
        "\n" +
        "        // Exception handling\n" +
        "        int a = 1;\n" +
        "        System.out.println(a / 0);\n" +
        "\n" +
        "        /* endless loop handling\n" +
        "        for (;;) {\n" +
        "            System.out.println(\"running...\");\n" +
        "        }\n" +
        "         */\n" +
        "    }\n" +
        "}\n";

    var luaCode = '-- defines a factorial function\n' +
        '    function fact (n)\n' +
        '      if n == 0 then\n' +
        '        return 1\n' +
        '      else\n' +
        '        return n * fact(n-1)\n' +
        '      end\n' +
        '    end\n' +
        '    \n' +
        '    print("enter a number:")\n' +
        '    a = io.read("*number")        -- read a number\n' +
        '    print(fact(a))';

    var pythonCode = '#!/usr/bin/python\n' +
        '\n' +
        'def varfunc():\n' +
        '    var = 0\n' +
        '    print \'var = %d\' % var\n' +
        '    var += 1\n' +
        '\n' +
        'if __name__ == \'__main__\':\n' +
        '    print("Hello, World!");\n' +
        '    for i in range(3):\n' +
        '        varfunc()\n' +
        '\n' +
        'class Static:\n' +
        '    StaticVar = 5\n' +
        '    def varfunc(self):\n' +
        '        self.StaticVar += 1\n' +
        '        print self.StaticVar\n' +
        '\n' +
        'print Static.StaticVar\n' +
        'a = Static()\n' +
        'for i in range(3):\n' +
        '    a.varfunc()';

    var me;

    require.config({ paths: { 'vs': 'min/vs' }});
    require(['vs/editor/editor.main'], function() {
        var editor = monaco.editor.create(document.getElementById('editor'), {
            value: [
                javaCode
            ].join('\n'),
            language: lp.options[index1].value,
            theme: tp.options[index2].value
        });
        me = editor;
    });

    lp.addEventListener('change', function (e) {
        console.log(e);
        var selectedIndex = e.path[0].selectedIndex;
        if (selectedIndex == 0) {
            me.setValue(javaCode)
        } else if (selectedIndex === 1) {
            me.setValue(pythonCode)
        } else if (selectedIndex === 2) {
            me.setValue(luaCode)
        }
    });

    tp.addEventListener('change', function (e) {
        monaco.editor.setTheme(e.path[0].value);
    });

    function run(){
        var source = me.getValue();
        var lang_picker = document.getElementById("lang-picker");
        var index = lang_picker.selectedIndex;
        var lang = lang_picker.options[index].value;

        var data = { code: source, lang: lang };

        fetch('/run', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data, null, null),
        }).then(res => res.json()).then(json => {
            if (json.code === 200) {
                console.log(json.data);
                console.log(document.getElementById('resArea'))
                document.getElementById('resArea').innerHTML = json.data;
            } else {
                document.getElementById('resArea').xalue = json.message;
            }
        });
    }
</script>
</body>
</html>
