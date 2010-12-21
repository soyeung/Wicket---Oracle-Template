prompt creating view v_adm_rd_uty (for table app_user_type)

create or replace view app_data.v_adm_rd_uty
as 
select 
       uty.uty_id
     , uty.uty_code
     , uty.uty_name
     , uty.uty_is_user_visible
     , uty.uty_order
     , uty.uty_created_by
     , uty.uty_created_date
     , uty.uty_updated_date
  from 
       app_data.app_user_type uty
  with
       read only
/