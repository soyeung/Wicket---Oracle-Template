prompt creating view v_adm_rd_dbrlc (for table db_role_category)

create or replace view app_data.v_adm_rd_dbrlc
as 
select 
       dbrlc.dbrlc_id
     , dbrlc.dbrlc_name
     , dbrlc.dbrlc_is_user_visible
     , dbrlc.dbrlc_order
     , dbrlc.dbrlc_created_by
     , dbrlc.dbrlc_created_date
     , dbrlc.dbrlc_updated_date
  from 
       app_data.db_role_category dbrlc
  with
       read only
/