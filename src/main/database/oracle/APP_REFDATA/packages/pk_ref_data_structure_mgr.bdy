prompt create package app_refdata.pk_ref_data_structure_mgr

create or replace package body app_refdata.pk_ref_data_structure_mgr

    /**
      * manage the data which allows the reference data structure management facility to operate.
      * the conventions which must be followed are described in the commentary. such is the cost of generic code.
      */

as

    /**
      * name  : pr_register_data_structure
      * scope : private
      */

    procedure pr_register_data_structure
    (
        p_owning_schema in  app_data.v_ref_data_structure.rds_owning_schema%type
    ,   p_description   in  app_data.v_ref_data_structure.rds_description%type
    ,   p_table_name    in  app_data.v_ref_data_structure.rds_table_name%type
    ,   p_alias         in  app_data.v_ref_data_structure.rds_alias%type
    ,   p_column_prefix in  app_data.v_ref_data_structure.rds_column_prefix%type
    ,   p_rds_id        out app_data.v_ref_data_structure.rds_id%type
    )
    is
    begin

           insert
             into
                  app_data.ref_data_structure rds
                (
                  rds.rds_id
                , rds.rds_owning_schema
                , rds.rds_description
                , rds.rds_table_name
                , rds.rds_alias
                , rds.rds_column_prefix
                )
           values
                (
                  upper( app_data.sq_rds.nextval )
                , upper( p_owning_schema )
                , p_description
                , upper( p_table_name )
                , upper( p_alias )
                , upper( p_column_prefix )
                )
        returning
                  rds_id
             into
                  p_rds_id
                ;

    end pr_register_data_structure;

    /**
      * name : fn_get_data_structure_list
      */

    function fn_get_data_structure_list
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        null;

    end fn_get_data_structure_list;

    /**
      * name : pr_add_coded_list
      */

    procedure pr_add_coded_list
    (
        p_owning_schema in app_data.v_coded_list_ref_data.rds_owning_schema%type
    ,   p_description   in app_data.v_coded_list_ref_data.rds_description%type
    ,   p_table_name    in app_data.v_coded_list_ref_data.rds_table_name%type
    ,   p_alias         in app_data.v_coded_list_ref_data.rds_alias%type
    ,   p_column_prefix in app_data.v_coded_list_ref_data.rds_column_prefix%type
    ,   p_sequence      in app_data.v_coded_list_ref_data.rds_id_sequence%type
    )
    is

        v_rds_id app_data.v_coded_list_ref_data.rds_id%type;

        /**
          * name: pr_register
          */

        procedure pr_register
        is
        begin

            insert
              into
                   app_data.coded_list_ref_data lrds
                 (
                   lrds.rds_id
                 , lrds.rds_id_sequence
                 )
            values
                 (
                   v_rds_id
                 , upper( p_sequence )
                 );

        end pr_register;

    begin

        pr_register_data_structure
        (
            p_owning_schema => p_owning_schema
        ,   p_description   => p_description
        ,   p_table_name    => p_table_name
        ,   p_alias         => p_alias
        ,   p_column_prefix => p_column_prefix
        ,   p_rds_id        => v_rds_id
        );

        pr_register;

    end pr_add_coded_list;

    /**
      * name : pr_add_standard_list
      */

    procedure pr_add_standard_list
    (
        p_owning_schema in app_data.v_list_ref_data.rds_owning_schema%type
    ,   p_description   in app_data.v_list_ref_data.rds_description%type
    ,   p_table_name    in app_data.v_list_ref_data.rds_table_name%type
    ,   p_alias         in app_data.v_list_ref_data.rds_alias%type
    ,   p_column_prefix in app_data.v_list_ref_data.rds_column_prefix%type
    ,   p_sequence      in app_data.v_list_ref_data.rds_id_sequence%type
    )
    is

        v_rds_id app_data.v_list_ref_data.rds_id%type;

        /**
          * name: pr_register
          */

        procedure pr_register
        is
        begin

            insert
              into
                   app_data.list_ref_data lrds
                 (
                   lrds.rds_id
                 , lrds.rds_id_sequence
                 )
            values
                 (
                   v_rds_id
                 , upper( p_sequence )
                 );

        end pr_register;

    begin

        pr_register_data_structure
        (
            p_owning_schema => p_owning_schema
        ,   p_description   => p_description
        ,   p_table_name    => p_table_name
        ,   p_alias         => p_alias
        ,   p_column_prefix => p_column_prefix
        ,   p_rds_id        => v_rds_id
        );

        pr_register;

    end pr_add_standard_list;

    /**
      * name   : pr_add_update_only_list
      */

    procedure pr_add_update_only_list
    (
        p_owning_schema in app_data.v_update_only_list_ref_data.rds_owning_schema%type
    ,   p_description   in app_data.v_update_only_list_ref_data.rds_description%type
    ,   p_table_name    in app_data.v_update_only_list_ref_data.rds_table_name%type
    ,   p_alias         in app_data.v_update_only_list_ref_data.rds_alias%type
    ,   p_column_prefix in app_data.v_update_only_list_ref_data.rds_column_prefix%type
    ,   p_sequence      in app_data.v_update_only_list_ref_data.rds_id_sequence%type
    )
    is

        v_rds_id app_data.v_update_only_list_ref_data.rds_id%type;

        /**
          * name: pr_register
          */

        procedure pr_register
        is
        begin

            insert
              into
                   app_data.update_only_list_ref_data lrds
                 (
                   lrds.rds_id
                 , lrds.rds_id_sequence
                 )
            values
                 (
                   v_rds_id
                 , upper( p_sequence )
                 );

        end pr_register;

    begin

        pr_register_data_structure
        (
            p_owning_schema => p_owning_schema
        ,   p_description   => p_description
        ,   p_table_name    => p_table_name
        ,   p_alias         => p_alias
        ,   p_column_prefix => p_column_prefix
        ,   p_rds_id        => v_rds_id
        );

        pr_register;

    end pr_add_update_only_list;

    /**
      * name : pr_add_list_subdivision
      */

    procedure pr_add_list_subdivision
    (
        p_owning_schema  in app_data.v_ref_data_structure.rds_owning_schema%type
    ,   p_description    in app_data.v_ref_data_structure.rds_description%type
    ,   p_table_name     in app_data.v_ref_data_structure.rds_table_name%type
    ,   p_alias          in app_data.v_ref_data_structure.rds_alias%type
    ,   p_parent_rds_id  in app_data.v_ref_data_structure.rds_id%type
    )
    is

        v_rds_id            app_data.v_ref_data_structure.rds_id%type;
        v_rds_column_prefix app_data.v_ref_data_structure.rds_column_prefix%type;

       /**
         * name: pr_set_column_prefix
         */

        procedure pr_set_column_prefix
        is
        begin

            select
                   vrds.rds_column_prefix

                   into

                   v_rds_column_prefix
              from
                   app_data.v_ref_data_structure vrds
             where
                   vrds.rds_id = p_parent_rds_id
                 ;

        end pr_set_column_prefix;

       /**
         * name: pr_register
         */

        procedure pr_register
        is
        begin

          insert
            into
                 app_data.subdiv_ref_data srds
               (
                 rds_id
               )
          values
               (
                 v_rds_id
               );

        end pr_register;

        /**
          * name: pr_register_division_link
          */

        procedure pr_register_division_link
        is
        begin

            insert
              into
                   app_data.ref_data_structure_link rdsl
                 (
                   rdsl.rds_id
                 , rdsl.rds_child_id
                 )
            select
                   vrds.rds_id
                 , v_rds_id
              from
                   app_data.v_ref_data_structure vrds
             where
                   vrds.rds_id = p_parent_rds_id
                 ;

        end pr_register_division_link;

    begin

        pr_set_column_prefix;

        pr_register_data_structure
        (
            p_owning_schema => p_owning_schema
        ,   p_description   => p_description
        ,   p_table_name    => p_table_name
        ,   p_alias         => p_alias
        ,   p_column_prefix => v_rds_column_prefix
        ,   p_rds_id        => v_rds_id
        );

        pr_register;

        pr_register_division_link;

    end pr_add_list_subdivision;

    /**
      * name   : pr_add_list_intersection
      */

    procedure pr_add_list_intersection
    (
        p_owning_schema  in app_data.v_ref_data_structure.rds_owning_schema%type
    ,   p_description    in app_data.v_ref_data_structure.rds_description%type
    ,   p_table_name     in app_data.v_ref_data_structure.rds_table_name%type
    ,   p_alias          in app_data.v_ref_data_structure.rds_alias%type
    ,   p_column_prefix  in app_data.v_ref_data_structure.rds_column_prefix%type
    ,   p_rds_parent_id  in app_data.v_ref_data_structure.rds_id%type
    ,   p_rds_child_id   in app_data.v_ref_data_structure.rds_id%type
    )
    is

        v_rds_id app_data.v_ref_data_structure.rds_id%type;

        /**
          * name : pr_register
          */

        procedure pr_register
        is
        begin

            insert
              into
                   app_data.list_intersection_ref_data ilrds
                 (
                   rds_id
                 , rds_parent_id
                 , rds_child_id
                 )
            values
                 (
                   v_rds_id
                 , p_rds_parent_id
                 , p_rds_child_id
                 );

        end pr_register;

    begin

        pr_register_data_structure
        (
            p_owning_schema => p_owning_schema
        ,   p_description   => p_description
        ,   p_table_name    => p_table_name
        ,   p_alias         => p_alias
        ,   p_column_prefix => p_column_prefix
        ,   p_rds_id        => v_rds_id
        );

        pr_register;

    end pr_add_list_intersection;

end pk_ref_data_structure_mgr;
/

sho err