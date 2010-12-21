prompt creating package body app_report.pk_tables_without_pk_report

create or replace package body app_report.pk_tables_without_pk_report
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name: fn_get_report
      */

    function fn_get_report
    return sys_refcursor
    is

        c_start_time      constant number              := dbms_utility.get_time;
        c_procedure_name  constant varchar2( 30 char ) := 'fn_get_report';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
                select
                       dba_tab.owner       table_owner
                     , dba_tab.table_name  table_name
                  from
                       sys.dba_tables dba_tab
                 where
                      dba_tab.owner = 'APP_DATA'
                  and not exists (

                                   select
                                          null
                                     from
                                          sys.dba_constraints dba_cons
                                    where
                                          dba_cons.owner           = dba_tab.owner
                                      and dba_cons.table_name      = dba_tab.table_name
                                      and dba_cons.constraint_type = 'P'

                                 )
             order by
                      owner      asc
                    , table_name asc
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

end pk_tables_without_pk_report;
/

sho err