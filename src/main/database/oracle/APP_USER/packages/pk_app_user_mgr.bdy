prompt creating package body pk_app_user_mgr

create or replace package body app_user.pk_app_user_mgr
as

    c_package_name constant varchar2(30 char) := $$plsql_unit;
    c_user         constant varchar2(30 char) := user;

    /**
      * name: pr_disable_user
      */

    procedure pr_disable_user
    (
        p_username in app_data.v_app_user.aur_username%type
    )
    is

        c_procedure_name constant varchar2(30 char) := 'pr_disable_user';

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_username != c_user
            ,   p_aex_code          => app_exception.pk_exception_codes.c_cannot_disable_own_account
            ,   p_exception_message => 'it is not possible for a user to disable their own account.'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_username not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_username
            ,   p_exception_message => 'the username "' || p_username || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

        end pr_validate_input;

    begin

        pr_validate_input;

        execute immediate ('alter user "' || p_username || '" account lock');

    end pr_disable_user;

    /**
      * name: pr_enable_user
      */

    procedure pr_enable_user
    (
        p_username in app_data.v_app_user.aur_username%type
    )
    is

        c_procedure_name constant varchar2(30 char) := 'pr_enable_user';

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_username not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_username
            ,   p_exception_message => 'the username "' || p_username || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

        end pr_validate_input;

    begin

        pr_validate_input;

        execute immediate ('alter user "' || p_username || '" account unlock');

    end pr_enable_user;

end pk_app_user_mgr;
/

sho err