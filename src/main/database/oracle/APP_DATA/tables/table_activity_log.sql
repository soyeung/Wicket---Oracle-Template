prompt creating table activity_log

-- ---------------------------------------------------------------------
-- definition of activity_log table
-- ---------------------------------------------------------------------

create sequence app_data.sq_act
/

create table app_data.activity_log
(
  act_id              int                                           not null
, aur_id              int                                           not null
, act_parameter_info  varchar2(4000 char)      default 'n/a'        not null
, act_package_owner   varchar2(30   char)                           not null
, act_package_name    varchar2(30   char)                           not null
, act_procedure_name  varchar2(30   char)                           not null
, act_elapsed_time    interval day to second                        not null
, act_completion_time timestamp                default systimestamp not null
, constraint pk_act primary key (act_id)
, constraint fk_act_aur foreign key (aur_id) references app_data.app_user (aur_id)
)
pctfree 0
/

create index app_data.i_act_aur on app_data.activity_log (aur_id)
/