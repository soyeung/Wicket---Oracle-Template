prompt creating package body pk_standard_app_user_mgr

create or replace package body app_user.pk_standard_app_user_mgr
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;
    c_user          constant varchar2(30 char) := user;

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
                                          datasetkey
                                        , id
                                        , name
                 from (

                           select
                                  'DBRL'           datasetkey
                                , vdbrl.dbrl_id    id
                                , vdbrl.dbrl_name  name
                                , vdbrl.dbrl_order ordering
                             from
                                  app_data.v_usr_rd_dbrl vdbrl

                        union all

                           select
                                  'LNG'           datasetkey
                                , vlng.lng_id     id
                                , vlng.lng_name   name
                                , vlng.lng_order  ordering
                             from
                                  app_data.v_adm_rd_lng vlng

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
      * name: fn_get_standard_users
      */

    function fn_get_standard_users
    (
        p_aur_username       in app_data.v_standard_app_user.aur_username%type
    ,   p_is_account_enabled in app_data.v_standard_app_user.aur_is_account_enabled%type
    ,   p_is_tracing_enabled in app_data.v_standard_app_user.aur_is_tracing_enabled%type
    ,   p_dbrl_id            in app_data.v_usr_rd_dbrl.dbrl_id%type
    ,   p_lng_id             in app_data.v_standard_app_user.lng_id%type
    ,   p_lower_record_limit in int                                                      default null
    ,   p_upper_record_limit in int                                                      default null
    )
    return sys_refcursor
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'fn_get_standard_users';

        v_data_set sys_refcursor;
        v_sql      varchar2(32767 char) default    ' select '
                                                || '        vsaur.aur_id                 id '
                                                || '      , vsaur.aur_username           aur_username '
                                                || '      , vsaur.aur_profile            aur_profile '
                                                || '      , vsaur.aur_is_account_enabled aur_is_account_enabled '
                                                || '      , vsaur.aur_is_tracing_enabled aur_is_tracing_enabled '
                                                || '      , vsaur.lng_id                 lng_id '
                                                || '      , vsaur.aur_created_date       created_date '
                                                || '      , vsaur.aur_updated_date       updated_date '
                                                || '   from '
                                                || '        app_data.v_standard_app_user vsaur '
                                                || '  where '
                                                || '        1 = 1 '
                                                 ;

        /**
          * name: pr_set_username
          */

        procedure pr_set_username
        is
        begin

            if p_aur_username is not null then
                v_sql := v_sql || ' and vsaur.aur_username like ''%''||:p_aur_username||''%'' ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_aur_username is null) ';
            end if;

        end pr_set_username;

        /**
          * name: pr_set_is_account_enabled
          */

        procedure pr_set_is_account_enabled
        is
        begin

            if p_is_account_enabled is not null then
                v_sql := v_sql || ' and vsaur.aur_is_account_enabled = :p_is_account_enabled ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_is_account_enabled is null) ';
            end if;

        end pr_set_is_account_enabled;

        /**
          * name: pr_set_is_tracing_enabled
          */

        procedure pr_set_is_tracing_enabled
        is
        begin

            if p_is_tracing_enabled is not null then
                v_sql := v_sql || ' and vsaur.aur_is_tracing_enabled = :p_is_tracing_enabled ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_is_tracing_enabled is null) ';
            end if;

        end pr_set_is_tracing_enabled;

        /**
          * name: pr_set_db_role
          */

        procedure pr_set_db_role
        is
        begin
            if p_dbrl_id is not null then
                v_sql := v_sql || ' and vsaur.aur_id in ( '
                               || '                       select '
                               || '                              vaurrol.aur_id '
                               || '                         from '
                               || '                              app_data.v_app_user_role vaurrol '
                               || '                        where '
                               || '                              vaurrol.dbrl_id = :p_dbrl_id '
                               || '                     ) ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_dbrl_id is null) ';
            end if;
        end pr_set_db_role;

        /**
          * name: pr_set_language
          */

        procedure pr_set_language
        is
        begin

            if p_lng_id is not null then
                v_sql := v_sql || ' and vsaur.lng_id = :p_lng_id ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_lng_id is null) ';
            end if;

        end pr_set_language;

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
                         || '        /*+ first_rows_' || (p_upper_record_limit - p_lower_record_limit + 1) || '*/'
                         || '        id '
                         || '      , aur_username '
                         || '      , aur_profile '
                         || '      , aur_is_account_enabled '
                         || '      , aur_is_tracing_enabled '
                         || '      , lng_id '
                         || '      , created_date '
                         || '      , updated_date '
                         || '      , row_number() over (order by aur_username asc) rn '
                         || '   from (' || v_sql || ') '
                          ;

                v_sql :=    ' select '
                         || '        id '
                         || '      , aur_username '
                         || '      , aur_profile '
                         || '      , aur_is_account_enabled '
                         || '      , aur_is_tracing_enabled '
                         || '      , lng_id '
                         || '      , created_date '
                         || '      , updated_date '
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
                v_sql := v_sql || ' order by aur_username asc ';
            end if;

        end pr_add_ordering;

    begin

        pr_set_username;

        pr_set_is_account_enabled;

        pr_set_is_tracing_enabled;

        pr_set_db_role;

        pr_set_language;

        pr_set_pagination;

        pr_add_ordering;

         open
              v_data_set
          for
              v_sql
        using
                p_aur_username
            ,   p_is_account_enabled
            ,   p_is_tracing_enabled
            ,   p_dbrl_id
            ,   p_lng_id
            ,   p_lower_record_limit
            ,   p_upper_record_limit
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

    end fn_get_standard_users;

    /**
      * name: pr_update_standard_users
      */

    procedure pr_update_standard_users
    (
        p_dataset in app_user.tty_standard_app_user
    )
    is

    c_start_time     constant number            := dbms_utility.get_time;
    c_procedure_name constant varchar2(30 char) := 'pr_update_standard_users';

        /**
          * name: pr_update_user_details
          */

        procedure pr_update_user_details
        is

            c_now constant timestamp := systimestamp;

        begin

            --
            -- update the 'app_user' table so that we have a record of who made the last update and when.
            --

            merge
             into
                  app_data.standard_app_user saur
            using (
                    select
                           ds.aur_id
                         , ds.lng_id
                      from
                           table
                           (
                               cast
                               (
                                   p_dataset as app_user.tty_standard_app_user
                               )
                           ) ds
                  ) ds
               on (
                      ds.aur_id = saur.aur_id
                  )
             when
                  matched then
                               update
                                  set
                                      saur.lng_id = ds.lng_id
                ;

           update
                  app_data.app_user aur
              set
                  aur.aur_updated_by   = c_user
                , aur.aur_updated_date = c_now
            where
                  exists (
                           select
                                  null
                             from
                                  table
                                  (
                                      cast
                                      (
                                          p_dataset as app_user.tty_standard_app_user
                                      )
                                  ) ds
                            where
                                  ds.aur_id           = aur.aur_id
                              and ds.aur_updated_date = aur.aur_updated_date
                         );

        end pr_update_user_details;

    begin

        for i in (
                   select
                          ds.aur_id                      aur_id
                        , ds.aur_username                aur_username

                        , ds.aur_is_account_enabled      new_is_account_enabled
                        , vstaur.aur_is_account_enabled  current_is_account_enabled

                        , ds.aur_is_tracing_enabled      new_is_tracing_enabled
                        , vstaur.aur_is_tracing_enabled  current_is_tracing_enabled

                        , ds.aur_updated_date
                     from
                               table
                               (
                                   cast
                                   (
                                       p_dataset as app_user.tty_standard_app_user
                                   )
                               )                            ds
                          join app_data.v_standard_app_user vstaur on (
                                                                            ds.aur_username     = vstaur.aur_username
                                                                        and ds.aur_updated_date = vstaur.aur_updated_date
                                                                      )
                 )
        loop

            --
            -- enable/disable account
            --

            if (
                   i.new_is_account_enabled != i.current_is_account_enabled
               )
            then

                case i.new_is_account_enabled
                    when 'Y'
                    then app_user.pk_app_user_mgr.pr_enable_user
                         (
                             p_username => i.aur_username
                         );
                    when 'N'
                    then app_user.pk_app_user_mgr.pr_disable_user
                         (
                             p_username => i.aur_username
                         );
                end case;

            end if;

            --
            -- enable/disable tracing
            --

            if (
                   i.new_is_tracing_enabled != i.current_is_tracing_enabled
               )
            then

                case i.new_is_tracing_enabled
                    when 'Y'
                    then if (i.aur_username = c_user) then
                             app_utility.pk_session_utility.pr_set_trace_file_identifier( p_tracefile_identifier => c_user );
                         end if;

                         sys.dbms_monitor.client_id_trace_enable ( client_id => i.aur_username , waits => true , binds => true );
                    when 'N'
                    then sys.dbms_monitor.client_id_trace_disable ( i.aur_username );
                end case;

            end if;

        end loop;

        pr_update_user_details;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_update_standard_users;

end pk_standard_app_user_mgr;
/

sho err