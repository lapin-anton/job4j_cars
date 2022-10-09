CREATE TABLE auto_post (
   id SERIAL PRIMARY KEY,
   text text,
   created timestamp,
   auto_user_id integer,
   CONSTRAINT fk_auto_user
       FOREIGN KEY(auto_user_id)
           REFERENCES auto_user(id)
           ON DELETE CASCADE
);