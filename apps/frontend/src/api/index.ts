import { createApiClient } from "./contract.ts";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const client = createApiClient(BACKEND_URL);
