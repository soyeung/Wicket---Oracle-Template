prompt creating view v_adm_rd_dbrl (for table app_data.db_role)

create or replace view app_data.v_adm_rd_dbrl
as 
select 
       dbrl.dbrl_id
     , dbrl.dbrl_code
     , dbrl.dbrl_name
     , dbrl.dbrl_is_user_visible
     , dbrl.dbrl_order
     , dbrl.dbrl_created_by
     , dbrl.dbrl_created_date
     , dbrl.dbrl_updated_date
  from 
       app_data.db_role dbrl
  with
       read only
/