prompt creating package body pk_delegate_user_creation

create or replace package body app_user.pk_delegate_user_creation
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;
    c_user          constant varchar2( 30 char ) := user;

    /**
      * name: pr_create_delegate_user
      */

    procedure pr_create_delegate_user
    (
        p_username in app_data.v_app_user.aur_username%type
    ,   p_password in varchar2
    ,   p_uty_id   in app_data.v_usr_rd_uty.uty_id%type
    ,   p_aurp_id  in app_data.v_usr_rd_aurp.aurp_id%type
    )
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'pr_create_delegate_user';

        v_aur_id app_data.v_app_user.aur_id%type;

       /**
         * name: pr_register_delegate_user
         */

        procedure pr_register_delegate_user
        is
        begin

            insert
              into
                   app_data.delegate_app_user daur
                 (
                   daur.aur_id
                 )
            values
                 (
                   v_aur_id
                 );

        end pr_register_delegate_user;

    begin

        pk_app_user_creation.pr_create_user
        (
            p_username => p_username
        ,   p_password => p_password
        ,   p_uty_id   => p_uty_id
        ,   p_aurp_id  => p_aurp_id
        ,   p_aur_id   => v_aur_id
        );

        pr_register_delegate_user;

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => '(p_username => ' || p_username || '; p_uty_id => ' || p_uty_id || '; p_aurp_id => ' || p_aurp_id || ')'
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_create_delegate_user;

end pk_delegate_user_creation;
/

sho err