/**
 * Calls a WebHarvest function.
 * @param functionName The name of the WebHarvest function to call. 
 * @param params A map of String -> Object with context variables for the function.
 */
callFunction = { String functionName, HashMap<String, Object> functionParams -> 
    org.webharvest.runtime.Scraper scraper = sys.scraper;
    org.webharvest.definition.ScraperConfiguration configuration = scraper.getConfiguration();

    org.webharvest.runtime.processors.CallProcessor processor = new org.webharvest.runtime.processors.CallProcessor(null, configuration, scraper);
    org.webharvest.runtime.ScraperContext functionContext = processor.getFunctionContext();

    org.webharvest.definition.FunctionDef functionDef = scraper.getConfiguration().getFunctionDef(functionName);
    if (functionDef == null) {
        throw new RuntimeException("Function \"" + functionName + "\" is undefined!");
    }

    org.webharvest.runtime.processors.CallProcessor runningFunction = scraper.getRunningFunction();
    org.webharvest.runtime.ScraperContext callerContext = runningFunction == null 
        ? scraper.getContext() 
        : runningFunction.getFunctionContext();

    functionContext.putAll(callerContext);
    for (Map.Entry<String, Object> param : functionParams.entrySet()) {
        functionContext.put(param.getKey(), org.webharvest.utils.CommonUtil.createVariable(param.getValue()));
    }

    scraper.addRunningFunction(processor);

    org.webharvest.runtime.variables.Variable functionResult = new org.webharvest.runtime.processors.BodyProcessor(functionDef).execute(scraper, functionContext);

    scraper.removeRunningFunction();

    return functionResult;
};
