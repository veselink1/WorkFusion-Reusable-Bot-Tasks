# WorkFusion - Things You Might Have Missed

## Business Processes
- Doing a plain copy is rarely sufficient and should be used only when you plan on not changing the BP workflow, as changes will affect the original BP (and/or Use Case). Prefer using deep copies.
- Preserving input data throughout the BP will result in `start_date` and `author` columns being added to the results. 
- See the [Single Execution Flow](https://github.com/veselink1/WorkFusion-Reusable-Bot-Tasks/blob/master/scripts/single-execution-flow.xml) script to find an easy way to drop all previous records and continue the BP execution with a single execution flow.

## Bot Configs
- Only export primitive value types within the [`<export>`](https://kb.workfusion.com/display/WF/export) tag. Other types get converted to string via `toString()`. Prefer `<multi-column>`, generate `<single-column>` output via [`<loop>`](http://web-harvest.sourceforge.net/manual.php#loop), or use JSON when exporting and aggregate ([`com.google.gson.Gson#toJson`](https://static.javadoc.io/com.google.code.gson/gson/2.8.5/com/google/gson/Gson.html#toJson-com.google.gson.JsonElement-)).
- When declaring default input's for a bot config you are testing, prefer using [`<var-def name="..." overwrite="false">`](http://web-harvest.sourceforge.net/manual.php#var-def) so that you can later override these inputs via the Input Data/ETL Bot if needed. 

## General Scripting
- Use `log.warn(obj)` for logging data to the Results tab of the BP (there is a 1000 character limit). 
- Wrap your Groovy code in a top-level try-catch block, then use [`log.error(e.message, org.codehaus.groovy.runtime.StackTraceUtils.sanitize(e))`](http://docs.groovy-lang.org/2.2.1/html/api/org/codehaus/groovy/runtime/StackTraceUtils.html#sanitize(java.lang.Throwable)) to log the error to get proper stack-traces. Also do not forget to rethrow the exception via `throw e` to halt the execution OR use the [stack trace sanitization](https://github.com/veselink1/WorkFusion-Reusable-Bot-Tasks/blob/master/scripts/sanitize-stacktraces.groovy) utility
- You can use the [`@groovy.transform.TypeChecked`](http://docs.groovy-lang.org/latest/html/gapi/groovy/transform/TypeChecked.html) attribute to verify code at compile-time. 

## Data Stores
- The result of a query implements [`List`](https://docs.oracle.com/javase/8/docs/api/java/util/List.html). Accessing the columns of a record via index or name returns a [`NodeVariable`](http://web-harvest.sourceforge.net/doc/org/webharvest/runtime/variables/NodeVariable.html). Don't forget to convert it to the appropriate type.

## S3
- The [`S3#putToFileS3`](https://workfusion-docs.s3.amazonaws.com/rpa-simplified-api/latest/com/workfusion/rpa/helpers/S3.html#uploadFileS3-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-java.lang.String-com.workfusion.rpa.helpers.S3OverwriteStrategy-) method simply **does NOT** work and is not supported. Use the [`<s3-put>`](https://kb.workfusion.com/display/WF/S3+Plugins#S3Plugins-s3-put) plugin. 
- Using multiple [`<s3-put>`](https://kb.workfusion.com/display/WF/S3+Plugins#S3Plugins-s3-put) in a parent [`<s3>`](https://kb.workfusion.com/display/WF/S3+Plugins#S3Plugins-s3) and storing the result of that in a variable creates a list of the resulting links, but also allows simultaneous upload for multiple files.

## File Handling
- Prefer uploading files to S3 and then following the above tip, uploading to S3 and using the [`downloadFileOnAgent`](https://workfusion-docs.s3.amazonaws.com/rpa-simplified-api/latest/com/workfusion/rpa/helpers/RPA.html#downloadFileOnAgent-java.lang.String-) method or use the lesser-known [`sendToAgent`](https://workfusion-docs.s3.amazonaws.com/rpa-simplified-api/latest/com/workfusion/rpa/helpers/RPA.html#sendToAgent-java.lang.String-) method to directly upload a blob to the agent.
- **Not recommended** You can paste a file's URL address in the Windows Open File Dialog, instead of downloading the file first and then pasting the downloaded file's path. The file is downloaded as an IE temporary file and is then stored in a hidden folder under %localappdata%\Microsoft\Windows\INetCache\IE\. IE temp files are deleted automatically only when Storage Sense (Win10 feature) is enabled, and otherwise have to be manually deleted. Generally not recommended as explicit wait through sleep() is needed to wait for Windows to download the file and attach it.

## Miscellaneous
- As a general rule of thumb, prefer [`<script return="varname" />`](http://web-harvest.sourceforge.net/manual.php#script) when providing input to elements instead of `${varname}`, as some elements require an explicit [`<template>`](http://web-harvest.sourceforge.net/manual.php#template) tag to allow interpolation. `<script>` is simply less problematic. 

## Where is my code executed?
- That is a question that I've had to ask myself many times when debugging my bot configuraions. Here is the answer: Anything that is immediately wrapped in a [`<script>`](http://web-harvest.sourceforge.net/manual.php#script) tag or `${}` is evaluated on the Control Tower instance (usually under a Linux-based OS). Communication with remote environments is implemented by message-passing under the hood. Results of elements queries are wrapped in Selenium's [`RemoteWebElement`](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/remote/RemoteWebElement.html). 
- Execute your scripts on the remote machine using [`executeGroovyScript(code, "imports", [ key-1: value-1, ... ])`](https://kb.workfusion.com/display/RPA/Execute+Groovy+Script). All objects passed as inputs to the script are serialized to JSON internally. Generally speaking, primitive types, arrays, lists and maps are well supported.
