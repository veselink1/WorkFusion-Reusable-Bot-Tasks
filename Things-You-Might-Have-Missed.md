# WorkFusion - Things You Might Have Missed

## Business Processes
- Doing a plain copy is rarely sufficient and should be used only when you plan on not changing the BP workflow, as changes will affect the original BP (and/or Use Case). Prefer using deep copies.
- Preserving input data throughout the BP will result in `start_date` and `author` columns being added to the results. 

## Bot Configs
- Only export primitive value types within the `<export>` tag. Other types get converted to string via `toString()`. Prefer `<multi-column>`, generate `<single-column>` output via `<loop>`, or use JSON when exporting and aggregate (`com.google.gson.Gson#toJson`).
- When declaring default input's for a bot config you are testing, prefer using `<var-def name="..." overwrite="false">` so that you can later override these inputs via the Input Data/ETL Bot if needed. 

## General Scripting
- Use `log.warn(obj)` for logging data to the Results tab of the BP (there is a 1000 character limit). 
- Wrap your Groovy code in a top-level try-catch block, then use `log.error(e.message, org.codehaus.groovy.runtime.StackTraceUtils.sanitize(e))` to log the error to get proper stack-traces. Also do not forget to rethrow the exception via `throw e` to halt the execution OR use the [stack trace sanitization](https://github.com/veselink1/WorkFusion-Reusable-Bot-Tasks/blob/master/scripts/sanitize-stacktraces.groovy) utility
- You can use the `@groovy.transform.TypeChecked` attribute to verify code at compile-time. 

## Data Stores
- The result of a query implements `List`. Accessing the columns of a record via index or name returns a `NodeVariable`. Don't forget to convert it to the appropriate type.

## S3
- The `S3#putToFileS3` method simply **does NOT** work and is not supported. Use the `<s3-put>` plugin. 
- Using multiple `<s3-put>` in a parent `<s3>` and storing the result of that in a variable creates a list of the resulting links, but also allows simultaneous upload for multiple files.

## File Handling
- You can paste a file's URL address in the Windows Open File Dialog, instead of downloading the file first and then pasting the downloaded file's path. The file is downloaded as an IE temporary file and is then stored in a hidden folder under %localappdata%\Microsoft\Windows\INetCache\IE\. IE temp files are deleted automatically only when Storage Sense (Win10 feature) is enabled, and otherwise have to be manually deleted.
- Prefer uploading files to S3 and then following the above tip, uploading to S3 and using the `downloadFileOnAgent` method or use the [create-file-on-agent](https://github.com/veselink1/WorkFusion-Reusable-Bot-Tasks/blob/master/scripts/create-file-on-agent.groovy) script

## Miscellaneous
- As a general rule of thumb, prefer `<script return="varname" />` when providing input to elements instead of `${varname}`, as some elements require an explicit `<template>` tag to allow interpolation. `<script>` is simply less problematic. 

## Where is my code executed?
- That is a question that I've had to ask myself many times when debugging my bot configuraions. Here is the answer: Anything that is immediately wrapped in a `<script>` tag or `${}` is evaluated on the Control Tower instance (usually under a Linux-based OS). Communication with remote environments is implemented by message-passing under the hood. Results of elements queries are wrapped in Selenium's `RemoteWebElement`. 
- Execute your scripts on the remote machine using `executeGroovyScript(code, "imports", [ key-1: value-1, ... ])`. All objects passed as inputs to the script are serialized to JSON internally. Generally speaking, primitive types, arrays, lists and maps are well supported.