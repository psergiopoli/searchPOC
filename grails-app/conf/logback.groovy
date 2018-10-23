import ch.qos.logback.classic.filter.LevelFilter
import grails.util.BuildSettings
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

final String LOGGER_INFO = "LOGGER_INFO"

String stdoutPattern =
        '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                '%clr(%5p) ' + // Log level
                '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                '%m%n%wex' // Message

String filePattern =
        '%d{yyyy-MM-dd HH:mm:ss.SSS} ' +
                '%level %logger - %msg%n'

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = stdoutPattern
    }
}

def targetDir = BuildSettings.TARGET_DIR

Boolean logInfo = new Boolean(System.getenv(LOGGER_INFO)) // Variavel de ambiente!

if(targetDir != null) {
    new File("${targetDir}/logs/archive").mkdirs()
}

if (targetDir != null) {
    appender("ERROR_APPENDER", RollingFileAppender) {
        file = "${targetDir}/logs/log-error.log"
        append = true
        filter(LevelFilter) {
            level = ERROR
            onMatch = "ACCEPT"
            onMismatch = "DENY"
        }
        encoder(PatternLayoutEncoder) {
            pattern = filePattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${targetDir}/logs/archive/log-error%d{yyyy-MM-dd}.log"
            maxHistory = 30
            totalSizeCap = "10mb"
        }
    }
    logger("search", ERROR, ['ERROR_APPENDER','STDOUT'], false)
}

if (targetDir != null && logInfo) {
    appender("INFO_APPENDER", RollingFileAppender) {
        file = "${targetDir}/logs/log-info.log"
        append = true
        filter(LevelFilter) {
            level = INFO
            onMatch = "ACCEPT"
            onMismatch = "DENY"
        }
        encoder(PatternLayoutEncoder) {
            pattern = filePattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${targetDir}/logs/archive/log-info%d{yyyy-MM-dd}.log"
            maxHistory = 30
            totalSizeCap = "10mb"
        }
    }

    logger("search", INFO, ['INFO_APPENDER'], false)
}

root(ERROR, ['STDOUT'])
