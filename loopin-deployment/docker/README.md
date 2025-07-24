## change keycloak client password

```shell
/opt/keycloak/bin/kcadm.sh config credentials \
--server http://localhost:8080 \
--realm master \
--user user \
--password 'password'

```

```shell
/opt/keycloak/bin/kcadm.sh get clients -r loopin --fields id,clientId
```

```shell
/opt/keycloak/bin/kcadm.sh update clients/{clientId} \
-r loopin \
-s secret='keycloak-client-secret'
```