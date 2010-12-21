prompt register reference data structures with the reference data management facility

declare

    v_rds_id int;

    /**
      *
      */

    function fn_get_rds_id
    (
      p_table_alias in varchar2
    )
    return varchar2
    is

        parent_rds_id int;

    begin

        select
               r.rds_id into parent_rds_id
          from
               app_data.ref_data_structure r
         where
               r.rds_alias = p_table_alias
             ;

        return parent_rds_id;

    end fn_get_rds_id;


  begin

    app_refdata.pk_ref_data_structure_mgr.pr_add_coded_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Application Languages'
    ,   p_table_name    => 'LANGUAGE'
    ,   p_alias         => 'LNG'
    ,   p_column_prefix => 'LNG'
    ,   p_sequence      => 'SQ_LNG'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_standard_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Application User Types'
    ,   p_table_name    => 'APP_USER_TYPE'
    ,   p_alias         => 'UTY'
    ,   p_column_prefix => 'UTY'
    ,   p_sequence      => 'SQ_UTY'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_update_only_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Application User Profiles'
    ,   p_table_name    => 'APP_USER_PROFILE'
    ,   p_alias         => 'AURP'
    ,   p_column_prefix => 'AURP'
    ,   p_sequence      => 'SQ_AURP'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_update_only_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Reports'
    ,   p_table_name    => 'REPORT'
    ,   p_alias         => 'RPT'
    ,   p_column_prefix => 'RPT'
    ,   p_sequence      => 'SQ_RPT'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_standard_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Report Categories'
    ,   p_table_name    => 'REPORT_CATEGORY'
    ,   p_alias         => 'RPTC'
    ,   p_column_prefix => 'RPTC'
    ,   p_sequence      => 'SQ_RPTC'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_update_only_list
    (
        p_owning_schema => 'APP_DATA'
    ,   p_description   => 'Application Roles'
    ,   p_table_name    => 'DB_ROLE'
    ,   p_alias         => 'DBRL'
    ,   p_column_prefix => 'DBRL'
    ,   p_sequence      => 'SQ_DBRL'
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_list_subdivision
    (
        p_owning_schema  => 'APP_DATA'
    ,   p_description    => 'Grantable Application Roles'
    ,   p_table_name     => 'MANAGEABLE_DB_ROLE'
    ,   p_alias          => 'MDBRL'
    ,   p_parent_rds_id  => fn_get_rds_id( 'DBRL' )
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_list_intersection
    (
        p_owning_schema  => 'APP_DATA'
    ,   p_description    => 'Application User Type - Roles'
    ,   p_table_name     => 'APP_USER_TYPE_ROLE'
    ,   p_alias          => 'UTR'
    ,   p_column_prefix  => 'UTR'
    ,   p_rds_parent_id  => fn_get_rds_id( 'UTY' )
    ,   p_rds_child_id   => fn_get_rds_id( 'DBRL' )
    );

    app_refdata.pk_ref_data_structure_mgr.pr_add_list_intersection
    (
        p_owning_schema  => 'APP_DATA'
    ,   p_description    => 'Report category reports'
    ,   p_table_name     => 'REPORT_CATEGORY_REPORT'
    ,   p_alias          => 'RPTCR'
    ,   p_column_prefix  => 'RPTCR'
    ,   p_rds_parent_id  => fn_get_rds_id( 'RPTC' )
    ,   p_rds_child_id   => fn_get_rds_id( 'RPT' )
    );

    commit;

end;
/