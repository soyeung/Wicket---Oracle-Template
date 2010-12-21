prompt creating package body pk_app_user_password_mgr

create or replace package body app_user.pk_app_user_password_mgr
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;
    c_current_user  constant varchar2(30 char) := user;

    /**
      * name: pr_change_password
      */

    procedure pr_change_password
    (
        p_current_password in varchar2
    ,   p_new_password     in varchar2
    )
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(50 char) := 'pr_change_password';

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_current_password not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_username
            ,   p_exception_message => 'the password "' || p_current_password || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_new_password not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_username
            ,   p_exception_message => 'the password "' || p_new_password || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

        end pr_validate_input;

    begin

        pr_validate_input;

        execute immediate ('alter user "' || c_current_user || '" identified by "' || p_new_password || '" replace "' || p_current_password || '"');

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => 'c_current_user > ' || c_current_user
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_change_password;

end pk_app_user_password_mgr;
/

sho err