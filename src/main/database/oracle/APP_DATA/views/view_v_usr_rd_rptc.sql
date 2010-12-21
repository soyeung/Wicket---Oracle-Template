prompt creating view v_usr_rd_rptc (for table app_data.report_category)

create or replace view app_data.v_usr_rd_rptc
as 
select 
       rptc.rptc_id
     , rptc.rptc_name
     , rptc.rptc_order
     , rptc.rptc_created_by
     , rptc.rptc_created_date
  from 
       app_data.report_category rptc
 where 
       rptc.rptc_is_user_visible = 'Y'
  with
       read only
/