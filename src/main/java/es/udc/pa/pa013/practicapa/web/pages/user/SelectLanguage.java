package es.udc.pa.pa013.practicapa.web.pages.user;

import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

import es.udc.pa.pa013.practicapa.web.pages.Index;
import es.udc.pa.pa013.practicapa.web.util.SupportedLanguages;



public class SelectLanguage {

    @Property
    private String language;

    @Inject
    private Locale locale;

    @Inject
    private PersistentLocale persistentLocale;

    void onPrepareForRender() {
        language = locale.getLanguage();
    }

    public String getLanguages() {
        return SupportedLanguages.getOptions(locale.getLanguage());
    }

    Object onSuccess() {

        persistentLocale.set(new Locale(language));

        return Index.class;

    }

}

