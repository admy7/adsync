import { createApiClient } from "./contract.ts";
import axios from "axios";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const client = createApiClient(BACKEND_URL, {axiosInstance});