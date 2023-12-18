package org.eintr.springframework.context;

import org.eintr.springframework.beans.factory.Aware;
import org.eintr.springframework.core.env.Environment;

public interface EnvironmentAware extends Aware {
    void setEnvironment(Environment environment);
}
