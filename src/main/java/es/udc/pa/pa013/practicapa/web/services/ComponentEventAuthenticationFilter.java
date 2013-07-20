package es.udc.pa.pa013.practicapa.web.services;

import java.io.IOException;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

public class ComponentEventAuthenticationFilter implements
		ComponentEventRequestFilter {

	private ApplicationStateManager applicationStateManager;
	private ComponentSource componentSource;
	private MetaDataLocator locator;
	private PageRenderRequestHandler pageRenderRequestHandler;

	public ComponentEventAuthenticationFilter(
			ApplicationStateManager applicationStateManager,
			ComponentSource componentSource, MetaDataLocator locator,
			PageRenderRequestHandler pageRenderRequestHandler) {

		this.applicationStateManager = applicationStateManager;
		this.componentSource = componentSource;
		this.locator = locator;
		this.pageRenderRequestHandler = pageRenderRequestHandler;

	}

	public void handle(ComponentEventRequestParameters parameters,
			ComponentEventRequestHandler handler) throws IOException {

		ComponentEventRequestParameters handlerParameters = parameters;
		String redirectPage = AuthenticationValidator.checkForPage(parameters
				.getActivePageName(), applicationStateManager, componentSource,
				locator);
		if (redirectPage == null) {
			String componentId = parameters.getNestedComponentId();
			if (componentId != null) {
				String mainComponentId = null;
				String eventId = null;
				if (componentId.indexOf(".") != -1) {
					mainComponentId = componentId.substring(0, componentId
							.lastIndexOf("."));
					eventId = componentId.substring(componentId
							.lastIndexOf(".") + 1);
				} else {
					eventId = componentId;
				}

				redirectPage = AuthenticationValidator.checkForComponentEvent(
						parameters.getActivePageName(), mainComponentId,
						eventId, parameters.getEventType(),
						applicationStateManager, componentSource, locator);

			}
		}

		if (redirectPage != null) {
			pageRenderRequestHandler.handle(new PageRenderRequestParameters(
					redirectPage, new EmptyEventContext(), false));
		} else {
			handler.handle(handlerParameters);
		}
	}
}
