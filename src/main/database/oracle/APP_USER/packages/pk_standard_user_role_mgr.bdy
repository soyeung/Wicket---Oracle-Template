prompt creating package body pk_standard_user_role_mgr

create or replace package body app_user.pk_standard_user_role_mgr
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name: fn_get_standard_user_roles
      */

    function fn_get_standard_user_roles
    (
        p_aur_id in app_data.v_app_user.aur_id%type
    )
    return sys_refcursor
    is
    begin
        return app_user.pk_app_user_role_mgr.fn_get_all_roles( p_aur_id );
    end fn_get_standard_user_roles;

    /**
      * name: pr_update_standard_user_roles
      */

    procedure pr_update_standard_user_roles
    (
        p_aur_id  in app_data.v_app_user.aur_id%type
    ,   p_dataset in app_user.tty_db_role
    )
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'pr_update_standard_user_roles';

    begin

        app_user.pk_app_user_role_mgr.pr_update_user_roles
        (
            p_aur_id  => p_aur_id
        ,   p_dataset => p_dataset
        );

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => '(p_aur_id => ' || to_char(p_aur_id) || ')'
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_update_standard_user_roles;

end pk_standard_user_role_mgr;
/

sho err