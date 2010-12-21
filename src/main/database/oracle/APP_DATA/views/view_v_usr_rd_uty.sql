prompt creating view v_usr_rd_uty (for table app_user_type)

create or replace view app_data.v_usr_rd_uty
as 
select 
       uty.uty_id
     , uty.uty_code
     , uty.uty_name
     , uty.uty_order
     , uty.uty_created_by
     , uty.uty_created_date
  from 
       app_data.app_user_type uty
 where 
       uty.uty_is_user_visible = 'Y'
  with
       read only
/