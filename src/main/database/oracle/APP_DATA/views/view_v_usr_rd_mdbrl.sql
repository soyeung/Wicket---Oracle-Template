prompt creating view app_data.v_usr_rd_mdbrl

create or replace view app_data.v_usr_rd_mdbrl
as 
select 
        dbrl.dbrl_id
      , dbrl.dbrl_code
      , dbrl.dbrl_name
      , dbrlc.dbrlc_id
      , dbrlc.dbrlc_name
      , dbrl.dbrl_order
      , dbrl.dbrl_created_by
      , dbrl.dbrl_created_date
   from 
             app_data.db_role            dbrl
        join app_data.manageable_db_role mdbrl on dbrl.dbrl_id  = mdbrl.dbrl_id
        join app_data.db_role_category   dbrlc on dbrl.dbrlc_id = dbrlc.dbrlc_id
  where
        dbrl.dbrl_is_user_visible = 'Y'
/