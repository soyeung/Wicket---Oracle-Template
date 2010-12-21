prompt creating package body pk_string_utility

create or replace package body app_utility.pk_string_utility
as

    /**
      * name: fn_is_blank_string
      */

    function fn_is_blank_string
    (
        p_string in varchar2
    )
    return boolean
    deterministic
    is
        v_is_blank_string boolean := false;
    begin

        if (
                  p_string is null
               or trim( p_string ) = ''
           )
        then
            v_is_blank_string := true;
        end if;

        return v_is_blank_string;

    end fn_is_blank_string;

    /**
      * name: fn_get_comma_delimited_string
      */

    function fn_get_comma_delimited_string
    (
        p_string_list in app_utility.tty_string
    )
    return varchar2
    is
        v_comma_delimited_list varchar2( 32767 char );
    begin

        with
            data
        as
        (
            select
                   ds.string                                    string
                 , row_number() over ( order by ds.string asc ) rn
              from
                   table
                   (
                       cast
                       (
                           p_string_list as tty_string
                       )
                   ) ds
        )
            select
                   max( ltrim ( sys_connect_by_path ( d.string , ', ' ) , ', ' ) )
                   into
                   v_comma_delimited_list
              from
                   data d
        start with
                   d.rn = 1
        connect by
                   prior
                         d.rn = d.rn - 1
             ;

        return v_comma_delimited_list;

    end fn_get_comma_delimited_string;

    /**
      * name: fn_reduce_string
      */

    function fn_reduce_string
    (
        p_string in varchar2
    )
    return varchar2
    deterministic
    is

        c_permitted_characters     constant varchar2( 50   char ) := 'abcdefghijklmnopqrstuvwxyz';
        c_reduced_string           constant varchar2( 4000 char ) := trim( upper( p_string ) );
        c_length_of_reduced_string constant binary_integer        := length( c_reduced_string );

        v_reduced_string varchar2( 4000 char );
        v_current_char   varchar2( 1    char );

    begin

        if c_length_of_reduced_string > 0 then

            for i in 1 .. c_length_of_reduced_string loop

                v_current_char := substr( c_reduced_string , i , 1 );

                if instr( c_permitted_characters , v_current_char ) > 0 then

                    v_reduced_string := v_reduced_string || v_current_char;

                end if;

            end loop;

        end if;

        return v_reduced_string;

    end fn_reduce_string;

end pk_string_utility;
/

sho err