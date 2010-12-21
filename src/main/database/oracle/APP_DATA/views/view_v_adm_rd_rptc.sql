prompt creating view v_adm_rd_rptc (for table app_data.report_category)

create or replace view app_data.v_adm_rd_rptc
as 
select 
       rptc.rptc_id
     , rptc.rptc_name
     , rptc.rptc_is_user_visible
     , rptc.rptc_order
     , rptc.rptc_created_by
     , rptc.rptc_created_date
     , rptc.rptc_updated_date
  from 
       app_data.report_category rptc
  with
       read only
/