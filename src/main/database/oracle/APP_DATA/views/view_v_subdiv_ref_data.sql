prompt creating view v_subdiv_ref_data

create or replace view app_data.v_subdiv_ref_data
as
select
       rds.rds_id                          rds_id
     , rds.rds_table_name                  rds_table_name
     , rds.rds_alias                       rds_alias
     , rds.rds_column_prefix               rds_column_prefix
     , rds.rds_owning_schema               rds_owning_schema
     , upper('V_ADM_RD_' || rds.rds_alias) rds_admin_view
     , upper('V_USR_RD_' || rds.rds_alias) rds_user_view 
  from
            app_data.ref_data_structure rds
       join app_data.subdiv_ref_data    rdsd on rds.rds_id = rdsd.rds_id
  with
       read only
/