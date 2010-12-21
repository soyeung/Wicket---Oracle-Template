prompt creating view v_app_user_role

create or replace view app_data.v_app_user_role
as
select
       aur.aur_id            aur_id
     , aur.aur_username      aur_username
     , dbrl.dbrl_id          dbrl_id
     , dba_rp.granted_role   dbrl_code
  from 
            app_user           aur
       join sys.dba_role_privs dba_rp on aur.aur_username    = dba_rp.grantee
       join app_data.db_role   dbrl   on dba_rp.granted_role = dbrl.dbrl_code
  with
       read only
/