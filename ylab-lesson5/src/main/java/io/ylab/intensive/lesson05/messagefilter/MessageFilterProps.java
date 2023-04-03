package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messagefilter.properties")
public class MessageFilterProps {

    @Value("${filterwords.path}")
    private String filterWordsPath;
    @Value("${filterwords.mode}")
    private String filterWordsMode;

    public String getFilterWordsPath() {
        return filterWordsPath;
    }

    public String getFilterWordsMode() {
        return filterWordsMode;
    }

}
