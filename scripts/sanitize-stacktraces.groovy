/**
 * <example>
 * sanitizeStackTraces {
 *   ...my groovy code...
 * }
 * </example>
 */

Object sanitizeStackTraces(Closure block) {
    try {
        return block()
    }
    catch (Exception e) {
        log.error(e.message, org.codehaus.groovy.runtime.StackTraceUtils.sanitize(e))
        throw e
    }
}