prompt creating package body pk_authentication_report

create or replace package body app_report.pk_authentication_report
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name: fn_get_list_ref_data
      */

    function fn_get_list_ref_data
    return sys_refcursor
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'fn_get_list_ref_data';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
                select
                       /*+ result_cache */
                                           'AUR'              datasetkey
                                         , vsaur.aur_id       id
                                         , vsaur.aur_username name
                  from
                       app_data.v_usr_rd_saur vsaur
              order by
                       1 asc
                     , 3 asc;

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
        p_aur_id             in app_data.v_app_user.aur_id%type
    ,   p_start_lgn_time     in app_data.v_authentication_log.lgn_time%type
    ,   p_end_lgn_time       in app_data.v_authentication_log.lgn_time%type
    ,   p_lower_record_limit in int                                         default null
    ,   p_upper_record_limit in int                                         default null
    )
    return sys_refcursor
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'fngetreport';

        v_data_set sys_refcursor;
        v_sql      varchar2(32767 char) default    ' select '
                                                || '        vlgn.aur_username         '
                                                || '      , vlgn.lgn_time             '
                                                || '      , vlgn.lgn_ip_address       '
                                                || '      , vlgn.lgn_http_session     '
                                                || '   from '
                                                || '        app_data.v_authentication_log vlgn '
                                                || '  where '
                                                || '        1 = 1 '
                                                ;

        /**
          * name: pr_set_user_restriction
          */

        procedure pr_set_user_restriction
        is
        begin

            if p_aur_id is not null then
              v_sql := v_sql || ' and vlgn.aur_id = :p_aur_id ';
            else
              v_sql := v_sql || ' and (1 = 1 or :p_aur_id is null) ';
            end if;

        end pr_set_user_restriction;

        /**
          * name: pr_set_time_restriction
          */

        procedure pr_set_time_restriction
        is
        begin

            if       p_start_lgn_time is not null
                 and p_end_lgn_time   is not null
            then
                v_sql := v_sql || ' and vlgn.lgn_time between trunc(:p_start_lgn_time) and trunc(:p_end_lgn_time) + 1 ';
            end if;

            if       p_start_lgn_time is not null
                 and p_end_lgn_time   is     null
            then
                v_sql := v_sql || ' and vlgn.lgn_time >= trunc(:p_start_lgn_time) and (1 = 1 or :p_end_lgn_time is null) ';
            end if;

            if       p_start_lgn_time is     null
                 and p_end_lgn_time   is not null
            then
                v_sql := v_sql || ' and (1 = 1 or :p_start_lgn_time is null) and vlgn.lgn_time <= trunc(:p_end_lgn_time) + 1 ';
            end if;

            if       p_start_lgn_time is     null
                 and p_end_lgn_time   is     null
            then
                v_sql := v_sql || ' and (1 = 1 or :p_start_lgn_time is null) and (1 = 1 or :p_end_lgn_time is null) ';
            end if;

        end pr_set_time_restriction;

        /**
          * name: pr_set_pagination
          */

        procedure pr_set_pagination
        is
        begin

            if
                     p_lower_record_limit is not null
                 and p_upper_record_limit is not null
            then

                /* add extra attribute to gain ordering */

                v_sql :=    ' select '
                         || '        /*+ first_rows_' || to_char (p_upper_record_limit - p_lower_record_limit + 1) || ' */ '
                         || '        aur_username '
                         || '      , lgn_time '
                         || '      , lgn_ip_address '
                         || '      , lgn_http_session '
                         || '      , row_number() over (order by lgn_time desc) rn '
                         || '   from (' || v_sql || ') '
                          ;

                v_sql :=    ' select '
                         || '        aur_username '
                         || '      , lgn_time '
                         || '      , lgn_ip_address '
                         || '      , lgn_http_session '
                         || '   from (' || v_sql || ') '
                         || '  where '
                         || '        rn between :p_lower_record_limit and :p_upper_record_limit ';

            else

                v_sql := v_sql || ' and (1 = 1 or :p_lower_record_limit is null) and (1 = 1 or :p_upper_record_limit is null) ';

            end if;

        end pr_set_pagination;

        /**
          * name: pr_add_ordering
          */

        procedure pr_add_ordering
        is
        begin

            if
                     p_lower_record_limit is not null
                 and p_upper_record_limit is not null
            then
                v_sql := v_sql || ' order by rn asc ';
            else
                v_sql := v_sql || ' order by vlgn.lgn_time desc ';
            end if;

        end pr_add_ordering;

    begin

        pr_set_user_restriction;

        pr_set_time_restriction;

        pr_set_pagination;

        pr_add_ordering;

         open
              v_data_set
          for
              v_sql
        using
              p_aur_id
            , p_start_lgn_time
            , p_end_lgn_time
            , p_lower_record_limit
            , p_upper_record_limit
            ;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_parameter_info => '(p_aur_id => ' || to_char(p_aur_id) || '; p_start_lgn_time => ' || to_char(p_start_lgn_time) || '; p_end_lgn_time => ' || to_char(p_end_lgn_time) || ')'
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_report;

end pk_authentication_report;
/

sho err