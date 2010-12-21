prompt creating table ref_data_structure

create sequence app_data.sq_rds
/

create table app_data.ref_data_structure
(
  rds_id                 int                                     not null
, rds_description        varchar2(100 char)                      not null
, rds_table_name         varchar2(30  char)                      not null
, rds_alias              varchar2(30  char)                      not null
, rds_column_prefix      varchar2(30  char)                      not null
, rds_owning_schema      varchar2(30  char)                      not null
, rds_created_by         varchar2(30  char) default user         not null
, rds_created_date       timestamp          default systimestamp not null
, constraint pk_rds            primary key (rds_id)
, constraint uk_rds_alias      unique (rds_alias)
, constraint uk_rds_table_name unique (rds_table_name)
, constraint ck_rds_table_name_case    check(rds_table_name = upper(rds_table_name))
, constraint ck_rds_alias_case         check(rds_alias = upper(rds_alias))
, constraint ck_rds_column_prefix_case check(rds_column_prefix = upper(rds_column_prefix))
, constraint ck_rds_owning_schema_case check(rds_owning_schema = upper(rds_owning_schema))
)
cluster app_data.cl_ref_data(rds_id)
/