prompt creating package body app_refdata.pk_intersection_list_sql

create or replace package body app_refdata.pk_intersection_list_sql
as

    /**
      * name  : pr_set_list_details
      * scope : private to package
      */

    procedure pr_set_list_details
    (
        p_rds_id               in     app_data.v_list_ref_data.rds_id%type
    ,   p_owning_schema        in out app_data.v_list_ref_data.rds_owning_schema%type
    ,   p_table_name           in out app_data.v_list_ref_data.rds_table_name%type
    ,   p_alias                in out app_data.v_list_ref_data.rds_alias%type
    ,   p_parent_column_prefix in out app_data.v_list_ref_data.rds_column_prefix%type
    ,   p_child_column_prefix  in out app_data.v_list_ref_data.rds_column_prefix%type
    )
    is
    begin

        select
               rds.rds_owning_schema  ilrds_owning_schema
             , rds.rds_table_name     ilrds_table_name
             , rds.rds_alias          ilrds_alias
             , prds.rds_column_prefix prds_column_prefix
             , crds.rds_column_prefix crds_column_prefix

             into

               p_owning_schema
             , p_table_name
             , p_alias
             , p_parent_column_prefix
             , p_child_column_prefix
          from
                    app_data.list_intersection_ref_data ilrds
               join app_data.v_ref_data_structure       rds   on ilrds.rds_id        = rds.rds_id
               join app_data.v_ref_data_structure       prds  on ilrds.rds_parent_id = prds.rds_id
               join app_data.v_ref_data_structure       crds  on ilrds.rds_child_id  = crds.rds_id
         where
               ilrds.rds_id = p_rds_id
             ;

    end pr_set_list_details;

    /**
      * name : fn_get_parent_data_sql
      */

    function fn_get_parent_data_sql
    (
        p_rds_id in app_data.v_ref_data_structure.rds_id%type
    )
    return varchar2
    is

        v_sql               varchar2( 32767 char );
        v_rds_column_prefix app_data.v_ref_data_structure.rds_column_prefix%type;
        v_rds_owning_schema app_data.v_ref_data_structure.rds_owning_schema%type;
        v_rds_admin_view    app_data.v_ref_data_structure.rds_admin_view%type;
        v_rds_alias         app_data.v_ref_data_structure.rds_alias%type;
        v_view_alias        varchar2( 15 char );

    begin

        select
               vrds.rds_column_prefix
             , vrds.rds_owning_schema
             , vrds.rds_admin_view
             , vrds.rds_alias

               into

               v_rds_column_prefix
             , v_rds_owning_schema
             , v_rds_admin_view
             , v_rds_alias
          from
                    app_data.v_ref_data_structure       vrds
               join app_data.list_intersection_ref_data ilrds on vrds.rds_id = ilrds.rds_parent_id
         where
               ilrds.rds_id = p_rds_id
             ;

        v_view_alias := 'v' || v_rds_alias;

        v_sql := v_sql || '   select '
                       || '          ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix              || ' ' || pk_ref_data_column_names.c_id_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix            || ' ' || pk_ref_data_column_names.c_name_alias
                       || '     from '
                       || '          ' || v_rds_owning_schema || '.' || v_rds_admin_view || ' ' || v_view_alias
                       || ' order by '
                       || '          ' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix || ' asc '
                       || '        , ' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix  || ' asc ';

        return v_sql;

    end fn_get_parent_data_sql;

    /**
      * name : fn_get_child_data_sql
      */

    function fn_get_child_data_sql
    (
      p_rds_id in app_data.v_list_ref_data.rds_id%type
    )
    return varchar2
    is

        v_sql                 varchar2( 32767 char );

        v_ilrds_owning_schema app_data.v_list_ref_data.rds_owning_schema%type;
        v_ilrds_table_name    app_data.v_list_ref_data.rds_table_name%type;
        v_ilrds_alias         app_data.v_list_ref_data.rds_alias%type;
        v_ilrds_column_prefix app_data.v_list_ref_data.rds_column_prefix%type;

        v_prds_owning_schema  app_data.v_list_ref_data.rds_owning_schema%type;
        v_prds_admin_view     app_data.v_list_ref_data.rds_admin_view%type;
        v_prds_alias          app_data.v_list_ref_data.rds_alias%type;
        v_prds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;

        v_crds_owning_schema  app_data.v_list_ref_data.rds_owning_schema%type;
        v_crds_admin_view     app_data.v_list_ref_data.rds_admin_view%type;
        v_crds_alias          app_data.v_list_ref_data.rds_alias%type;
        v_crds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;

        /**
          * name : pr_set_meta_data
          */

        procedure pr_set_meta_data
        is
        begin

            select
                   rds.rds_owning_schema  ilrds_owning_schema
                 , rds.rds_table_name     ilrds_table_name
                 , rds.rds_alias          ilrds_alias
                 , rds.rds_column_prefix  ilrds_column_prefix

                 , prds.rds_owning_schema prds_owning_schema
                 , prds.rds_admin_view    prds_admin_view
                 , prds.rds_alias         prds_alias
                 , prds.rds_column_prefix prds_column_prefix

                 , crds.rds_owning_schema crds_owning_schema
                 , crds.rds_admin_view    crds_admin_view
                 , crds.rds_alias         crds_alias
                 , crds.rds_column_prefix crds_column_prefix

                 into

                   v_ilrds_owning_schema
                 , v_ilrds_table_name
                 , v_ilrds_alias
                 , v_ilrds_column_prefix

                 , v_prds_owning_schema
                 , v_prds_admin_view
                 , v_prds_alias
                 , v_prds_column_prefix

                 , v_crds_owning_schema
                 , v_crds_admin_view
                 , v_crds_alias
                 , v_crds_column_prefix
              from
                        app_data.list_intersection_ref_data ilrds
                   join app_data.v_ref_data_structure       rds   on ilrds.rds_id        = rds.rds_id
                   join app_data.v_ref_data_structure       prds  on ilrds.rds_parent_id = prds.rds_id
                   join app_data.v_ref_data_structure       crds  on ilrds.rds_child_id  = crds.rds_id
             where
                   ilrds.rds_id = p_rds_id
                 ;

        end pr_set_meta_data;

    begin

        pr_set_meta_data;

        v_sql :=
                 '    select '
              || '           ' || app_refdata.pk_ref_data_column_names.c_dataset_key_alias
              || '         , ' || app_refdata.pk_ref_data_column_names.c_id_alias
              || '         , ' || app_refdata.pk_ref_data_column_names.c_name_alias
              || '      from ('
              || '                select '
              ||                       ' ''AVAILABLE'' '                                                                                           || app_refdata.pk_ref_data_column_names.c_dataset_key_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_id_suffix           || ' ' || app_refdata.pk_ref_data_column_names.c_id_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_name_suffix         || ' ' || app_refdata.pk_ref_data_column_names.c_name_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_order_suffix        || ' ' || app_refdata.pk_ref_data_column_names.c_order_alias
              ||                  ' from '
              ||                       ' ' || v_crds_owning_schema  || '.' || v_crds_admin_view  || ' ' || v_crds_alias
              ||             ' union all '
              || '                select '
              ||                       ' ''SELECTED'' '                                                                                            || app_refdata.pk_ref_data_column_names.c_dataset_key_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_id_suffix           || ' ' || app_refdata.pk_ref_data_column_names.c_id_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_name_suffix         || ' ' || app_refdata.pk_ref_data_column_names.c_name_alias
              ||                     ' , ' || v_crds_alias || '.' || v_crds_alias || app_refdata.pk_ref_data_column_names.c_order_suffix        || ' ' || app_refdata.pk_ref_data_column_names.c_order_alias
              ||                  ' from '
              ||                       ' '      || v_prds_owning_schema  || '.' || v_prds_admin_view  || ' ' || v_prds_alias
              ||                       ' join ' || v_ilrds_owning_schema || '.' || v_ilrds_table_name || ' ' || v_ilrds_alias
              ||                         ' on ' || v_prds_alias || '.' || v_prds_column_prefix  || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = ' || v_ilrds_alias || '.' || v_prds_column_prefix  || app_refdata.pk_ref_data_column_names.c_id_suffix
              ||                        ' and ' || v_prds_alias || '.' || v_prds_column_prefix  || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = :p_parent_id '
              ||                       ' join ' || v_crds_owning_schema  || '.' || v_crds_admin_view  || ' ' || v_crds_alias
              ||                         ' on ' || v_ilrds_alias || '.' || v_crds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = ' || v_crds_alias || '.' || v_crds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix
              || '           )'
              || ' order by '
              || '          ' || app_refdata.pk_ref_data_column_names.c_dataset_key_alias || ' asc '
              || '        , ' || app_refdata.pk_ref_data_column_names.c_order_alias       || ' asc '
              || '        , ' || app_refdata.pk_ref_data_column_names.c_name_alias        || ' asc '
               ;

        return v_sql;

    end fn_get_child_data_sql;

    /**
      * name : fn_get_delete_sql
      */

    function fn_get_delete_sql
    (
        p_rds_id in app_data.v_list_ref_data.rds_id%type
    )
    return varchar2
    is

        v_delete_sql varchar2(32767 char);

        v_ilrds_owning_schema app_data.v_list_ref_data.rds_owning_schema%type;
        v_ilrds_table_name    app_data.v_list_ref_data.rds_table_name%type;
        v_ilrds_alias         app_data.v_list_ref_data.rds_alias%type;

        v_prds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;
        v_crds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;

        /**
          * name : pr_build_sql
          */

        procedure pr_build_sql
        is
        begin

            v_delete_sql :=    ' delete '
                           || '   from '
                           || '        ' || v_ilrds_owning_schema || '.' || v_ilrds_table_name || ' ' || v_ilrds_alias
                           || '  where '
                           || '        ' || v_ilrds_alias || '.' || v_prds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = :p_parent_id '
                           || '    and not exists ( '
                           ||                     ' select '
                           ||                            ' null '
                           ||                       ' from '
                           ||                            ' table '
                           ||                            ' ( '
                           ||                            '   cast '
                           ||                            '   ( '
                           ||                            '     :p_dataset as app_refdata.tty_intersection_list_ref_data '
                           ||                            '   ) '
                           ||                            ' ) ds '
                           ||                      ' where '
                           ||                            ' ds.rds_child_id = ' || v_ilrds_alias || '.' || v_crds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix
                           ||                   ' ) '
                      ;

        end pr_build_sql;

    begin

        pr_set_list_details
        (
            p_rds_id                   => p_rds_id
        ,   p_owning_schema        => v_ilrds_owning_schema
        ,   p_table_name           => v_ilrds_table_name
        ,   p_alias                => v_ilrds_alias
        ,   p_parent_column_prefix => v_prds_column_prefix
        ,   p_child_column_prefix  => v_crds_column_prefix
        );

        pr_build_sql;

        return v_delete_sql;

    end fn_get_delete_sql;

    /**
      * name : fn_get_insert_sql
      */

    function fn_get_insert_sql
    (
        p_rds_id in app_data.v_list_ref_data.rds_id%type
    )
    return varchar2
    is

        v_insert_sql varchar2( 32767 char );

        v_ilrds_owning_schema app_data.v_list_ref_data.rds_owning_schema%type;
        v_ilrds_table_name    app_data.v_list_ref_data.rds_table_name%type;
        v_ilrds_alias         app_data.v_list_ref_data.rds_alias%type;

        v_prds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;
        v_crds_column_prefix  app_data.v_list_ref_data.rds_column_prefix%type;

        /**
          * name : pr_build_sql
          */

        procedure pr_build_sql
        is
        begin

            v_insert_sql :=   ' insert '
                           || '   into '
                           || '        ' || v_ilrds_owning_schema || '.' || v_ilrds_table_name
                           || '      ( '
                           || '        ' || v_prds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix
                           || '      , ' || v_crds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix
                           || '      ) '
                           || ' select '
                           || '        :p_parent_id '
                           || '      , ds.rds_child_id '
                           ||   ' from '
                           ||        ' table '
                           ||        ' ( '
                           ||        '   cast '
                           ||        '   ( '
                           ||        '     :p_dataset as app_refdata.tty_intersection_list_ref_data '
                           ||        '   ) '
                           ||        ' ) ds '
                           || ' where '
                           ||       ' not exists ( '
                           ||                    ' select '
                           ||                           ' null '
                           ||                      ' from '
                           ||                             v_ilrds_owning_schema || '.' || v_ilrds_table_name || ' ' || v_ilrds_alias
                           ||                     ' where '
                           ||                                  v_ilrds_alias || '.' || v_prds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = :p_parent_id '
                           ||                       ' and ' || v_ilrds_alias || '.' || v_crds_column_prefix || app_refdata.pk_ref_data_column_names.c_id_suffix || ' = ds.rds_child_id '
                           ||                  ' ) '
                      ;

        end pr_build_sql;

    begin

        pr_set_list_details
        (
            p_rds_id               => p_rds_id
        ,   p_owning_schema        => v_ilrds_owning_schema
        ,   p_table_name           => v_ilrds_table_name
        ,   p_alias                => v_ilrds_alias
        ,   p_parent_column_prefix => v_prds_column_prefix
        ,   p_child_column_prefix  => v_crds_column_prefix
        );

        pr_build_sql;

        return v_insert_sql;

    end fn_get_insert_sql;

end pk_intersection_list_sql;
/

sho err