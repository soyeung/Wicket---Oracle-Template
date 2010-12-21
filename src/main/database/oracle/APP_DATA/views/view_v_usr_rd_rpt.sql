prompt creating view v_usr_rd_rpt (for table app_data.report)

create or replace view app_data.v_usr_rd_rpt
as 
select 
       rpt.rpt_id
     , rpt.rpt_name
     , rpt.rpt_page
     , rpt.dbrl_id
     , rpt.rpt_order
     , rpt.rpt_created_by
     , rpt.rpt_created_date
  from 
       app_data.report rpt
 where 
       rpt.rpt_is_user_visible = 'Y'
  with
       read only
/