package com.github.sibmaks.session_service;


import com.github.sibmaks.session_service.api.Session;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface SessionService {

    /**
     * Get session common info.<br/>
     * If session not found null will be returned
     *
     * @param sessionId session id
     * @return session or null
     */
    Session get(String sessionId);

    /**
     * Get attribute from session
     *
     * @param sessionId session id
     * @param section section in session
     * @param attribute attribute code
     * @return attribute value or null
     * @param <T> type of attribute
     */
    <T extends Serializable> T getAttribute(String sessionId, String section, String attribute);

    /**
     * Put attribute value into session
     *
     * @param sessionId session id
     * @param section section in session
     * @param attribute attribute code
     * @param value attribute value
     * @param <T> type of attribute
     */
    <T extends Serializable> void putAttribute(String sessionId, String section, String attribute, T value);

    /**
     * Remove attribute value from session
     *
     * @param sessionId session id
     * @param section section in session
     * @param attribute attribute code
     */
    void removeAttribute(String sessionId, String section, String attribute);
}
