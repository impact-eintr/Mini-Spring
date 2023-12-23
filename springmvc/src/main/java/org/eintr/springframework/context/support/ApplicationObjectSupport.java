package org.eintr.springframework.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationContextAware;

public class ApplicationObjectSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public final ApplicationContext getApplicationContext() throws IllegalStateException {
        if (this.applicationContext == null && isContextRequired()) {
            throw new IllegalStateException(
                    "ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
        }
        return this.applicationContext;
    }

    protected boolean isContextRequired() {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (context == null && !isContextRequired()) {
            this.applicationContext = null;
        } else if (this.applicationContext == null) {
            this.applicationContext = context;
            initApplicationContext(context);
        } else {
            if (this.applicationContext != context) {
                throw new BeansException(
                        "Cannot reinitialize with different application context: current one is [" +
                                this.applicationContext + "], passed-in one is [" + context + "]");
            }
        }
    }

    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        initApplicationContext();
    }

    protected void initApplicationContext() throws BeansException {}
}
