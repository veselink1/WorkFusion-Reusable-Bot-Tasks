/**
 * <example>
 * createFileOnAgent(xlsData, '.xls') {
 *    $('.Edit').sendKeys(it).pressEnter()
 *    ...
 * }
 * </example>
 */
def createFileOnAgent(byte[] data, String extension, Closure block) {
    String filePath = executeGroovyScript("""
        import java.io.File
        import org.apache.commons.io.FileUtils
        File tempFile = File.createTempFile('', '${extension}')
        FileUtils.writeByteArrayToFile(tempFile, data)
        return tempFile.path
    """, new ScriptParams('data', data)) as String
    
    try {
        block(filePath)    
    } finally {
        executeGroovyScript("""
            try {
                new java.io.File(filePath).delete()
            } catch (Exception e) {
                // Ignore.
            }
        """, new ScriptParams('filePath', filePath)) as String

    }
}