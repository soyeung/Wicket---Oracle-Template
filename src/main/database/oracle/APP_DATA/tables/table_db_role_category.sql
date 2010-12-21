prompt creating table db_role_category

create sequence app_data.sq_dbrlc
/

create table app_data.db_role_category
(
  dbrlc_id              int                                      not null
, dbrlc_name            varchar2( 50 char )                      not null
, dbrlc_is_user_visible varchar2( 1  char ) default 'Y'          not null
, dbrlc_order           int                 default 1            not null
, dbrlc_created_by      varchar2( 30 char ) default user         not null
, dbrlc_created_date    timestamp           default systimestamp not null
, dbrlc_updated_by      varchar2( 30 char ) default user         not null
, dbrlc_updated_date    timestamp           default systimestamp not null
, constraint pk_dbrlc primary key ( dbrlc_id )
)
/

create unique index app_data.uk_dbrlc_name on app_data.db_role_category (app_utility.pk_string_utility.fn_reduce_string(dbrlc_name))
/