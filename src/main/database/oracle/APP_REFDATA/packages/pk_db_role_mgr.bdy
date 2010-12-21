prompt create package header app_refdata.pk_db_role_mgr

create or replace package body app_refdata.pk_db_role_mgr
as

    /**
      * name  : pr_add_role
      * scope : private
      */
    procedure pr_add_role
    (
        p_dbrl_code in  app_data.db_role.dbrl_code%type
    ,   p_dbrl_name in  app_data.db_role.dbrl_name%type
    ,   p_dbrlc_id  in  app_data.db_role_category.dbrlc_id%type
    ,   p_dbrl_id   out app_data.db_role.dbrl_id%type
    )
    is
    begin

            insert
              into
                   app_data.db_role
                 (
                    dbrl_id
                 ,  dbrl_code
                 ,  dbrl_name
                 ,  dbrlc_id
                 )
           values
                 (
                     app_data.sq_dbrl.nextval
                 ,   p_dbrl_code
                 ,   p_dbrl_name
                 ,   p_dbrlc_id
                 )
        returning
                  dbrl_id into p_dbrl_id
                ;

           insert
             into
                  app_data.manageable_db_role
                (
                    dbrl_id
                )
           values
                (
                    p_dbrl_id
                );

    end pr_add_role;

    /**
      * name : pr_add_default_role
      */
    procedure pr_add_default_role
    (
        p_dbrl_code in app_data.db_role.dbrl_code%type
    ,   p_dbrl_name in app_data.db_role.dbrl_name%type
    ,   p_dbrlc_id  in app_data.db_role_category.dbrlc_id%type
    )
    is

        v_dbrl_id app_data.db_role.dbrl_id%type;

        /**
          * name : pr_set_default
          */
        procedure pr_set_default
        is
        begin

            insert
              into
                   app_data.default_db_role
                   (
                       dbrl_id
                   )
            values (
                       v_dbrl_id
                   );

        end pr_set_default;

    begin

        pr_add_role
        (
            p_dbrl_code => p_dbrl_code
        ,   p_dbrl_name => p_dbrl_name
        ,   p_dbrlc_id  => p_dbrlc_id
        ,   p_dbrl_id   => v_dbrl_id
        );

        pr_set_default;

    end pr_add_default_role;

    /**
      * name : pr_add_non_default_role
      */
    procedure pr_add_non_default_role
    (
        p_dbrl_code in app_data.db_role.dbrl_code%type
    ,   p_dbrl_name in app_data.db_role.dbrl_name%type
    ,   p_dbrlc_id  in app_data.db_role_category.dbrlc_id%type
    )
    is

        v_dbrl_id app_data.db_role.dbrl_id%type;

    begin

        pr_add_role
        (
            p_dbrl_code => p_dbrl_code
        ,   p_dbrl_name => p_dbrl_name
        ,   p_dbrlc_id  => p_dbrlc_id
        ,   p_dbrl_id   => v_dbrl_id
        );

    end pr_add_non_default_role;

end pk_db_role_mgr;
/

sho err