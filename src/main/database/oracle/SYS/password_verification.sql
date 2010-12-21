create or replace function sys.password_verification
(
    username     in varchar2
,   password     in varchar2
,   old_password in varchar2
)
return boolean
is

    c_min_password_length constant int := 5;

    /**
      * name: pr_compare_username_and_pass
      */

    procedure pr_compare_username_and_pass
    is
    begin

        if nls_lower ( password ) = nls_lower ( username ) then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_similar_username_password
            );
        end if;

    end pr_compare_username_and_pass;

    /**
      * name: pr_check_password_length
      */

    procedure pr_check_password_length
    is
    begin
        if length ( password ) < c_min_password_length then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_password_too_small
            );
        end if;
    end pr_check_password_length;

    /**
      * name: pr_check_banned_password
      */

    procedure pr_check_banned_password
    is
    begin
        if nls_lower ( password ) in ( 'welcome' , 'database' , 'account' , 'user' , 'password' , 'oracle' , 'computer' , 'abcd' ) then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_banned_password
            );
        end if;
    end pr_check_banned_password;

    /**
      * name: pr_check_password_has_digit
      */

    procedure pr_check_password_has_digit
    is
    begin
        if ( regexp_instr ( password , '[[:digit:]]' ) = 0 ) then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_missing_password_content
            );
        end if;
    end pr_check_password_has_digit;

    /**
      * name: pr_check_password_has_char
      */

    procedure pr_check_password_has_char
    is
    begin
        if ( regexp_instr ( password , '[[:alpha:]]' ) = 0 ) then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_missing_password_content
            );
        end if;
    end pr_check_password_has_char;

    /**
      * name: pr_check_password_has_punct
      */

    procedure pr_check_password_has_punct
    is
    begin
        if ( regexp_instr ( password , '[[:punct:]]' ) = 0 ) then
            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code => app_exception.pk_exception_codes.c_missing_password_content
            );
        end if;
    end pr_check_password_has_punct;

begin

    pr_compare_username_and_pass;

    pr_check_password_length;

    pr_check_banned_password;

    pr_check_password_has_digit;

    pr_check_password_has_char;

    pr_check_password_has_punct;

    return true;

end;
/

sho err