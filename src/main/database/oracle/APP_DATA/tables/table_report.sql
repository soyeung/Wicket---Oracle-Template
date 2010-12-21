prompt creating table report

create sequence app_data.sq_rpt
/

create table app_data.report
(
  rpt_id              int                                     not null
, rpt_name            varchar2(300 char)                      not null
, rpt_page            varchar2(500 char)                      not null
, rpt_is_user_visible varchar2(1   char) default 'Y'          not null
, rpt_order           int                default 1            not null
, dbrl_id             int                                     not null
, rpt_created_by      varchar2(30  char) default user         not null
, rpt_created_date    timestamp          default systimestamp not null
, rpt_updated_by      varchar2(30  char) default user         not null
, rpt_updated_date    timestamp          default systimestamp not null
, constraint pk_rpt primary key (rpt_id)
, constraint uk_rpt_dbrl unique (dbrl_id)
, constraint fk_rpt_dbrl foreign key (dbrl_id) references app_data.db_role (dbrl_id)
)
/

create unique index app_data.uk_rpt_name on app_data.report (app_utility.pk_string_utility.fn_reduce_string(rpt_name))
/