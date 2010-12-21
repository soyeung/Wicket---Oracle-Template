prompt creating view v_usr_rd_dbrl (for table app_data.db_role)

create or replace view app_data.v_usr_rd_dbrl
as 
select 
       dbrl.dbrl_id
     , dbrl.dbrl_code
     , dbrl.dbrl_name
     , dbrl.dbrl_order
     , dbrl.dbrl_created_by
     , dbrl.dbrl_created_date
  from 
       app_data.db_role dbrl
 where 
       dbrl.dbrl_is_user_visible = 'Y'
  with
       read only
/