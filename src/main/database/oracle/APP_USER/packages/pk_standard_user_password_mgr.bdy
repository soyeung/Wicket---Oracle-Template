prompt creating package pk_standard_user_password_mgr

create or replace package body app_user.pk_standard_user_password_mgr
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name: pr_change_password
      */

    procedure pr_change_password
    (
        p_aur_id   in app_data.v_app_user.aur_id%type
    ,   p_password in varchar2
    )
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 50 char ) := 'pr_change_password';

        v_aur_username   app_data.v_standard_app_user.aur_username%type;

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_password not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_password
            ,   p_exception_message => 'the password "' || p_password || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

        end pr_validate_input;

        /**
          * name: pr_set_username
          */

        procedure pr_set_username
        is
        begin

            select
                   vstaur.aur_username
              into
                   v_aur_username
              from
                   app_data.v_standard_app_user vstaur
             where
                   vstaur.aur_id = p_aur_id
                 ;

        end pr_set_username;

    begin

        pr_validate_input;

        pr_set_username;

        execute immediate( 'alter user "' || v_aur_username || '" identified by "' || p_password || '"' );

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => 'p_aur_id -> ' || to_char ( p_aur_id )
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_change_password;

end pk_standard_user_password_mgr;
/

sho err