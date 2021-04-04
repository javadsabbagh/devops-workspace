# Build Kong Image
We need to build a kong image based on official image that contains required plugins, e.g OIDC.
We call it "tjb/kong" and use it in docker compose yaml file.

```bash
docker build . --no-cache -t tjb/kong
```


docker run --rm \
    --link kong-database:kong-database \
    -e "KONG_DATABASE=postgres" \
    -e "KONG_PG_HOST=kong-database" \
    kong kong migrations bootstrap



TODO
Kong log:
[warn] ulimit is currently set to "1024". For better performance set it to at least "4096" using "ulimit -n



Migration logs:
kong_db_1 is up-to-date
Creating kong_kong-migrations_1 ... done
Attaching to kong_kong-migrations_1
kong-migrations_1     | 2021/03/31 11:59:50 [warn] ulimit is currently set to "1024". For better performance set it to at least "4096" using "ulimit -n"
kong-migrations_1     | Bootstrapping database...
kong-migrations_1     | migrating core on database 'kong'...
kong-migrations_1     | core migrated up to: 000_base (executed)
kong-migrations_1     | core migrated up to: 003_100_to_110 (executed)
kong-migrations_1     | core migrated up to: 004_110_to_120 (executed)
kong-migrations_1     | core migrated up to: 005_120_to_130 (executed)
kong-migrations_1     | core migrated up to: 006_130_to_140 (executed)
kong-migrations_1     | core migrated up to: 007_140_to_150 (executed)
kong-migrations_1     | core migrated up to: 008_150_to_200 (executed)
kong-migrations_1     | core migrated up to: 009_200_to_210 (executed)
kong-migrations_1     | core migrated up to: 010_210_to_211 (executed)
kong-migrations_1     | core migrated up to: 011_212_to_213 (executed)
kong-migrations_1     | core migrated up to: 012_213_to_220 (executed)
kong-migrations_1     | core migrated up to: 013_220_to_230 (executed)
kong-migrations_1     | migrating rate-limiting on database 'kong'...
kong-migrations_1     | rate-limiting migrated up to: 000_base_rate_limiting (executed)
kong-migrations_1     | rate-limiting migrated up to: 003_10_to_112 (executed)
kong-migrations_1     | rate-limiting migrated up to: 004_200_to_210 (executed)
kong-migrations_1     | migrating hmac-auth on database 'kong'...
kong-migrations_1     | hmac-auth migrated up to: 000_base_hmac_auth (executed)
kong-migrations_1     | hmac-auth migrated up to: 002_130_to_140 (executed)
kong-migrations_1     | hmac-auth migrated up to: 003_200_to_210 (executed)
kong-migrations_1     | migrating oauth2 on database 'kong'...
kong-migrations_1     | oauth2 migrated up to: 000_base_oauth2 (executed)
kong-migrations_1     | oauth2 migrated up to: 003_130_to_140 (executed)
kong-migrations_1     | oauth2 migrated up to: 004_200_to_210 (executed)
kong-migrations_1     | oauth2 migrated up to: 005_210_to_211 (executed)
kong-migrations_1     | migrating ip-restriction on database 'kong'...
kong-migrations_1     | ip-restriction migrated up to: 001_200_to_210 (executed)
kong-migrations_1     | migrating jwt on database 'kong'...
kong-migrations_1     | jwt migrated up to: 000_base_jwt (executed)
kong-migrations_1     | jwt migrated up to: 002_130_to_140 (executed)
kong-migrations_1     | jwt migrated up to: 003_200_to_210 (executed)
kong-migrations_1     | migrating basic-auth on database 'kong'...
kong-migrations_1     | basic-auth migrated up to: 000_base_basic_auth (executed)
kong-migrations_1     | basic-auth migrated up to: 002_130_to_140 (executed)
kong-migrations_1     | basic-auth migrated up to: 003_200_to_210 (executed)
kong-migrations_1     | migrating key-auth on database 'kong'...
kong-migrations_1     | key-auth migrated up to: 000_base_key_auth (executed)
kong-migrations_1     | key-auth migrated up to: 002_130_to_140 (executed)
kong-migrations_1     | key-auth migrated up to: 003_200_to_210 (executed)
kong-migrations_1     | migrating session on database 'kong'...
kong-migrations_1     | session migrated up to: 000_base_session (executed)
kong-migrations_1     | session migrated up to: 001_add_ttl_index (executed)
kong-migrations_1     | migrating acl on database 'kong'...
kong-migrations_1     | acl migrated up to: 000_base_acl (executed)
kong-migrations_1     | acl migrated up to: 002_130_to_140 (executed)
kong-migrations_1     | acl migrated up to: 003_200_to_210 (executed)
kong-migrations_1     | acl migrated up to: 004_212_to_213 (executed)
kong-migrations_1     | migrating response-ratelimiting on database 'kong'...
kong-migrations_1     | response-ratelimiting migrated up to: 000_base_response_rate_limiting (executed)
kong-migrations_1     | migrating bot-detection on database 'kong'...
kong-migrations_1     | bot-detection migrated up to: 001_200_to_210 (executed)
kong-migrations_1     | migrating acme on database 'kong'...
kong-migrations_1     | acme migrated up to: 000_base_acme (executed)
kong-migrations_1     | 41 migrations processed
kong-migrations_1     | 41 executed
kong-migrations_1     | Database is up-to-date
kong_kong-migrations_1 exited with code 0


It should exit with code 0





Kong running notes:

kong_db_1 is up-to-date
Creating kong_kong_1 ... done
Attaching to kong_kong_1
kong_1                | 2021/03/31 12:02:00 [warn] ulimit is currently set to "1024". For better performance set it to at least "4096" using "ulimit -n"
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: using the "epoll" event method
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: openresty/1.17.8.2
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: built by gcc 10.2.1 20201203 (Alpine 10.2.1_pre1) 
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: OS: Linux 5.8.0-48-generic
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: getrlimit(RLIMIT_NOFILE): 1024:524288
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker processes
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 23
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 24
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 25
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 26
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 27
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 28
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 29
kong_1                | 2021/03/31 12:02:00 [notice] 1#0: start worker process 30
kong_1                | 2021/03/31 12:02:00 [notice] 24#0: *2 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 27#0: *4 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 30#0: *6 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 28#0: *5 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 23#0: *1 [lua] warmup.lua:78: single_dao(): Preloading 'services' into the core_cache..., context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 29#0: *7 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 25#0: *8 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 23#0: *1 [lua] warmup.lua:115: single_dao(): finished preloading 'services' into the core_cache (in 0ms), context: init_worker_by_lua*
kong_1                | 2021/03/31 12:02:00 [notice] 26#0: *3 [kong] init.lua:417 only worker #0 can manage, context: init_worker_by_lua*



