prompt creating table list_intersection_ref_data

create table app_data.list_intersection_ref_data
(
  rds_id                    int                                    not null
, rds_parent_id             int                                    not null
, rds_child_id              int                                    not null
, rds_created_by            varchar2(30 char) default user         not null
, rds_created_date          timestamp         default systimestamp not null
, constraint pk_ilrds      primary key (rds_id)
, constraint fk_ilrds_rds  foreign key (rds_id)        references app_data.ref_data_structure (rds_id)
, constraint fk_ilrds_rdsp foreign key (rds_parent_id) references app_data.ref_data_structure (rds_id)
, constraint fk_ilrds_rdsc foreign key (rds_child_id)  references app_data.ref_data_structure (rds_id)
, constraint ck_ilrds_id   check (rds_id != rds_parent_id and rds_id != rds_child_id and rds_parent_id != rds_child_id)
)
cluster app_data.cl_ref_data(rds_id)
/

create index app_data.i_ilrds_parent on app_data.list_intersection_ref_data (rds_parent_id)
/

create index app_data.i_ilrds_child  on app_data.list_intersection_ref_data (rds_child_id)
/