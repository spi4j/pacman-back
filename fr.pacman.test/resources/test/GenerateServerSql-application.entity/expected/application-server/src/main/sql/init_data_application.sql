/*
Ne pas décommenter ce code. Copier ce code d'exemple dans les balises User Code en fin de fichier.


alter table AW_COMPETENCE drop constraint AW_COMPETENCE_CK1_1;
alter table AW_COMPETENCEDISPOSE drop constraint AW_COMPETENCEDISPOSE_FK1_1;
alter table AW_COMPETENCEDISPOSE drop constraint AW_COMPETENCEDISPOSE_FK1_2;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_1;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_2;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_3;
alter table AW_ADRESSE_POSTALE drop constraint ADRESSE_POSTALE_FK1_1;
alter table AW_PAYS drop constraint PAYS_FK1_1;



insert into AW_GRADE (GRADE_ID, LIBELLE, TRIGRAMME, XDMAJ, XTOPSUP)
values (1, 'S', 'S', current_date, '0');

insert into AW_COMPETENCE (COMPETENCE_ID, LIBELLE, TYPECOMPETENCE, XDMAJ, XTOPSUP)
values (1, 'S', 'TYPECOMP1', current_date, '0');
insert into AW_PERSONNE (PERSONNE_ID, NOM, PRENOM, CIVIL, DATENAISSANCE, SALAIRE, GRADE_ID, PERSONNE_1_ID, PERSONNE_2_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', false, current_date, 0, 1, 1, 1, current_date, '0');
insert into AW_ADRESSE_POSTALE (ADRESSE_POSTALE_ID, RUE, VILLE, CP, PERSONNE_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', 'S', 1, current_date, '0');
insert into AW_PAYS (PAYS_ID, NOM, CAPITALE, PERSONNE_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', 1, current_date, '0');
insert into AW_COMPETENCEDISPOSE (COMPETENCE_ID, PERSONNE_ID, XTOPSUP, XDMAJ)
values (1, 1, '0', current_date);


alter table AW_COMPETENCE add constraint AW_COMPETENCE_CK1_1 CHECK (TYPECOMPETENCE IN ('TYPECOMP1','TYPECOMP2','TYPECOMP3'));
alter table AW_COMPETENCEDISPOSE add constraint AW_COMPETENCEDISPOSE_FK1_1 foreign key (COMPETENCE_ID) references AW_COMPETENCE (COMPETENCE_ID);
alter table AW_COMPETENCEDISPOSE add constraint AW_COMPETENCEDISPOSE_FK1_2 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_1 foreign key (GRADE_ID) references AW_GRADE (GRADE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_2 foreign key (PERSONNE_1_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_3 foreign key (PERSONNE_2_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_ADRESSE_POSTALE add constraint ADRESSE_POSTALE_FK1_1 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PAYS add constraint PAYS_FK1_1 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);


*/

/*
Start of user code init data
*/


/****************************************************************/
/* Constraints                                                  */
/****************************************************************/
alter table AW_COMPETENCE drop constraint AW_COMPETENCE_CK1_1;
alter table AW_COMPETENCEDISPOSE drop constraint AW_COMPETENCEDISPOSE_FK1_1;
alter table AW_COMPETENCEDISPOSE drop constraint AW_COMPETENCEDISPOSE_FK1_2;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_1;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_2;
alter table AW_PERSONNE drop constraint PERSONNE_FK1_3;
alter table AW_ADRESSE_POSTALE drop constraint ADRESSE_POSTALE_FK1_1;
alter table AW_PAYS drop constraint PAYS_FK1_1;



insert into AW_GRADE (GRADE_ID, LIBELLE, TRIGRAMME, XDMAJ, XTOPSUP)
values (1, 'S', 'S', current_date, '0');

insert into AW_COMPETENCE (COMPETENCE_ID, LIBELLE, TYPECOMPETENCE, XDMAJ, XTOPSUP)
values (1, 'S', 'TYPECOMP1', current_date, '0');
insert into AW_PERSONNE (PERSONNE_ID, NOM, PRENOM, CIVIL, DATENAISSANCE, SALAIRE, GRADE_ID, PERSONNE_1_ID, PERSONNE_2_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', false, current_date, 0, 1, 1, 1, current_date, '0');
insert into AW_ADRESSE_POSTALE (ADRESSE_POSTALE_ID, RUE, VILLE, CP, PERSONNE_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', 'S', 1, current_date, '0');
insert into AW_PAYS (PAYS_ID, NOM, CAPITALE, PERSONNE_ID, XDMAJ, XTOPSUP)
values (1, 'S', 'S', 1, current_date, '0');
insert into AW_COMPETENCEDISPOSE (COMPETENCE_ID, PERSONNE_ID, XTOPSUP, XDMAJ)
values (1, 1, '0', current_date);

/****************************************************************/
/* Constraints                                                  */
/****************************************************************/
alter table AW_COMPETENCE add constraint AW_COMPETENCE_CK1_1 CHECK (TYPECOMPETENCE IN ('TYPECOMP1','TYPECOMP2','TYPECOMP3'));
alter table AW_COMPETENCEDISPOSE add constraint AW_COMPETENCEDISPOSE_FK1_1 foreign key (COMPETENCE_ID) references AW_COMPETENCE (COMPETENCE_ID);
alter table AW_COMPETENCEDISPOSE add constraint AW_COMPETENCEDISPOSE_FK1_2 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_1 foreign key (GRADE_ID) references AW_GRADE (GRADE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_2 foreign key (PERSONNE_1_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PERSONNE add constraint PERSONNE_FK1_3 foreign key (PERSONNE_2_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_ADRESSE_POSTALE add constraint ADRESSE_POSTALE_FK1_1 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);
alter table AW_PAYS add constraint PAYS_FK1_1 foreign key (PERSONNE_ID) references AW_PERSONNE (PERSONNE_ID);


/*
End of user code
*/
