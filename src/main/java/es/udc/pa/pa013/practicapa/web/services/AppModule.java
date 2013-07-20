package es.udc.pa.pa013.practicapa.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

import es.udc.pa.pa013.practicapa.web.util.SupportedLanguages;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

	public static void bind(ServiceBinder binder) {

		/* Bind filters. */
		binder.bind(SessionFilter.class);
		binder.bind(PageRenderAuthenticationFilter.class);
		binder.bind(ComponentEventAuthenticationFilter.class);

	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {

		// Contributions to ApplicationDefaults will override any contributions
		// to FactoryDefaults (with the same key). Here we're restricting the
		// supported locales.
		SupportedLanguages.initialize();
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,es,gl");

	}

	/**
	 * Contribute our {@link ComponentClassTransformWorker2} to transformation
	 * pipeline to add our code to loaded classes
	 *
	 * @param configuration
	 *            component class transformer configuration
	 */
	public static void contributeComponentClassTransformWorker(
			OrderedConfiguration<ComponentClassTransformWorker2> configuration) {

		configuration.add("AuthenticationPolicy",
				new AuthenticationPolicyWorker());

	}


	public static void contributeRequestHandler(
			OrderedConfiguration<RequestFilter> configuration,
			SessionFilter sessionFilter) {

		/* Add filters to the RequestHandler service. */
		configuration.add("SessionFilter", sessionFilter,
				"after:*");

	}

	/**
	 * Contributes "PageRenderAuthenticationFilter" filter which checks for
	 * access rights of requests.
	 */
	public void contributePageRenderRequestHandler(
			OrderedConfiguration<PageRenderRequestFilter> configuration,
			PageRenderRequestFilter pageRenderAuthenticationFilter) {

		/*
		 * Add filters to the filters pipeline of the PageRender command of the
		 * MasterDispatcher service.
		 */
		configuration.add("PageRenderAuthenticationFilter",
				pageRenderAuthenticationFilter, "before:*");

	}

	/**
	 * Contribute "PageRenderAuthenticationFilter" filter to determine if the
	 * event can be processed and the user has enough rights to do so.
	 */
	public void contributeComponentEventRequestHandler(
			OrderedConfiguration<ComponentEventRequestFilter> configuration,
			ComponentEventRequestFilter componentEventAuthenticationFilter) {

		/*
		 * Add filters to the filters pipeline of the ComponentEvent command of
		 * the MasterDispatcher service.
		 */
		configuration.add("ComponentEventAuthenticationFilter",
				componentEventAuthenticationFilter, "before:*");

	}

}
