prompt creating table manageable_db_role

create table app_data.manageable_db_role
(
  dbrl_id           int                                    not null
, dbrl_created_by   varchar2(30 char) default user         not null
, dbrl_created_date timestamp         default systimestamp not null
, constraint pk_mdbrl primary key (dbrl_id)
, constraint fk_mdbrl_dbrl foreign key (dbrl_id) references app_data.db_role (dbrl_id)
)
cluster app_data.cl_db_role (dbrl_id)
/