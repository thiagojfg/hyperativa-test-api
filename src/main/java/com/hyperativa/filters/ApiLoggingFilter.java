package com.hyperativa.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final int maxSizeBodyToLogBytes;
    private final List<String> ignoreUrls;
    private final List<String> excludeContentTypes;
    private final List<String> sensitiveKeys;
    private final ObjectMapper objectMapper;

    public ApiLoggingFilter(
            @Value("${api.logging.max-size-body-to-log-bytes}") int maxSizeBodyToLogBytes,
            @Value("${api.logging.ignore-urls}") List<String> ignoreUrls,
            @Value("${api.logging.exclude-content-types}") List<String> excludeContentTypes,
            @Value("${api.logging.sensitive-keys}") List<String> sensitiveKeys,
            ObjectMapper objectMapper) {
        this.maxSizeBodyToLogBytes = maxSizeBodyToLogBytes;
        this.ignoreUrls = ignoreUrls;
        this.excludeContentTypes = excludeContentTypes;
        this.sensitiveKeys = sensitiveKeys;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ignoreUrls.stream().anyMatch(url-> PATH_MATCHER.match(url, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        if (log.isInfoEnabled()) {
            String requestBody = shouldLogContent(request)
                    ? new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8)
                    : null;
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);

            String sanitizedRequestBody = getTruncatedBody(maskSensitiveData(requestBody, request.getContentType()));
            String sanitizedResponseBody = getTruncatedBody(maskSensitiveData(responseBody, response.getContentType()));

            long duration = System.currentTimeMillis() - startTime;

            log.info("Request: {} '{}' - Body: {} - IP: {}",
                    wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), sanitizedRequestBody,
                    wrappedRequest.getRemoteAddr());

            log.info("Response: {} '{}' - Status: {} - Time: {}ms - Body: {}",
                    wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), wrappedResponse.getStatus(), duration,
                    sanitizedResponseBody);
        }
        wrappedResponse.copyBodyToResponse();
    }

    private String getTruncatedBody(String body) {
        if (body == null || body.isEmpty()) {
            return "";
        }
        return body.length() > maxSizeBodyToLogBytes
                ? body.substring(0, maxSizeBodyToLogBytes) + "..."
                : body;
    }


    private boolean shouldLogContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType == null || excludeContentTypes.stream().noneMatch(contentType::startsWith);
    }

    private String maskSensitiveData(String body, String contentType) {
        if (StringUtils.isBlank(body) || sensitiveKeys.isEmpty()) {
            return body;
        }
        try {
            if (contentType.contains("application/json")
                    && body.startsWith("[")
                    || body.startsWith("{")) {
                ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(body);
                for (String key : sensitiveKeys) {
                    if (jsonNode.has(key)) {
                        jsonNode.put(key, "******");
                    }
                }
                return objectMapper.writeValueAsString(jsonNode);
            }
        } catch (IOException e) {
            log.warn("Failed to mask sensitive data, returning original body");
        }
        return body;
    }
}
