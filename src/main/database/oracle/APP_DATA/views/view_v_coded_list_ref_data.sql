prompt creating view v_coded_list_ref_data

create or replace view app_data.v_coded_list_ref_data
as
  select
         rds.rds_id               rds_id
       , rds.rds_description      rds_description
       , rds.rds_table_name       rds_table_name
       , rds.rds_alias            rds_alias
       , rds.rds_column_prefix    rds_column_prefix
       , rds.rds_owning_schema    rds_owning_schema
       , rds.rds_admin_view       rds_admin_view
       , rds.rds_user_view        rds_user_view
       , clrds.rds_id_sequence    rds_id_sequence
       , rds.rdt_code             rdt_code
    from
              app_data.v_ref_data_structure rds
         join app_data.coded_list_ref_data  clrds on rds.rds_id = clrds.rds_id
  with
       read only
/