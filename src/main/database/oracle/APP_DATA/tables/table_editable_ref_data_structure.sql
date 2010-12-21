prompt creating table editable_ref_data_structure

create table app_data.editable_ref_data_structure
(
  rds_id           int                                    not null
, rds_created_by   varchar2(30 char) default user         not null
, rds_created_date timestamp         default systimestamp not null
, constraint pk_erds primary key (rds_id)
, constraint fk_erds_rds foreign key (rds_id) references app_data.ref_data_structure(rds_id)
)
cluster app_data.cl_ref_data(rds_id)
/