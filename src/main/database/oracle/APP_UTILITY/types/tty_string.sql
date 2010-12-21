prompt creating tty_string

create type app_utility.tty_string as table of app_utility.ty_string
/

grant execute on app_utility.tty_string to public
/