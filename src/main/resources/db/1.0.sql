/*
	OJO: Las vistas de Jira están filtradas por la categoría 10000 (Servicios Centrales /
 */

CREATE OR REPLACE VIEW issue AS
	select i.id as id,
				 concat(p.pkey,'-',i.issuenum) issuekey,
				 i.reporter as reporter,
				 ur.display_name as reporter_desc,
				 ur.email_address as reporter_email,
				 i.assignee as assignee,
				 assi.display_name as assignee_desc,
				 assi.email_address as assignee_email,
				 i.summary as summary,
				 i.description as description,
				 i.project as project_id,
				 s.id as status_id,
				 s.pname as status,
				 i.created as created,
				 i.updated as updated,
				 i.duedate as duedate,
				 it.id as issuetype_id,
				 it.pname as issuetype,
				 pr.id as priority_id,
				 pr.pname as priority,
				 IFNULL(i.timeestimate,0) as timeestimate,
				 IFNULL(i.timeoriginalestimate,0) as timeoriginalestimate,
				 IFNULL(i.timespent,0) as timespent,
				 re.pname as resolution,
				 i.resolutiondate as resolutiondate,
				 cf.stringvalue as centrocoste
	from jiradb.jiraissue i
		inner join jiradb.project p ON p.id = i.project and p.id in (select SOURCE_NODE_ID from jiradb.nodeassociation
		where ASSOCIATION_TYPE='ProjectCategory' and SINK_NODE_ID in(10000))
		left join jiradb.cwd_user ur ON ur.user_name = i.reporter
		left join jiradb.cwd_user assi ON assi.user_name = i.assignee
		left join jiradb.resolution re ON re.id = i.resolution
		left join jiradb.customfieldvalue cf ON (cf.issue = i.id and customfield = 10200)
		inner join jiradb.issuestatus s ON s.id = i.issuestatus
		inner join jiradb.issuetype it ON it.id = i.issuetype
		inner join jiradb.priority pr ON pr.id = i.priority;

CREATE OR REPLACE VIEW project AS
	select p.id,p.pkey as projectkey,p.pname as name,p.lead as responsable
	from jiradb.project p where p.id in (select SOURCE_NODE_ID from jiradb.nodeassociation
	where ASSOCIATION_TYPE='ProjectCategory' and SINK_NODE_ID in(10000));

CREATE OR REPLACE VIEW component AS
	select c.id as id,
				 c.cname as name,
				 c.project as project_id
	from jiradb.component c where c.project in (select SOURCE_NODE_ID from jiradb.nodeassociation
	where ASSOCIATION_TYPE='ProjectCategory' and SINK_NODE_ID in(10000));

CREATE OR REPLACE VIEW issuecomponent AS
	select concat(na.SOURCE_NODE_ID,c.id) as id,
				 na.SOURCE_NODE_ID as issue_id,
				 c.id as component_id
	from jiradb.nodeassociation na INNER JOIN jiradb.component c ON c.id = na.SINK_NODE_ID
	where na.ASSOCIATION_TYPE='IssueComponent';

CREATE OR REPLACE VIEW version AS
	select v.id as id,
				 v.vname as name,
				 v.description as description,
				 v.startdate as startdate,
				 v.releasedate as releasedate,
				 v.project as project_id
	from jiradb.projectversion v
	where v.project in (select SOURCE_NODE_ID from jiradb.nodeassociation
	where ASSOCIATION_TYPE='ProjectCategory' and SINK_NODE_ID in(10000));


CREATE OR REPLACE VIEW issueversion AS
	select concat(na.SOURCE_NODE_ID,v.id) as id,
				 na.SOURCE_NODE_ID as issue_id,
				 v.id as version_id
	from jiradb.nodeassociation na INNER JOIN jiradb.projectversion v ON v.id = na.SINK_NODE_ID
	where na.ASSOCIATION_TYPE='IssueFixVersion';

CREATE OR REPLACE VIEW worklog AS
select w.id as id, 
w.author as author,
au.display_name as author_desc,
au.email_address as author_email,
w.updateauthor as updateauthor,
aup.display_name as updateauthor_desc,
aup.email_address as updateauthor_email,
worklogbody as comment,
w.created as created,
w.updated as updated,
w.startdate as started,
IFNULL(w.timeworked,0) as timespentseconds,
w.issueid as issue_id
from jiradb.worklog w 
inner join jiradb.cwd_user au ON au.user_name = w.author
left join jiradb.cwd_user aup ON aup.user_name = w.updateauthor
inner join jiradb.jiraissue i ON i.id = w.issueid;


DROP TABLE IF EXISTS userauth CASCADE;

CREATE TABLE userauth (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL UNIQUE,
	rol INT NOT NULL DEFAULT 0
);

insert into userauth(user_id)
	(select id from jiradb.cwd_user u
	where u.id in (select ms.child_id from jiradb.cwd_membership ms where ms.parent_id = 10214)
				and u.id <> 10201)
;

insert into userauth(user_id,rol) values(
	(SELECT id FROM jiradb.cwd_user u where u.user_name = 'ahg'),100);

update userauth set rol = 10 where user_id in (10208,10336,10348,10398);


CREATE OR REPLACE VIEW user AS
	select u.id,u.user_name,u.display_name,u.email_address,ua.rol
	from jiradb.cwd_user u JOIN userauth ua ON ua.user_id = u.id;


DROP TABLE IF EXISTS issuesprint CASCADE;
DROP TABLE IF EXISTS riesgopredef CASCADE;
DROP TABLE IF EXISTS sprint CASCADE;
DROP TABLE IF EXISTS riesgo CASCADE;

DROP TABLE IF EXISTS logtrabajo CASCADE;
DROP TABLE IF EXISTS actividad CASCADE;

DROP TABLE IF EXISTS ausencia CASCADE;
DROP TABLE IF EXISTS issuesolicitud CASCADE;
DROP TABLE IF EXISTS equipo CASCADE;
DROP TABLE IF EXISTS planpruebas CASCADE;    	
DROP TABLE IF EXISTS comentarios CASCADE;
DROP TABLE IF EXISTS rfcsolicitud CASCADE;
DROP TABLE IF EXISTS solicitud CASCADE;
DROP TABLE IF EXISTS tiposolicitud CASCADE;    
DROP TABLE IF EXISTS estadosolicitud CASCADE;    
DROP TABLE IF EXISTS rfc CASCADE;


CREATE TABLE tiposolicitud (
	id INT NOT NULL PRIMARY KEY, 
	nombre VARCHAR(150) NOT NULL UNIQUE
);

insert into tiposolicitud(id,nombre) VALUES (1, 'Nuevo proyecto');
insert into tiposolicitud(id,nombre) VALUES (2,'Mantenimiento');
insert into tiposolicitud(id,nombre) VALUES (3,'Parche');

 CREATE TABLE estadosolicitud (
	id INT NOT NULL PRIMARY KEY,  
	nombre VARCHAR(150) NOT NULL UNIQUE
);

insert into estadosolicitud(id,nombre) VALUES (0,'Pendiente asignación equipo');
insert into estadosolicitud(id,nombre) VALUES (10,'Pendiente planificación desarrollo');
insert into estadosolicitud(id,nombre) VALUES (20,'Desarrollando');
insert into estadosolicitud(id,nombre) VALUES (30,'Pendiente planificación pruebas');
insert into estadosolicitud(id,nombre) VALUES (40,'Probando');
insert into estadosolicitud(id,nombre) VALUES (50,'Pendiente de paso a producción');
insert into estadosolicitud(id,nombre) VALUES (60,'En producción');
insert into estadosolicitud(id,nombre) VALUES (70,'Cerrado');


CREATE TABLE solicitud (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	tiposolicitud_id INT NOT NULL,
	estadosolicitud_id INT NOT NULL,
	parada INT DEFAULT 0,
	titulo VARCHAR(512) NOT NULL UNIQUE,
	descripcion TEXT NOT NULL,
	fecha_inicio_desarrollo DATE,
	fecha_fin_desarrollo DATE,
	fecha_inicio_pruebas DATE,
	fecha_fin_pruebas DATE,
	informecalidad TEXT,
	solucion TEXT,
	plan_paso_produccion TEXT,
	plan_marcha_atras TEXT,
	fecha_implantacion DATE,
	observaciones TEXT,

	CONSTRAINT fk_solicitud_tiposolicitud FOREIGN KEY (tiposolicitud_id) REFERENCES tiposolicitud(id),
	CONSTRAINT fk_solicitud_estadosolicitud FOREIGN KEY (estadosolicitud_id) REFERENCES estadosolicitud(id)
);

CREATE INDEX solicitud_titulo_idx ON solicitud (titulo);
CREATE INDEX solicitud_parado_idx ON solicitud (parada);

CREATE INDEX solicitud_finides_idx ON solicitud (fecha_fin_desarrollo,fecha_inicio_desarrollo);
CREATE INDEX solicitud_finipru_idx ON solicitud (fecha_fin_pruebas,fecha_inicio_pruebas);

CREATE TABLE comentarios (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fecha DATETIME NOT NULL,
	comentario TEXT NOT NULL,
	solicitud_id INT NOT NULL,

	CONSTRAINT fk_comentarios_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud(id)
);

CREATE TABLE equipo (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	solicitud_id INT NOT NULL,

	CONSTRAINT fk_equipo_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud(id),
	CONSTRAINT equipo_solicituduser_idx UNIQUE (solicitud_id,user_id)
);

CREATE INDEX equipo_user_idx ON equipo (user_id);

CREATE TABLE issuesolicitud (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	solicitud_id INT NOT NULL,
	issue_id INT NOT NULL,
	
  CONSTRAINT fk_issuesolicitud_idx FOREIGN KEY (solicitud_id) REFERENCES solicitud(id),
	CONSTRAINT issuesolicitud_u_idx UNIQUE (issue_id,solicitud_id)
);

CREATE TABLE ausencia (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	user_id INT NOT NULL,
	fechainicio DATE NOT NULL,
  fechafin DATE NOT NULL,
	tipo INT NOT NULL DEFAULT 0
);

CREATE INDEX ausencia_user_idx ON ausencia (user_id);

CREATE INDEX ausencia_fechas_idx ON ausencia (fechainicio,fechafin);

CREATE TABLE actividad (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	solicitud_id INT NOT NULL,
	fecha DATE NOT NULL,
	log VARCHAR(1000),
	CONSTRAINT fk_actividad_solicitud_idx FOREIGN KEY (solicitud_id) REFERENCES solicitud(id)
);

CREATE INDEX actividad_user_idx ON actividad (user_id);


CREATE INDEX actividad_idx ON actividad (fecha);

CREATE TABLE logtrabajo (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL,
	solicitud_id INT NOT NULL,
	fecha DATE NOT NULL,
	log TEXT NOT NULL,
	tiempo INT NOT NULL DEFAULT 0,

	CONSTRAINT fk_logtrabajo_solicitud_idx FOREIGN KEY (solicitud_id) REFERENCES solicitud(id)
);

CREATE INDEX logtrabajo_user_idx ON logtrabajo (user_id);


CREATE INDEX logtrabajo_idx ON logtrabajo (fecha);

COMMIT;  