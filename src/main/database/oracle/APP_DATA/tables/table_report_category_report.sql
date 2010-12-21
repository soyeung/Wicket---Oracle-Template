prompt creating table report_category_report

create table app_data.report_category_report
(
  rptc_id            int                                    not null
, rpt_id             int                                    not null
, rptcr_created_by   varchar2(30 char) default user         not null
, rptcr_created_date timestamp         default systimestamp not null
, constraint pk_rptcr primary key (rptc_id, rpt_id)
, constraint fk_rptcr_rptc foreign key (rptc_id) references app_data.report_category (rptc_id)
, constraint fk_rptcr_rpt  foreign key (rpt_id)  references app_data.report          (rpt_id)
)
organization index
/

create index app_data.i_rptcr_rpt on app_data.report_category_report (rpt_id)
/