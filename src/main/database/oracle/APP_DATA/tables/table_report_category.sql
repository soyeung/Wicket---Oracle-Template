prompt creating table report_category

create sequence app_data.sq_rptc
/

create table app_data.report_category
(
  rptc_id              int                                     not null
, rptc_name            varchar2(300 char)                      not null
, rptc_is_user_visible varchar2(1   char) default 'Y'          not null
, rptc_order           int                default 0            not null
, rptc_created_by      varchar2(30  char) default user         not null
, rptc_created_date    timestamp          default systimestamp not null
, rptc_updated_by      varchar2(30  char) default user         not null
, rptc_updated_date    timestamp          default systimestamp not null
, constraint pk_rptc primary key (rptc_id)
)
/

create unique index app_data.uk_rptc_name on app_data.report_category(app_utility.pk_string_utility.fn_reduce_string(rptc_name))
/