prompt creating profiles

begin
  for i in ( 
             select
                    distinct 
                             dba_pro.profile
               from
                    SYS.dba_profiles dba_pro
              where
                    dba_pro.profile = 'PROFILE_STANDARD'
           )
  loop
    dbms_output.put_line ( 'dropping profile profile_standard' );
    execute immediate 'drop profile ' || i.profile || ' cascade';
  end loop;
end;
/

create profile profile_standard limit
  failed_login_attempts    5
  password_life_time       30
  password_grace_time      5
  password_reuse_time      90
  password_reuse_max       1
  password_verify_function password_verification
/

insert into app_data.app_user_profile ( aurp_id , aurp_code , aurp_name ) values ( app_data.sq_aurp.nextval , 'PROFILE_STANDARD' , 'Standard' )
/

commit
/