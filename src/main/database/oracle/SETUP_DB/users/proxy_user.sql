prompt creating proxy user

define proxy_username = PROXY_USER
define proxy_password = PROXY_USER

create user &proxy_username identified by &proxy_password
/

grant &role_connect to &proxy_username
/

prompt registering proxy user

declare

  l_aur_id app_data.app_user.aur_id%type;

begin

     insert
       into
            app_data.app_user aur
          (
            aur.aur_id
          , aur.aur_username
          )
     values
          (
            app_data.sq_aur.nextval
          , '&proxy_username'
          )
  returning
            aur.aur_id
       into
            l_aur_id
          ;

  insert 
    into
         app_data.proxy_app_user paur
       (
         paur.aur_id
       )
  values
       (
         l_aur_id
       );

end;
/