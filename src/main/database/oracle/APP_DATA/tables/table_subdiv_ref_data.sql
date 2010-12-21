prompt creating table subdiv_ref_data

create table app_data.subdiv_ref_data
(
  rds_id              int                                    not null
, rds_created_by      varchar2(30 char) default user         not null
, rds_created_date    timestamp         default systimestamp not null
, constraint pk_rfs primary key (rds_id)
, constraint fk_rfs_rds foreign key (rds_id) references app_data.ref_data_structure (rds_id)
)
cluster app_data.cl_ref_data(rds_id)
/