prompt creating package body pk_current_activity_report

create or replace package body app_report.pk_current_activity_report
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name : fn_get_report
      */

    function fn_get_report
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_report';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
               select
                      v$ses.username          aur_username
                    , v$ses.sid               v$ses_sid
                    , v$ses.serial#           v$ses_serial
                    , v$ses.client_info       v$ses_client_info
                    , v$ses.client_identifier v$ses_client_identifier
                    , v$ses.logon_time        v$ses_logon_time
                    , v$ses.status            v$ses_status
                    , v$ses.program           v$ses_program
                    , v$ses.module            v$ses_module
                    , v$ses.action            v$ses_action
                    , lower ( par.par_db_name || '_ora_' || to_char ( v$prc.spid ) || nvl2 ( v$prc.traceid , '_' || v$prc.traceid || '.trc' , '.trc' ) ) trace_file_name
                 from
                                 v$session         v$ses
                            join v$process         v$prc  on v$ses.paddr    = v$prc.addr
                            join app_data.app_user aur    on v$ses.username = aur.aur_username
                      cross join (
                                   select
                                          max ( case when v$par.name = 'user_dump_dest' then v$par.value end ) par_user_dump_dest
                                        , max ( case when v$par.name = 'db_name'        then v$par.value end ) par_db_name
                                     from
                                          v$parameter v$par
                                    where
                                          v$par.name in ( 'user_dump_dest' , 'db_name' )
                                 ) par
             order by
                      aur_username     asc
                    , v$ses_logon_time asc
                      ;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_report;

end pk_current_activity_report;
/

sho err