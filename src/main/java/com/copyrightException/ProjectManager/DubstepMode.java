package com.copyrightException.ProjectManager;

public class DubstepMode {
      private static final String JAVA_SCRIPT = " setInterval(function() {\n" +
"\n" +
"   var randEl = document.getElementsByTagName(\"*\");\n" +
"\n" +
"   var randomNumber = Math.floor(Math.random() * randEl.length);\n" +
"\n" +
"\n" +
"\n" +
"   randEl = randEl[randomNumber];\n" +
"\n" +
"\n" +
"\n" +
"   var rand1 = Math.floor(Math.random() * 255);\n" +
"\n" +
"   var rand2 = Math.floor(Math.random() * 255);\n" +
"\n" +
"   var rand3 = Math.floor(Math.random() * 255);\n" +
"\n" +
"\n" +
"\n" +
"   try {\n" +
"\n" +
"     var style = randEl.getAttribute(\"style\");\n" +
"\n" +
"     var pattern = /background-color:rgb\\([0-9]{1,3},[0-9]{1,3},[0-9]{1,3}\\)/g;\n" +
"\n" +
"     if (pattern.test(style)) {\n" +
"\n" +
"       style = style.replace(pattern, \"background-color:rgb(\" + rand1 + \",\" + rand2 + \",\" + rand3 + \")\");\n" +
"\n" +
"       randEl.setAttribute(\"style\", style);\n" +
"\n" +
"\n" +
"\n" +
"     } else{\n" +
"     		 randEl.setAttribute(\"style\", style + \";background-color:rgb(\" + rand1 + \",\" + rand2 + \",\" + rand3 + \")\");\n" +
"     }\n" +
"\n" +
"   } catch (e) {\n" +
"\n" +
"\n" +
"\n" +
"   }\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"\n" +
" }, 5);";
}
