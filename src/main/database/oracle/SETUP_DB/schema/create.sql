--
-- drop users that were registered with the last installation of the template application!!!
--

begin
    for i in (
                 select
                        username
                   from
                        sys.dba_users
                  where
                        username in (
                                        'SUPERUSER'
                                    ,   'AUTO_USER_MGR'
                                    ,   'PROXY_USER'
                                    ,   'UNIT_TEST'
                                    )
             )
    loop
        execute immediate( 'drop user ' || i.username );
        dbms_output.put_line( 'dropped :: ' || i.username  );
    end loop;

    for i in (
                 select
                        username
                   from
                        sys.dba_users
                  where
                        username in (
                                        '&utility_user'
                                    ,   '&data_user'
                                    ,   '&log_user'
                                    ,   '&exception_user'
                                    ,   '&usermgr_user'
                                    ,   '&refdata_user'
                                    ,   '&report_user'
                                    ,   '&ucp_user'
                                    )
            )
    loop
        execute immediate( 'drop user ' || i.username || ' cascade' );
        dbms_output.put_line( 'dropped :: ' || i.username  );
    end loop;

end;
/

--
-- utility system
--

prompt creating utility_schema

create user &utility_user identified by &utility_password
/

grant
      create session
    , create procedure
    , create type
   to
      &utility_user
/

--
-- data system
--

prompt creating data schema

create user &data_user identified by &data_password
/

grant
      create    session
    , create    table
    , create    view
    , create    sequence
    , select    any dictionary
    , unlimited tablespace
    , create    cluster
   to
      &data_user
/

--
-- logging system
--

prompt creating logging schema

create user &log_user identified by &log_password
/

grant
      create    session
    , create    procedure
    , create    type
   to
      &log_user
/

--
-- exception system
--

prompt creating exception system

create user &exception_user identified by &exception_password
/

grant
      create session
    , create procedure
   to
      &exception_user
/

--
-- user management system
--

create user &usermgr_user identified by &usermgr_password
/

prompt grant connect to application admin user with admin option

grant
      create session
    , create user
    , alter  user
   to
      &usermgr_user
 with
      admin option
/

grant
      create procedure
    , create type
    , create role
    , drop any role
   to
      &usermgr_user
/

--
-- refdata management schema
--

create user &refdata_user identified by &refdata_password
/

grant
      create session
    , create procedure
    , create type
    , create role
    , drop any role
   to
      &refdata_user
/

--
-- report schema
--

create user &report_user identified by &report_password
/

grant
      create session
    , create procedure
    , create type
    , create role
    , drop any role
    , select any dictionary
   to
      &report_user
/

--
-- ucp mgr schema
--

create user &ucp_user identified by &ucp_password
/

grant
      create session
    , create role
    , drop any role
   to
      &ucp_user
/

-- ---------------------------------------------------------------------
-- configure the pup table to lock down sql*plus access
-- ---------------------------------------------------------------------

prompt deleting contents of product_user_profile

delete
  from
       product_user_profile
/

prompt granting privileges on product_user_profile

grant
      all
   on
      product_user_profile
   to
      &usermgr_user
/

-- ---------------------------------------------------------------------
-- access for utility system
-- ---------------------------------------------------------------------

grant select on sys.dba_users to &utility_user
/

grant select on sys.dba_enabled_traces to &utility_user
/

grant select on sys.dba_users to &data_user with grant option
/

grant select on sys.dba_enabled_traces to &data_user with grant option
/

grant select on sys.dba_role_privs to &data_user with grant option
/

grant select on sys.dba_roles to &data_user with grant option
/

grant select on sys.dba_tab_privs to &data_user with grant option
/

grant select on sys.dba_tab_cols to &refdata_user
/

-- ---------------------------------------------------------------------
-- execute dbms_monitor
-- ---------------------------------------------------------------------

prompt grant permission to execute dbms_monitor

grant
      execute
   on
      dbms_monitor
   to
      &usermgr_user
/