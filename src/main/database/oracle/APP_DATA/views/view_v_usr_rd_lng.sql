prompt creating view v_usr_rd_lng (for table language)

create or replace view app_data.v_usr_rd_lng
as 
select 
       lng.lng_id
     , lng.lng_code
     , lng.lng_name
     , lng.lng_order
     , lng.lng_created_by
     , lng.lng_created_date
  from 
       app_data.language lng
 where 
       lng.lng_is_user_visible = 'Y'
  with
       read only
/