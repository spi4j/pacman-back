package fr.pacman.core.service;

import java.util.HashSet;
import java.util.Set;

import org.obeonetwork.dsl.environment.DTO;
import org.obeonetwork.dsl.environment.Reference;

/**
 * 
 * @author MINARM
 */
public class ServiceUtils {

	/**
	 * 
	 * @param p_associatedDtos
	 * @param p_services
	 * @return
	 */
	public static Set<DTO> get_associatedProvider(Set<DTO> p_associatedDtos) {
		Set<DTO> dtos = new HashSet<>();
		for (DTO associatedDto : p_associatedDtos) {
			dtos.add(associatedDto);
			for (Reference reference : associatedDto.getReferences()) {
				if (null != reference.getOppositeOf()) {
					dtos.add((DTO) reference.getOppositeOf().getContainingType());
				} else {
					dtos.add((DTO) reference.getReferencedType());
				}
			}
		}
		return dtos;
	}
}
