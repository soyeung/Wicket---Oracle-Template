prompt creating view v_ref_data_structure

create or replace view app_data.v_ref_data_structure
as
select
       rds.rds_id                          rds_id
     , rds.rds_description                 rds_description
     , rds.rds_table_name                  rds_table_name
     , rds.rds_alias                       rds_alias
     , rds.rds_column_prefix               rds_column_prefix
     , rds.rds_owning_schema               rds_owning_schema
     , upper('V_ADM_RD_' || rds.rds_alias) rds_admin_view
     , upper('V_USR_RD_' || rds.rds_alias) rds_user_view 
     , (
         /* 
            determine from the data dictionary which role is meant to be used for management of the data structure.
            the responsibilities encompassed by roles should be orthogonal with respect to each other - therefore only 1 role
            should match. if not, we should consider reevaluating our system's roles.
         */
         select
                distinct
                         db_tpv.grantee
           from
                     sys.dba_tab_privs db_tpv
                join app_data.db_role  dbrl   on db_tpv.grantee = dbrl.dbrl_code
          where
                db_tpv.owner      = rds.rds_owning_schema
            and db_tpv.table_name = rds.rds_table_name
            and db_tpv.privilege  in ('INSERT', 'UPDATE')
       ) dbrl_code
     , case
         when (select 'Y' from app_data.list_ref_data              slrds where slrds.rds_id = rds.rds_id) = 'Y'
         then 'SPL'
         when (select 'Y' from app_data.coded_list_ref_data        clrds where clrds.rds_id = rds.rds_id) = 'Y'
         then 'CLT'
         when (select 'Y' from app_data.update_only_list_ref_data  ulrds where ulrds.rds_id = rds.rds_id) = 'Y'
         then 'ULT'
         when (select 'Y' from app_data.subdiv_ref_data            sdrds where sdrds.rds_id = rds.rds_id) = 'Y'
         then 'SDV'
         when (select 'Y' from app_data.list_intersection_ref_data ilrds where ilrds.rds_id = rds.rds_id) = 'Y'
         then 'ILT'
       end                                         rdt_code
     , nvl2(erds.rds_id, 'Y', 'N')                 rds_is_editable
  from
                 app_data.ref_data_structure          rds
       left join app_data.editable_ref_data_structure erds on rds.rds_id = erds.rds_id
  with
       read only
/