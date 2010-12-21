prompt creating view v_usr_rd_aurp (for table app_user_profile)

create or replace view app_data.v_usr_rd_aurp
as 
select 
       aurp.aurp_id
     , aurp.aurp_code
     , aurp.aurp_name
     , aurp.aurp_order
     , aurp.aurp_created_by
     , aurp.aurp_created_date
  from 
       app_data.app_user_profile aurp
 where 
       aurp.aurp_is_user_visible = 'Y'
  with
       read only
/