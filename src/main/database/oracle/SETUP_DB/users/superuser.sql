prompt creating superuser

begin

  app_user.pk_standard_user_creation.pr_create_standard_user
  (
    p_username => 'SUPERUSER'
  , p_password => 'sup3r!2'
  , p_uty_id   => 1
  , p_aurp_id  => 1
  , p_lng_id   => 1
  );

  for i in (select dbrl_code from app_data.v_usr_rd_dbrl) loop
    execute immediate 'grant ' || i.dbrl_code || ' to superuser';
  end loop;

end;
/