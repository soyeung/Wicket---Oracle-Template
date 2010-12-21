prompt creating view v_adm_rd_rpt (for table app_data.report)

create or replace view app_data.v_adm_rd_rpt
as 
select 
       rpt.rpt_id
     , rpt.rpt_name
     , rpt.rpt_is_user_visible
     , rpt.rpt_order
     , rpt.rpt_created_by
     , rpt.rpt_created_date
     , rpt.rpt_updated_date
  from 
       app_data.report rpt
  with
       read only
/