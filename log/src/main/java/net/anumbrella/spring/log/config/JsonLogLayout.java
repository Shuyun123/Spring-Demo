package net.anumbrella.spring.log.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

/**
 * @auther anumbrella
 */

public class JsonLogLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder result = new StringBuilder();
        if (null != event) {
            result.append("{");

            result.append("\"time\":\"");
            result.append(System.currentTimeMillis());
            result.append("\",");

            result.append("\"level\":\"");
            result.append(event.getLevel());
            result.append("\",");

            result.append("\"threadName\":\"");
            result.append(event.getThreadName());
            result.append("\",");

            result.append("\"msg\":\"");
            result.append(event.getFormattedMessage().replace("\"", "\\\""));
            result.append("\",");

            result.append("\"method\":\"");
            result.append(event.getLoggerName());
            result.append("\"} \n");

        }

        return result.toString();
    }
}
