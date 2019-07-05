# WorkFusion - Things You Might Have Missed

## BP 
- Doing a plain copy is rarely sufficient and should be used only when you plan on not changing the BP workflow, as changes will affect the original BP (and/or Use Case). Prefer using deep copies.

## Bot Configs
- Only export primitive value types within the `<export>` tag. Other types get converted to string via `toString()`. Prefer `<multi-column>`, generate `<single-column>` output via `<loop>`, or use JSON when exporting and aggregate (com.google.gson.Gson#toJson).

## S3
- The `S3#putToFileS3` method simply *does NOT* work and is not supported. Use the <s3-put> plugin. 

## Miscellaneous
- As a general rule of thumb, prefer `<script return="varname" />` when providing input to elements instead of `${varname}`, as some elements require an explicit `<template>` tag to allow interpolation. `<script>` is simply less problematic. 