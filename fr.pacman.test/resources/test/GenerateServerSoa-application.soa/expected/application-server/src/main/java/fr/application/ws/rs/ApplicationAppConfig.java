/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.application.ws.rs;
// Start of user code for imports

import fr.application.ws.api.annuaire.GradesServiceRSFacade_Itf;
import fr.application.ws.api.annuaire.PersonneServiceRSFacade_Itf;
import fr.application.ws.impl_server.annuaire.GradesServiceRSFacade;
import fr.application.ws.impl_server.annuaire.PersonneServiceRSFacade;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

// End of user code
/**
 * Enregistrement des liaisons (implémentation / interface) dans le cadre 
 * de l'injection CDI / HK2 pour Jersey.
 *
 * @author safr@n.
 */
public class ApplicationAppConfig extends ResourceConfig
{
	public ApplicationAppConfig() {

		packages("fr.application.business.ws.rs.resources");

		register(new AbstractBinder() {
			@Override
			protected void configure() {

				bind(GradesServiceRSFacade.class).to(GradesServiceRSFacade_Itf.class);
				bind(PersonneServiceRSFacade.class).to(PersonneServiceRSFacade_Itf.class);
			}
		});
	}
}
