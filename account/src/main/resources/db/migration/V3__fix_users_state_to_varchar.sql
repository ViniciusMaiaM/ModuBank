ALTER TABLE users
  ALTER COLUMN state TYPE varchar(2)
  USING state::varchar(2);
