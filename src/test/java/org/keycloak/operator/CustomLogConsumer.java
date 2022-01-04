package org.keycloak.operator;

import org.testcontainers.containers.output.BaseConsumer;
import org.testcontainers.containers.output.OutputFrame;

import java.util.logging.Logger;

public class CustomLogConsumer extends BaseConsumer<CustomLogConsumer> {
    private final Logger logger;
    private boolean separateOutputStreams;
    private String prefix;

    public CustomLogConsumer(Logger logger) {
        this(logger, false);
    }

    public CustomLogConsumer(Logger logger, boolean separateOutputStreams) {
        this.prefix = "";
        this.logger = logger;
        this.separateOutputStreams = separateOutputStreams;
    }

    public CustomLogConsumer withPrefix(String prefix) {
        this.prefix = "[" + prefix + "] ";
        return this;
    }

    public CustomLogConsumer withSeparateOutputStreams() {
        this.separateOutputStreams = true;
        return this;
    }

    public void accept(OutputFrame outputFrame) {
        OutputFrame.OutputType outputType = outputFrame.getType();
        String utf8String = outputFrame.getUtf8String();
        utf8String = utf8String.replaceAll("((\\r?\\n)|(\\r))$", "");

        switch (outputType) {
            case END:
                break;
            case STDOUT:
                if (this.separateOutputStreams) {
                    this.logger.info(String.format("%s %s", this.prefix.isEmpty() ? "" : this.prefix + ": ", utf8String));
                } else {
                    this.logger.info(String.format("%s %s: %s", this.prefix, outputType, utf8String));
                }
                break;
            case STDERR:
                if (this.separateOutputStreams) {
                    this.logger.severe(String.format("%s %s", this.prefix.isEmpty() ? "" : this.prefix + ": ", utf8String));
                } else {
                    this.logger.severe(String.format("%s %s: %s", this.prefix, outputType, utf8String));
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected outputType " + outputType);
        }
    }
}
