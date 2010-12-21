prompt creating table report_category_link

create table app_data.report_category_link
(
  rptc_id               int                                    not null
, rptc_child_id         int                                    not null
, rptcl_created_by      varchar2(30 char) default user         not null
, rptcl_created_date    timestamp         default systimestamp not null
, constraint pk_rptcl        primary key (rptc_id, rptc_child_id)
, constraint fk_rptcl_rptc   foreign key (rptc_id)                 references app_data.report_category (rptc_id)
, constraint fk_rptcl_rptcsd foreign key (rptc_child_id)           references app_data.report_category (rptc_id)
, constraint ck_rptcl        check       (rptc_id != rptc_child_id)
)
organization index
/

create index app_data.i_rptcl_rptc_child on app_data.report_category_link (rptc_child_id)
/