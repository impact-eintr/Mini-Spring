package org.eintr.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping{


    /**
     * Name of the {@link HttpServletRequest} attribute that contains the mapped
     * handler for the best matching pattern.
     * @since 4.3.21
     */
    String BEST_MATCHING_HANDLER_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingHandler";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains the path
     * used to look up the matching handler, which depending on the configured
     * or without the context path, decoded or not, etc.
     * @since 5.2
     */
    String LOOKUP_PATH = HandlerMapping.class.getName() + ".lookupPath";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains the path
     * within the handler mapping, in case of a pattern match, or the full
     * relevant URI (typically within the DispatcherServlet's mapping) else.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations. URL-based HandlerMappings will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains the
     * best matching pattern within the handler mapping.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations. URL-based HandlerMappings will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String BEST_MATCHING_PATTERN_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingPattern";

    /**
     * Name of the boolean {@link HttpServletRequest} attribute that indicates
     * whether type-level mappings should be inspected.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations.
     */
    String INTROSPECT_TYPE_LEVEL_MAPPING = HandlerMapping.class.getName() + ".introspectTypeLevelMapping";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains the URI
     * templates map, mapping variable names to values.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations. URL-based HandlerMappings will
     * typically support it, but handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String URI_TEMPLATE_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".uriTemplateVariables";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains a map with
     * URI variable names and a corresponding MultiValueMap of URI matrix
     * variables for each.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations and may also not be present depending on
     * whether the HandlerMapping is configured to keep matrix variable content
     */
    String MATRIX_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".matrixVariables";

    /**
     * Name of the {@link HttpServletRequest} attribute that contains the set of
     * producible MediaTypes applicable to the mapped handler.
     * <p>Note: This attribute is not required to be supported by all
     * HandlerMapping implementations. Handlers should not necessarily expect
     * this request attribute to be present in all scenarios.
     */
    String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class.getName() + ".producibleMediaTypes";

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
