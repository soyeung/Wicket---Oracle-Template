prompt create view app_data.v_adm_rd_utr

create or replace view app_data.v_adm_rd_utr
as
select
       uty.uty_id
     , uty.uty_name
     , dbrl.dbrl_id
     , dbrl.dbrl_code
  from 
            app_data.app_user_type      uty
       join app_data.app_user_type_role utr  on uty.uty_id  = utr.uty_id
       join app_data.db_role            dbrl on utr.dbrl_id = dbrl.dbrl_id
  with
       read only
/