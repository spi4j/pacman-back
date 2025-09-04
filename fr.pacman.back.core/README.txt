Présence d'annotation FIC_CSV, EXCLUDE_CRITERIA,INCLUDE_CRITERIA,FK_CRITERE

************************
Architecture hexagonale
************************

pattern Ports-Adapters
	-> Ports 
		-> Interfaces
		-> Objets du domaine
	-> Adapters
		-> Implémentations
		-> Mappers