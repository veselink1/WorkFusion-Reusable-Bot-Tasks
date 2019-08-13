/**
 * Calls a WebHarvest function.
 * @param functionName The name of the WebHarvest function to call. 
 * @param params A map of String -> Object with context variables for the function.
 */
callFunction = { String functionName, Map<String, Object> params = [:] ->
    StringBuilder sb = new StringBuilder();
    sb.append("""<call name="${functionName}">""");
    params.each { key, value ->
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("The parameter name '${key}' is not of type String!")
        }
        if (!(value instanceof CharSequence) && !(value instanceof Number) && !(value instanceof Boolean)) {
            throw new IllegalArgumentException("The value of parameter '${key}' is neither a CharSequence nor a primitive type!");
        }
        sb.append('<call-param name="');
        sb.append(key);
        sb.append('"><![CDATA').append('[');
        sb.append(value);
        sb.append(']]').append('></call-param>');
    };
    sb.append('</call>');
    String generated = sb.toString();
    
    def node = new org.webharvest.definition.CallDef(
        org.webharvest.definition.XmlNode.getInstance(
            new org.xml.sax.InputSource(new java.io.StringReader(generated))));
    
    try {
        return sys.scraper.execute(Arrays.asList(node));
    }
    catch (org.webharvest.exception.PluginException e) {
        throw new RuntimeException("Exception during execution of <function name=\"${functionName}\" /> (params=${params})", e);
    }
}