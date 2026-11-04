package org.omnione.did.tas.core;

import org.omnione.did.common.util.multibase.BaseMultibaseUtil;

import java.util.Arrays;

/**
 * Compares authNonce values by their decoded bytes, because the same nonce can be encoded in
 * different multibase formats.
 */
public final class AuthNonceMatcher {

    private AuthNonceMatcher() {
    }

    public static boolean matches(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }
        try {
            return Arrays.equals(BaseMultibaseUtil.decode(expected), BaseMultibaseUtil.decode(actual));
        } catch (Exception e) {
            return false;
        }
    }
}
