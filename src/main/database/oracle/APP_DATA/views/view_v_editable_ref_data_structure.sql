prompt creating view v_editable_ref_data_structure

create or replace view app_data.v_editable_ref_data_structure
as
select
       vrds.rds_id  rds_id
  from 
            app_data.v_ref_data_structure        vrds
       join app_data.editable_ref_data_structure erds on vrds.rds_id = erds.rds_id
 where
       vrds.dbrl_code is not null
  with
       read only
/