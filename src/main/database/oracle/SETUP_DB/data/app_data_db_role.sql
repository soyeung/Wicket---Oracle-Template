prompt register application roles with the application

declare

    /**
      * name: fn_get_category_id
      */
    function fn_get_category_id
    (
        p_dbrlc_name in app_data.db_role_category.dbrlc_name%type
    )
    return int
    is
        v_dbrlc_id app_data.db_role_category.dbrlc_name%type;
    begin
        select dbrlc_id into v_dbrlc_id from app_data.db_role_category where dbrlc_name = p_dbrlc_name;
        return v_dbrlc_id;
    end fn_get_category_id;

begin

    /* application management */
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_report_user'        , p_dbrl_name => 'use the report facility'  , p_dbrlc_id => fn_get_category_id( '&rc_application_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_ref_data_mgr'       , p_dbrl_name => 'manage reference data'    , p_dbrlc_id => fn_get_category_id( '&rc_application_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_configure_ref_data' , p_dbrl_name => 'configure reference data' , p_dbrlc_id => fn_get_category_id( '&rc_application_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_view_debug_info'    , p_dbrl_name => 'view debug info'          , p_dbrlc_id => fn_get_category_id( '&rc_application_management' ) );

    /* db management */
    app_refdata.pk_db_role_mgr.pr_add_default_role    ( p_dbrl_code => '&role_connect' , p_dbrl_name => 'create a connection to the db' , p_dbrlc_id => fn_get_category_id( '&rc_db_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_ucp_mgr' , p_dbrl_name => 'manage the ucp'                , p_dbrlc_id => fn_get_category_id( '&rc_db_management' ) );

    /* user management */
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_dgt_app_user_role_mgr'     , p_dbrl_name => 'manage delegate users'' roles'     , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_std_app_user_role_mgr'     , p_dbrl_name => 'manage standard users'' roles'     , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_delegate_app_user_mgr'     , p_dbrl_name => 'manage delegate users'             , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_standard_app_user_mgr'     , p_dbrl_name => 'manage standard users'             , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_standard_app_user_creator' , p_dbrl_name => 'create standard users'             , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_std_user_password_mgr'     , p_dbrl_name => 'change standard users'' passwords' , p_dbrlc_id => fn_get_category_id( '&rc_user_management' ) );

    /* reference data */
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_language'           , p_dbrl_name => 'manage available application languages' , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_app_user_profile'   , p_dbrl_name => 'manage user profiles'                   , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_app_user_type'      , p_dbrl_name => 'manage user types'                      , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_app_user_type_role' , p_dbrl_name => 'manage user type - roles'               , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_db_role'            , p_dbrl_name => 'manage application roles'               , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_manageable_db_role' , p_dbrl_name => 'manage grantable roles'                 , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_report'             , p_dbrl_name => 'manage report titles'                   , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_report_category'    , p_dbrl_name => 'manage report category titles'          , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_rdm_report_cat_report'  , p_dbrl_name => 'manage report category - reports'       , p_dbrlc_id => fn_get_category_id( '&rc_ref_data_management' ) );

    /* report access */
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_authenticate_report_user'  , p_dbrl_name => 'view successful logins'           , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_security_matrix_by_user'   , p_dbrl_name => 'security matrix report (by user)' , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_security_matrix_by_role'   , p_dbrl_name => 'security matrix report (by role)' , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_unindexed_fk_report'       , p_dbrl_name => 'unindexed foreign key report'     , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_curr_user_activity_report' , p_dbrl_name => 'current user activity'            , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_tables_without_pk_report'  , p_dbrl_name => 'tables without primary keys'      , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_session_lock_report'       , p_dbrl_name => 'session lock report'              , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_usage_data_report'         , p_dbrl_name => 'usage data report'                , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_security_model_report'     , p_dbrl_name => 'security model report'            , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_user_privileges_report'    , p_dbrl_name => 'user privileges report'           , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    app_refdata.pk_db_role_mgr.pr_add_non_default_role( p_dbrl_code => '&role_least_privileges_report'   , p_dbrl_name => 'least privileges report'          , p_dbrlc_id => fn_get_category_id( '&rc_report' ) );
    commit;

end;
/