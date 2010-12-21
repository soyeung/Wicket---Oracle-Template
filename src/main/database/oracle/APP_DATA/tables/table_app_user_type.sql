prompt creating table app_data.app_user_type

create sequence app_data.sq_uty
/

create table app_data.app_user_type
(
  uty_id              int                                    not null
, uty_code            varchar2(10 char)
, uty_name            varchar2(50 char)                      not null
, uty_is_user_visible varchar2(1  char) default 'Y'          not null
, uty_order           int               default 1            not null
, uty_created_by      varchar2(30 char) default user         not null
, uty_created_date    timestamp         default systimestamp not null
, uty_updated_by      varchar2(30 char) default user         not null
, uty_updated_date    timestamp         default systimestamp not null
, constraint pk_uty primary key (uty_id)
, constraint ck_uty_is_user_visible check (uty_is_user_visible in ('Y', 'N'))
, constraint uk_uty_code            unique (uty_code)
, constraint uk_uty_name            unique (uty_name)
)
cluster app_data.cl_app_user_type (uty_id)
/