package es.udc.pa.pa013.practicapa.web.services;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

public class AuthenticationPolicyWorker implements
		ComponentClassTransformWorker2 {

	@Override
	public void transform(PlasticClass plasticClass,
			TransformationSupport support, MutableComponentModel model) {

		processPageAnnotations(plasticClass, model);
		processEventHandlerAnnotations(plasticClass, model);

	}

	/**
	 * Read and process restriction on page classes annotated with
	 * {@link AuthenticationPolicy} annotation
	 * 
	 * @param plasticClass
	 *            Contains class-specific information used when transforming a
	 *            raw component class into an executable component class.
	 * @param model
	 *            Mutable version of
	 *            {@link org.apache.tapestry5.model.ComponentModel} used during
	 *            the transformation phase.
	 */
	private void processPageAnnotations(PlasticClass plasticClass,
			MutableComponentModel model) {

		AuthenticationPolicy policy = plasticClass
				.getAnnotation(AuthenticationPolicy.class);
		if (policy != null) {
			model.setMeta(AuthenticationValidator.PAGE_AUTHENTICATION_TYPE,
					policy.value().toString());
		}

	}

	/**
	 * Inject meta datas about annotated methods
	 * 
	 * @param plasticClass
	 *            Contains class-specific information used when transforming a
	 *            raw component class into an executable component class.
	 * @param model
	 *            Mutable version of
	 *            {@link org.apache.tapestry5.model.ComponentModel} used during
	 *            the transformation phase.
	 */
	private void processEventHandlerAnnotations(PlasticClass plasticClass,
			MutableComponentModel model) {

		for (PlasticMethod method : plasticClass
				.getMethodsWithAnnotation(AuthenticationPolicy.class)) {
			String methodName = method.getDescription().methodName;
			AuthenticationPolicy policy = method
					.getAnnotation(AuthenticationPolicy.class);
			OnEvent event = method.getAnnotation(OnEvent.class);
			if (methodName.startsWith("on") || event != null) {
				String componentId = extractComponentId(methodName, event);
				String eventType = extractEventType(methodName, event);
				String authenticationPolicyMeta = AuthenticationValidator.EVENT_HANDLER_AUTHENTICATION_TYPE
						+ "-" + componentId + "-" + eventType;

				authenticationPolicyMeta = authenticationPolicyMeta
						.toLowerCase();
				model.setMeta(authenticationPolicyMeta, policy.value()
						.toString());
			} else {
				throw new RuntimeException(
						"Cannot put AuthenticationPolicy annotation on a non event handler method");
			}
		}

	}

	/**
	 * This code is taken deliberatly from:
	 * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/
	 * main
	 * /java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=
	 * markup
	 * 
	 * Returns the component id to match against, or the empty string if the
	 * component id is not specified. The component id is provided by the
	 * OnEvent annotation or (if that is not present) by the part of the method
	 * name following "From" ("onActionFromFoo").
	 */
	private String extractComponentId(String methodName, OnEvent annotation) {
		if (annotation != null)
			return annotation.component();

		// Method name started with "on". Extract the component id, if present.

		int fromx = methodName.indexOf("From");

		if (fromx < 0)
			return "";

		return methodName.substring(fromx + 4);
	}

	/**
	 * This code is taken deliberatly from:
	 * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/
	 * main
	 * /java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=
	 * markup
	 * 
	 * Returns the event name to match against, as specified in the annotation
	 * or (if the annotation is not present) extracted from the name of the
	 * method. "onActionFromFoo" or just "onAction".
	 */
	private String extractEventType(String methodName, OnEvent annotation) {
		if (annotation != null)
			return annotation.value();

		int fromx = methodName.indexOf("From");

		// The first two characters are always "on" as in "onActionFromFoo".
		return fromx == -1 ? methodName.substring(2) : methodName.substring(2,
				fromx);
	}
}