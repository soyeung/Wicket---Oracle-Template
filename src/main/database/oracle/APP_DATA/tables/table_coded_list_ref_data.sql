prompt creating table coded_list_ref_data

create table app_data.coded_list_ref_data
(
  rds_id                    int                                    not null
, rds_id_sequence           varchar2(30 char)                      not null
, rds_created_by            varchar2(30 char) default user         not null
, rds_created_date          timestamp         default systimestamp not null
, constraint pk_clrds     primary key (rds_id)
, constraint fk_clrds_rds foreign key (rds_id) references app_data.ref_data_structure (rds_id)
, constraint uk_clrds_id_sequence unique (rds_id_sequence)
, constraint ck_crds_id_sequence_case check (rds_id_sequence = upper(rds_id_sequence))
)
cluster app_data.cl_ref_data ( rds_id )
/