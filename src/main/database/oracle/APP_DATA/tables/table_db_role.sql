prompt creating table db_role

create sequence app_data.sq_dbrl
/

create table app_data.db_role
(
  dbrl_id              int                                      not null
, dbrl_code            varchar2(30   char)                      not null
, dbrl_name            varchar2(250  char)                      not null
, dbrl_is_user_visible varchar2(1    char) default 'Y'          not null
, dbrl_order           int                 default 1            not null
, dbrlc_id             int                                      not null
, dbrl_created_by      varchar2(30 char)   default user         not null
, dbrl_created_date    timestamp           default systimestamp not null
, dbrl_updated_by      varchar2(30 char)   default user         not null
, dbrl_updated_date    timestamp           default systimestamp not null
, constraint pk_dbrl           primary key (dbrl_id)
, constraint fk_dbrlc_dbrl     foreign key (dbrlc_id) references app_data.db_role_category (dbrlc_id)
, constraint uk_dbrl_code           unique (dbrl_code)
, constraint ck_dbrl_code_case       check (dbrl_code = upper(dbrl_code))
, constraint ck_dbrl_is_user_visible check (dbrl_is_user_visible in ('Y', 'N'))
)
cluster app_data.cl_db_role (dbrl_id)
/

create index app_data.i_dbrl_dbrlc on app_data.db_role ( dbrlc_id )
/