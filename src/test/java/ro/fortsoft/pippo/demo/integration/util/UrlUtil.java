package ro.fortsoft.pippo.demo.integration.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sbalamaci
 */
public class UrlUtil {
    /**
     * Appends trailing slashed to an url
     *
     * @param url The url to append the trailing slashes to
     * @return The modified url
     */
    public static String appendSlashOnRightSide(final String url) {
        if (!StringUtils.isEmpty(url) && !url.endsWith("/")) {
            return url + "/";
        }

        return url;
    }
}
