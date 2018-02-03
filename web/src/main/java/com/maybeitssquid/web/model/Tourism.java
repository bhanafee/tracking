package com.maybeitssquid.web.model;

import java.util.Locale;
import java.util.Optional;

public interface Tourism {

    /** Retrieves information of interest to a tourist.
     * @param locale localization information for the tourist
     * @param country the country code where the tourist is
     * @param postal the postal code within the country
     * @return a short blurb for the tourist.
     */
    String info (Locale locale, String country, Optional<String> postal);
}
