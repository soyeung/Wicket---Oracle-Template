prompt creating view v_usr_rd_dbrlc (for table db_role_category)

create or replace view app_data.v_usr_rd_dbrlc
as 
select 
       dbrlc.dbrlc_id
     , dbrlc.dbrlc_name
     , dbrlc.dbrlc_order
     , dbrlc.dbrlc_created_by
     , dbrlc.dbrlc_created_date
  from 
       app_data.db_role_category dbrlc
 where 
       dbrlc.dbrlc_is_user_visible = 'Y'
  with
       read only
/