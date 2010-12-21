prompt creating table ref_data_structure_link

create table app_data.ref_data_structure_link
(
  rds_id              int                                    not null
, rds_child_id        int                                    not null
, rds_created_by      varchar2(30 char) default user         not null
, rds_created_date    timestamp         default systimestamp not null
, constraint pk_rdsl      primary key (rds_id, rds_child_id)
, constraint fk_rdsl_rds  foreign key (rds_id)                     references app_data.ref_data_structure   (rds_id)
, constraint fk_rdsl_rdsd foreign key (rds_child_id)               references app_data.subdiv_ref_data      (rds_id)
, constraint ck_rdsl      check       (rds_id != rds_child_id)
)
cluster app_data.cl_ref_data(rds_id)
/

create index app_data.i_rdsl_rdsd_child on app_data.ref_data_structure_link (rds_child_id)
/