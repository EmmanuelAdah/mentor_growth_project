import http from "k6/http";
import { check } from "k6";

const baseUrl = (__ENV.BASE_URL || "http://localhost:8080").replace(/\/$/, "");
const paymentToken = __ENV.PAYMENT_TOKEN;

if (!paymentToken) {
  throw new Error("Set PAYMENT_TOKEN to a valid mentee, mentor, or admin access token.");
}

export const options = {
  scenarios: {
    payment_list_load: {
      executor: "ramping-vus",
      startVUs: 1,
      stages: [
        { duration: "30s", target: 5 },
        { duration: "60s", target: 15 },
        { duration: "30s", target: 0 },
      ],
      gracefulRampDown: "10s",
    },
  },
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<500"],
  },
};

export default function () {
  const response = http.get(`${baseUrl}/api/v1/payment/user`, {
    headers: {
      Authorization: `Bearer ${paymentToken}`,
      Accept: "application/json",
    },
    tags: { endpoint: "payment-list" },
  });

  check(response, {
    "payment list returns HTTP 200": (result) => result.status === 200,
  });
}
