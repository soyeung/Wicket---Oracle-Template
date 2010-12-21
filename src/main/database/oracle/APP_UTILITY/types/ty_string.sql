prompt creating app_utility.ty_string

create type app_utility.ty_string as object (string varchar2(32767 char))
/

grant execute on app_utility.ty_string to public
/