/*
	OJO: Las vistas de Jira están filtradas por la categoría 10000 (Servicios Centrales /
 */

CREATE OR REPLACE VIEW issue AS
	select i.id as id,
				 concat(p.pkey,'-',i.issuenum) issuekey,
				 ur.id as reporter_id,
				 assi.id as assignee_id,
				 i.summary as summary,
				 i.description as description,
				 i.project as project_id,
				 i.created as created,
				 i.updated as updated,
				 i.duedate as duedate,
				 i.issuestatus as status_id,
				 i.issuetype as issuetype_id,
				 i.priority as priority_id,
				 IFNULL(i.timeestimate,0) as timeestimate,
				 IFNULL(i.timeoriginalestimate,0) as timeoriginalestimate,
				 IFNULL(i.timespent,0) as timespent,
				 re.pname as resolution,
				 i.resolutiondate as resolutiondate,
		     cat.SINK_NODE_ID as projectcategory_id,
				 cf_cencos.stringvalue as centro_de_coste
	from jiradb.jiraissue i
		inner join jiradb.project p ON p.id = i.project
		left join jiradb.nodeassociation cat ON (cat.SOURCE_NODE_ID = i.project and
		ASSOCIATION_TYPE='ProjectCategory')
		left join jiradb.cwd_user ur ON ur.user_name = i.reporter
		left join jiradb.cwd_user assi ON assi.user_name = i.assignee
		left join jiradb.resolution re ON re.id = i.resolution
		left join jiradb.customfieldvalue cf_cencos ON cf_cencos.issue = i.id and cf_cencos.customfield = 10200;

CREATE OR REPLACE VIEW rfc AS
	select i.id as id,
				 concat(p.pkey,'-',i.issuenum) issuekey,
				 ur.id as reporter_id,
				 assi.id as assignee_id,
				 i.summary as summary,
				 i.description as description,
				 i.created as created,
				 i.updated as updated,
				 i.duedate as duedate,
				 i.issuestatus as status_id,
		     i.issuetype as issuetype_id,
				 i.priority as priority_id,
				 IFNULL(i.timeestimate,0) as timeestimate,
				 IFNULL(i.timeoriginalestimate,0) as timeoriginalestimate,
				 IFNULL(i.timespent,0) as timespent,
				 re.pname as resolution,
				 i.resolutiondate as resolutiondate,
				 FLOOR(cf_pp.numbervalue) project_id,
				 cf_fid.datevalue f_inicio_desa,
				 cf_ffd.datevalue f_fin_desa,
				 cf_fit.datevalue f_inicio_test,
				 cf_fft.datevalue f_fin_test,
				 cf_fpp.datevalue f_paso_prod,
				 cf_planpru.stringvalue plan_pruebas,
				 cf_obser.textvalue observaciones,
				 cf_planprod.textvalue plan_paso_prod,
				 cf_planatras.textvalue plan_marcha_atras,
				 cf_cencos.stringvalue as centro_de_coste
	from jiradb.jiraissue i
		inner join jiradb.project p ON p.id = i.project
		left join jiradb.cwd_user ur ON ur.user_name = i.reporter
		left join jiradb.cwd_user assi ON assi.user_name = i.assignee
		left join jiradb.resolution re ON re.id = i.resolution
		left join jiradb.customfieldvalue cf_pp ON cf_pp.issue = i.id and cf_pp.customfield = 10311
		left join jiradb.customfieldvalue cf_fid ON cf_fid.issue = i.id and cf_fid.customfield = 10312
		left join jiradb.customfieldvalue cf_ffd ON cf_ffd.issue = i.id and cf_ffd.customfield = 10313
		left join jiradb.customfieldvalue cf_fit ON cf_fit.issue = i.id and cf_fit.customfield = 10314
		left join jiradb.customfieldvalue cf_fft ON cf_fft.issue = i.id and cf_fft.customfield = 10315
		left join jiradb.customfieldvalue cf_fpp ON cf_fpp.issue = i.id and cf_fpp.customfield = 10309
		left join jiradb.customfieldvalue cf_planpru ON cf_planpru.issue = i.id and cf_planpru.customfield = 10325
		left join jiradb.customfieldvalue cf_obser ON cf_obser.issue = i.id and cf_obser.customfield = 10320
		left join jiradb.customfieldvalue cf_planprod ON cf_planprod.issue = i.id and cf_planprod.customfield = 10322
		left join jiradb.customfieldvalue cf_planatras ON cf_planatras.issue = i.id and cf_planatras.customfield = 10323
		left join jiradb.customfieldvalue cf_cencos ON cf_cencos.issue = i.id and cf_cencos.customfield = 10200
	where p.id = 10210;
;

CREATE OR REPLACE VIEW rfcequipodesarrollo AS
	select cfv.id, cfv.issue as rfc_id, u.id as user_id
	from jiradb.customfieldvalue cfv
		left join jiradb.cwd_user u ON u.user_name = cfv.stringvalue
	where cfv.customfield = 10310;

CREATE OR REPLACE VIEW rfcequipocalidad AS
	select cfv.id, cfv.issue as rfc_id, u.id as user_id
	from jiradb.customfieldvalue cfv
		left join jiradb.cwd_user u ON u.user_name = cfv.stringvalue
	where cfv.customfield = 10319;


CREATE OR REPLACE VIEW rfcriesgos AS
	select cfv.id, cfv.issue as rfc_id, cfo.customvalue as name
	from jiradb.customfieldvalue cfv
		left join jiradb.customfieldoption cfo ON cfo.customfield = cfv.customfield and cfo.id = cfv.stringvalue
	where cfv.customfield = 10318;

CREATE OR REPLACE VIEW rfcissuelink AS
	select id,source as rfc_id,DESTINATION issue_id
	from jiradb.issuelink
	where source in (select id from rfc);


CREATE OR REPLACE VIEW status AS
	SELECT id,pname as name from jiradb.issuestatus s;

CREATE OR REPLACE VIEW type AS
	SELECT id,pname as name from jiradb.issuetype;

CREATE OR REPLACE VIEW priority AS
	SELECT id,pname as name from jiradb.priority;


CREATE OR REPLACE VIEW project AS
	select p.id,p.pkey as projectkey,p.pname as name,p.lead as responsable
		,cat.SINK_NODE_ID as projectcategory_id
	from jiradb.project p
		left join jiradb.nodeassociation cat ON (cat.SOURCE_NODE_ID = p.id and
																						 ASSOCIATION_TYPE='ProjectCategory');

CREATE OR REPLACE VIEW component AS
	select c.id as id,
				 c.cname as name,
				 c.project as project_id
	from jiradb.component c;

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
	from jiradb.projectversion v;


CREATE OR REPLACE VIEW issueversion AS
	select concat(na.SOURCE_NODE_ID,v.id) as id,
				 na.SOURCE_NODE_ID as issue_id,
				 v.id as version_id
	from jiradb.nodeassociation na INNER JOIN jiradb.projectversion v ON v.id = na.SINK_NODE_ID
	where na.ASSOCIATION_TYPE='IssueFixVersion';

CREATE OR REPLACE VIEW worklog AS
select w.id as id, 
au.id as author_id,
aup.id as updateauthor_id,
worklogbody as comment,
w.created as created,
w.updated as updated,
w.startdate as started,
IFNULL(w.timeworked,0) as timespentseconds,
w.issueid as issue_id
from jiradb.worklog w 
inner join jiradb.cwd_user au ON au.user_name = w.author
left join jiradb.cwd_user aup ON aup.user_name = w.updateauthor;

CREATE OR REPLACE VIEW scuser AS
	select id,user_name,display_name,email_address from jiradb.cwd_user u
	where u.id in (select ms.child_id from jiradb.cwd_membership ms where ms.parent_id = 10214)
				and u.id <> 10201;

CREATE OR REPLACE VIEW user AS
	select id,user_name,display_name,email_address from jiradb.cwd_user u;

