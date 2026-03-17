package com.ic.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Security configuration properties loaded from the application environment.
 *
 * <p>Prefix: <code>ic.security</code></p>
 * <ul>
 *   <li><code>public-paths</code> &mdash; URL patterns that should bypass authentication.</li>
 * </ul>
 */
@ConfigurationProperties(prefix = "ic.security")
public class SecurityProperties {

    /**
     * URL patterns that do not require authentication.
     */
    private List<String> publicPaths = new ArrayList<>();

    /**
     * Returns the configured public URL patterns. Defaults to an empty list.
     */
    public List<String> getPublicPaths() {
        return publicPaths;
    }

    /**
     * Sets the public URL patterns that should be excluded from authentication.
     */
    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
