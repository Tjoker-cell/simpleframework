package org.simpleframework.mvc.processor;

import org.simpleframework.mvc.RequestProcessorChain;

public interface RequestProcessor {
    boolean process(RequestProcessorChain requestProcessorChain)throws Exception;
}
