prompt create auto_user_mgr

begin

  app_user.pk_delegate_user_creation.pr_create_delegate_user
  (
    p_username => 'AUTO_USER_MGR'
  , p_password => 'AUT0!'
  , p_uty_id   => 1
  , p_aurp_id  => 1
  );

end;
/