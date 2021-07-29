package com.github.lianjiatech.retrofit.spring.boot.degrade;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 陈添明
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Degrade {

    /**
     * Threshold count.
     */
    double count();

    /**
     * Degrade recover timeout (in seconds) when degradation occurs.
     */
    int timeWindow() default 5;

    /**
     * Circuit breaking strategy (0: average RT, 1: exception ratio, 2: exception count).
     */
    int grade() default RuleConstant.DEGRADE_GRADE_RT;

    /**
     * Minimum number of requests (in an active statistic time span) that can trigger circuit breaking.
     */
    int minRequestAmount() default RuleConstant.DEGRADE_DEFAULT_MIN_REQUEST_AMOUNT;

    /**
     * The threshold of slow request ratio in RT mode.
     */
    double slowRatioThreshold() default 1.0d;

    int statIntervalMs() default 1000;
}
