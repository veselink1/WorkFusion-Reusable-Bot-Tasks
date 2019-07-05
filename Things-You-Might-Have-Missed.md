# WorkFusion - Things You Might Have Missed

## BP 
- Doing a plain copy is rarely sufficient and should be used only when you plan on not changing the BP workflow, as changes will affect the original BP (and/or Use Case). Prefer using deep copies.


## Bot Configs
- Only export primitive value types within the `<export>` tag. Other types get converted to string via `toString()`. Prefer `<multi-column>`, generate `<single-column>` output via `<loop>`, or use JSON when exporting and aggregate (com.google.gson.Gson#toJson).
- Use `log.error(obj)` for logging data to the Results tab of the BP (there is a 1000 character limit). 
- When declaring default input's for a bot config you are testing, prefer using `<var-def name="..." overwrite="false">` so that you can later override these inputs via the Input Data/ETL Bot if needed. 


## S3
- The `S3#putToFileS3` method simply **does NOT** work and is not supported. Use the <s3-put> plugin. 
- Using multiple `<s3-put>` in a parent `<s3>` and storing the result of that in a variable creates a list of the resulting links, but also allows simultaneous upload for multiple files.


## Miscellaneous
- As a general rule of thumb, prefer `<script return="varname" />` when providing input to elements instead of `${varname}`, as some elements require an explicit `<template>` tag to allow interpolation. `<script>` is simply less problematic. 

