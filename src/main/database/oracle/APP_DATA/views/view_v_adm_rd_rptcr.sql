prompt create view app_data.v_adm_rd_rptcr

create or replace view app_data.v_adm_rd_rptcr
as
select
       rptc.rptc_id
     , rptc.rptc_name
     , rpt.rpt_id
     , rpt.rpt_name
  from 
            app_data.report_category        rptc
       join app_data.report_category_report rptcr on rptc.rptc_id = rptcr.rptc_id
       join app_data.report                 rpt   on rptcr.rpt_id = rpt.rpt_id
  with
       read only
/