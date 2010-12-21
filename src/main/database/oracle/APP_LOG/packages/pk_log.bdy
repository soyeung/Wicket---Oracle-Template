prompt create package body app_log.pk_log

create or replace package body app_log.pk_log
as

    c_current_user constant app_data.v_app_user.aur_username%type := user;

    /**
      * name: pr_log_app_authentication
      */

    procedure pr_log_app_authentication
    (
        p_ip_address   in app_data.authentication_log.lgn_ip_address%type
    ,   p_http_session in app_data.authentication_log.lgn_http_session%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.authentication_log lgn
             (
                 lgn.lgn_id
             ,   lgn.lgn_ip_address
             ,   lgn.lgn_http_session
             ,   lgn.aur_id
             )
        values
             (
                 app_data.sq_lgn.nextval
             ,   p_ip_address
             ,   p_http_session
             ,   (
                     select
                            vaur.aur_id
                       from
                            app_data.v_app_user vaur
                      where
                            vaur.aur_username = c_current_user
                 )
             );

        commit;

    end pr_log_app_authentication;

    /**
      * name: pr_log_error
      */

    procedure pr_log_error
    (
        p_sqlcode           in app_data.error_log.erl_sqlcode%type
    ,   p_sqlerrm           in app_data.error_log.erl_sqlerrm%type
    ,   p_calling_procedure in app_data.error_log.erl_calling_procedure%type
    ,   p_error_backtrace   in app_data.error_log.erl_error_backtrace%type
    ,   p_message           in app_data.error_log.erl_message%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.error_log erl
             (
                 erl.erl_id
             ,   erl.erl_sqlcode
             ,   erl.erl_sqlerrm
             ,   erl.erl_calling_procedure
             ,   erl.erl_error_backtrace
             ,   erl.erl_message
             )
        values
             (
                 app_data.sq_erl.nextval
             ,   p_sqlcode
             ,   p_sqlerrm
             ,   p_calling_procedure
             ,   p_error_backtrace
             ,   p_message
             );

        commit;

    end pr_log_error;

    /**
      * name: pr_log_message
      */

    procedure pr_log_message
    (
        p_message           in app_data.message_log.msl_message%type
    ,   p_calling_procedure in app_data.message_log.msl_calling_procedure%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.message_log msl
             (
                 msl.msl_id
             ,   msl.msl_message
             ,   msl.msl_calling_procedure
             )
        values
             (
                 app_data.sq_msl.nextval
             ,   p_message
             ,   p_calling_procedure
             );

        commit;

    end pr_log_message;

    /**
      * name: pr_log_activity
      */

    procedure pr_log_activity
    (
        p_parameter_info in app_data.activity_log.act_parameter_info%type default 'n/a'
    ,   p_package_owner  in app_data.activity_log.act_package_owner%type
    ,   p_package_name   in app_data.activity_log.act_package_name%type
    ,   p_procedure_name in app_data.activity_log.act_procedure_name%type
    ,   p_start_time     in number
    ,   p_end_time       in number                                        default sys.dbms_utility.get_time
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.activity_log act
             (
               act.act_id
             , act.aur_id
             , act.act_parameter_info
             , act.act_package_owner
             , act.act_package_name
             , act.act_procedure_name
             , act.act_elapsed_time
             )
        values
             (
               app_data.sq_act.nextval
             , (
                 select
                        vaur.aur_id
                   from
                        app_data.v_app_user vaur
                  where
                        vaur.aur_username = c_current_user
               )
             , p_parameter_info
             , p_package_owner
             , p_package_name
             , p_procedure_name
             , numtodsinterval
               (
                 abs
                 (
                   ( greatest( p_start_time , p_end_time ) - least( p_start_time , p_end_time ) ) / 100
                 )
               , 'second'
               )
             );

        commit;

    end pr_log_activity;

end pk_log;
/

sho err