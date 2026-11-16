# Payment API performance test

This k6 scenario exercises the authenticated, read-only `GET /api/v1/payment/user`
endpoint under a gradual 15-virtual-user load. It checks that fewer than 1% of
requests fail and that the 95th-percentile response time stays below 500 ms.

Run it against a running test/staging instance with a valid access token:

```sh
BASE_URL=http://localhost:8080 PAYMENT_TOKEN=<access-token> k6 run src/test/performance/payments.js
```

The test does not create or modify payment records. Do not run it against
production without approval. Use representative test data and a non-production
account/token.
