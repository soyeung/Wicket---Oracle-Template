prompt creating package app_report.pk_role_matrix_by_user_report

create or replace package body app_report.pk_role_matrix_by_user_report
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name: fn_get_list_ref_data
      */

    function fn_get_list_ref_data
    return sys_refcursor
    is

        c_start_time      constant number              := dbms_utility.get_time;
        c_procedure_name  constant varchar2( 30 char ) := 'fn_get_list_ref_data';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
               select
                      /*+ result_cache */
                                          datasetkey
                                        , id
                                        , name
                 from (
                           select
                                 'AUR'              datasetkey
                                , vaur.aur_id       id
                                , vaur.aur_username name
                                , 1                 ordering
                             from
                                  app_data.v_app_user vaur

                        union all

                           select
                                  'DBRL'            DATASETKEY
                                , vdbrl.dbrl_id     id
                                , vdbrl.dbrl_name   name
                                , vdbrl.dbrl_order  ordering
                             from
                                  app_data.v_adm_rd_dbrl vdbrl
                      )
             order by
                      datasetkey asc
                    , ordering   asc
                    , name       asc
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

    end fn_get_list_ref_data;

    /**
      * name: fn_get_report
      */

    function fn_get_report
    (
        p_aur_id  in app_data.v_app_user.aur_id%type
    ,   p_dbrl_id in app_data.v_usr_rd_dbrl.dbrl_id%type
    )
    return sys_refcursor
    is

        c_start_time      constant number              := dbms_utility.get_time;
        c_procedure_name  constant varchar2( 30 char ) := 'fn_get_report';

        v_data_set          sys_refcursor;

        v_sql               varchar2( 32767 char );
        v_column_definition varchar2( 32767 char);

        /**
          * name: pr_set_column_definition
          */

        procedure pr_set_column_definition
        is
        begin

            for i in (
                         select
                                vaur.aur_id          aur_id
                              , vaur.aur_username    aur_username
                           from
                                app_data.v_app_user vaur
                          where
                                vaur.aur_id = nvl(p_aur_id, vaur.aur_id)
                       order by
                                aur_username asc
                     )
            loop
                v_column_definition := nvl( v_column_definition , ' ') || ', max(case when vaurr.aur_id=' || to_char( i.aur_id ) || ' and vdbrl.dbrl_id = vaurr.dbrl_id then ''Y'' end) "' || i.aur_username || '"';
            end loop;

        end pr_set_column_definition;

        /**
          * name: pr_set_sql
          */

        procedure pr_set_sql
        is
        begin

            v_sql :=    ' select '
                     ||        ' vdbrl.dbrl_code "role"'
                     ||      ' , vdbrl.dbrl_name "role description"'
                     || '   ' || v_column_definition
                     ||   ' from '
                     ||                  ' app_data.v_adm_rd_dbrl   vdbrl '
                     ||        ' left join app_data.v_app_user_role vaurr on vdbrl.dbrl_id = vaurr.dbrl_id '
                     ||  ' where '
                     ||        ' 1 = 1 '
                      ;

             v_sql := v_sql || case
                                 when
                                     p_aur_id is not null
                                 then
                                     ' and vaurr.aur_id = :p_aur_id '
                                 else
                                     ' and (1 = 1 or :p_aur_id is null) '
                               end;

             v_sql := v_sql || case
                                 when
                                     p_dbrl_id is not null
                                 then
                                     ' and vdbrl.dbrl_id = :p_dbrl_id '
                                 else
                                     ' and (1 = 1 or :p_dbrl_id is null) '
                               end;

             v_sql := v_sql || ' group by '
                            ||          ' vdbrl.dbrl_code'
                            ||         ', vdbrl.dbrl_name'
                            || ' order by '
                            || '           vdbrl.dbrl_code asc ';

        end pr_set_sql;

    begin

        pr_set_column_definition;

        pr_set_sql;

         open
              v_data_set
          for
              v_sql
        using
              p_aur_id
            , p_dbrl_id
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

end pk_role_matrix_by_user_report;
/

sho err