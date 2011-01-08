-- ---------------------------------------------------------------------
-- filename: install.sql
-- author  : andrew hall
-- purpose : a basic sql*plus script to install the database element
--         : of the template application.
--         :
--
--         @c:\oracleapp\src\main\database\oracle\install
-- ---------------------------------------------------------------------

whenever sqlerror exit failure

set serveroutput on

define path_to_this_file            = &1
define sys_user                     = &2
define sys_password                 = &3
define tnsnames_alias               = &4

--
-- define an easy way to reference the logon string based on user supplied params
--

define sys_logon = &sys_user/&sys_password@&tnsnames_alias

define data_path                = APP_DATA
define data_user                = APP_DATA
define data_password            = APP_DATA

define logging_system_path      = APP_LOG
define log_user                 = APP_LOG
define log_password             = APP_LOG

define utility_system_path      = APP_UTILITY
define utility_user             = APP_UTILITY
define utility_password         = APP_UTILITY

define exception_system_path    = APP_EXCEPTION
define exception_user           = APP_EXCEPTION
define exception_password       = APP_EXCEPTION

define user_mgr_system_path     = APP_USER
define usermgr_user             = APP_USER
define usermgr_password         = APP_USER

define ref_data_mgr_system_path = APP_REFDATA
define refdata_user             = APP_REFDATA
define refdata_password         = APP_REFDATA

define reporting_system_path    = APP_REPORT
define report_user              = APP_REPORT
define report_password          = APP_REPORT

define ucp_mgr_system_path      = APP_UCP_MGR
define ucp_user                 = APP_UCP
define ucp_password             = APP_UCP

conn &sys_logon as sysdba

--
-- create the new database schema that are required by the template appplication
--

@&path_to_this_file/SETUP_DB/schema/create

--
-- install utilities
--

@&path_to_this_file/&utility_system_path/install

--
-- install data structures
--

@&path_to_this_file/&data_path/install

--
-- install event logging system
--

@&path_to_this_file/&logging_system_path/install

--
-- install exception handling system
--

@&path_to_this_file/&exception_system_path/install


--
-- create the session configuration package - on which so much relies
--

grant select on app_data.app_user to &utility_user
/

grant select on app_data.standard_app_user to &utility_user
/

grant select on app_data.v_standard_app_user to &utility_user
/

grant select on app_data.app_exception to &utility_user
/

@&path_to_this_file/&utility_system_path/packages/pk_session_utility.hdr

@&path_to_this_file/&utility_system_path/packages/pk_session_utility.bdy

@&path_to_this_file/&utility_system_path/packages/pk_assert.hdr

@&path_to_this_file/&utility_system_path/packages/pk_assert.bdy

--
-- install sys objects
--

@&path_to_this_file/sys/password_verification

--
-- create and register user profiles
--

@&path_to_this_file/setup_db/profiles/install

--
-- install contents of the application admin schema
--

@&path_to_this_file/&user_mgr_system_path/install

--
-- install contents of the reference data admin schema
--

@&path_to_this_file/&ref_data_mgr_system_path/install

--
-- install contents of reporting system
--

@&path_to_this_file/&reporting_system_path/install

--
-- roles
--

@&path_to_this_file/SETUP_DB/roles/install

--
-- setup seed data
--

@&path_to_this_file/SETUP_DB/data/install

--
-- users
--

@&path_to_this_file/setup_db/users/install

--
-- add a version of the logging package whose procedure bodies are implemented - i do not want to record events during installation.
--

@&path_to_this_file/&logging_system_path/packages/pk_log.bdy

prompt revoke all roles from sys that have been created as part of this installation

begin
  for i in ( select dbrl.dbrl_code from app_data.db_role dbrl ) loop
    dbms_output.put_line ( i.dbrl_code );
    execute immediate 'revoke ' || i.dbrl_code || ' from sys';
  end loop;
exception
  when others then
    dbms_output.put_line ( sqlerrm );
end;
/

prompt recompile all invalid objects
exec sys.utl_recomp.recomp_serial( null );

--
-- revoke privileges from schema which they do not require post installation
--

@&path_to_this_file/SETUP_DB/schema/revoke_privileges

--
-- make sure that the cbo comes into play
--
exec sys.dbms_stats.gather_schema_stats( '&data_user' );

--
-- flush all crap from installation out of the shared pool
--

alter system flush shared_pool;

disc

exit success