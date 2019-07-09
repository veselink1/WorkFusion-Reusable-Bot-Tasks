/*
 * Example usage:  
 * sanitizeStackTraces {
 *   ...my groovy code...
 * }
 */

def sanitizeStackTraces(Closure block) {
    try {
        return block()
    }
    catch (Exception e) {
        log.error(e.message, org.codehaus.groovy.runtime.StackTraceUtils.sanitize(e))
        throw e
    }
}