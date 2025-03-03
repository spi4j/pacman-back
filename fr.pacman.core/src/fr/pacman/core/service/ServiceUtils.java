package fr.pacman.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.obeonetwork.dsl.environment.DTO;
import org.obeonetwork.dsl.environment.Reference;
import org.obeonetwork.dsl.soa.Service;

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
	public static Set<DTO> get_associatedProvider(Set<DTO> p_associatedDtos, List<Service> p_services) {
		Set<DTO> dtos = new HashSet<>();
		DTO dto;

		for (DTO associatedDto : p_associatedDtos) {
			dtos.add(associatedDto);
			for (Reference reference : associatedDto.getReferences()) {
				if (null != reference.getOppositeOf()) {
					dto = (DTO) reference.getOppositeOf().getContainingType();
				} else {
					dto = (DTO) reference.getReferencedType();
				}
				for (Service service : p_services) {
					if (service.getName().equalsIgnoreCase(dto.getName())) {
						dtos.add(dto);
						break;
					}
				}
			}
		}
		return dtos;
	}
}
