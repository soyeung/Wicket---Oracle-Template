prompt creating package body app_report.pk_unindexed_fk_report

create or replace package body app_report.pk_unindexed_fk_report
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;

    /* ****************************************************************
     * name: fn_get_report
     * **************************************************************** */

    function fn_get_report
    return sys_refcursor
    is

        c_start_time      constant number            := dbms_utility.get_time;
        c_procedure_name  constant varchar2(30 char) := 'fn_get_report';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
                select
                       owner                                 table_owner
                     , table_name                            table_name
                     , constraint_name                       constraint_name
                     ,    cname1
                       || nvl2(cname2, ',' || cname2, null)
                       || nvl2(cname3, ',' || cname3, null)
                       || nvl2(cname4, ',' || cname4, null)
                       || nvl2(cname5, ',' || cname5, null)
                       || nvl2(cname6, ',' || cname6, null)
                       || nvl2(cname7, ',' || cname7, null)
                       || nvl2(cname8, ',' || cname8, null)  column_names
                 from (
                          select
                                 b.owner
                               , b.table_name
                               , b.constraint_name
                               , max(decode(position, 1, column_name, null)) cname1
                               , max(decode(position, 2, column_name, null)) cname2
                               , max(decode(position, 3, column_name, null)) cname3
                               , max(decode(position, 4, column_name, null)) cname4
                               , max(decode(position, 5, column_name, null)) cname5
                               , max(decode(position, 6, column_name, null)) cname6
                               , max(decode(position, 7, column_name, null)) cname7
                               , max(decode(position, 8, column_name, null)) cname8
                               , count(*)                                    col_cnt
                            from
                                 (
                                   select
                                         substr(table_name     , 1, 30) table_name
                                       , substr(constraint_name, 1, 30) constraint_name
                                       , substr(column_name    , 1 ,30) column_name
                                       , position
                                    from
                                         dba_cons_columns
                                   where
                                         owner = 'APP_DATA'
                                 )               a
                               , dba_constraints b
                           where
                                 a.constraint_name = b.constraint_name
                             and b.constraint_type = 'R'
                        group by
                                 b.owner
                               , b.table_name
                               , b.constraint_name
                      ) cons
                where
                      col_cnt > all
                                (
                                    select
                                           count(*)
                                      from
                                           dba_ind_columns i
                                     where
                                           i.table_owner = cons.owner
                                       and i.table_name  = cons.table_name
                                       and i.column_name in (
                                                              cname1
                                                            , cname2
                                                            , cname3
                                                            , cname4
                                                            , cname5
                                                            , cname6
                                                            , cname7
                                                            , cname8
                                                            )
                                       and i.column_position <= cons.col_cnt
                                  group by
                                           i.index_name
                                )
             order by
                      table_name      asc
                    , constraint_name asc
                    , column_names    asc
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

end pk_unindexed_fk_report;
/

sho err