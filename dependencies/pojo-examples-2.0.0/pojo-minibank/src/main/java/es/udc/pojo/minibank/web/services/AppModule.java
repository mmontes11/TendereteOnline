package es.udc.pojo.minibank.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;

import es.udc.pojo.minibank.web.util.SupportedLanguages;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

    public static void contributeApplicationDefaults(
        MappedConfiguration<String, String> configuration) {

        SupportedLanguages.initialize();
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, SupportedLanguages
                .getCodes());

    }

}
