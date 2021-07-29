package com.github.lianjiatech.retrofit.spring.boot.degrade;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.github.lianjiatech.retrofit.spring.boot.config.DegradeProperty;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author 陈添明
 */
public class RetrofitDegradeRuleInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger logger = LoggerFactory.getLogger(RetrofitDegradeRuleInitializer.class);

    private final DegradeProperty degradeProperty;


    private static List<DegradeRule> LIST = new CopyOnWriteArrayList<>();

    public RetrofitDegradeRuleInitializer(DegradeProperty degradeProperty) {
        this.degradeProperty = degradeProperty;
    }


    public static void addRetrofitDegradeRule(DegradeRule degradeRule) {
        if (degradeRule == null) {
            return;
        }
        LIST.add(degradeRule);
    }


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!degradeProperty.isEnable()) {
            return;
        }

        DegradeType degradeType = degradeProperty.getDegradeType();
        switch (degradeType) {
            case SENTINEL: {
                try {
                    Class.forName("com.alibaba.csp.sentinel.SphU");
                    DegradeRuleManager.loadRules(LIST);
                } catch (Exception e) {
                    logger.warn("com.alibaba.csp.sentinel not found! No SentinelDegradeInterceptor is set.");
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Not currently supported! degradeType=" + degradeType);
            }

        }
    }
}
