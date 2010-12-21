prompt creating table app_data.app_user_profile

create sequence app_data.sq_aurp
/

create table app_data.app_user_profile
(
  aurp_id              int                                    not null
, aurp_code            varchar2(30 char)                      not null
, aurp_name            varchar2(30 char)                      not null
, aurp_is_user_visible varchar2(1  char) default 'Y'          not null
, aurp_order           int               default 1            not null
, aurp_created_by      varchar2(30 char) default user         not null
, aurp_created_date    timestamp         default systimestamp not null
, aurp_updated_by      varchar2(30 char) default user         not null
, aurp_updated_date    timestamp         default systimestamp not null
, constraint pk_aurp           primary key (aurp_id)
, constraint ck_aurp_code_case check       (aurp_code = upper(aurp_code))
, constraint uk_aurp_code      unique      (aurp_code)
, constraint uk_aurp_name      unique      (aurp_name)
)
cluster app_data.cl_app_user_profile (aurp_id)
/