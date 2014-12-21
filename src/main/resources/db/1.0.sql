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


CREATE OR REPLACE VIEW rfc AS
	SELECT * from issue where issue.project_id = 10210;

CREATE OR REPLACE VIEW rfc_extend AS
	SELECT * from customfieldvalue where issue.project_id = 10210;


var cf = {
equipodesarrollo: 'customfield_10310',
proyecto: 'customfield_10311',
equipocalidad: 'customfield_10319',
f_ini_desa: 'customfield_10312',
f_fin_desa: 'customfield_10313',
f_ini_test: 'customfield_10314',
f_fin_test: 'customfield_10315',
f_paso_prod: 'customfield_10309',
plan_pruebas: 'customfield_10325',
observaciones: 'customfield_10320',
riesgos: 'customfield_10318',
planpasoprod: 'customfield_10322',
planmarchaatras: 'customfield_10323'

};

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

CREATE OR REPLACE VIEW user AS
	select id,user_name,display_name,email_address from jiradb.cwd_user u
	where u.id in (select ms.child_id from jiradb.cwd_membership ms where ms.parent_id = 10214)
				and u.id <> 10201;

