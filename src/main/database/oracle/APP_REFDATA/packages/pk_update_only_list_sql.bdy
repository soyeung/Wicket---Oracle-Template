prompt creating package body app_refdata.pk_update_only_list_sql

create or replace package body app_refdata.pk_update_only_list_sql
as

    /**
      * name  : pr_set_list_details
      * scope : private
      */

    procedure pr_set_list_details
    (
        p_rds_id            in     app_data.v_update_only_list_ref_data.rds_id%type
    ,   p_rds_owning_schema in out app_data.v_update_only_list_ref_data.rds_owning_schema%type
    ,   p_rds_table_name    in out app_data.v_update_only_list_ref_data.rds_table_name%type
    ,   p_rds_alias         in out app_data.v_update_only_list_ref_data.rds_alias%type
    ,   p_rds_column_prefix in out app_data.v_update_only_list_ref_data.rds_column_prefix%type
    ,   p_rds_id_sequence   in out app_data.v_update_only_list_ref_data.rds_id_sequence%type
    )
    is
    begin

       select
              vuords.rds_owning_schema
            , vuords.rds_table_name
            , vuords.rds_alias
            , vuords.rds_column_prefix
            , vuords.rds_id_sequence

              into

              p_rds_owning_schema
            , p_rds_table_name
            , p_rds_alias
            , p_rds_column_prefix
            , p_rds_id_sequence
         from
                   app_data.v_update_only_list_ref_data   vuords
              join app_data.v_editable_ref_data_structure vmrds  on vuords.rds_id = vmrds.rds_id
        where
              vuords.rds_id = p_rds_id
            ;

    end pr_set_list_details;

    /**
      * name : fn_get_data_sql
      */

    function fn_get_data_sql
    (
        p_rds_id in app_data.v_update_only_list_ref_data.rds_id%type
    )
    return varchar2
    is

        v_sql               varchar2( 32767 char );
        v_rds_column_prefix app_data.v_update_only_list_ref_data.rds_column_prefix%type;
        v_rds_owning_schema app_data.v_update_only_list_ref_data.rds_owning_schema%type;
        v_rds_admin_view    app_data.v_update_only_list_ref_data.rds_admin_view%type;
        v_rds_alias         app_data.v_update_only_list_ref_data.rds_alias%type;
        v_view_alias        varchar2( 15 char );

    begin

        select
               vuords.rds_column_prefix
             , vuords.rds_owning_schema
             , vuords.rds_admin_view
             , vuords.rds_alias
               into
               v_rds_column_prefix
             , v_rds_owning_schema
             , v_rds_admin_view
             , v_rds_alias
          from
               app_data.v_update_only_list_ref_data vuords
         where
               vuords.rds_id = p_rds_id
             ;

        v_view_alias := 'v' || v_rds_alias;

        v_sql := v_sql || '   select '
                       || '          ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix              || ' ' || pk_ref_data_column_names.c_id_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix            || ' ' || pk_ref_data_column_names.c_name_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_is_user_visible_suffix || ' ' || pk_ref_data_column_names.c_is_user_visible_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix           || ' ' || pk_ref_data_column_names.c_order_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_updated_date_suffix    || ' ' || pk_ref_data_column_names.c_updated_date_alias
                       || '     from '
                       || '          ' || v_rds_owning_schema || '.' || v_rds_admin_view || ' ' || v_view_alias
                       || ' order by '
                       || '          ' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix || ' asc '
                       || '        , ' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix  || ' asc ';

        return v_sql;

    end fn_get_data_sql;

    /**
      * name : fn_get_update_sql
      */

    function fn_get_update_sql
    (
        p_rds_id in app_data.v_update_only_list_ref_data.rds_id%type
    )
    return app_utility.tty_string
    is

        /* variables storing products of function */

        v_sql_statements app_utility.tty_string := app_utility.tty_string();
        v_merge_sql      varchar2( 32767 char );

        /* define the data structure being operated on */

        v_rds_owning_schema app_data.v_update_only_list_ref_data.rds_owning_schema%type;
        v_rds_table_name    app_data.v_update_only_list_ref_data.rds_table_name%type;
        v_rds_alias         app_data.v_update_only_list_ref_data.rds_alias%type;
        v_rds_column_prefix app_data.v_update_only_list_ref_data.rds_column_prefix%type;
        v_rds_id_sequence   app_data.v_update_only_list_ref_data.rds_id_sequence%type;

        /**
          * name : pr_build_sql
          */

        procedure pr_build_sql
        is

            c_in_dataset_col_prefix constant varchar2(3 char) := 'lrd';
            c_in_dataset_alias      constant varchar2(2 char) := 'ds';

        begin

            v_merge_sql :=    ' merge '
                          || '  into '
                          || '       ' || v_rds_owning_schema || '.' || v_rds_table_name || ' ' || v_rds_alias
                          || ' using ( '
                          || '         select '
                          || '                ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix
                          || '              , ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_name_suffix
                          || '              , ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_is_user_visible_suffix
                          || '              , ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_order_suffix
                          || '              , ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_updated_date_suffix
                          || '           from '
                          || '                table(cast(:p_dataset as app_refdata.tty_list_ref_data)) ' || c_in_dataset_alias
                          || '       ) '     || c_in_dataset_alias
                          || '    on ('      || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix           || ' = ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix
                          || '         and ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_updated_date_suffix || ' = ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_updated_date_suffix
                          || '       )'
                          || '  when '
                          || '       matched '
                          || '          then '
                          || '        update '
                          || '           set '
                          || '               ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix            || ' = ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_name_suffix
                          || '             , ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_is_user_visible_suffix || ' = ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_is_user_visible_suffix
                          || '             , ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix           || ' = ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_order_suffix
                          || '             , ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_updated_by_suffix      || ' = user '
                           ;

            v_sql_statements.extend();
            v_sql_statements( v_sql_statements.count ) := app_utility.ty_string( v_merge_sql );

        end pr_build_sql;

    begin

        pr_set_list_details
        (
            p_rds_id            => p_rds_id
        ,   p_rds_owning_schema => v_rds_owning_schema
        ,   p_rds_table_name    => v_rds_table_name
        ,   p_rds_alias         => v_rds_alias
        ,   p_rds_column_prefix => v_rds_column_prefix
        ,   p_rds_id_sequence   => v_rds_id_sequence
        );

        pr_build_sql;

        return v_sql_statements;

    end fn_get_update_sql;

    /**
      * name : fn_get_lock_sql
      */

    function fn_get_lock_sql
    (
        p_rds_id in app_data.v_update_only_list_ref_data.rds_id%type
    )
    return varchar2
    is

        v_lock_sql varchar2(32767 char);

        /* define the data structure being operated on */

        v_rds_owning_schema app_data.v_update_only_list_ref_data.rds_owning_schema%type;
        v_rds_table_name    app_data.v_update_only_list_ref_data.rds_table_name%type;
        v_rds_alias         app_data.v_update_only_list_ref_data.rds_alias%type;
        v_rds_column_prefix app_data.v_update_only_list_ref_data.rds_column_prefix%type;
        v_rds_id_sequence   app_data.v_update_only_list_ref_data.rds_id_sequence%type;

        c_in_dataset_col_prefix constant varchar2(3 char) := 'lrd';
        c_in_dataset_alias      constant varchar2(2 char) := 'ds';

    begin

        pr_set_list_details
        (
            p_rds_id            => p_rds_id
        ,   p_rds_owning_schema => v_rds_owning_schema
        ,   p_rds_table_name    => v_rds_table_name
        ,   p_rds_alias         => v_rds_alias
        ,   p_rds_column_prefix => v_rds_column_prefix
        ,   p_rds_id_sequence   => v_rds_id_sequence
        );

        v_lock_sql :=    ' update '
                      || '        ' || v_rds_owning_schema || '.' || v_rds_table_name || ' ' || v_rds_alias
                      || '    set '
                      || '        ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_updated_date_suffix || ' = :p_timestamp '
                      || '  where '
                      || '        exists ('
                      || '                 select '
                      || '                        null '
                      || '                   from '
                      || '                        table(cast(:p_dataset as app_refdata.tty_list_ref_data)) ' || c_in_dataset_alias
                      || '                  where '
                      || '                        ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix           || ' = ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix
                      || '                    and ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_updated_date_suffix || ' = ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_updated_date_suffix
                      || '               )'
                      ;

        return v_lock_sql;

    end fn_get_lock_sql;

end pk_update_only_list_sql;
/

sho err